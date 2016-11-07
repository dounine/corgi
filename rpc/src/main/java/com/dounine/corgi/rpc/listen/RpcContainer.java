package com.dounine.corgi.rpc.listen;

import com.dounine.corgi.remoting.P2PPushRemoting;
import com.dounine.corgi.rpc.protocol.IProtocol;
import com.dounine.corgi.rpc.utils.ThreadPools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by huanghuanlai on 2016/10/15.
 */
public class RpcContainer implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcContainer.class);
    private static boolean isListener = false;

    private IProtocol protocol;
    public RpcContainer(final IProtocol protocol){
        this.protocol = protocol;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            ServerSocket serverSocket = new ServerSocket(protocol.getPort());
            LOGGER.info("CORGI rpc provider port [ "+protocol.getPort()+" ] started.");
            LOGGER.info("CORGI rpc connect watching...");
            isListener = true;
            for(;;){
                socket = serverSocket.accept();
                LOGGER.info("CORGI [ "+socket.getRemoteSocketAddress()+" ] client connected.");
                ThreadPools.getExecutor().execute(new P2PPushRemoting(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void waitRpcListener(){
//        try {
//            RPC_LISTENERED.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public static boolean isListener(){
        return isListener;
    }
}
