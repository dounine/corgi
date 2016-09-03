package com.dounine.corgi.rpc.spring;

import org.springframework.context.support.AbstractApplicationContext;

/**
 * Created by huanghuanlai on 16/9/3.
 */
interface ApplicationBean {

    AbstractApplicationContext getApplicationContext();

}
