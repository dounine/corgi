package corgi.spring.test_java_service.code;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by huanghuanlai on 16/9/27.
 */

@Configuration
@EnableJpaRepositories(basePackages = {"corgi.spring.test_java_service.code.dao"})
@EnableTransactionManagement(proxyTargetClass = true)
@EnableCaching
@PropertySource("classpath:config.properties")
@ComponentScan(basePackages = {"corgi.spring.test_java_service.code"},
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = {Configuration.class})})
public class ApplicationConfiguration {

}
