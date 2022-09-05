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

import com.github.shyiko.mysql.binlog.event.*;

import java.util.ArrayList;

/**
 * Description: Transaction class
 * @author douxin
 * @date 2022/08/09
 **/
public class Transaction {
    private String gtid;
    private long lastCommitted;
    private long sequenceNumber;
    private String binlogFile;
    private long binlogPosition;
    private ArrayList<Event> eventList = new ArrayList<>();
    private ArrayList<String> sqlList = new ArrayList<>();
    private boolean isDml = true;

    /**
     * Constructor
     *
     * @param String the gtid
     */
    public Transaction(String gtid) {
        this.gtid = gtid;
    }

    /**
     * Constructor
     *
     * @param String the gtid
     * @param String the last_committed
     * @param String the sequence_number
     */
    public Transaction(String gtid, long lastCommitted, long sequenceNumber) {
        this.gtid = gtid;
        this.lastCommitted = lastCommitted;
        this.sequenceNumber = sequenceNumber;
    };

    /**
     * Sets gtid
     *
     * @param String the gtid
     */
    public void setGtid(String gtid) {
        this.gtid = gtid;
    }

    /**
     * Gets gtid
     *
     * @return String the gtid
     */
    public String getGtid() {
        return gtid;
    }

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
        return lastCommitted;
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
        return sequenceNumber;
    }

    /**
     * Sets binlog file
     *
     * @param String the binlog file
     */
    public void setBinlogFile(String binlogFile) {
        this.binlogFile = binlogFile;
    }

    /**
     * Gets binlog file
     *
     * @return String the binlog file
     */
    public String getBinlogFile() {
        return binlogFile;
    }

    /**
     * Sets binlog position
     *
     * @param long the binlog position
     */
    public void setBinlogPosition(long binlogPosition) {
        this.binlogPosition = binlogPosition;
    }

    /**
     * Gets binlog position
     *
     * @return long the binlog position
     */
    public long getBinlogPosition() {
        return binlogPosition;
    }

    /**
     * Sets event list
     *
     * @param ArrayList<Event> the event list
     */
    public void setEventList(ArrayList<Event> eventList) {
        this.eventList = eventList;
    }

    /**
     * Gets event list
     *
     * @return ArrayList<Event> the event list
     */
    public ArrayList<Event> getEventList() {
        return eventList;
    }

    /**
     * Sets sql list
     *
     * @param ArrayList<String> the sql list
     */
    public void setSqlList(ArrayList<String> sqlList) {
        this.sqlList = sqlList;
    }

    /**
     * Gets sql list
     *
     * @return ArrayList<String> the sql list
     */
    public ArrayList<String> getSqlList() {
        return sqlList;
    }

    /**
     * Sets is dml
     *
     * @param boolean true if is dml
     */
    public void setIsDml(boolean dml) {
        isDml = dml;
    }

    /**
     * Gets is dml
     *
     * @return boolean true if is dml
     */
    public boolean getIsDml() {
        return isDml;
    }


    /**
     * interleave with other transaction
     *
     * @param Transaction the other transaction
     * @return boolean true if can interleave with other transaction
     */
    public boolean interleaved(Transaction other) {
        return other.getSequenceNumber() > this.lastCommitted;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "gtid='" + gtid + '\'' +
                ", lastCommitted=" + lastCommitted +
                ", sequenceNumber=" + sequenceNumber +
                ", binlogFile='" + binlogFile + '\'' +
                ", binlogPosition=" + binlogPosition +
                ", eventList=" + eventList +
                ", sqlList=" + sqlList +
                ", isDml=" + isDml +
                '}';
    }
}

