package com.dounine.corgi.rpc.serialize.result;

/**
 * Created by huanghuanlai on 16/9/26.
 */
public interface IResult {

    Object data() throws Throwable;

    Throwable exception();

    boolean hasException();

}
