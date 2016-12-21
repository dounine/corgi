package com.dounine.corgi.jta;

import org.apache.commons.io.FileUtils;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.transaction.Transaction;

/**
 * Created by huanghuanlai on 2016/11/23.
 */
public class ProviderJTATXContext {

    private static final ThreadLocal<Transaction> TRANSACTION_STATUS_THREAD_LOCAL = new ThreadLocal<>();

    public static final Transaction create(Transaction transaction){
        TRANSACTION_STATUS_THREAD_LOCAL.set(transaction);
        return TRANSACTION_STATUS_THREAD_LOCAL.get();
    }

    public static final Transaction get(){
        return TRANSACTION_STATUS_THREAD_LOCAL.get();
    }
}
