package com.dounine.corgi.spring.rpc;

import java.lang.annotation.*;

/**
 * Created by huanghuanlai on 16/10/10.
 */
@org.springframework.stereotype.Service
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Method {
    String value() default "";
    int timeout() default 3000;
    int retries() default 2;
}
