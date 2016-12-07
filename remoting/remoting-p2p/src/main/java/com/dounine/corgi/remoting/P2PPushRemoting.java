package com.dounine.corgi.remoting;

import com.dounine.corgi.context.RpcContext;
import com.dounine.corgi.context.TokenContext;
import com.dounine.corgi.exception.RPCException;
import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.filter.ConsumerFilter;
import com.dounine.corgi.filter.ProviderFilter;
import com.dounine.corgi.filter.ProviderTxFilter;
import com.dounine.corgi.filter.impl.DefaultProviderFilter;
import com.dounine.corgi.register.Register;
import com.dounine.corgi.context.ApplicationContext;
import com.dounine.corgi.spring.rpc.Reference;
import com.dounine.corgi.spring.rpc.RpcMethod;
import com.dounine.corgi.spring.rpc.Service;
import com.dounine.corgi.utils.ClassUtils;
import com.dounine.corgi.utils.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by huanghuanlai on 2016/10/23.
 */
public class P2PPushRemoting implements PushRemoting, Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(P2PPushRemoting.class);
    private static final Map<String, Token> EXECUTE_METHOD_TOKENS = new ConcurrentHashMap<>();

    private Socket socket;
    private Register register;
    private ProviderFilter providerFilter;
    private ConsumerFilter consumerFilter;
    private ProviderTxFilter providerTxFilter;

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
                    execResult(ois, oos);
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

    /**
     * 获取执行方法信息
     *
     * @param ois 输入流
     * @param oos 输出流
     */
    public void execToken(ObjectInputStream ois, ObjectOutputStream oos) {
        LOGGER.info("CORGI [ " + socket.getRemoteSocketAddress() + " ] client >> get token << service begin...");
        Throwable exception = null;
        try {
            readAttachments(ois);
            String methodName = ois.readUTF();
            String version = ois.readUTF();
            Class<?> clazz = (Class<?>) ois.readObject();
            Class<?>[] paramterTypes = (Class<?>[]) ois.readObject();
            Reference reference = (Reference) ois.readObject();
            Method method = clazz.getMethod(methodName, paramterTypes);
            Set<Class<?>> obs = ClassUtils.queryInterfaceByImpls(register.getRegisterClass(), clazz);

            Optional<Class<?>> oo = obs.stream().filter(o -> null != o.getAnnotation(Service.class) && version.equals(o.getAnnotation(Service.class).version())).findFirst();
            if (!oo.isPresent() && obs.size() == 0) {
                throw new RPCException(clazz.getName() + " not found.");
            } else if (!oo.isPresent() && obs.size() > 0) {
                throw new RPCException(clazz.getName() + " version [ " + version + " ] not found.");
            }

            Method methodImpl = oo.get().getMethod(methodName, paramterTypes);

            Optional<RpcMethod> rpcMethodOptional = Optional.ofNullable(methodImpl.getAnnotation(RpcMethod.class));
            P2PToken p2PToken = new P2PToken();
            p2PToken.setClazz(clazz);
            p2PToken.setVersion(version);
            p2PToken.setParamterTypes(paramterTypes);
            p2PToken.setMethod(methodImpl);
            p2PToken.setInvokeObj(ApplicationContext.getContext().getBean(oo.get()));
            if (rpcMethodOptional.isPresent()) {
                p2PToken.setTimeout(reference.timeout() > rpcMethodOptional.get().timeout() ? reference.timeout() : rpcMethodOptional.get().timeout());
                p2PToken.setRetries(reference.retries() > rpcMethodOptional.get().retries() ? reference.retries() : rpcMethodOptional.get().retries());
            } else {
                p2PToken.setTimeout(reference.timeout());
                p2PToken.setRetries(reference.retries());
            }
            String token = UUIDUtils.create();
            EXECUTE_METHOD_TOKENS.put(token, p2PToken);
            oos.writeObject(new P2PFetchToken(token, p2PToken.getTimeout(), p2PToken.getRetries(), socket.getLocalAddress().getHostAddress() + ":" + socket.getLocalPort(),providerTxFilter.checkTxAnnotation(methodImpl)));
            oos.flush();
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

    /**
     * 获取方法执行结果
     *
     * @param ois 输入流
     * @param oos 输出流
     */
    public void execResult(ObjectInputStream ois, ObjectOutputStream oos) {
        LOGGER.info("CORGI [ " + socket.getRemoteSocketAddress() + " ] client >> get result << service begin...");
        Throwable exception = null;
        try {
            readAttachments(ois);
            String tokenId = ois.readUTF();
            TokenContext.set(tokenId);// CONTEXT
            Token token = EXECUTE_METHOD_TOKENS.get(tokenId);
            Object[] args = (Object[]) ois.readObject();
            Method method = token.getMethod();

            Object result = null;
            try {
                providerFilter.invokeBefore(method, token.getInvokeObj(), args);//过滤前置
                result = method.invoke(token.getInvokeObj(), args);
                providerFilter.invokeAfter(method,result);//过滤后置
            } catch (Exception e) {
                exception = e;
                providerFilter.invokeError(e);//异常调用
                throw e;
            }
            oos.writeObject(null);//write null exception
            oos.flush();
            oos.writeObject(result);//write metod invoke result
            oos.flush();
            LOGGER.info("CORGI [ " + socket.getRemoteSocketAddress() + " ] client >> get result << service finish.");
        } catch (IOException e) {
            exception = e;
        } catch (ClassNotFoundException e) {
            exception = e;
        } catch (InvocationTargetException e) {
            /**
             * 检测是否有业务逻辑异常
             */
            if (e.getCause() instanceof SerException) {
                exception = e.getCause();
            } else {
                exception = e;
            }
        } catch (IllegalAccessException e) {
            exception = e;
        } catch (Exception e) {
            exception = e;
        } finally {
            socksClose(ois, oos, exception);
        }
    }

    /**
     * 执行方法事务
     *
     * @param ois 输入流
     * @param oos 输出流
     */
    private void execTx(ObjectInputStream ois, ObjectOutputStream oos) {
        LOGGER.info("CORGI [ " + socket.getRemoteSocketAddress() + " ] client >> TX << service begin...");
        Throwable exception = null;
        try {
            readAttachments(ois);
            String tokenId = ois.readUTF();
            TokenContext.set(tokenId);// CONTEXT
            String txType = ois.readUTF();
            try {
                LOGGER.info("CORGI [ " + socket.getRemoteSocketAddress() + " ] client >> TX " + txType);
                providerFilter.callback(txType);
                oos.writeObject(null);
                oos.flush();
                oos.writeObject("success");
                oos.flush();
            } catch (Exception e) {
                exception = e;
                e.printStackTrace();
            }
            LOGGER.info("CORGI [ " + socket.getRemoteSocketAddress() + " ] client >> TX << service finish.");
        } catch (IOException e) {
            exception = e;
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            socksClose(ois, oos, exception);
        }
    }

    public void readAttachments(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        int attachmentSize = ois.readInt();
        if (attachmentSize > 0) {
            LOGGER.info("CORGI RPC read attachments size:"+attachmentSize);
            RpcContext.put((Map<String, Serializable>) ois.readObject());
        }
    }

    public void checkSpringContext() {
        if (null == ApplicationContext.getContext()) {
            throw new RPCException("CORGI rpc spring context not null.");
        }
    }

    /**
     * 结果处理方法
     *
     * @param ois       输入流
     * @param oos       输出流
     * @param exception 异常
     */
    private void socksClose(ObjectInputStream ois, ObjectOutputStream oos, Throwable exception) {
        if (null != exception) {
            try {
                /**
                 * 有异常则将异常写出client
                 */
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

    public ConsumerFilter getConsumerFilter() {
        return consumerFilter;
    }

    public void setConsumerFilter(ConsumerFilter consumerFilter) {
        this.consumerFilter = consumerFilter;
    }

    public ProviderTxFilter getProviderTxFilter() {
        return providerTxFilter;
    }

    public void setProviderTxFilter(ProviderTxFilter providerTxFilter) {
        this.providerTxFilter = providerTxFilter;
    }
}

