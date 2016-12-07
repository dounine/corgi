package com.dounine.corgi.jta.impl;

import com.dounine.corgi.remoting.FetchRemoting;
import com.dounine.corgi.remoting.FetchToken;

/**
 * Created by huanghuanlai on 2016/12/6.
 */
public class JTATX {
    private FetchRemoting client;
    private FetchToken fetchToken;
    private String txType;

    public JTATX(FetchRemoting client, FetchToken fetchToken, String txType) {
        this.client = client;
        this.fetchToken = fetchToken;
        this.txType = txType;
    }

    public FetchRemoting getClient() {
        return client;
    }

    public void setClient(FetchRemoting client) {
        this.client = client;
    }

    public FetchToken getFetchToken() {
        return fetchToken;
    }

    public void setFetchToken(FetchToken fetchToken) {
        this.fetchToken = fetchToken;
    }

    public String getTxType() {
        return txType;
    }

    public void setTxType(String txType) {
        this.txType = txType;
    }
}
