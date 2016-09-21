package corgi.consumer;

import com.dounine.corgi.rpc.invoke.config.Consumer;
import com.dounine.corgi.rpc.proxy.ConsumerProxyFactory;
import corgi.IWelcome;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by huanghuanlai on 16/7/28.
 */
@Component
public class ACompont{

    @Bean
    public IWelcome hello(){
        return consumerProxyFactory().create(IWelcome.class);
    }

    @Bean
    public ConsumerProxyFactory consumerProxyFactory(){
        ConsumerProxyFactory consumerProxyFactory = new ConsumerProxyFactory();
        Consumer consumer = new Consumer();
        //provider.unUseZK();//不使用zookeeper作为服务路由
        consumer.setUrl("127.0.0.1:2181");
        consumerProxyFactory.setClassName("com.dounine.corgi.IWelcome");
        consumerProxyFactory.setConsumer(consumer);
        return consumerProxyFactory;
    }
}
