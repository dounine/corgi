package com.dounine.corgi.rpc.container;

public abstract class Container {
    public static volatile boolean isStart = false;

    public abstract void start();

    public static volatile Container container = null;
}
