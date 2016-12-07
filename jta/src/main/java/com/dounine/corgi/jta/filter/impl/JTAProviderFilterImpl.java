package com.dounine.corgi.jta.filter.impl;

import com.dounine.corgi.context.TokenContext;
import com.dounine.corgi.filter.ProviderFilter;
import com.dounine.corgi.jta.filter.ProviderTXContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.jta.JtaTransactionManager;

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
    protected JtaTransactionManager jtm;

    @Override
    public void invokeBefore(Method method, Object object, Object[] args) {
        if (checkTxTransaction(method)) {
            ProviderTXContext.create(jtm);
            LOGGER.info("CORGI JTA create method tx.");
        }
    }

    @Override
    public void invokeAfter(Method method,Object result) {
        if (checkTxTransaction(method)) {
            String txId = TokenContext.get();
            TxObj txObj = new TxObj(jtm, ProviderTXContext.get(), LocalDateTime.now());
            new Thread(txObj);
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
            TX_OBJ_MAP.get(txObjOpts.get()).begin(txType);
            LOGGER.info("CORGI JTA exec [ " + txType + " ] tx.");
        } else {
            throw new Exception("CORGI txId:" + txId + " not found.");
        }
    }

    @Override
    public void invokeError(Throwable throwable) {
        jtm.rollback(ProviderTXContext.get());
    }
}
