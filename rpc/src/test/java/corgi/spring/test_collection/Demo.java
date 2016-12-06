package corgi.spring.test_collection;

import com.dounine.corgi.context.ApplicationContext;
import corgi.spring.test_collection.code.ApplicationConfiguration;
import corgi.spring.test_collection.code.People;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huanghuanlai on 16/9/27.
 */
public class Demo {

    @Test
    public void testMap(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        ApplicationContext.setApplicationContext(context);
        Map<String,String> map = new HashMap<>();
        map.put("you","admin");
        map.put("my","nihao");
        Assert.assertEquals(map,context.getBean(People.class).getMap("admin"));
    }

    @Test
    public void testList(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        ApplicationContext.setApplicationContext(context);
        List<String> list = new ArrayList<>();
        list.add("admin");
        list.add("nihao");
        Assert.assertEquals(list,context.getBean(People.class).getList("admin"));
    }

}
