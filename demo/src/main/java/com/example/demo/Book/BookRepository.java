package com.example.demo.Book;

import com.example.demo.Loan.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class BookRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Book> getBooks() {
        return jdbcTemplate.query("SELECT id, title, author, release_date, image FROM BOOKS", BeanPropertyRowMapper.newInstance(Book.class));
    }

    public Book getBook(int id){
        return jdbcTemplate.queryForObject("SELECT id, title, author, release_date, image FROM BOOKS WHERE id = ?", BeanPropertyRowMapper.newInstance(Book.class), id);
    }

    public void addBook(Book book){
        jdbcTemplate.update("INSERT INTO BOOKS (title, author, release_date, image) VALUES (?, ? , ?, ?)", book.getTitle(), book.getTitle(), book.getReleaseDate(), book.getImage());
    }

    public void updateBook(Book book){
        jdbcTemplate.update("UPDATE BOOKS SET title = ?, author = ?, release_date = ?, WHERE id = ?", book.getTitle(), book.getAuthor(), book.getReleaseDate(), book.getId());
    }

    public void deleteBook(int id){
        jdbcTemplate.update("DELETE FROM BOOKS WHERE id = ?", id);
    }

    public boolean checkIfBookCanBeLoaned(int id){
        List<Loan> loans = jdbcTemplate.query("SELECT book_id, returned FROM LOANS", BeanPropertyRowMapper.newInstance(Loan.class));
        AtomicInteger counter = new AtomicInteger();
        Book book = jdbcTemplate.queryForObject("SELECT id, title, author, release_date, image, amount FROM BOOKS WHERE id = ?", BeanPropertyRowMapper.newInstance(Book.class), id);

        if(book == null) return false;
        loans.forEach((loan -> {

            if(loan.getBookId() == book.getId()){
                counter.getAndIncrement();
            }
        }));

        return book.getAmount() > counter.get();
    }
}
