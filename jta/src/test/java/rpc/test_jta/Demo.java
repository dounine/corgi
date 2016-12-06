package rpc.test_jta;

import com.dounine.corgi.context.ApplicationContext;
import com.dounine.corgi.exception.SerException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import rpc.test_jta.code.ApplicationConfiguration;
import rpc.test_jta.code.People;

/**
 * Created by huanghuanlai on 16/9/27.
 */
public class Demo {

    @Test
    public void testLogin(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        ApplicationContext.setApplicationContext(context);
        try {
            Assert.assertEquals("success",context.getBean(People.class).login("admin"));
        } catch (SerException e) {
            e.printStackTrace();
        }
    }

}
