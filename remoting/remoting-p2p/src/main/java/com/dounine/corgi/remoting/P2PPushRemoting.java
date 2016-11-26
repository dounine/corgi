package com.dounine.corgi.remoting;

import com.alibaba.fastjson.JSON;
import com.dounine.corgi.exception.RPCException;
import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.filter.ProviderFilter;
import com.dounine.corgi.filter.impl.DefaultProviderFilter;
import com.dounine.corgi.register.Register;
import com.dounine.corgi.spring.ApplicationContext;
import com.dounine.corgi.spring.rpc.RpcMethod;
import com.dounine.corgi.spring.rpc.Service;
import com.dounine.corgi.utils.ClassUtils;
import com.dounine.corgi.utils.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by huanghuanlai on 2016/10/23.
 */
public class P2PPushRemoting implements PushRemoting, Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(P2PPushRemoting.class);
    private static final int RPC_SOCKET_TIMEOUT = 3000;

    private Socket socket;
    private Register register;
    private ProviderFilter providerFilter;

    public P2PPushRemoting(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void push() {
        checkSpringContext();
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            RemotType remotEnum = (RemotType) ois.readObject();
            switch (remotEnum) {
                case TOKEN://获取接口token执行队列
                    execToken(ois, oos);
                    break;
                case RESULT://执行实现结果
                    execInvocation(ois, oos);
                    break;
                case TX://事务
                    execTx(ois, oos);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void execTx(ObjectInputStream ois, ObjectOutputStream oos) {
        LOGGER.info("CORGI [ " + socket.getRemoteSocketAddress() + " ] client >> TX << service begin...");
        Throwable exception = null;
        try {
            String txId = ois.readUTF();
            String txType = ois.readUTF();
            try {
                LOGGER.info("CORGI [ " + socket.getRemoteSocketAddress() + " ] client >> TX " + txType);
                if (null == providerFilter) {
                    providerFilter = new DefaultProviderFilter();
                }
                providerFilter.callback(txType, txId);
                oos.writeUTF("success");
            } catch (Exception e) {
                exception = e;
                e.printStackTrace();
            }
            LOGGER.info("CORGI [ " + socket.getRemoteSocketAddress() + " ] client >> TX << service finish.");
        } catch (IOException e) {
            exception = e;
        } finally {
            socksClose(ois, oos, exception);
        }
    }

    private static final Map<String, Token> EXECUTE_METHOD_TOKENS = new ConcurrentHashMap<>();

    public void execToken(ObjectInputStream ois, ObjectOutputStream oos) {
        LOGGER.info("CORGI [ " + socket.getRemoteSocketAddress() + " ] client >> get token << service begin...");
        Throwable exception = null;
        try {
            String methodName = ois.readUTF();
            String version = ois.readUTF();
            Class<?> clazz = (Class<?>) ois.readObject();
            Class<?>[] paramterTypes = (Class<?>[]) ois.readObject();
            Method method = clazz.getMethod(methodName, paramterTypes);
            Set<Class<?>> obs = ClassUtils.queryInterfaceByImpls(register.getRegisterClass(), clazz);
            Optional<Class<?>> oo = obs.stream().filter(o -> null != o.getAnnotation(Service.class) && version.equals(o.getAnnotation(Service.class).version())).findFirst();
            if (!oo.isPresent() && obs.size() == 0) {
                throw new RPCException(clazz.getName() + " not found.");
            } else if (!oo.isPresent() && obs.size() > 0) {
                throw new RPCException(clazz.getName() + " version [ " + version + " ] not found.");
            }
            Optional<RpcMethod> rpcMethodOptional = Optional.ofNullable(oo.get().getMethod(methodName, paramterTypes).getAnnotation(RpcMethod.class));
            P2PToken p2PToken = new P2PToken();
            p2PToken.setClazz(clazz);
            p2PToken.setVersion(version);
            p2PToken.setParamterTypes(paramterTypes);
            p2PToken.setMethod(method);
            p2PToken.setInvokeObj(ApplicationContext.getContext().getBean(oo.get()));
            if (rpcMethodOptional.isPresent()) {
                p2PToken.setTimeout(rpcMethodOptional.get().timeout());
                p2PToken.setRetries(rpcMethodOptional.get().retries());
            } else {
                p2PToken.setTimeout(RpcMethod.TIMEOUT);
                p2PToken.setRetries(RpcMethod.RETRIES);
            }
            String token = UUIDUtils.create();
            EXECUTE_METHOD_TOKENS.put(token, p2PToken);
            oos.writeObject(new P2PFetchToken(token, p2PToken.getTimeout(), p2PToken.getRetries()));
            LOGGER.info("CORGI [ " + socket.getRemoteSocketAddress() + " ] client >> get token << service finish.");
        } catch (IOException e) {
            exception = e;
        } catch (ClassNotFoundException e) {
            exception = e;
        } catch (NoSuchMethodException e) {
            exception = e;
        } finally {
            socksClose(ois, oos, exception);
        }
    }

    public void execInvocation(ObjectInputStream ois, ObjectOutputStream oos) {
        LOGGER.info("CORGI [ " + socket.getRemoteSocketAddress() + " ] client >> get result << service begin...");
        Throwable exception = null;
        try {
            String tokenStr = ois.readUTF();
            Token token = EXECUTE_METHOD_TOKENS.get(tokenStr);
            String txId = "aaaa";//TODO 要获取真正生成的事务ID
            Object[] args = (Object[]) ois.readObject();
            Method method = token.getMethod();
            if (null == providerFilter) {
                providerFilter = new DefaultProviderFilter();
            }
            providerFilter.invokeBefore(method, token.getInvokeObj(), args);//过滤前置

            Object result = null;
            try {
                result = method.invoke(token.getInvokeObj(), args);
            } catch (Exception e) {
                exception = e;
                providerFilter.invokeError(e);//异常调用
                throw e;
            }
            providerFilter.invokeAfter(result, txId);//过滤后置
            oos.writeObject(null);//write null exception
            oos.writeObject(result);//write metod invoke result
            LOGGER.info("CORGI [ " + socket.getRemoteSocketAddress() + " ] client >> get result << service finish.");
        } catch (IOException e) {
            exception = e;
        } catch (ClassNotFoundException e) {
            exception = e;
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof SerException) {
                exception = e.getCause();
            } else {
                exception = e;
            }
        } catch (IllegalAccessException e) {
            exception = e;
        }catch (Exception e){
            exception = e;
        }finally {
            socksClose(ois, oos, exception);
        }
    }

    private void socksClose(ObjectInputStream ois, ObjectOutputStream oos, Throwable exception) {
        if (null != exception) {
            try {
                oos.writeObject(exception);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null != ois) {
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null != oos) {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null != socket) {
            if (socket.isConnected()) {
                try {
                    socket.close();
                    LOGGER.info("CORGI [ " + socket.getRemoteSocketAddress() + " ] client connect closed.\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void checkSpringContext() {
        if (null == ApplicationContext.getContext()) {
            throw new RPCException("CORGI rpc spring context not null.");
        }
    }

    @Override
    public void run() {
        push();
    }

    public ProviderFilter getProviderFilter() {
        return providerFilter;
    }

    public void setProviderFilter(ProviderFilter providerFilter) {
        this.providerFilter = providerFilter;
    }

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }
}

