package com.dounine.corgi.rpc.serialize.result;

/**
 * Created by huanghuanlai on 16/9/26.
 */
public interface IResult {

    Object result() throws Throwable;

    Throwable exception();

    boolean hasException();

}
