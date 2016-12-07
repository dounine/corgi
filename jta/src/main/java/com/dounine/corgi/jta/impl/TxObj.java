package com.dounine.corgi.jta.impl;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.jta.JtaTransactionManager;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

/**
 * Created by huanghuanlai on 2016/11/24.
 */
public class TxObj implements Runnable{

    private JtaTransactionManager jtm;
    private TransactionStatus ts;
    private LocalDateTime createTime;
    private CountDownLatch finish = new CountDownLatch(1);
    private TxType type;

    public TxObj(JtaTransactionManager jtm, TransactionStatus ts, LocalDateTime createTime){
        this.jtm = jtm;
        this.ts = ts;
        this.createTime = createTime;
    }

    @Override
    public void run() {
        try {
            finish.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(null!=type){
            if(type.equals(TxType.COMMIT)){
                jtm.commit(ts);
            }else if(type.equals(TxType.ROLLBACK)){
                jtm.rollback(ts);
            }
        }
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public JtaTransactionManager getJtm() {
        return jtm;
    }

    public void setJtm(JtaTransactionManager jtm) {
        this.jtm = jtm;
    }

    public TransactionStatus getTs() {
        return ts;
    }

    public void setTs(TransactionStatus ts) {
        this.ts = ts;
    }

    public void begin(final TxType type) throws Exception {
        if(null==type){
            throw new Exception("CORGI TxType not empty");
        }
        this.type = type;
        this.finish.countDown();
    }
}
