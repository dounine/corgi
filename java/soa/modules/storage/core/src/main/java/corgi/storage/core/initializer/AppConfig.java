package corgi.storage.core.initializer;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(
        basePackages = Constant.PROJECT_NAME,
        excludeFilters = { @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = {Controller.class,Configuration.class})})
public class AppConfig {

}
