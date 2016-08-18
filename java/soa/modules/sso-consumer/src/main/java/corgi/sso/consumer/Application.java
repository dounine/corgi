package corgi.sso.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = "corgi.sso.consumer",
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = {Configuration.class})})
public class Application implements EmbeddedServletContainerCustomizer {

	public static void main(String[] args){
		SpringApplication.run(Application.class,args);
	}

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(8080);
    }
}

