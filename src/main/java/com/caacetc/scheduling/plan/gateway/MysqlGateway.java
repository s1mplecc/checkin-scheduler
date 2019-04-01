package com.caacetc.scheduling.plan.gateway;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlGateway {
    private static DSLContext context;

    public static DSLContext context() {
        if (context == null) {
            context = init();
        }
        return context;
    }

    private static DSLContext init() {
//        String username = "root";
//        String password = "HP-PrintJet2107";
//        String url = "jdbc:mysql://172.18.21.87:3306/guitai";
//        String password = "root";
//        String url = "jdbc:mysql://127.0.0.1:3306/guitai";

        String username = "dbo";
        String password = "caecaodb";
        String url = "jdbc:mysql://172.18.21.21:3306/guitai?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return DSL.using(conn, SQLDialect.MYSQL);
    }
}
