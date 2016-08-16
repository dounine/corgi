package corgi.sso.boot;

import corgi.rpc.invoke.config.Provider;
import corgi.rpc.proxy.ProviderProxyFactory;
import corgi.sso.service.user.IUserSer;
import corgi.sso.service.user.UserSerImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Application {

	public static void main(String[] args){
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(App.class);
        Map<Class,Object> providerObjs = new HashMap<>();
        providerObjs.put(IUserSer.class,context.getBean(UserSerImpl.class));//要提供服务的对象
        Provider provider = new Provider();//提供者配置信息
        provider.unUseZK();//不使用zookeeper作为服务路由
        provider.setPort(7777);//对外提供服务端口
        provider.setTarget("127.0.0.1:7777");//zookeeper 地扯,调用unUseZK此方法无效
        ProviderProxyFactory providerProxyFactory = new ProviderProxyFactory(providerObjs,provider);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
}

