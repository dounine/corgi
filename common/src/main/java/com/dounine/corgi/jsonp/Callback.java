package com.dounine.corgi.jsonp;

import java.lang.annotation.*;

/**
 * Created by huanghuanlai on 16/6/24.
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Callback {

}
