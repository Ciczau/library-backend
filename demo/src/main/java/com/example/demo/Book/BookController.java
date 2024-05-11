package com.example.demo.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    BookRepository bookRepository;

    @GetMapping()
    public List<Book> getBooks(){
        return bookRepository.getBooks();
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable("id") int id){
        return bookRepository.getBook(id);
    }

    @PostMapping("/add")
    public void addBook(@RequestBody Book book){
        bookRepository.addBook(book);
    }

    @PostMapping("/update")
    public void updateBook(@RequestBody Book book){
        bookRepository.updateBook(book);
    }

    @GetMapping("/delete/{id}")
    public void deleteBook(@PathVariable("id") int id){
        bookRepository.deleteBook(id);
    }

    @GetMapping("/check/{id}")
    public boolean checkIfBookCanBeLoaned(@PathVariable("id") int id){
        return bookRepository.checkIfBookCanBeLoaned(id);
    }
}
