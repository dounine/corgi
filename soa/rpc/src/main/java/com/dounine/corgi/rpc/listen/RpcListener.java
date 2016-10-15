package com.dounine.corgi.rpc.listen;

import com.dounine.corgi.rpc.serialize.rmi.RpcServer;
import com.dounine.corgi.rpc.utils.RpcProperties;
import com.dounine.corgi.rpc.utils.ThreadPools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * Created by huanghuanlai on 2016/10/15.
 */
public class RpcListener implements Runnable {
    public static final CountDownLatch RPC_LISTENERED = new CountDownLatch(1);

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcListener.class);
    private static boolean isListener = false;
    private static final int RPC_SOCKET_TIMEOUT = RpcProperties.instance().getInteger("corgi.rpc.rmi.timeout");
    private int port;
    public RpcListener(final int port){
        this.port = port;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            LOGGER.info("CORGI rpc provider port [ "+port+" ] started.");
            LOGGER.info("CORGI rpc connect watching...");
            RPC_LISTENERED.countDown();
            isListener = true;
            for(;;){
                socket = serverSocket.accept();
                socket.setSoTimeout(RPC_SOCKET_TIMEOUT);
                LOGGER.info("CORGI [ "+socket.getRemoteSocketAddress()+" ] client connected.");
                ThreadPools.getExecutor().execute(new RpcServer(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void waitRpcListener(){
        try {
            RPC_LISTENERED.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean isListener(){
        return isListener;
    }
}
