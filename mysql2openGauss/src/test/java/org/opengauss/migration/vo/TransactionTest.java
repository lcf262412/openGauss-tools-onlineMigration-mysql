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

import java.util.ArrayList;

/**
 * Description: TransactionTest class
 * @author douxin
 * @date 2022/09/06
 **/
public class TransactionTest {
    @Test
    public void test() {
        String gtid = "gtid";
        Transaction trx1 = new Transaction(gtid);
        trx1.setLastCommitted(1);
        trx1.setSequenceNumber(2);
        trx1.setGtid("new_gtid");
        trx1.setBinlogFile("file");
        trx1.setBinlogPosition(123);
        Assert.assertEquals(1, trx1.getLastCommitted());
        Assert.assertEquals(2, trx1.getSequenceNumber());
        Assert.assertEquals("new_gtid", trx1.getGtid());
        Assert.assertEquals("file", trx1.getBinlogFile());
        Assert.assertEquals(123, trx1.getBinlogPosition());
        Assert.assertTrue(trx1.getIsDml());
        trx1.setIsDml(false);
        Assert.assertFalse(trx1.getIsDml());
        ArrayList<String> sqlList = new ArrayList<>();
        sqlList.add("insert sql");
        sqlList.add("update sql");
        sqlList.add("delete sql");
        trx1.setSqlList(sqlList);
        Assert.assertEquals(3, trx1.getSqlList().size());
        Assert.assertTrue(trx1.toString().contains("1"));
    }

    @Test
    public void testInterleaved() {
        String gtid = "gtid";
        Transaction trx1 = new Transaction(gtid);
        trx1.setLastCommitted(1);
        trx1.setSequenceNumber(2);

        Transaction trx2 = new Transaction(gtid, 1, 3);
        Transaction trx3 = new Transaction(gtid, 3, 4);
        boolean canInterleaved = trx2.interleaved(trx1);
        Assert.assertTrue(canInterleaved);

        canInterleaved = trx3.interleaved(trx2);
        Assert.assertFalse(canInterleaved);
    }
}
