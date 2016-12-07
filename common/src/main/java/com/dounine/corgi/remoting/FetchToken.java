package com.dounine.corgi.remoting;

/**
 * Created by huanghuanlai on 2016/11/7.
 */
public interface FetchToken {

    String getToken();

    String getAddress();

    boolean checkCommit();

    int getTimeout();

    int getRetries();

}
