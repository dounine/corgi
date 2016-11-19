package jta.consumer;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by huanghuanlai on 16/8/16.
 */
@Configuration
@PropertySource({"classpath:config.properties"})
@ComponentScan(basePackages = {"jta.consumer","com.dounine.jta"},
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = {Configuration.class})})
public class ApplicationConfiguration {

}
