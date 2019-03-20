package com.caacetc.scheduling.plan.core;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    public static DSLContext context() throws SQLException {
        String userName = "root";
        String password = "";
        String url = "jdbc:mysql://localhost:3306/library";

        Connection conn = DriverManager.getConnection(url, userName, password);
        return DSL.using(conn, SQLDialect.MYSQL);
    }
}
