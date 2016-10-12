package com.dounine.corgi.rpc.serialize;

/**
 * Created by huanghuanlai on 16/9/26.
 */
public interface Result {

    Object data();

    Throwable exception();

    boolean hasError();

}
