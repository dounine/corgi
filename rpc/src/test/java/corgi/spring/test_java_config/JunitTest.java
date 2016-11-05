package corgi.spring.test_java_config;

import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.spring.ApplicationContext;
import com.dounine.corgi.spring.rpc.Autowired;
import corgi.spring.test_java_config.code.ApplicationConfiguration;
import corgi.spring.test_java_config.code.UserApi;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by huanghuanulai on 2016/10/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class JunitTest extends AbstractJUnit4SpringContextTests {

    @Autowired(version = "1.0.0")
    UserApi userApi;

    @Before
    public void initContext(){
        ApplicationContext.setApplicationContext(super.applicationContext);
    }

    @Test
    public void testLogin(){
        try {
            userApi.login("admin");
        } catch (SerException e) {
            e.printStackTrace();
        }
    }

}
