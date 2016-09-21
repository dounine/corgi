package corgi.provider;

import com.dounine.corgi.rpc.invoke.config.Provider;
import com.dounine.corgi.rpc.proxy.ProviderProxyFactory;
import corgi.IWelcome;
import corgi.WelcomeImpl;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 服务注册
 */
public class RPCProviderTest {

    @Test
    public void testProvider(){
        Map<Class,Object> providerObjs = new HashMap<>();
        providerObjs.put(IWelcome.class,new WelcomeImpl());//要提供服务的对象
        Provider provider = new Provider();//提供者配置信息
        //provider.unUseZK();//不使用zookeeper作为服务路由
        provider.setPort(2182);//对外提供服务端口
        provider.setTarget("127.0.0.1:2181");//zookeeper 地扯,调用unUseZK此方法无效
        ProviderProxyFactory providerProxyFactory = new ProviderProxyFactory(providerObjs,provider);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
