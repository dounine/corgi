package com.dounine.corgi.remoting;

/**
 * Created by huanghuanlai on 2016/10/23.
 */
public interface FetchRemoting {

    Result fetchResult(FetchToken fetchToken);

    FetchToken fetchToken(String txId);

    void txCall(FetchToken fetchToken,String txType);

}
