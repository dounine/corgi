package jta.provider;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by huanghuanlai on 16/8/16.
 */
@Configuration
@PropertySource({"classpath:corgi.properties","classpath:config.properties"})
@ComponentScan(basePackages = {"jta.provider","com.dounine.jta"},
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = {Configuration.class})})
public class ApplicationConfiguration {

}
