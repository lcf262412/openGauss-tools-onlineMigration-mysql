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

import com.github.shyiko.mysql.binlog.event.GtidEventData;

/**
 * Description: MyGtidEventData class
 * @author douxin
 * @date 2022/08/09
 **/
public class MyGtidEventData extends GtidEventData {
    private long lastCommitted;
    private long sequenceNumber;

    /**
     * Sets last_committed
     *
     * @param long the last_committed
     */
    public void setLastCommitted(long lastCommitted) {
        this.lastCommitted = lastCommitted;
    }

    /**
     * Gets last_committed
     *
     * @return long the last_committed
     */
    public long getLastCommitted() {
        return this.lastCommitted;
    }

    /**
     * Sets sequence_number
     *
     * @param long the sequence_number
     */
    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Gets sequence_number
     *
     * @return long the sequence_number
     */
    public long getSequenceNumber() {
        return this.sequenceNumber;
    }

    /**
     * To string method
     *
     * @return String the string
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("GtidEventData");
        sb.append("{flags=").append(getFlags()).append(", gtid='").append(getGtid()).append('\'');
        sb.append(", lastCommitted = ").append(lastCommitted);
        sb.append(", sequenceNumber = ").append(sequenceNumber);
        sb.append('}');
        return sb.toString();
    }
}
