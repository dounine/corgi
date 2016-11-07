package corgi.spring.test_rpcmethod;

import com.dounine.corgi.exception.RPCException;
import com.dounine.corgi.spring.ApplicationContext;
import corgi.spring.test_rpcmethod.code.ApplicationConfiguration;
import corgi.spring.test_rpcmethod.code.People;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
        }catch (RPCException e){
            if(!e.getMessage().equals("API接口连接超时")){
                Assert.fail("fail");
            }
        }
    }

}
