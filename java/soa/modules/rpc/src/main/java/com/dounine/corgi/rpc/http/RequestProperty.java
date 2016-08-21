package com.dounine.corgi.rpc.http;


import com.dounine.corgi.rpc.http.consts.RequestPropertyType;

import java.net.URLConnection;

/**
 * Created by huanghuanlai on 16/7/7.
 */
public final class RequestProperty {

    public static final void init(URLConnection connection){
        connection.setRequestProperty(RequestPropertyType.CONTENT_TYPE_NAME, RequestPropertyType.APPLICATION_X_WWW_FORM_URLENCODED_UTF8);
    }

}
