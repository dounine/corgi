package corgi.rpc.proxy;

import com.alibaba.fastjson.JSON;
import com.dounine.fasthttp.rep.ResponseText;
import com.dounine.fasthttp.utils.InputStreamUtils;
import corgi.rpc.container.Container;
import corgi.rpc.container.HttpContainer;
import corgi.rpc.invoke.HttpInvoke;
import corgi.rpc.invoke.Invoke;
import corgi.rpc.invoke.config.Provider;
import corgi.rpc.serialize.Request;
import org.apache.commons.lang3.StringUtils;
import org.mortbay.jetty.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static corgi.rpc.serialize.Constant.*;

/**
 * 服务注册
 */
public class ProviderProxyFactory extends AbstractHandler {

    private static ProviderProxyFactory PROVIDERPROXYFACTORY;
    private final Invoke invoke = HttpInvoke.instanct();
    private final Map<Class, Object> providers = new ConcurrentHashMap<>();
    private Provider provider;
    private static final int ERR_CODE = 1;

    public ProviderProxyFactory(Map<Class, Object> providers, Provider provider) {
        this.provider = provider;
        if (!Container.isStart) {
            new HttpContainer(this, provider).start();
        }
        if (null != providers) {
            for (Map.Entry<Class, Object> entry : providers.entrySet()) {
                register(entry.getKey(), entry.getValue());
            }
        }
        PROVIDERPROXYFACTORY = this;
    }

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
        String data = null;
        System.out.println("调用");
        if (request.getMethod().equals(METHOD_GET)) {
            data = request.getParameter(RPC_NAME);
        } else if (request.getMethod().equals(METHOD_POST)) {
            data = InputStreamUtils.istreamToString(request.getInputStream());
            if (null != data) {
                if(data.startsWith(RPC_NAME+"=")){
                    data = data.substring(RPC_NAME_LEN);
                }else{
                    data = null;
                }
            }
        }
        ResponseText responseText = new ResponseText();
        responseText.setErrno(ERR_CODE);
        if (StringUtils.isNotBlank(data)) {
            data = URLDecoder.decode(data, "utf-8");
            Request req = JSON.parseObject(data, Request.class);
            Object bean = providers.get(req.getClazz());
            try {
                this.utf8Chartset(response);
                if (null == req.getClazz()) {
                    responseText.setMsg("clazz not empty");
                } else if (StringUtils.isBlank(req.getMethod())) {
                    responseText.setMsg("invoke method not empty");
                } else {
                    responseText.setData(req.getClazz().getMethod(req.getMethod(), req.getParameterTypes()).invoke(bean, req.getArgs()).toString());
                }
                responseText.setErrno(ResponseText.SUCCESS_CODE);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                responseText.setMsg("not such method");
            } finally {
                invoke.push(responseText, response.getOutputStream());
            }
        }else{
            responseText.setMsg(RPC_NAME+" attr not empty");
            invoke.push(responseText, response.getOutputStream());
        }
    }

    public void utf8Chartset(HttpServletResponse response) {
        response.setHeader("Accept", "*");
        response.setHeader("Server", "CORGI-RPC(1.0.0)");
        response.setContentType("text/html;charset=utf-8");
    }

    public void register(Class clazz, Object obj) {
        providers.put(clazz, obj);
        if(null!=provider){
            provider.register(clazz);
        }
    }

    public static ProviderProxyFactory instance() {
        return PROVIDERPROXYFACTORY;
    }
}
