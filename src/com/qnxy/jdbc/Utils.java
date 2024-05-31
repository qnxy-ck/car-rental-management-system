package com.qnxy.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Qnxy
 */
public final class Utils {
    private Utils() {
    }

    public static void closeAction(Connection connection, PreparedStatement ps, ResultSet resultSet) {
        Stream.of(resultSet, ps, connection)
                .filter(Objects::nonNull)
                .forEach(it -> {
                    try {
                        it.close();
                    } catch (Exception e) {
                        // ignore
                    }
                });

    }
}
