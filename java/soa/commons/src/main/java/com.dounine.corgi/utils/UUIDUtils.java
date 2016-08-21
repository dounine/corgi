package com.dounine.corgi.utils;

import java.util.UUID;

/**
 * Created by huanghuanlai on 16/6/26.
 */
public final class UUIDUtils {


    public static String create(){
        return UUID.randomUUID().toString().replace("-","");
    }

}
