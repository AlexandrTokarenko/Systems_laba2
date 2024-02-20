package com.example.laba2_2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class MySQLDBConfig {


    @Bean(name = "mysqlDataSource")
    @ConfigurationProperties(prefix = "application-mysql.properties")
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/library2")
                .password("mysql")
                .username("mysql")
                .build();
    }


   /* @Bean
    @ConfigurationProperties("application-mysql.properties")
    public DataSourceProperties topicsDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource todosDataSource() {
        return topicsDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }*/
}
