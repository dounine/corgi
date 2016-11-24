package com.dounine.corgi.jta.filter.impl;

import com.dounine.corgi.filter.ProviderFilter;
import com.dounine.corgi.jta.filter.ProviderTXContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by huanghuanlai on 2016/11/16.
 */
@Component
public class JTAProviderFilterImpl implements ProviderFilter {

    private static final Map<String, TxObj> TX_OBJ_MAP = new HashMap<>();

    @Autowired
    private JpaTransactionManager jtm;

    @Override
    public void invokeBefore(Method method, Object object, Object[] args) {
        ProviderTXContext.create(jtm);
    }

    @Override
    public void invokeAfter(Object result, String txId) {
        TxObj txObj = new TxObj(jtm, ProviderTXContext.get(), LocalDateTime.now());
        new Thread(txObj);
        TX_OBJ_MAP.put(txId, txObj);
    }

    public void callback(String txTypeStr, String txId) throws Exception {
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
        } else {
            throw new Exception("CORGI txId:" + txId + " not found.");
        }
    }

    @Override
    public void invokeError(Throwable throwable) {
        jtm.rollback(ProviderTXContext.get());
    }
}
