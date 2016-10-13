package corgi.spring.test_exception;

import com.dounine.corgi.spring.ApplicationContextUtils;
import corgi.spring.test_exception.code.ApplicationConfiguration;
import corgi.spring.test_exception.code.People;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by huanghuanlai on 16/9/27.
 */
public class Demo {

    @Test
    public void testLogin(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        ApplicationContextUtils.setApplicationContext(context);
        System.out.println(context.getBean(People.class).login("admin1"));
    }

}
