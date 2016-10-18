package com.dounine.corgi.rpc.serialize.rmi;

import com.dounine.corgi.exception.RPCException;
import com.dounine.corgi.rpc.invoke.Invocation;
import com.dounine.corgi.rpc.serialize.result.IResult;
import com.dounine.corgi.rpc.serialize.result.InvokeResult;
import com.dounine.corgi.rpc.utils.RpcProperties;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by huanghuanlai on 16/9/22.
 */
public class RpcClient implements IClient{
    private static final int RPC_SOCKET_TIMEOUT = RpcProperties.instance().getInteger("corgi.rpc.rmi.timeout");
    protected Invocation invocation;
    protected IResult result;

    public RpcClient(Invocation invocation) {
        this.invocation = invocation;
        if (!validInvocation()) {
            throw new RPCException("invocation not empty");
        }
    }



    public IResult fetch() {
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            Class clazz = invocation.getMethod().getDeclaringClass();
            InetSocketAddress isa = invocation.getAddress(clazz);
            socket = new Socket(isa.getAddress(), isa.getPort());
            socket.setSoTimeout(RPC_SOCKET_TIMEOUT);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeUTF(invocation.getMethod().getName());
            oos.writeUTF(invocation.getVersion());
            oos.writeObject(clazz);
            oos.writeObject(invocation.getMethod().getParameterTypes());
            oos.writeObject(invocation.getArgs());
            Object exception = ois.readObject();
            IResult result = null;
            if (null != exception) {
                result = new InvokeResult(null, (Throwable) exception);
            } else {
                result = new InvokeResult(ois.readObject(), null);
            }
            this.result = result;
        } catch (IOException e) {
            if (e instanceof ConnectException) {
                throw new RPCException("服务方连接失败");
            } else {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (null != ois) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != oos) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != socket) {
                try {
                    if (socket.isConnected()) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return this.result;
    }

    public boolean validInvocation() {
        return null != invocation;
    }


    public IResult getResult() {
        return this.result;
    }
}
