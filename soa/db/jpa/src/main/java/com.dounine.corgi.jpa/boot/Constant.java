package com.dounine.corgi.jpa.boot;

import com.dounine.corgi.jpa.boot.initializer.AppConfig;

public class Constant extends AppConfig {
	public static final String SCAN_APP_PACKAGES = "com.dounine.corgi.jpa";
	public static final String CACHE_NAME[] = {"queryCache","daoCache","serviceCache"};

}