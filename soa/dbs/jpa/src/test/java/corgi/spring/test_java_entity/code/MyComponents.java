package corgi.spring.test_java_entity.code;

import com.dounine.corgi.jpa.boot.initializer.Components;
import org.springframework.stereotype.Component;

/**
 * Created by lgq on 16-10-13.
 */
@Component
public class MyComponents extends Components {

    @Override
    public String[] scanPackages() {
        return new String[]{"corgi.spring.test_java_entity.code.entity"};
    }
}
