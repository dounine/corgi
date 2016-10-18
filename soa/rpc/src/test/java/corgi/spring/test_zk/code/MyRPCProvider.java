package corgi.spring.test_zk.code;

import com.dounine.corgi.rpc.spring.RpcProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by huanghuanlai on 2016/10/12.
 */
@Component
public class MyRPCProvider extends RpcProvider implements BeanPostProcessor {

}
