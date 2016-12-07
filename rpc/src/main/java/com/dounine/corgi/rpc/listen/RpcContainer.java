package com.dounine.corgi.rpc.listen;

import com.dounine.corgi.filter.ConsumerFilter;
import com.dounine.corgi.filter.ProviderFilter;
import com.dounine.corgi.filter.ProviderTxFilter;
import com.dounine.corgi.register.Register;
import com.dounine.corgi.remoting.P2PPushRemoting;
import com.dounine.corgi.rpc.protocol.IProtocol;
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
public class RpcContainer implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcContainer.class);
    private static boolean isListener = false;

    private IProtocol protocol;
    private ProviderFilter providerFilter;
    private ConsumerFilter consumerFilter;
    private ProviderTxFilter providerTxFilter;
    private Register register;
    public RpcContainer(final IProtocol protocol, Register register, ProviderFilter providerFilter, ConsumerFilter consumerFilter,ProviderTxFilter providerTxFilter){
        this.protocol = protocol;
        this.register = register;
        this.providerFilter = providerFilter;
        this.consumerFilter = consumerFilter;
        this.providerTxFilter = providerTxFilter;
    }

    public RpcContainer(final IProtocol protocol, Register register){
        this.protocol = protocol;
        this.register = register;
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
                P2PPushRemoting p2PPushRemoting = new P2PPushRemoting(socket);
                p2PPushRemoting.setProviderFilter(providerFilter);
                p2PPushRemoting.setConsumerFilter(consumerFilter);
                p2PPushRemoting.setProviderTxFilter(providerTxFilter);
                p2PPushRemoting.setRegister(register);
                ThreadPools.getExecutor().execute(p2PPushRemoting);
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
