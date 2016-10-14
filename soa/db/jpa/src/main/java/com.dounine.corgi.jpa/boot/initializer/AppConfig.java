package com.dounine.corgi.jpa.boot.initializer;

import com.dounine.corgi.jpa.boot.Constant;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("classpath:config.properties")
@EnableJpaRepositories(basePackages = {"com.dounine.corgi.jpa.dao"})
public class AppConfig {

}
