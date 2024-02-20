package com.example.laba2_2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class PostgreSQLDBConfig {


    @Bean(name = "postgresqlDataSource")
    @ConfigurationProperties(prefix = "application-postgresql.properties")
    public DataSource postgresqlDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5432/library2")
                .password("postgres")
                .username("postgres")
                .build();
    }



   /* @Bean
    @ConfigurationProperties("application-postgresql.properties")
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
