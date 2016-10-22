package com.dounine.corgi.rpc.serialize.rmi;

import com.dounine.corgi.exception.RPCException;
import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.spring.ApplicationContext;
import com.dounine.corgi.rpc.spring.annotation.Service;
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

/**
 * Created by huanghuanlai on 16/9/26.
 */
public class RpcServer implements Runnable, IServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    private Socket socket;

    public RpcServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        push();
    }

    @Override
    public void push() {
        checkSpringContext();
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        Throwable exception = null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            String methodName = ois.readUTF();
            String version = ois.readUTF();
            Class<?> clazz = (Class<?>) ois.readObject();
            Class<?>[] paramterTypes = (Class<?>[]) ois.readObject();
            Object[] args = (Object[]) ois.readObject();
            Method method = clazz.getMethod(methodName, paramterTypes);
            Map obs = ApplicationContext.getContext().getBeansOfType(clazz);
            Optional<Object> oo = obs.values().stream().filter(o -> o.getClass().getAnnotation(Service.class).version().equals(version)).findFirst();
            if (!oo.isPresent() && obs.values().size() == 0) {
                throw new RPCException("class not found.");
            } else if (!oo.isPresent() && obs.values().size() > 0) {
                throw new RPCException("class version [ " + version + " ] not found.");
            }
            Object result = method.invoke(oo.get(), args);
            oos.writeObject(null);//write null exception
            oos.writeObject(result);//write metod invoke result
            LOGGER.info("CORGI [ " + socket.getRemoteSocketAddress() + " ] client service finish.");
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

    public void checkSpringContext() {
        if (null == ApplicationContext.getContext()) {
            throw new RPCException("CORGI rpc spring context not null.");
        }
    }
}