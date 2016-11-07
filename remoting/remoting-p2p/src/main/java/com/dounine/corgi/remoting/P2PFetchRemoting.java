package com.dounine.corgi.remoting;

import com.dounine.corgi.exception.RPCException;
import com.dounine.corgi.utils.RpcProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;


/**
 * Created by huanghuanlai on 2016/10/23.
 */
public class P2PFetchRemoting implements FetchRemoting {
    private static final Logger LOGGER = LoggerFactory.getLogger(P2PFetchRemoting.class);

    private static final int RPC_SOCKET_TIMEOUT = RpcProperties.instance().getInteger("corgi.consumer.timeout",10000);
    protected Invocation invocation;
    protected Result result;
    protected FetchToken fetchToken;

    public P2PFetchRemoting(Invocation invocation) {
        this.invocation = invocation;
        if (!validInvocation()) {
            throw new RPCException("invocation not empty");
        }
    }

    @Override
    public Result fetch(FetchToken fetchToken) {
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            Class clazz = invocation.getMethod().getDeclaringClass();
            InetSocketAddress isa = null;
            String autoUrl = invocation.getReference().url();
            if(StringUtils.isNotBlank(autoUrl)){
                isa = new InetSocketAddress(autoUrl.split(":")[0],Integer.parseInt(autoUrl.split(":")[1]));
                LOGGER.info("CORGI RPC use Reference url [ "+autoUrl+" ]");
            }else{
                isa = invocation.getAddress(clazz);
            }
            socket = new Socket(isa.getAddress(), isa.getPort());
            socket.setSoTimeout(fetchToken.getTimeout());
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(RemotType.RESULT);
            oos.writeUTF(fetchToken.getToken());
            oos.writeObject(invocation.getArgs());
            Object exception = ois.readObject();
            Result result = null;
            if (null != exception) {
                result = new DefaultResult(null, (Throwable) exception);
            } else {
                result = new DefaultResult(ois.readObject(), null);
            }
            this.result = result;
        } catch (IOException e) {
            if (e instanceof ConnectException) {
                throw new RPCException("服务方连接失败");
            } else if(e instanceof SocketTimeoutException){
                throw new RPCException("API接口连接超时");
            }else{
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

    @Override
    public FetchToken fetchToken() {
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            Class clazz = invocation.getMethod().getDeclaringClass();
            InetSocketAddress isa = null;
            String autoUrl = invocation.getReference().url();
            if(StringUtils.isNotBlank(autoUrl)){
                isa = new InetSocketAddress(autoUrl.split(":")[0],Integer.parseInt(autoUrl.split(":")[1]));
                LOGGER.info("CORGI RPC use Reference url [ "+autoUrl+" ]");
            }else{
                isa = invocation.getAddress(clazz);
            }
            socket = new Socket(isa.getAddress(), isa.getPort());
            socket.setSoTimeout(RPC_SOCKET_TIMEOUT);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(RemotType.TOKEN);
            oos.writeUTF(invocation.getMethod().getName());
            oos.writeUTF(invocation.getReference().version());
            oos.writeObject(clazz);
            oos.writeObject(invocation.getMethod().getParameterTypes());

            fetchToken = (FetchToken) ois.readObject();

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
        return this.fetchToken;
    }

    public boolean validInvocation() {
        return null != invocation;
    }


    public Result getResult() {
        return this.result;
    }
}
