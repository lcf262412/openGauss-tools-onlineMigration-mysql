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

import java.sql.Connection;

/**
 * Description: WorkThreadTest class
 * @author douxin
 * @date 2022/09/06
 **/
public class WorkThreadTest {
    @Test
    public void test() {
        ConnectionInfo connectionInfo = new ConnectionInfo();
        WorkThread workThread = new WorkThread(connectionInfo);
        Assert.assertNotNull(workThread);
        Transaction transaction = new Transaction("gtid");
        workThread.setTransaction(transaction);
        Assert.assertEquals("gtid", workThread.getTransaction().getGtid());
        workThread.cleanTransaction();
        Assert.assertNull(workThread.getTransaction());
    }
}
