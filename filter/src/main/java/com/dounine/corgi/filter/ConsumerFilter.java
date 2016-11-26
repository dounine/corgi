package com.dounine.corgi.filter;

import com.dounine.corgi.remoting.FetchRemoting;
import com.dounine.corgi.remoting.FetchToken;
import com.dounine.corgi.remoting.Result;

/**
 * Created by huanghuanlai on 2016/11/16.
 */
public interface ConsumerFilter {

    default FetchToken getToken(FetchRemoting client){
        return client.fetchToken();
    }

    default Result getResult(FetchRemoting client, FetchToken token){
        return client.fetch(token);
    }

    default void execTransaction(FetchRemoting client,FetchToken fetchToken, String txType){
        //client.txCall(fetchToken,"rollback");
    }
}
