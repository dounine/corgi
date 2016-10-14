package com.dounine.corgi.jpa.boot.initializer;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@PropertySource("classpath:config.properties")
@EnableJpaRepositories(basePackages = {"com.dounine.corgi.jpa.dao"})
public class AppConfig {

}
