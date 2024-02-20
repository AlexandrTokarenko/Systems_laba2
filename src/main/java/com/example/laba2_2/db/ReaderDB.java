package com.example.laba2_2.db;

import com.example.laba2_2.entity.Reader;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReaderDB {

    private Connection connection;
    private PreparedStatement preparedStatement;

    public ReaderDB() {
    }


    public List<Reader> getAll(DataSource dataSource) {


        long before = System.currentTimeMillis();
        String SELECT = "select * from readers";
        List<Reader> list = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(SELECT);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("reader_id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");

                list.add(new Reader(id,name,email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after - before));

        return list;
    }
}
