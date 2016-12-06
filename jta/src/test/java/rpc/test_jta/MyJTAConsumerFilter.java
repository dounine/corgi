package rpc.test_jta;

import com.dounine.corgi.jta.filter.impl.JTAConsumerFilterImpl;
import com.dounine.corgi.jta.filter.impl.JTAProviderFilterImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by huanghuanlai on 2016/11/30.
 */
@Component
@Primary
public class MyJTAConsumerFilter extends JTAConsumerFilterImpl {

}
