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

import org.opengauss.migration.vo.ConnectionInfo;
import org.opengauss.migration.vo.Transaction;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Description: TransactionDispatcher class
 * @author douxin
 * @date 2022/08/09
 **/
public class TransactionDispatcher {
    /**
     * Default max thread count
     */
    public static final int MAX_THREAD_COUNT = 50;

    private int threadCount;
    private ConnectionInfo connectionInfo;
    private Transaction selectedTransaction = null;
    private ArrayList<WorkThread> threadList = new ArrayList<>();
    private ConcurrentLinkedDeque<Transaction> trxQueue;
    private BlockingDeque<Integer> queue = new LinkedBlockingDeque<>();
    private int count = 0;
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Constructor
     *
     * @param ConnectionInfo the connection info
     * @param ConcurrentLinkedDeque<Transaction> the trx queue
     */
    public TransactionDispatcher(ConnectionInfo connectionInfo, ConcurrentLinkedDeque<Transaction> trxQueue) {
        this.threadCount = MAX_THREAD_COUNT;
        this.connectionInfo = connectionInfo;
        this.trxQueue = trxQueue;
    }

    /**
     * Constructor
     *
     * @param int the thread count
     * @param ConnectionInfo the connection info
     * @param ConcurrentLinkedDeque<Transaction> the trx queue
     */
    public TransactionDispatcher(int threadCount, ConnectionInfo connectionInfo, ConcurrentLinkedDeque<Transaction> trxQueue) {
        this.threadCount = threadCount;
        this.connectionInfo = connectionInfo;
        this.trxQueue = trxQueue;
    }

    /**
     * Dispatcher
     */
    public void dispatcher() {
        createThreads();
        statTask();
        Transaction transaction = null;
        int freeThreadIndex = -1;
        while (true) {
            if (selectedTransaction == null) {
                transaction = trxQueue.poll();
                if (transaction != null) {
                    count++;
                } else {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException exp) {
                        exp.printStackTrace();
                    }
                }
            } else {
                transaction = selectedTransaction;
                selectedTransaction = null;
            }
            if (transaction != null) {
                freeThreadIndex = canParallelAndFindFreeThread(transaction, threadList);
                if (freeThreadIndex == -1) {
                    selectedTransaction = transaction;
                } else {
                    threadList.get(freeThreadIndex).resumeThread(transaction);
                }
            }
        }
    }

    private void createThreads() {
        for (int i = 0; i < threadCount; i++) {
            WorkThread workThread = new WorkThread(connectionInfo);
            threadList.add(workThread);
            workThread.start();
        }
    }

    private void statTask() {
        new Thread(() -> {
            int before = count;
            int delta = 0;
            while(true) {
                try {
                    Thread.sleep(1000);
                    delta = count - before;
                    before = count;
                    String date = df.format(new Date());
                    String result = String.format("have replayed %s transaction, and current time is %s, and current " +
                            "speed is %s", count, date, delta);
                    System.out.println(result);
                } catch (InterruptedException exp) {
                    exp.printStackTrace();
                }
            }
        }).start();
    }

    private int canParallelAndFindFreeThread(Transaction transaction, ArrayList<WorkThread> threadList) {
        int freeThreadIndex = -1;
        for (int i = 0; i < threadCount; i++) {
            Transaction runningTransaction = threadList.get(i).getTransaction();
            if (runningTransaction != null) {
                boolean canParallel = transaction.interleaved(runningTransaction);
                if (!canParallel) {
                    return -1;
                }
            } else {
                freeThreadIndex = i;
            }
        }
        return freeThreadIndex;
    }
}
