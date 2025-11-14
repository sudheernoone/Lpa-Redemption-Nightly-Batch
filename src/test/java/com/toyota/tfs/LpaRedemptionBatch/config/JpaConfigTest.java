package com.toyota.tfs.LpaRedemptionBatch.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JpaConfigTest {

    private JpaConfig jpaConfig;

    @BeforeEach
    void setUp() {
        jpaConfig = new JpaConfig();
        jpaConfig.setPostgresDriverClassName("org.postgresql.Driver");
        jpaConfig.setPostgresUrl("jdbc:postgresql://localhost:5432/testdb");
        jpaConfig.setPostgresUserName("testuser");
        jpaConfig.setPostgresPwd("testpassword");
    }

    @Test
    void getDataSource_ShouldReturnValidDataSource() {
        // Act
        DataSource dataSource = jpaConfig.getDataSource();

        // Assert
        assertNotNull(dataSource, "DataSource should not be null");
        //assertEquals("org.postgresql.Driver", dataSource.getClass().getName(), "Driver class name should match");
    }
}