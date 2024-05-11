package com.example.demo.Loan;

import com.example.demo.Book.Book;
import com.example.demo.Book.BorrowedBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {
    @Autowired
    private LoanRepository loanRepository;

    @GetMapping()
    public List<Loan> getAllLoans() {
        return loanRepository.getAllLoans();
    }

    @GetMapping("/{id}")
    public Loan getLoanById(@PathVariable("id") int id) {
        return loanRepository.getLoanById(id);
    }

    @PostMapping("/add")
    public void addLoan(@RequestBody Loan loan) {
        loanRepository.addLoan(loan);
    }

    @PostMapping("/update")
    public void updateLoan(@RequestBody Loan loan) {
        loanRepository.updateLoan(loan);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteLoan(@PathVariable("id") int id) {
        loanRepository.deleteLoan(id);
    }

    @GetMapping("/available/{bookId}")
    public boolean isBookAvailable(@PathVariable("bookId") int bookId) {
        return loanRepository.isBookAvailable(bookId);
    }

    @GetMapping("/user/{userId}")
    public List<BorrowedBook> getBorrowedBooks(@PathVariable("userId") String userId){
        return loanRepository.getBooksByUserId(userId);
    }

}
