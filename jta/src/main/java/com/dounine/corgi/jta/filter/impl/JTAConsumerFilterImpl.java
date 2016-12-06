package com.dounine.corgi.jta.filter.impl;

import com.dounine.corgi.filter.ConsumerFilter;
import com.dounine.corgi.jta.filter.ProviderTXContext;
import com.dounine.corgi.remoting.FetchRemoting;
import com.dounine.corgi.remoting.FetchToken;
import com.dounine.corgi.remoting.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huanghuanlai on 2016/11/24.
 */
public class JTAConsumerFilterImpl implements ConsumerFilter {

    private static final ThreadLocal<List<JTATX>> CURRENT_THREAD_TXS = new ThreadLocal<>();

    @Override
    public void execTransaction(FetchRemoting client, FetchToken fetchToken, String txType) {
        List<JTATX> jtatxes = CURRENT_THREAD_TXS.get();
        if(null==jtatxes){
            CURRENT_THREAD_TXS.set(new ArrayList<>());
        }
        CURRENT_THREAD_TXS.get().add(new JTATX(client,fetchToken,txType));
        //client.txCall(fetchToken,txType);
    }

    public List<JTATX> getJtaTxs(){
        return CURRENT_THREAD_TXS.get();
    }
}
