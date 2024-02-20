package com.example.laba2_2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Borrowing {

    private int borrowingId;
    private int bookId;
    private int readerId;
    private Date dateBorrowed;
    private Date dateReturned;

    public Borrowing(int bookId, int readerId, Date dateBorrowed, Date dateReturned) {
        this.bookId = bookId;
        this.readerId = readerId;
        this.dateBorrowed = dateBorrowed;
        this.dateReturned = dateReturned;
    }
}
