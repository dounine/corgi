package rpc.test_jta.code;

import com.dounine.corgi.rpc.spring.RpcApplicationConfiguration;
import org.springframework.context.annotation.*;

/**
 * Created by huanghuanlai on 16/9/27.
 */
@EnableAspectJAutoProxy(proxyTargetClass = true)
@PropertySource({"classpath:config.properties"})
@ComponentScan(basePackages = {"rpc.test_jta"})
public class ApplicationConfiguration extends RpcApplicationConfiguration{

}
