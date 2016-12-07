package com.dounine.corgi.filter;

import java.lang.reflect.Method;

/**
 * Created by huanghuanlai on 2016/12/6.
 */
public interface ProviderTxFilter {

    boolean checkTxAnnotation(Method method);

}
