package com.dounine.corgi.rpc.spring;

import org.springframework.context.support.AbstractApplicationContext;

/**
 * Created by huanghuanlai on 16/9/3.
 */
public class ApplicationBeanUtils{

    private static AbstractApplicationContext aac;

    public static void setApplicationContext(AbstractApplicationContext aac){
        ApplicationBeanUtils.aac = aac;
    }

    public static AbstractApplicationContext getAac(){
        return aac;
    }

}
