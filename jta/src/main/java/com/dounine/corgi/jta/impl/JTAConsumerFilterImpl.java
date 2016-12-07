package com.dounine.corgi.jta.impl;

import com.dounine.corgi.filter.ConsumerFilter;
import com.dounine.corgi.remoting.FetchRemoting;
import com.dounine.corgi.remoting.FetchToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huanghuanlai on 2016/11/24.
 */
public class JTAConsumerFilterImpl implements ConsumerFilter {

    private static final ThreadLocal<List<JTATX>> CURRENT_THREAD_TXS = new ThreadLocal<>();

    @Override
    public void execTransaction(FetchRemoting client, FetchToken fetchToken, String txType) {
        if(fetchToken.checkCommit()){//提供者所提供的接口需要提交事务
            List<JTATX> jtatxes = CURRENT_THREAD_TXS.get();
            if(null==jtatxes){
                CURRENT_THREAD_TXS.set(new ArrayList<>());
            }
            CURRENT_THREAD_TXS.get().add(new JTATX(client,fetchToken,txType));
        }
    }

    public List<JTATX> getJtaTxs(){
        return CURRENT_THREAD_TXS.get()==null?new ArrayList<>(0):CURRENT_THREAD_TXS.get();
    }
}
