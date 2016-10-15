package com.dounine.corgi.rpc.spring.annotation;

import java.lang.annotation.*;

/**
 * Created by huanghuanlai on 16/10/10.
 */
@org.springframework.stereotype.Service
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default "";
    String version() default "1.0.0";
}
