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

package org.opengauss.migration.util;

import org.opengauss.migration.vo.ConnectionInfo;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: Config tools class
 * @author douxin
 * @date 2022/09/03
 **/
public class ConfigTools {
    /**
     * Config path
     */
    public static final String CONFIG_PATH = "src/main/resources/config.yml";

    /**
     * OpenGauss connection
     */
    public static final String OPENGAUSS_CONN = "openGauss_conn";

    /**
     * Mysql connection
     */
    public static final String MYSQL_CONN = "mysql_conn";

    /**
     * Config map
     */
    public static Map<String, Object> configMap = new HashMap<>();

    /**
     * Load yaml file
     *
     * @throws FileNotFoundException file not found exception
     */
    public static void loadYml() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(CONFIG_PATH);
        Yaml yaml = new Yaml();
        configMap = (Map<String, Object>) yaml.load(inputStream);
    }

    /**
     * Get connection
     *
     * @param int the database type, 0 is mysql and other is openGauss
     * @return ConnectionInfo the connection info
     */
    public static ConnectionInfo getConn(int databaseType) {
        ConnectionInfo connectionInfo = new ConnectionInfo();
        HashMap<String, String> connHashMap;
        if (databaseType == 0) {
            connHashMap = (HashMap<String, String>) configMap.get(MYSQL_CONN);
        } else {
            connHashMap = (HashMap<String, String>) configMap.get(OPENGAUSS_CONN);
        }
        connectionInfo.setHost(connHashMap.get("host"));
        connectionInfo.setPort(Integer.valueOf(connHashMap.get("port")));
        connectionInfo.setUsername(connHashMap.get("user"));
        connectionInfo.setPassword(connHashMap.get("password"));
        connectionInfo.setDatabase(connHashMap.get("database"));
        return connectionInfo;
    }
}
