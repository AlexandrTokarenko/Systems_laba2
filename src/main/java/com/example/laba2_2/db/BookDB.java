package com.example.laba2_2.db;

import com.example.laba2_2.entity.Book;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookDB {

    private Connection connection;
    private PreparedStatement preparedStatement;

    public BookDB() {
    }

    public List<Book> getAll(DataSource dataSource) {

        long before = System.currentTimeMillis();
        String SELECT = "select * from books";
        List<Book> list = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(SELECT);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String genre = resultSet.getString("genre");
                int publicationYear = resultSet.getInt("publication_year");
                List<Integer> quantities = new ArrayList<>();

                list.add(new Book(id,title,author,genre,publicationYear));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after - before));


        return list;
    }

    public List<Book> getAllByReaderId(int readerId, DataSource dataSource) {

        long before = System.currentTimeMillis();

        String SELECT = "select * from books inner join borrowings b on books.book_id = b.book_id where b.reader_id = ? and b.date_returned IS NULL";
        List<Book> list = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setInt(1,readerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String genre = resultSet.getString("genre");
                int publicationYear = resultSet.getInt("publication_year");
                List<Integer> quantities = new ArrayList<>();

                list.add(new Book(id,title,author,genre,publicationYear));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after - before));


        return list;
    }
}
