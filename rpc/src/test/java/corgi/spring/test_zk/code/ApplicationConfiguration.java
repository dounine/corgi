package corgi.spring.test_zk.code;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by huanghuanlai on 16/9/27.
 */
@Configuration
@PropertySource("classpath:corgi.properties")
@ComponentScan(basePackages = {"corgi.spring.test_zk"},
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = {Configuration.class})})
public class ApplicationConfiguration {

}
