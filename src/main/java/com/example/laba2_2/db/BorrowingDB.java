package com.example.laba2_2.db;

import com.example.laba2_2.entity.Borrowing;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BorrowingDB {

    private Connection connection;
    private PreparedStatement preparedStatement;

    public BorrowingDB() {
    }

    public List<Borrowing> getAllByReaderId(int readerId, DataSource dataSource) {

        long before = System.currentTimeMillis();

        String SELECT = "select * from borrowings where reader_id = ?";
        List<Borrowing> list = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setInt(1, readerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("borrowing_id");
                int bookId = resultSet.getInt("book_id");
                Date dateBorrowed = resultSet.getDate("date_borrowed");
//                Date dateDue = resultSet.getDate("date_due");
                Date dateReturned = resultSet.getDate("date_returned");

                list.add(new Borrowing(id, bookId, readerId, dateBorrowed, dateReturned));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after - before));

        return list;
    }

    public void deleteByBookIdAndReaderId(int bookId, int readerId, DataSource dataSource) {

        long before = System.currentTimeMillis();

        String SELECT = "delete from borrowings where reader_id = ? and  book_id = ?";
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setInt(1, readerId);
            preparedStatement.setInt(2, bookId);

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after - before));
    }

    public void updateBorrowingDateReturned(int bookId, int readerId, Date date, DataSource dataSource) {

        long before = System.currentTimeMillis();

        String SELECT = "update borrowings set date_returned = ? where reader_id = ? and  book_id = ?";
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setDate(1, date);
            preparedStatement.setInt(2, readerId);
            preparedStatement.setInt(3, bookId);

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after - before));
    }

    public void save(Borrowing borrowing, DataSource dataSource) {

        long before = System.currentTimeMillis();

        String SELECT = "insert into borrowings(book_id,reader_id,date_borrowed,date_returned) values(?,?,?,?)";
        try {
            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setInt(1, borrowing.getBookId());
            preparedStatement.setInt(2, borrowing.getReaderId());
            preparedStatement.setDate(3, borrowing.getDateBorrowed());
            preparedStatement.setDate(4,borrowing.getDateReturned());

            preparedStatement.execute();
            connection.commit();;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after - before));
    }

    @Transactional
    public void deleteEntryAndReader(int readerId, DataSource firstDataSource, DataSource secondDataSource) {
        long before = System.currentTimeMillis();

        String DELETE1 = "DELETE FROM borrowings WHERE reader_id = ?";
        String DELETE2 = "DELETE FROM readers WHERE reader_id = ?";
        String INSERT = "INSERT INTO deleted_readers(reader_id) VALUES(?)";

        try (Connection connection1 = firstDataSource.getConnection();
             Connection connection2 = secondDataSource.getConnection()) {

            connection1.setAutoCommit(false);
            connection2.setAutoCommit(false);

            try (PreparedStatement preparedStatement1 = connection1.prepareStatement(DELETE1);
                 PreparedStatement preparedStatement2 = connection1.prepareStatement(DELETE2);
                 PreparedStatement preparedStatement3 = connection2.prepareStatement(INSERT)) {

                preparedStatement1.setInt(1, readerId);
                preparedStatement1.executeUpdate();

                preparedStatement2.setInt(1, readerId);
                preparedStatement2.executeUpdate();

                preparedStatement3.setInt(1, readerId);
                preparedStatement3.executeUpdate();

                connection1.commit();
                connection2.commit();
            } catch (SQLException e) {
                connection1.rollback();
                connection2.rollback();
                throw new RuntimeException("Error occurred during transaction", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred during database connection", e);
        }

        long after = System.currentTimeMillis();
        System.out.println("Time: " + (after - before));
    }
}
