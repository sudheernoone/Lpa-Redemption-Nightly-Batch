package com.toyota.tfs.LpaRedemptionBatch.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
public class JpaConfig {

    @Value("${postgres.datasource.driverClassName}")
    private String postgresDriverClassName;
    @Value("${postgres.datasource.url}")
    private String postgresUrl;
    @Value("${postgres.datasource.username}")
    private String postgresUserName;
    @Value("${postgres.datasource.pwd}")
    private String postgresPwd;

    @Bean(name="dataSource")
    public DataSource getDataSource()
    {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(postgresDriverClassName);
        dataSourceBuilder.url(postgresUrl);
        dataSourceBuilder.username(postgresUserName);
        dataSourceBuilder.password(postgresPwd);
        return dataSourceBuilder.build();
    }
}