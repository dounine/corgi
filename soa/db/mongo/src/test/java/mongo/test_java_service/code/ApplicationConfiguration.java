package mongo.test_java_service.code;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by huanghuanlai on 16/9/27.
 */

@Configuration
@PropertySource("classpath:config.properties")
@ComponentScan(basePackages = {"com.dounine.corgi.mongo","mongo.test_java_service.code"},
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = {Configuration.class})})
public class ApplicationConfiguration {


}
