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


import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Description: DataRunnable class
 *
 * @author zhangyaozhong
 * @date 2022/09/09
 **/
public class DataThread extends Thread {
    private final BlockingQueue<OpenGaussLogicData> queue = new LinkedBlockingQueue(Integer.MAX_VALUE);

    static String INSERT = "INSERT";
    static String UPDATE = "UPDATE";
    static String DELETE = "DELETE";

    /**
     * run Thread
     *
     */
    @Override
    public void run() {
        Statement stmt = mysqlconnection.main();
        OpenGaussLogicData logicData = null;
        while (true) {
            try {
                logicData = queue.take();
                stmt.execute(insertMysql(logicData));
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Thread addData
     *
     * @param sql openGauss's sql object
     */
    public void addData(OpenGaussLogicData sql) {
        queue.add(sql);
    }

    /**
     * parse the mysql's sql
     *
     * @param sqlObj openGauss's sql object
     */
    public static String insertMysql(OpenGaussLogicData sqlObj) {

        String sql = "";
        if (INSERT.equals(sqlObj.opType)) {
            sql = "INSERT INTO " + sqlObj.tableName + "(" + StringUtils.join(sqlObj.columnsName, ',') + ") VALUES(" + StringUtils.join(sqlObj.columnsVal, ',') + ");";
        }
        if (UPDATE.equals(sqlObj.opType)) {
            sql = "UPDATE " + sqlObj.tableName + " set " + getKeyValue(sqlObj.columnsName, sqlObj.columnsVal) + " WHERE (" + StringUtils.join(sqlObj.oldKeysName, ',') + ") = (" + StringUtils.join(sqlObj.oldKeysVal, ',') + ");";
        }

        if (DELETE.equals(sqlObj.opType)) {
            sql = "DELETE FROM " + sqlObj.tableName + " WHERE (" + StringUtils.join(sqlObj.oldKeysName, ',') + ") = (" + StringUtils.join(sqlObj.oldKeysVal, ',') + ");";
        }
        return sql;
    }

    private static String getKeyValue(List keys, List values) {
        String sql = "";
        for (int i = 0; i < keys.size(); i++) {
            sql = sql + keys.get(i) + "=" + values.get(i) + ",";
        }
        sql = sql.substring(0, sql.length() - 1);
        return sql;
    }

}


