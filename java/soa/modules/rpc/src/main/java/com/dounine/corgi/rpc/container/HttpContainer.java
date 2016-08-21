package com.dounine.corgi.rpc.container;

import com.dounine.corgi.rpc.invoke.config.Provider;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;

/**
 * Created by huanghuanlai on 16/7/27.
 */
public class HttpContainer extends Container {

    private AbstractHandler handler;
    private Provider provider;

    public HttpContainer(AbstractHandler handler, Provider provider) {
        this.handler = handler;
        this.provider = provider;
    }

    @Override
    public void start() {
        Server server = new Server();
        try {
            SelectChannelConnector connector = new SelectChannelConnector();
            connector.setPort(provider.getPort());
            server.setConnectors(new Connector[]{connector});
            server.setHandler(handler);
            server.start();
            Container.isStart = true;
            Container.container = this;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
