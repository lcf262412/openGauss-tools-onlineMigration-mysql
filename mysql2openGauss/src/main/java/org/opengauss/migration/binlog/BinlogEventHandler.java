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

package org.opengauss.migration.binlog;

import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventHeaderV4;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.QueryEventData;
import com.github.shyiko.mysql.binlog.event.RotateEventData;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import org.opengauss.migration.event.MyGtidEventData;
import org.opengauss.migration.util.SqlTools;
import org.opengauss.migration.vo.ColumnVo;
import org.opengauss.migration.vo.ConnectionInfo;
import org.opengauss.migration.vo.TableVo;
import org.opengauss.migration.vo.Transaction;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

/**
 * Description: BinlogEventHandler class
 * @author douxin
 * @date 2022/08/09
 **/
public class BinlogEventHandler {
    /**
     * Ddl pattern
     */
    public static Set<String> ddlPattern = new HashSet() {
        {
            add("create table");
            add("drop table");
            add("truncate table");
            add("alter table");
            add("create index");
            add("create unique index");
            add("drop index");
            add("alter index");
            add("create database");
            add("drop database");
            add("create user");
            add("drop user");
            add("rename");
        }
    };

    private ConcurrentLinkedDeque<Transaction> trxQueue;
    private HashMap<Long, String> tableNameHashMap = new HashMap<>();
    private HashMap<Long, TableVo> tableInfoMap = new HashMap<>();
    private String binlogFileName;
    private Transaction transaction= null;
    private HashMap<EventType, EventHandler> eventHandlerHashMap;
    private ConnectionInfo connectionInfo;
    private Connection connection;


    /**
     * Constructor
     *
     * @param ConcurrentLinkedDeque<Transaction> the trx queue
     * @param ConnectionInfo the connection info
     */
    public BinlogEventHandler(ConcurrentLinkedDeque<Transaction> trxQueue, ConnectionInfo connectionInfo) {
        this.trxQueue = trxQueue;
        this.connectionInfo = connectionInfo;
        this.connection = connectionInfo.createMysqlConnection();
        initializeEventHandleMap();
    }

    /**
     * Handle event
     *
     * @param Event the event
     */
    public void handleEvent(Event event) {
        EventType eventType = event.getHeader().getEventType();
        EventHandler eventHandler = eventHandlerHashMap.get(eventType);
        if (eventHandler != null) {
            eventHandler.handle(event);
        }
    }

    private void initializeEventHandleMap() {
        eventHandlerHashMap = new HashMap<>();
        eventHandlerHashMap.put(EventType.GTID, (event) -> gtidEventHandler(event));
        eventHandlerHashMap.put(EventType.ROTATE, (event) -> rotateEventHandler(event));
        eventHandlerHashMap.put(EventType.QUERY, (event) -> queryEventHandler(event));
        eventHandlerHashMap.put(EventType.TABLE_MAP, (event) -> tableMapEventHandler(event));
        eventHandlerHashMap.put(EventType.EXT_WRITE_ROWS, (event) -> rowEventHandler(event));
        eventHandlerHashMap.put(EventType.EXT_DELETE_ROWS, (event) -> rowEventHandler(event));
        eventHandlerHashMap.put(EventType.EXT_UPDATE_ROWS, (event) -> rowEventHandler(event));
        eventHandlerHashMap.put(EventType.XID, (event) -> xidEventHandler(event));
    }

    private void gtidEventHandler(Event event) {
        MyGtidEventData eventData = event.getData();
        transaction = new Transaction(eventData.getGtid());
        transaction.setBinlogFile(this.binlogFileName);
        transaction.setLastCommitted(eventData.getLastCommitted());
        transaction.setSequenceNumber(eventData.getSequenceNumber());
        if (event.getHeader() instanceof EventHeaderV4) {
            transaction.setBinlogPosition(((EventHeaderV4) event.getHeader()).getNextPosition());
        }
    }

    private void rotateEventHandler(Event event) {
        RotateEventData eventData = event.getData();
        binlogFileName = eventData.getBinlogFilename();
    }

    private void queryEventHandler(Event event) {
        QueryEventData eventData = event.getData();
        if (isDdl(eventData.getSql())) {
            ArrayList<String> sqlList = new ArrayList<>();
            sqlList.add(eventData.getSql());
            transaction.setSqlList(sqlList);
            transaction.setIsDml(false);
            trxQueue.add(transaction);
            transaction = null;
        }
    }

    private void tableMapEventHandler(Event event) {
        TableMapEventData eventData = event.getData();
        long tableId = eventData.getTableId();
        String tableSchema = eventData.getDatabase();
        String tableName = eventData.getTable();
        if (!tableNameHashMap.containsKey(tableId)) {
            tableNameHashMap.put(tableId, tableName);
            List<ColumnVo> columnVoList = new ArrayList<>();
            String sql = String.format(Locale.ENGLISH, "select column_name, data_type from " +
                            "information_schema.columns where table_schema = '%s' and table_name = '%s'" +
                            " order by ORDINAL_POSITION",
                    tableSchema, tableName);
            try (Statement statement = connection.createStatement();
                 ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    columnVoList.add(new ColumnVo(rs.getString("column_name"),
                            rs.getString("data_type")));
                }
                TableVo tableVo = new TableVo(tableId, tableSchema, tableName, columnVoList);
                tableInfoMap.put(tableId, tableVo);
            } catch (SQLException exp) {
                exp.printStackTrace();
            }
        }
    }

    private void rowEventHandler(Event event) {
        transaction.getEventList().add(event);
    }

    private void xidEventHandler(Event event) {
        fetchSql(transaction);
        trxQueue.add(transaction);
        transaction = null;
    }

    private void fetchSql(Transaction transaction) {
        for (Event event : transaction.getEventList()) {
            switch (event.getHeader().getEventType()) {
                case EXT_WRITE_ROWS:
                    insertSql(transaction, event);
                    break;
                case EXT_DELETE_ROWS:
                    deleteSql(transaction, event);
                    break;
                case EXT_UPDATE_ROWS:
                    updateSql(transaction, event);
                    break;
                default:
                    break;
            }
        }
    }

    private void insertSql(Transaction transaction, Event event) {
        WriteRowsEventData eventData = event.getData();
        long tableOid = eventData.getTableId();
        transaction.getSqlList().addAll(SqlTools.getInsertSql(tableInfoMap.get(tableOid), eventData.getRows()));
    }

    private void deleteSql(Transaction transaction, Event event) {
        DeleteRowsEventData eventData = event.getData();
        long tableOid = eventData.getTableId();
        transaction.getSqlList().addAll(SqlTools.getDeleteSql(tableInfoMap.get(tableOid), eventData.getRows()));
    }

    private void updateSql(Transaction transaction, Event event) {
        UpdateRowsEventData eventData = event.getData();
        long tableOid = eventData.getTableId();
        List<Serializable[]> multipleRowsBefore = eventData.getRows().stream().map(entry -> entry.getKey())
                .collect(Collectors.toList());
        List<Serializable[]> multipleRowsAfter = eventData.getRows().stream().map(entry -> entry.getValue())
                .collect(Collectors.toList());
        transaction.getSqlList().addAll(
                SqlTools.getUpdateSql(tableInfoMap.get(tableOid), multipleRowsBefore, multipleRowsAfter));
    }

    private boolean isDdl(String sql) {
        for (String ddl : ddlPattern) {
            if (sql.startsWith(ddl)) {
                return true;
            }
        }
        return false;
    }
}
