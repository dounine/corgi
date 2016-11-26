package com.dounine.corgi.remoting;

/**
 * Created by huanghuanlai on 2016/10/23.
 */
public interface FetchRemoting {

    Result fetch(FetchToken fetchToken);

    FetchToken fetchToken();

    void txCall(FetchToken fetchToken,String txType);

}
