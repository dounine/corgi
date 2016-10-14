package com.dounine.corgi.rpc.serialize;

import com.dounine.corgi.exception.RPCException;
import com.dounine.corgi.rpc.invoke.Invocation;
import com.dounine.corgi.rpc.proxy.RPC;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

/**
 * Created by huanghuanlai on 16/9/22.
 */
public class RpcClient {

    protected Invocation invocation;
    protected Result result;

    public RpcClient(Invocation invocation){
        this.invocation = invocation;
        if(!validInvocation()){
            throw new RPCException("invocation not empty");
        }
        if(!validAddress()){
            throw new RPCException("address not empty");
        }
    }

    public Result fetch(){
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            socket = new Socket(invocation.getAddress().getAddress(),invocation.getAddress().getPort());
            socket.setSoTimeout(RPC.SOCKET_TIMEOUT);
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(invocation.getInterfaceClass());
            oos.writeUTF(invocation.getMethod().getName());
            oos.writeUTF(invocation.getVersion());
            oos.writeObject(invocation.getMethod().getParameterTypes());
            oos.writeObject(invocation.getArgs());
            ois = new ObjectInputStream(socket.getInputStream());
            Object exception = ois.readObject();
            Result result = null;
            if(null!=exception){
                result = new RequestResult(null,(Throwable)exception);
            }else{
                result = new RequestResult(ois.readObject(),null);
            }
            this.result = result;
        } catch (IOException e) {
            if(e instanceof ConnectException){
                throw new RPCException("服务方连接失败");
            }else{
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(null!=ois){
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null!=oos){
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null!=socket){
                try {
                    if(socket.isConnected()){
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return this.result;
    }

    public boolean validInvocation(){
        return null!=invocation;
    }

    public boolean validAddress(){
        return null!=invocation.getAddress();
    }

    public Result getResult() {
        return this.result;
    }
}
