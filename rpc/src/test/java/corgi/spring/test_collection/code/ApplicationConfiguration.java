package corgi.spring.test_collection.code;

import com.dounine.corgi.rpc.spring.RpcApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by huanghuanlai on 16/9/27.
 */
@ComponentScan(basePackages = {"corgi.spring.test_collection","corgi.common"})
public class ApplicationConfiguration extends RpcApplicationConfiguration{

}
