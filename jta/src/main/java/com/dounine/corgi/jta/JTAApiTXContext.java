package com.dounine.corgi.jta;

import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Created by huanghuanlai on 2016/11/23.
 */
public class JTAApiTXContext {

    private static final ThreadLocal<TransactionStatus> TRANSACTION_STATUS_THREAD_LOCAL = new ThreadLocal<>();

    public static final void create(JpaTransactionManager jtm){
        create(jtm,30);
    }

    public static final void create(JpaTransactionManager jtm, int timeout){
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setTimeout(timeout);
        TRANSACTION_STATUS_THREAD_LOCAL.set(jtm.getTransaction(def));
    }

    public static final TransactionStatus get(){
        return TRANSACTION_STATUS_THREAD_LOCAL.get();
    }
}
