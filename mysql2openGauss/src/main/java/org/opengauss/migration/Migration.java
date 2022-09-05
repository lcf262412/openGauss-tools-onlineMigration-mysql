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

package org.opengauss.migration;

import org.opengauss.migration.binlog.BinlogListener;
import org.opengauss.migration.replay.TransactionDispatcher;
import org.opengauss.migration.util.ConfigTools;
import org.opengauss.migration.vo.ConnectionInfo;
import org.opengauss.migration.vo.Transaction;

import java.io.FileNotFoundException;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Description: Migration class
 * @author douxin
 * @date 2022/08/09
 **/
public class Migration {
    public static void main(String[] args) {
        ConcurrentLinkedDeque<Transaction> trxQueue = new ConcurrentLinkedDeque<>();
        ConnectionInfo mysqlConnectionInfo;
        ConnectionInfo openGaussConnectionInfo;

        try {
            ConfigTools.loadYml();
            mysqlConnectionInfo = ConfigTools.getConn(0);
            openGaussConnectionInfo = ConfigTools.getConn(1);
        } catch (FileNotFoundException exp) {
            System.out.println("The configuration file was not found, so the online migration program exited");
            return;
        }

        BinlogListener binlogListener = new BinlogListener(mysqlConnectionInfo, trxQueue);
        binlogListener.listenBinlog();

        TransactionDispatcher transactionDispatcher = new TransactionDispatcher(50, openGaussConnectionInfo, trxQueue);
        transactionDispatcher.dispatcher();
    }
}
