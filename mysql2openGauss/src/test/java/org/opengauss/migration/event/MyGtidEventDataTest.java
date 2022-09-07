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

package org.opengauss.migration.event;

import org.junit.Assert;
import org.junit.Test;

/**
 * Description: MyGtidEventDataTest class
 * @author douxin
 * @date 2022/09/06
 **/
public class MyGtidEventDataTest {
    @Test
    public void test() {
        MyGtidEventData myGtidEventData = new MyGtidEventData();
        myGtidEventData.setLastCommitted(1);
        myGtidEventData.setSequenceNumber(2);
        Assert.assertEquals(1, myGtidEventData.getLastCommitted());
        Assert.assertEquals(2, myGtidEventData.getSequenceNumber());
        Assert.assertTrue(myGtidEventData.toString().contains("lastCommitted"));
    }
}
