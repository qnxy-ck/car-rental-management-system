package com.qnxy.jdbc;

import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Qnxy
 */
@Slf4j
public class UpdateSpace {

    private final List<Object> paramList = new ArrayList<>();
    private String sql;

    public UpdateSpace sql(String sql) {
        this.sql = sql;
        return this;
    }

    public UpdateSpace params(Object... params) {
        this.paramList.addAll(Arrays.asList(params));
        return this;
    }

    public UpdateSpace param(Object param) {
        this.paramList.add(param);
        return this;
    }

    public int update() throws SQLException {
        final DataSource dataSource = DataSourceConfiguration.dataSource();
        final Connection connection = dataSource.getConnection();
        final PreparedStatement ps = connection.prepareStatement(sql);
        for (int i = 0; i < this.paramList.size(); i++) {
            ps.setObject(i + 1, this.paramList.get(i));
        }

        return ps.executeUpdate();
    }

}
