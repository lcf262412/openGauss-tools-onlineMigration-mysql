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

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import org.opengauss.migration.event.MyGtidEventDataDeserializer;
import org.opengauss.migration.vo.ConnectionInfo;
import org.opengauss.migration.vo.Transaction;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Description: BinlogEventHandler class
 * @author douxin
 * @date 2022/08/09
 **/
public class BinlogListener {
    private BinaryLogClient client;
    private EventDeserializer eventDeserializer;
    private BinlogEventHandler binlogEventHandler;

    /**
     * Constructor
     *
     * @param ConnectionInfo the connection info
     * @param ConcurrentLinkedDeque<Transaction> the trx queue
     */
    public BinlogListener(ConnectionInfo connectionInfo, ConcurrentLinkedDeque<Transaction> trxQueue) {
        this.client = new BinaryLogClient(connectionInfo.getHost(),
                connectionInfo.getPort(),
                connectionInfo.getDatabase(),
                connectionInfo.getUsername(),
                connectionInfo.getPassword());
        this.binlogEventHandler = new BinlogEventHandler(trxQueue, connectionInfo);
    }

    private void initialEventDeserializer() {
        eventDeserializer = new EventDeserializer();
        eventDeserializer.setEventDataDeserializer(EventType.GTID, new MyGtidEventDataDeserializer());
        eventDeserializer.setCompatibilityMode(
                EventDeserializer.CompatibilityMode.DATE_AND_TIME_AS_LONG
        );
    }

    /**
     * Listen binlog
     */
    public void listenBinlog() {
        initialEventDeserializer();
        client.setEventDeserializer(eventDeserializer);
        client.registerEventListener(new BinaryLogClient.EventListener() {
            @Override
            public void onEvent(Event event) {
                binlogEventHandler.handleEvent(event);
            }
        });
        new Thread(() -> {
            try {
                client.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
