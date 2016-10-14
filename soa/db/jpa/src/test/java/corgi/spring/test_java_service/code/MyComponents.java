package corgi.spring.test_java_service.code;

import com.dounine.corgi.jpa.boot.initializer.Components;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by lgq on 16-10-13.
 */
@Component
public class MyComponents extends Components {

    @Override
    public String[] scanPackages() {
        return new String[]{"corgi.spring.test_java_service.code.entity"};
    }
}
