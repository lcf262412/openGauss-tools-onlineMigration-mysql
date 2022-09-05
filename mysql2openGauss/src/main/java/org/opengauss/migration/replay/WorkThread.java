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

package org.opengauss.migration.replay;

import org.opengauss.migration.vo.ConnectionInfo;
import org.opengauss.migration.vo.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Description: WorkThread class
 * @author douxin
 * @date 2022/08/09
 **/
public class WorkThread extends Thread{
    private ConnectionInfo connectionInfo;
    private Transaction transaction = null;
    private final Object lock = new Object();

    /**
     * Constructor
     *
     * @param ConnectionInfo the connection info
     */
    public WorkThread (ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    /**
     * Sets transaction
     *
     * @param Transaction the transaction
     */
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    /**
     * Gets transaction
     *
     * @return Transaction the transaction
     */
    public Transaction getTransaction() {
        return this.transaction;
    }

    /**
     * Clean transaction
     */
    public void cleanTransaction() {
        this.transaction = null;
    }

    /**
     * Resumen thread
     *
     * @param Transaction the transaction
     */
    public void resumeThread(Transaction transaction) {
        synchronized (lock) {
            setTransaction(transaction);
            lock.notify();
        }
    }

    /**
     * Pause thread
     */
    public void pauseThread() {
        synchronized (lock) {
            try {
                cleanTransaction();
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run () {
        try (Connection connection = connectionInfo.createOpenGaussConnection();
             Statement statement = connection.createStatement()){
            while (true) {
                pauseThread();
                for (String sql : transaction.getSqlList()) {
                    statement.execute(sql);
                }
            }
        } catch (SQLException exp) {
            exp.printStackTrace();
        }
    }
}
