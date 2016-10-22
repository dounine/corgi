package com.dounine.corgi.mongo.boot.datasource;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by lgq on 16-10-17.
 */
@Configuration
@PropertySource("classpath:config.properties")
@ComponentScan(basePackages = {"com.dounine.corgi.mongo"},
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = {Configuration.class})})
public class AppConfig {
}
