package corgi.spring.test_version.code;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * Created by huanghuanlai on 16/9/27.
 */
@Configuration
@ComponentScan(basePackages = {"corgi.spring.test_version","corgi.common"},
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = {Configuration.class})})
public class ApplicationConfiguration {

}
