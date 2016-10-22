package com.dounine.corgi.spring;

/**
 * Created by huanghuanlai on 16/9/3.
 */
public class ApplicationContext {

    private static org.springframework.context.ApplicationContext context;

    public static void setApplicationContext(org.springframework.context.ApplicationContext aac){
        ApplicationContext.context = aac;
    }

    public static org.springframework.context.ApplicationContext getContext(){
        return context;
    }

}
