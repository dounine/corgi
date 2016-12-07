package com.dounine.corgi.filter.impl;

import com.dounine.corgi.filter.ProviderTxFilter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * Created by huanghuanlai on 2016/12/6.
 */
@Component
public class DefaultProviderTxFilter implements ProviderTxFilter {
    @Override
    public boolean checkTxAnnotation(Method method) {
        return method.isAnnotationPresent(Transactional.class);
    }
}
