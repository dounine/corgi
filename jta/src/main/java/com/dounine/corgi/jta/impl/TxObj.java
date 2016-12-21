package com.dounine.corgi.jta.impl;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.*;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

/**
 * Created by huanghuanlai on 2016/11/24.
 */
public class TxObj{

    private Transaction ts;
    private LocalDateTime createTime;
    private TxType type;

    public TxObj(Transaction ts, LocalDateTime createTime){
        this.ts = ts;
        this.createTime = createTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Transaction getTs() {
        return ts;
    }

    public void setTs(Transaction ts) {
        this.ts = ts;
    }

}
