package com.dounine.corgi.context.request;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by huanghuanlai on 16/6/20.
 */
public final class RequestContext {

    private RequestContext(){}

    public static HttpServletRequest get(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}

