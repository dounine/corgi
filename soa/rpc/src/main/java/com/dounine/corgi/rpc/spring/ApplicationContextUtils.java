package com.dounine.corgi.rpc.spring;

import org.springframework.context.ApplicationContext;

/**
 * Created by huanghuanlai on 16/9/3.
 */
public class ApplicationContextUtils{

    private static ApplicationContext context;

    public static void setApplicationContext(ApplicationContext aac){
        ApplicationContextUtils.context = aac;
    }

    public static ApplicationContext getContext(){
        return context;
    }

}
