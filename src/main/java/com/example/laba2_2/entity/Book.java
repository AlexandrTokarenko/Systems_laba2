package com.example.laba2_2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private int bookId;
    private String title;
    private String author;
    private String genre;
    private int publicationYear;
}
