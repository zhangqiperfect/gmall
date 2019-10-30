package com.atguigu.gmall.sms.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author ZQ
 * @create 2019-10-29 9:59
 */
@Configuration
public class DatasourceConfig {
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource(@Value("${spring.datasource.url}") String url){
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(url);
        return  hikariDataSource;
    }
}
