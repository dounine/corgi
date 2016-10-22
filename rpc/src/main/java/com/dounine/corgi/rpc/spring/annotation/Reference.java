package com.dounine.corgi.rpc.spring.annotation;

import java.lang.annotation.*;

/**
 * Created by huanghuanlai on 16/10/10.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Reference {
    String version() default "1.0.0";
    int timeout() default 3000;
    String url() default "";
}
