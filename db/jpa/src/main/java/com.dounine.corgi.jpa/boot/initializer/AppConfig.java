package com.dounine.corgi.jpa.boot.initializer;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("classpath:config.properties")
@EnableJpaRepositories(basePackages = {"com.dounine.corgi.jpa.dao"})
@EnableTransactionManagement(proxyTargetClass = true)
public class AppConfig {

}
