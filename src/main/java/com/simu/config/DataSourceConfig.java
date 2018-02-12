package com.simu.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.exemodel.cache.ICache;
import org.exemodel.session.impl.JdbcSessionFactory;
import org.exemodel.transation.spring.TransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author DengrongGuan
 * @create 2018-02-08
 **/
@Configuration
public class DataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix = "druid.datasource")
    public DataSource DataSource() {
        return new DruidDataSource();
    }
    @Bean
    public JdbcSessionFactory JdbcSessionFactory(
            @Qualifier("DataSource") DataSource dataSource) {
        return new JdbcSessionFactory(dataSource, null);
    }

}
