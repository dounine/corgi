package com.dounine.corgi.remoting;

import com.dounine.corgi.context.ApiContext;
import com.dounine.corgi.context.RpcContext;
import com.dounine.corgi.exception.RPCException;
import com.dounine.corgi.utils.RpcProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;


/**
 * Created by huanghuanlai on 2016/10/23.
 */
public class P2PFetchRemoting implements FetchRemoting {
    private static final Logger LOGGER = LoggerFactory.getLogger(P2PFetchRemoting.class);

    private static final int RPC_SOCKET_TIMEOUT = RpcProperties.instance().getInteger("corgi.consumer.timeout", 10000);
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
    public FetchToken fetchToken(String txId) {
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            Class clazz = invocation.getMethod().getDeclaringClass();
            InetSocketAddress isa = null;
            String autoUrl = invocation.getReference().url();
            if (StringUtils.isNotBlank(autoUrl)) {
                isa = new InetSocketAddress(autoUrl.split(":")[0], Integer.parseInt(autoUrl.split(":")[1]));
                LOGGER.info("CORGI RPC use Reference url [ " + autoUrl + " ]");
            } else {
                isa = invocation.getAddress(clazz);
            }
            socket = new Socket(isa.getAddress(), isa.getPort());
            socket.setSoTimeout(RPC_SOCKET_TIMEOUT);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(RemotType.TOKEN);
            writeAttachments(oos);//RPC将请求协带的值传输
            oos.flush();
            oos.writeUTF(invocation.getMethod().getName());
            oos.flush();
            oos.writeUTF(invocation.getReference().version());
            oos.flush();
            oos.writeObject(clazz);
            oos.flush();
            oos.writeObject(invocation.getMethod().getParameterTypes());
            oos.flush();
            oos.writeObject(invocation.getReference());
            oos.flush();
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
            socksClose(socket, oos, ois);
        }
        return this.fetchToken;
    }

    @Override
    public Result fetchResult(FetchToken fetchToken) {
        if (null == fetchToken) {
            throw new RPCException("CORGI RPC fetchToken not empty.");
        }
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            Class clazz = invocation.getMethod().getDeclaringClass();
            InetSocketAddress isa = null;
            String targetUrl = invocation.getReference().url();
            if (StringUtils.isNotBlank(targetUrl)) {
                isa = new InetSocketAddress(targetUrl.split(":")[0], Integer.parseInt(targetUrl.split(":")[1]));
                LOGGER.info("CORGI RPC use Reference url [ " + targetUrl + " ]");
            } else {
                isa = invocation.getAddress(clazz);
            }
            for (int i = 0; i <= fetchToken.getRetries(); i++) {
                try {
                    if (i > 0) {
                        LOGGER.info("CORGI client >> get result << retrie [ " + i + " ]");
                    }
                    socket = new Socket(isa.getAddress(), isa.getPort());
                    socket.setSoTimeout(fetchToken.getTimeout());
                    ois = new ObjectInputStream(socket.getInputStream());
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(RemotType.RESULT);
                    writeAttachments(oos);//RPC将请求协带的值传输
                    oos.flush();
                    oos.writeUTF(fetchToken.getToken());
                    oos.flush();
                    oos.writeObject(invocation.getArgs());
                    oos.flush();
                    Object exception = ois.readObject();
                    Result result = null;
                    if (null != exception) {
                        result = new DefaultResult(null, (Throwable) exception);
                    } else {
                        result = new DefaultResult(ois.readObject(), null);
                    }
                    this.result = result;
                    break;
                } catch (IOException e) {
                    if (e instanceof ConnectException || e instanceof SocketTimeoutException) {
                        continue;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            socksClose(socket, oos, ois);
        }
        return this.result;
    }

    @Override
    public void txCall(FetchToken fetchToken, String txType) {
        if (null == fetchToken) {
            throw new RPCException("CORGI RPC fetchToken not empty.");
        }
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            InetSocketAddress isa = new InetSocketAddress(fetchToken.getAddress().split(":")[0], Integer.parseInt(fetchToken.getAddress().split(":")[1]));
            Result result = null;
            socket = new Socket(isa.getAddress(), isa.getPort());
            socket.setSoTimeout(fetchToken.getTimeout());
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(RemotType.TX);
            writeAttachments(oos);//RPC将请求协带的值传输
            oos.flush();
            oos.writeUTF(fetchToken.getToken());
            oos.flush();
            oos.writeUTF(txType);
            oos.flush();
            Object exception = ois.readObject();
            if (null != exception) {
                result = new DefaultResult(null, (Throwable) exception);
            } else {
                result = new DefaultResult(ois.readObject(), null);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socksClose(socket, oos, ois);
        }
    }

    public void writeAttachments(ObjectOutputStream oos) throws IOException {
        int attachmentSize = RpcContext.size();
        oos.writeInt(attachmentSize);
        oos.flush();
        if(attachmentSize>0){
            LOGGER.info("CORGI RPC write attachments size:"+attachmentSize);
            oos.writeObject(RpcContext.all());
            oos.flush();
        }
    }

    private void socksClose(Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
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
            try {
                if (socket.isConnected()) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public boolean validInvocation() {
        return null != invocation;
    }


    public Result getResult() {
        return this.result;
    }
}
