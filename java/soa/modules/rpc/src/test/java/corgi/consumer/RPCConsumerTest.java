package corgi.consumer;

import corgi.IWelcome;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 服务使用
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RPCConsumerTest.class)
@ComponentScan(basePackages = "corgi.consumer",
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                value = {Configuration.class})})
public class RPCConsumerTest {

    @Autowired
    private IWelcome welcome;

    @Test
    public void testConsumer() {
        for (int i = 0; i < 3; i++) {
            System.out.println(welcome.hello() + " => " + i + " 次");
        }
    }

}
