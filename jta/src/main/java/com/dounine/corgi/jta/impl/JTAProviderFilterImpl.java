package com.dounine.corgi.jta.impl;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.dounine.corgi.context.TokenContext;
import com.dounine.corgi.filter.ProviderFilter;
import com.dounine.corgi.jta.ProviderJTATXContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.*;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by huanghuanlai on 2016/11/16.
 */
public class JTAProviderFilterImpl implements ProviderFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JTAProviderFilterImpl.class);
    private static final Map<String, TxObj> TX_OBJ_MAP = new HashMap<>();

    @Autowired
    private UserTransactionManager utm;

    @Override
    public void invokeBefore(Method method, Object object, Object[] args) {
        if (checkTxTransaction(method)) {
            try {
                utm.begin();
            } catch (SystemException e) {
                e.printStackTrace();
            } catch (NotSupportedException e) {
                e.printStackTrace();
            }
            LOGGER.info("CORGI JTA create method tx.");
        }
    }

    @Override
    public void invokeAfter(Method method,Object result) {
        if (checkTxTransaction(method)) {
            Transaction transaction = null;
            try {
                transaction = utm.suspend();
            } catch (SystemException e) {
                e.printStackTrace();
            }
            String txId = TokenContext.get();
            TxObj txObj = new TxObj(transaction, LocalDateTime.now());
            TX_OBJ_MAP.put(txId, txObj);
            LOGGER.info("CORGI JTA waiting tx commit or rollback.");
        }
    }

    public void callback(String txTypeStr) throws Exception {
        String txId = TokenContext.get();
        Optional<String> txObjOpts = TX_OBJ_MAP.keySet().stream().filter(id -> txId.equals(id)).findFirst();
        TxType txType = null;
        if ("commit".equals(txTypeStr)) {
            txType = TxType.COMMIT;
        } else if ("rollback".equals(txTypeStr)) {
            txType = TxType.ROLLBACK;
        } else {
            throw new Exception("CORGI txType not empty.");
        }
        if (txObjOpts.isPresent()) {
            utm.resume(TX_OBJ_MAP.get(txObjOpts.get()).getTs());
            switch (txType){
                case COMMIT:
                    utm.commit();
                    break;
                case ROLLBACK:
                    utm.rollback();
                    break;
            }
            LOGGER.info("CORGI JTA exec [ " + txType + " ] tx.");
        } else {
            throw new Exception("CORGI txId:" + txId + " not found.");
        }
    }

    @Override
    public void invokeError(Throwable throwable) {
        try {
            utm.rollback();
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }
}
