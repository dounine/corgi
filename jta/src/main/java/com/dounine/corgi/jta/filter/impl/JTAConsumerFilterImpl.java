package com.dounine.corgi.jta.filter.impl;

import com.dounine.corgi.filter.ConsumerFilter;
import com.dounine.corgi.jta.filter.ProviderTXContext;
import com.dounine.corgi.remoting.FetchRemoting;
import com.dounine.corgi.remoting.FetchToken;
import com.dounine.corgi.remoting.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;

import java.lang.reflect.Method;

/**
 * Created by huanghuanlai on 2016/11/24.
 */
public class JTAConsumerFilterImpl implements ConsumerFilter {
    @Autowired
    private JpaTransactionManager jtm;

}
