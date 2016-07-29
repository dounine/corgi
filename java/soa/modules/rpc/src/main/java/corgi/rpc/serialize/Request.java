package corgi.rpc.serialize;

/**
 * Created by huanghuanlai on 16/7/27.
 */
public class Request {
    private Class clazz;
    private String method;
    private Class<?> returnType;
    private Class<?>[] parameterTypes;
    private Object[] args;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getMethod() {
        return method;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
