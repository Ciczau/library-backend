package com.example.demo.Book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowedBook {
    private int id;
    private String title;
    private String author;
    private String releaseDate;
    private String image;
    private int amount;
    private Date returnDate;
    private boolean returned;
}
