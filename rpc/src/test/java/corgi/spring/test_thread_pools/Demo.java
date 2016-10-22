package corgi.spring.test_thread_pools;

import com.dounine.corgi.spring.ApplicationContext;
import corgi.spring.test_thread_pools.code.ApplicationConfiguration;
import corgi.spring.test_thread_pools.code.People;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.CountDownLatch;

/**
 * Created by huanghuanlai on 16/9/27.
 */
public class Demo {

    @Test
    public void testLogin(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        ApplicationContext.setApplicationContext(context);
        CountDownLatch run = new CountDownLatch(20);
        for(int i =0;i<20;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Assert.assertEquals("success",context.getBean(People.class).login("admin"));
                    run.countDown();
                }
            }).start();
        }
        try {
            run.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
