package com.dounine.corgi.rpc.serialize;

import com.dounine.corgi.exception.RPCException;
import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.spring.ApplicationContext;
import com.dounine.corgi.rpc.spring.Service;

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
public class RpcServer implements Runnable {

    private Socket socket;

    public RpcServer(Socket socket) {
        this.socket = socket;
    }

    public void checkSpringContext(){
        if(null== ApplicationContext.getContext()){
            throw new RPCException("CORGI rpc spring context not null.");
        }
    }

    @Override
    public void run() {
        checkSpringContext();
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        Throwable exception = null;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            Class<?> clazz = (Class<?>) ois.readObject();//read invoke class object
            String methodName = ois.readUTF();//read invoke method
            String version = ois.readUTF();//read invoke class version
            Class<?>[] paramterTypes = (Class<?>[]) ois.readObject();//read method paramters type
            Object[] args = (Object[]) ois.readObject();//read method paramters values
            Method method = clazz.getMethod(methodName, paramterTypes);//get local interfact method
            oos = new ObjectOutputStream(socket.getOutputStream());
            Map obs = ApplicationContext.getContext().getBeansOfType(clazz);
            Optional<Object> oo = obs.values().stream().filter(o -> o.getClass().getAnnotation(Service.class).version().equals(version)).findFirst();
            if (!oo.isPresent() && obs.values().size() == 0) {
                throw new RPCException("class not found.");
            }else if(!oo.isPresent()&&obs.values().size()>0){
                throw new RPCException("class version:"+version+" not found.");
            }
            Object result = method.invoke(oo.get(), args);
            oos.writeObject(null);//write null exception
            oos.writeObject(result);//write metod invoke result
            System.out.println("CORGI rpc provider service finish.\n");
        } catch (IOException e) {
            exception = e;
        } catch (ClassNotFoundException e) {
            exception = e;
        } catch (InvocationTargetException e) {
            if(e.getCause() instanceof SerException){
                exception = e.getCause();
            }else{
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}