package com.dounine.corgi.rpc.utils;

/**
 * Created by huanghuanlai on 2016/10/15.
 */
public class VersionContext {

    private static final String DEFAULT_VERSION = "1.0.0";
    private static final ThreadLocal<String> VERSION = new ThreadLocal<String>();

    public static String currentVersion() {
        String version = VERSION.get();
        if (null == version) {
            version = DEFAULT_VERSION;
        }
        return version;
    }

    public static void initVersion(String version) {
        VERSION.set(version);
    }
}
