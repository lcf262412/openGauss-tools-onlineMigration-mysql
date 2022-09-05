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

import com.github.shyiko.mysql.binlog.event.deserialization.GtidEventDataDeserializer;
import com.github.shyiko.mysql.binlog.io.ByteArrayInputStream;

import java.io.IOException;

/**
 * Description: MyGtidEventDataDeserializer class
 * @author douxin
 * @date 2022/08/09
 **/
public class MyGtidEventDataDeserializer extends GtidEventDataDeserializer {
    @Override
    public MyGtidEventData deserialize(ByteArrayInputStream inputStream) throws IOException {
        MyGtidEventData eventData = new MyGtidEventData();
        byte flags = (byte) inputStream.readInteger(1);
        byte[] sid = inputStream.read(16);
        long gno = inputStream.readLong(8);
        eventData.setFlags(flags);
        byte ltType = (byte) inputStream.readInteger(1);
        if (ltType == 2) {
            long lastCommitted = inputStream.readLong(8);
            long sequenceNumber = inputStream.readLong(8);
            eventData.setLastCommitted(lastCommitted);
            eventData.setSequenceNumber(sequenceNumber);
        }
        return eventData;
    }
}
