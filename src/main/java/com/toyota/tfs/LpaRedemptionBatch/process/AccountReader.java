package com.toyota.tfs.LpaRedemptionBatch.process;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import com.toyota.tfs.LpaRedemptionBatch.model.dao.RedemptionHistory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AccountReader {

    @Autowired
    private DataSource dataSource;

    public JdbcCursorItemReader<RedemptionHistory> jdbcCursorItemReader() {
        JdbcCursorItemReader<RedemptionHistory> jdbcCursorItemReader =
                new JdbcCursorItemReader<>();
        jdbcCursorItemReader.setDataSource(dataSource);
        jdbcCursorItemReader.setSql(
                "select * from lpa_redemption_history where check_in_daily_batch_job = 'Y'");
        jdbcCursorItemReader.setRowMapper(new BeanPropertyRowMapper<RedemptionHistory>() {
            {
                setMappedClass(RedemptionHistory.class);
            }
        });

//        jdbcCursorItemReader.setCurrentItemCount(2);
//        jdbcCursorItemReader.setMaxItemCount(8);

        return jdbcCursorItemReader;
    }

}
