package com.dounine.corgi.remoting;

/**
 * Created by huanghuanlai on 16/9/26.
 */
public interface Result {

    Object result() throws Throwable;

    Throwable exception();

    boolean hasException();

}
