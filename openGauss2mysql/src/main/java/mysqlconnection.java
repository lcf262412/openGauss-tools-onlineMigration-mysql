/*
 * Copyright (c) 2022-2022 Huawei Technologies Co.,Ltd.
 *
 * openGauss is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *
 *           http://license.coscl.org.cn/MulanPSL2
 *
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

/**
 * Description: mysqlconnection class
 *
 * @author zhangyaozhong
 * @date 2022/09/09
 **/
public class mysqlconnection {

    static String config = "config.yaml";
    static YamlTool yamls = new YamlTool(config);
    static LinkedHashMap re_mysql_conn = yamls.getValue("mysql");
    static String HOST = (String) re_mysql_conn.get("host");
    static String PORT = (String) re_mysql_conn.get("port");
    static String USER = (String) re_mysql_conn.get("user");
    static String PASSWORD = (String) re_mysql_conn.get("password");
    static String DATABASE = (String) re_mysql_conn.get("database");

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    /**
     * connect mysql and return Statement
     *
     */
    public static Statement main() {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            stmt = conn.createStatement();


        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stmt;
    }
}

