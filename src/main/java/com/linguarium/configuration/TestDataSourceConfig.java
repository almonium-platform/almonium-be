package com.linguarium.configuration;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@Profile("dev")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestDataSourceConfig {
    static final String DRIVER_CLASS_NAME = "org.postgresql.Driver";
    static final String URL = "jdbc:postgresql://localhost:5432/liquid_test";
    static final String USERNAME = "postgres";
    static final String PASSWORD = "password";

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER_CLASS_NAME);
        dataSource.setUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        return dataSource;
    }
}
