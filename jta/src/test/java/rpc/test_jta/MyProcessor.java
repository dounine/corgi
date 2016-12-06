package rpc.test_jta;

import com.dounine.corgi.filter.ConsumerFilter;
import com.dounine.corgi.jta.filter.impl.JTAConsumerFilterImpl;
import com.dounine.corgi.rpc.spring.SpringProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by huanghuanlai on 16/10/10.
 */
@Component
public class MyProcessor extends SpringProcessor {

}
