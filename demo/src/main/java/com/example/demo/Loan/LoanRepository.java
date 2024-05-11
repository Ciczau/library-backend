package com.example.demo.Loan;

import com.example.demo.Book.Book;
import com.example.demo.Book.BorrowedBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class LoanRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Loan> getAllLoans() {
        String sql = "SELECT id, user_id, book_id, loan_date, return_date FROM LOANS";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Loan.class));
    }

    public Loan getLoanById(int id) {
        String sql = "SELECT id, user_id, book_id, loan_date, return_date FROM LOANS WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Loan.class), id);
    }

    public void addLoan(Loan loan) {
        String sql = "INSERT INTO LOANS (user_id, book_id, loan_date, return_date, returned) VALUES (?, ?, ?, ?, false)";
        LocalDate currentDate = LocalDate.now();
        LocalDate nextMonth = currentDate.plusMonths(1);
        jdbcTemplate.update(sql, loan.getUserId(), loan.getBookId(), currentDate, nextMonth);
    }

    public void updateLoan(Loan loan) {
        String sql = "UPDATE LOANS SET user_id = ?, book_id = ?, loan_date = ?, return_date = ? WHERE id = ?";
        jdbcTemplate.update(sql, loan.getUserId(), loan.getBookId(), loan.getLoanDate(), loan.getReturnDate(), loan.getId());
    }

    public void deleteLoan(int id) {
        String sql = "DELETE FROM LOANS WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean isBookAvailable(int bookId) {
        String sql = "SELECT COUNT(*) FROM LOANS WHERE book_id = ? AND return_date IS NULL";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, bookId);
        return count != null && count == 0;
    }
    public List<BorrowedBook> getBooksByUserId(String userId) {
        String sql = "SELECT b.id, b.title, b.author, b.release_date, b.image, b.amount, l.return_date, l.returned FROM BOOKS b " +
                "JOIN LOANS l ON b.id = l.book_id WHERE l.user_id = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(BorrowedBook.class), userId);
    }

}
