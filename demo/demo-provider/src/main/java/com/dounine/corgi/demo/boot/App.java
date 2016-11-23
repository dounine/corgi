package com.dounine.corgi.demo.boot;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by huanghuanlai on 16/8/16.
 */
@Configuration
@PropertySource("classpath:corgi.properties")
@ComponentScan(basePackages = "com.dounine.corgi.demo",
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = {Configuration.class})})
public class App {

}
