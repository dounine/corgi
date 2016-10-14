package corgi.spring.test_java_entity.code;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by huanghuanlai on 16/9/27.
 */

@Configuration
@EnableJpaRepositories(basePackages = {"corgi.spring.test_java_service.dao"})
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages = { "com.dounine.corgi.jpa","corgi.spring.test_java_service"},
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = {Configuration.class})})
public class ApplicationConfiguration {


}
