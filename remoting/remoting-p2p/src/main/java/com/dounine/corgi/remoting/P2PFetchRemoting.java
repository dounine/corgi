package com.dounine.corgi.remoting;

import com.dounine.corgi.exception.RPCException;
import com.dounine.corgi.utils.RpcProperties;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by huanghuanlai on 2016/10/23.
 */
public class P2PFetchRemoting implements FetchRemoting {
    private static final int RPC_SOCKET_TIMEOUT = RpcProperties.instance().getInteger("corgi.consumer.timeout",10000);
    protected Invocation invocation;
    protected IResult result;

    public P2PFetchRemoting(Invocation invocation) {
        this.invocation = invocation;
        if (!validInvocation()) {
            throw new RPCException("invocation not empty");
        }
    }

    @Override
    public IResult fetch() {
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            Class clazz = invocation.getMethod().getDeclaringClass();
            InetSocketAddress isa = null;
            String autoUrl = invocation.getReference().url();
            if(StringUtils.isNotBlank(autoUrl)){
                isa = new InetSocketAddress(autoUrl.split(":")[0],Integer.parseInt(autoUrl.split(":")[1]));
                LOGGER.info("CORGI RPC use Autowired url [ "+autoUrl+" ]");
            }else{
                isa = invocation.getAddress(clazz);
            }
            socket = new Socket(isa.getAddress(), isa.getPort());
            socket.setSoTimeout(RPC_SOCKET_TIMEOUT);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeUTF(invocation.getMethod().getName());
            oos.writeUTF(invocation.getReference().version());
            oos.writeObject(clazz);
            oos.writeObject(invocation.getMethod().getParameterTypes());
            oos.writeObject(invocation.getArgs());
            Object exception = ois.readObject();
            IResult result = null;
            if (null != exception) {
                result = new DefaultResult(null, (Throwable) exception);
            } else {
                result = new DefaultResult(ois.readObject(), null);
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
