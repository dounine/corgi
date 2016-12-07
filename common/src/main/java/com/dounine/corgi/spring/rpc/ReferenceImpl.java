package com.dounine.corgi.spring.rpc;

import java.lang.annotation.Annotation;

/**
 * Created by huanghuanlai on 2016/11/21.
 */
public class ReferenceImpl implements Reference {
    private String url = "";
    private String version = "1.0.0";
    private int timeout = 3000;
    private int retries = 3;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

    @Override
    public String version() {
        return version;
    }

    @Override
    public int timeout() {
        return timeout;
    }

    @Override
    public String url() {
        return url;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    @Override
    public int retries() {
        return retries;
    }
}
