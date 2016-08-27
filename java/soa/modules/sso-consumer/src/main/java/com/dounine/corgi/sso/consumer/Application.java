package com.dounine.corgi.sso.consumer;

import com.dounine.corgi.sso.consumer.config.exception.MyHanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = "com.dounine.corgi.sso.consumer",
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = {Configuration.class})})
public class Application extends WebMvcConfigurerAdapter implements EmbeddedServletContainerCustomizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args){
        LOGGER.info("sso-consumer starting...");
		SpringApplication.run(Application.class,args);
	}

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new MyHanException());//unified exception handling
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(8080);
    }
}

