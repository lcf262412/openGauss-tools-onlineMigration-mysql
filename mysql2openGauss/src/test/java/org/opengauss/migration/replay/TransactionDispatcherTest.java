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

import org.junit.Assert;
import org.junit.Test;
import org.opengauss.migration.vo.ConnectionInfo;
import org.opengauss.migration.vo.Transaction;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Description: TransactionDispatcherTest class
 * @author douxin
 * @date 2022/09/06
 **/
public class TransactionDispatcherTest {
    @Test
    public void test() {
        Assert.assertEquals(50, TransactionDispatcher.MAX_THREAD_COUNT);
        ConnectionInfo connectionInfo = new ConnectionInfo();
        ConcurrentLinkedDeque<Transaction> trxQueue = new ConcurrentLinkedDeque<>();
        TransactionDispatcher transactionDispatcher = new TransactionDispatcher(connectionInfo, trxQueue);
        Assert.assertNotNull(transactionDispatcher);
        transactionDispatcher = new TransactionDispatcher(30, connectionInfo, trxQueue);
        Assert.assertNotNull(transactionDispatcher);
    }
}
