package com.almonium.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@Testcontainers
public interface PostgresContainer {
    String POSTGRES_VERSION = "16";

    @Container
    @ServiceConnection
    PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:" + POSTGRES_VERSION);
}
