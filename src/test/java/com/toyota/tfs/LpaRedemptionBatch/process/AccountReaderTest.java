package com.toyota.tfs.LpaRedemptionBatch.process;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verifyNoInteractions;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.item.database.JdbcCursorItemReader;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionHistory;

class AccountReaderTest {

    @InjectMocks
    private AccountReader accountReader;

    @Mock
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testJdbcCursorItemReader() {
        // Act
        JdbcCursorItemReader<RedemptionHistory> reader = accountReader.jdbcCursorItemReader();

        // Assert
        assertNotNull(reader);
        assertEquals(dataSource, reader.getDataSource());
        assertEquals("select * from lpa_redemption_history where check_in_daily_batch_job = 'Y'", reader.getSql());
        //assertNotNull(reader.getRowMapper());
        //assertEquals(BeanPropertyRowMapper.class, reader.getRowMapper().getClass());
        verifyNoInteractions(dataSource); // Ensure no interactions with the data source during setup
    }
}