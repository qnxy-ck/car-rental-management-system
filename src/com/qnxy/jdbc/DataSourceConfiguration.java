package com.qnxy.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * 数据源配置及获取
 *
 * @author Qnxy
 */
public final class DataSourceConfiguration {

    private static final DataSource dataSource;

    static {
        final HikariConfig hikariConfig = new HikariConfig("/hikari_config.properties");
        dataSource = new HikariDataSource(hikariConfig);
    }


    private DataSourceConfiguration() {
    }

    public static DataSource dataSource() {
        return dataSource;
    }

}
