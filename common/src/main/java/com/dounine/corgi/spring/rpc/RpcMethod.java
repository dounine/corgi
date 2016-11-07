package com.dounine.corgi.spring.rpc;

import java.lang.annotation.*;

/**
 * Created by huanghuanlai on 16/10/10.
 */
@org.springframework.stereotype.Service
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcMethod {
    public static final int TIMEOUT = 3000;
    public static final int RETRIES = 2;

    String value() default "";
    int timeout() default TIMEOUT;
    int retries() default RETRIES;
}
