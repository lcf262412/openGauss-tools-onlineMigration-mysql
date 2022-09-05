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

import org.junit.Assert;
import org.junit.Test;

/**
 * Description: ConnectionInfo class
 * @author douxin
 * @date 2022/09/06
 **/
public class ConnectionInfoTest {
    @Test
    public void test() {
        ConnectionInfo connectionInfo = new ConnectionInfo();
        connectionInfo.setHost("127.0.0.1");
        connectionInfo.setPort(5432);
        connectionInfo.setDatabase("postgres");
        connectionInfo.setUsername("opengauss");
        connectionInfo.setPassword("password123");
        Assert.assertEquals("127.0.0.1", connectionInfo.getHost());
        Assert.assertEquals(5432, connectionInfo.getPort());
        Assert.assertEquals("postgres", connectionInfo.getDatabase());
        Assert.assertEquals("opengauss", connectionInfo.getUsername());
        Assert.assertEquals("password123", connectionInfo.getPassword());
        Assert.assertEquals("org.opengauss.Driver", ConnectionInfo.OPENGAUSS_JDBC_DRIVER);
        Assert.assertEquals("com.mysql.jdbc.Driver", ConnectionInfo.MYSQL_JDBC_SRIVER);
        connectionInfo = new ConnectionInfo("127.0.0.1", 3306, "mysql", "mysql", "password123");
        Assert.assertEquals(3306, connectionInfo.getPort());
    }
}
