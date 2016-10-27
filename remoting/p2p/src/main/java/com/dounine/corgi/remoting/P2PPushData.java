package com.dounine.corgi.remoting;

/**
 * Created by huanghuanlai on 2016/10/23.
 */
public class P2PPushData implements PushData {
    private Class<?> clazz;
    private Class<?>[] paramterTypes;
    private Object[] objects;
    private String methodName;


    @Override
    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<?>[] getParamterTypes() {
        return paramterTypes;
    }

    public void setParamterTypes(Class<?>[] paramterTypes) {
        this.paramterTypes = paramterTypes;
    }

    @Override
    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object[] objects) {
        this.objects = objects;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
