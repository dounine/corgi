package com.dounine.corgi.rpc.proxy;

import com.alibaba.fastjson.JSON;
import com.dounine.corgi.exception.RPCException;
import com.dounine.corgi.rpc.http.medias.IMedia;
import com.dounine.corgi.rpc.http.medias.Media;
import com.dounine.corgi.rpc.http.rep.ResponseText;
import com.dounine.corgi.rpc.invoke.HttpInvoke;
import com.dounine.corgi.rpc.invoke.Invoke;
import com.dounine.corgi.rpc.invoke.config.Consumer;
import com.dounine.corgi.rpc.serialize.Request;
import com.dounine.corgi.rpc.utils.ParserUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ConnectException;

import static com.dounine.corgi.rpc.serialize.Constant.RPC_NAME;

/**
 * 服务使用
 */
public class ConsumerProxyFactory implements InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerProxyFactory.class);
    private Invoke invoke = HttpInvoke.instanct();
    private Consumer consumer;
    private String className;

    public Object create() {
        try {
            Class cc = Class.forName(className);
            return Proxy.newProxyInstance(cc.getClassLoader(), new Class[]{cc}, this);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T create(Class<T> cc) {
        return (T) Proxy.newProxyInstance(cc.getClassLoader(), new Class[]{cc}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> interfacesClass = proxy.getClass().getInterfaces()[0];
        Request request = new Request();
        request.setClazz(interfacesClass);
        request.setMethod(method.getName());
        request.setReturnType(method.getReturnType());
        request.setParameterTypes(method.getParameterTypes());
        request.setArgs(args);

        IMedia media = new Media();
        media.setName(RPC_NAME);
        media.setValue(JSON.toJSONString(request));
        if (null == consumer) {
            throw new RuntimeException("consumer not empty");
        }
        Class<?> reType = method.getReturnType();
        String responseText = null;
        try {
            responseText =invoke.fetch(media, consumer.getUrl(interfacesClass));
            ResponseText returnRep = JSON.parseObject(responseText, ResponseText.class);
            if (null != returnRep) {
                if(0==returnRep.getErrno()){
                    return ParserUtils.parseObject(returnRep.getData(), reType);
                }else if(StringUtils.isNotBlank(returnRep.getMsg())){
                    throw new RPCException(returnRep.getMsg());
                }
            }
        }catch (ConnectException e){
            LOGGER.info("connection refush");
        }
        throw new RPCException("rpc remote call failed");
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }
}
