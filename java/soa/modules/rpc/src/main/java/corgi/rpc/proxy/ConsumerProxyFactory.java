package corgi.rpc.proxy;

import com.alibaba.fastjson.JSON;
import com.dounine.fasthttp.medias.IMedia;
import com.dounine.fasthttp.medias.Media;
import com.dounine.fasthttp.rep.ResponseText;
import corgi.rpc.invoke.HttpInvoke;
import corgi.rpc.invoke.Invoke;
import corgi.rpc.invoke.config.Consumer;
import corgi.rpc.serialize.Request;
import corgi.rpc.utils.ParserUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static corgi.rpc.serialize.Constant.RPC_NAME;

/**
 * 服务使用
 */
public class ConsumerProxyFactory implements InvocationHandler {

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
        String responseText = invoke.fetch(media, consumer.getUrl(interfacesClass));
        ResponseText returnRep = JSON.parseObject(responseText, ResponseText.class);
        if (null != returnRep) {
            return ParserUtils.parseObject(returnRep.getData(), reType);
        }
        throw new RuntimeException("调用失败");
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
