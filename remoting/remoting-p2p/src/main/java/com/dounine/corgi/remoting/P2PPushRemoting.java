package com.dounine.corgi.remoting;

import com.dounine.corgi.exception.RPCException;
import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.spring.ApplicationContext;
import com.dounine.corgi.spring.rpc.RpcMethod;
import com.dounine.corgi.spring.rpc.Service;
import com.dounine.corgi.utils.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by huanghuanlai on 2016/10/23.
 */
public class P2PPushRemoting implements PushRemoting,Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(P2PPushRemoting.class);
    private static final int RPC_SOCKET_TIMEOUT = 3000;

    private Socket socket;
    public P2PPushRemoting(Socket socket){
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
            switch (remotEnum){
                case TOKEN://获取接口token执行队列
                    execToken(ois,oos);
                    break;
                case RESULT://执行实现结果
                    execInvocation(ois,oos);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static final Map<String,Token> EXECUTE_METHOD_TOKENS = new ConcurrentHashMap<>();

    public void execToken(ObjectInputStream ois,ObjectOutputStream oos){
        LOGGER.info("CORGI [ " + socket.getRemoteSocketAddress() + " ] client >> get token << service begin...");
        Throwable exception = null;
        try {
            String methodName = ois.readUTF();
            String version = ois.readUTF();
            Class<?> clazz = (Class<?>) ois.readObject();
            Class<?>[] paramterTypes = (Class<?>[]) ois.readObject();
            Method method = clazz.getMethod(methodName, paramterTypes);
            Map obs = ApplicationContext.getContext().getBeansOfType(clazz);
            Optional<Object> oo = obs.values().stream().filter(o -> null!=o.getClass().getAnnotation(Service.class)&&version.equals(o.getClass().getAnnotation(Service.class).version())).findFirst();
            if (!oo.isPresent() && obs.values().size() == 0) {
                throw new RPCException("class not found.");
            } else if (!oo.isPresent() && obs.values().size() > 0) {
                throw new RPCException("class version [ " + version + " ] not found.");
            }
            Optional<RpcMethod> rpcMethodOptional =  Optional.ofNullable(oo.get().getClass().getMethod(methodName, paramterTypes).getAnnotation(RpcMethod.class));
            P2PToken p2PToken = new P2PToken();
            p2PToken.setClazz(clazz);
            p2PToken.setVersion(version);
            p2PToken.setParamterTypes(paramterTypes);
            p2PToken.setMethod(method);
            p2PToken.setInvokeObj(oo.get());
            if(rpcMethodOptional.isPresent()){
                p2PToken.setTimeout(rpcMethodOptional.get().timeout());
                p2PToken.setRetries(rpcMethodOptional.get().retries());
            }else{
                p2PToken.setTimeout(RpcMethod.TIMEOUT);
                p2PToken.setRetries(RpcMethod.RETRIES);
            }
            String token = UUIDUtils.create();
            EXECUTE_METHOD_TOKENS.put(token,p2PToken);
            oos.writeObject(new P2PFetchToken(token,p2PToken.getTimeout(),p2PToken.getRetries()));
            LOGGER.info("CORGI [ " + socket.getRemoteSocketAddress() + " ] client >> get token << service finish.");
        } catch (IOException e) {
            exception = e;
        } catch (ClassNotFoundException e) {
            exception = e;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } finally {
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
    }

    public void execInvocation(ObjectInputStream ois,ObjectOutputStream oos){
        LOGGER.info("CORGI [ " + socket.getRemoteSocketAddress() + " ] client >> get result << service begin...");
        Throwable exception = null;
        try {
            String tokenStr = ois.readUTF();
            Token token = EXECUTE_METHOD_TOKENS.get(tokenStr);
            Object[] args = (Object[]) ois.readObject();
            Method method = token.getMethod();
            Object result = method.invoke(token.getInvokeObj(), args);
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
        } finally {
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
}
