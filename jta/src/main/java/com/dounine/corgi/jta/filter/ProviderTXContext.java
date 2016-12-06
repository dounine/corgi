package com.dounine.corgi.jta.filter;

import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Created by huanghuanlai on 2016/11/23.
 */
public class ProviderTXContext {

    private static final ThreadLocal<TransactionStatus> TRANSACTION_STATUS_THREAD_LOCAL = new ThreadLocal<>();

    public static final TransactionStatus create(JtaTransactionManager jtm){
        return create(jtm,30);
    }

    public static final TransactionStatus create(JtaTransactionManager jtm, int timeout){
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setTimeout(timeout);
        TRANSACTION_STATUS_THREAD_LOCAL.set(jtm.getTransaction(def));
        return TRANSACTION_STATUS_THREAD_LOCAL.get();
    }

    public static final TransactionStatus get(){
        return TRANSACTION_STATUS_THREAD_LOCAL.get();
    }
}
