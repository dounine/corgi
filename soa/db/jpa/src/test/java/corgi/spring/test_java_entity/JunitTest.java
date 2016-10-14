package corgi.spring.test_java_entity;

import com.alibaba.fastjson.JSON;
import com.dounine.corgi.jpa.utils.EntityPackageUtil;
import com.dounine.corgi.spring.ApplicationContextUtils;
import corgi.spring.test_java_entity.code.ApplicationConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by huanghuanlai on 2016/10/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class JunitTest extends AbstractJUnit4SpringContextTests{


    @Test
    public void  generateUserTable(){


    }

}
