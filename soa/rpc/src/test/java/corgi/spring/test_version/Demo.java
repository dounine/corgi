package corgi.spring.test_version;

import com.dounine.corgi.rpc.spring.ApplicationBeanUtils;
import corgi.spring.test_version.code.ApplicationConfigurationTest;
import corgi.spring.test_version.code.People;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by huanghuanlai on 16/9/27.
 */
public class Demo {

    @Test
    public void testLogin(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfigurationTest.class);
        ApplicationBeanUtils.setApplicationContext(context);
        System.out.println(context.getBean(People.class).login("admin"));
    }

}