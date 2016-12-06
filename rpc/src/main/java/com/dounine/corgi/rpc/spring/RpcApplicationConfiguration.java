package com.dounine.corgi.rpc.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by huanghuanlai on 2016/12/6.
 */
@Configuration
@PropertySource("classpath:corgi.properties")
@ComponentScan(basePackages = {"com.dounine.corgi.filter"},
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = {Configuration.class})})
public class RpcApplicationConfiguration {
}
