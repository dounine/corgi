package corgi.spring.test_zk.code;

import com.dounine.corgi.rpc.spring.RpcProvider;
import com.dounine.corgi.rpc.spring.annotation.Service;
import com.dounine.corgi.rpc.zk.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huanghuanlai on 2016/10/12.
 */
@Component
public class MyRPCProvider extends RpcProvider implements BeanPostProcessor {

    public static final Logger LOGGER = LoggerFactory.getLogger(MyRPCProvider.class);

    ZkClient zkClient = new ZkClient("localhost:2181");
    static String hostName = null;
    private static final List<Class> REGISTER_ClASS = new ArrayList<>();

    static {
        try {
            hostName = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public boolean checkRpcService(Object bean) {
        return bean.getClass().isAnnotationPresent(Service.class);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (checkRpcService(bean)) {
            registerObject(bean);
        }
        return bean;
    }

    public void registerObject(Object bean) {
        Class apiClass = null;
        for(Class interfac : bean.getClass().getInterfaces()){
            if(!REGISTER_ClASS.contains(interfac)){
                apiClass = interfac;
                break;
            }
        }
        if(null!=apiClass){
            REGISTER_ClASS.add(apiClass);
            String apiPath = apiClass.getName().replace(".", "/");
            zkClient.createPersistent("/" + apiPath);
            zkClient.createEpseq("/" + apiPath + "/node", nodeInfo());
            LOGGER.info("CORGI rpc provider { name : '" + apiClass.getName() + "' }");
        }
    }

    public String nodeInfo(){
        return hostName+":"+getPort();
    }

}
