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

package org.opengauss.migration.vo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Description: ConnectionInfo class
 * @author douxin
 * @date 2022/08/09
 **/
public class ConnectionInfo {
    /**
     * The openGauss JDBC driver class
     */
    public static final String OPENGAUSS_JDBC_DRIVER = "org.opengauss.Driver";

    /**
     * The mysql JDBC driver class
     */
    public static final String MYSQL_JDBC_SRIVER = "com.mysql.jdbc.Driver";

    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    /**
     * Constructor
     */
    public ConnectionInfo() {}

    /**
     * Constructor
     *
     * @param String the host
     * @param int the port
     * @param String the database
     * @param String the username
     * @param String the password
     */
    public ConnectionInfo(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    /**
     * Sets host
     *
     * @param String the host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets host
     *
     * @return String the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets port
     *
     * @param int the port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Gets port
     *
     * @return int the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets database
     *
     * @param String the database
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * Gets database
     *
     * @return String the database
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Sets username
     *
     * @param String the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets username
     *
     * @return String the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets password
     *
     * @param String the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets password
     *
     * @return String the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Create openGauss connection
     *
     * @return Connection the connection
     */
    public Connection createOpenGaussConnection() {
        String driver = OPENGAUSS_JDBC_DRIVER;
        String url = String.format(Locale.ENGLISH, "jdbc:opengauss://%s:%s/%s?loggerLevel=OFF", host, port, database);
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement("set session_timeout = 0");
            ps.execute();
        } catch (ClassNotFoundException | SQLException exp) {
            exp.printStackTrace();
        }
        return connection;
    }

    /**
     * Create mysql connection
     *
     * @return Connection the Connection
     */
    public Connection createMysqlConnection() {
        String driver = MYSQL_JDBC_SRIVER;
        String url = String.format(Locale.ENGLISH, "jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", host, port, database);
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException exp) {
            exp.printStackTrace();
        }
        return connection;
    }
}

