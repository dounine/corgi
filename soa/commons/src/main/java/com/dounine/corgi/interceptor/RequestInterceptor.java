package com.dounine.corgi.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Created by huanghuanlai on 16/6/20.
 */
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestInterceptor.class);
    private static final String STARTTIME = "startTime";

    /**
     * Servlet请求超过指定时间作提示
     */
    private static final int ACTIONE_SECONDS = 2;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(STARTTIME, LocalDateTime.now());
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LocalDateTime startTime = LocalDateTime.parse(request.getAttribute(STARTTIME).toString());
        long seconds = Duration.between(startTime,LocalDateTime.now()).getSeconds();
        if(seconds>=ACTIONE_SECONDS){
            LOGGER.info(request.getRequestURI()+" 处理时间为 => "+ seconds+"s");
        }
        super.afterCompletion(request, response, handler, ex);
    }
}
