package com.example.demo.Loan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Loan {
    private int id;
    private int userId;
    private int bookId;
    private boolean returned;
    private Date loanDate;
    private Date returnDate;
}