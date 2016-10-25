package corgi.spring.test_java_service;

import com.alibaba.fastjson.JSON;
import com.dounine.corgi.jpa.dto.Condition;
import com.dounine.corgi.jpa.enums.DataType;
import com.dounine.corgi.jpa.enums.RestrictionType;
import com.dounine.corgi.jpa.exception.SerException;
import corgi.spring.test_java_service.code.ApplicationConfiguration;
import corgi.spring.test_java_service.code.dto.UserDto;
import corgi.spring.test_java_service.code.entity.User;
import corgi.spring.test_java_service.code.entity.UserGroup;
import corgi.spring.test_java_service.code.service.IUserGroupSer;
import corgi.spring.test_java_service.code.service.IUserSer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Created by huanghuanlai on 2016/10/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class ManyToMany {
    /**
     *   双方共同维护关系
     *   一般不会直接用manytomany,而是用中间表进行维护关系
     *   即 双向一对多来实现多对多
     */


    @Autowired
    private IUserSer userSer;

    @Before
    public void init()throws SerException {
        if(null==userSer.findByUsername("liguiqin")){
            User user = new User();
            user.setUsername("liguiqin");
            user.setPassword("123456");
            user.setMoney(5000.0);
            user.setAge(55);
            user.setHeight(1.2f);
            user.setNickname("xiaoming");
            userSer.save(user);
        }
    }

    @Test
    public void findUser()throws SerException {
        User u  = userSer.findByUsername("liguiqin");
        System.out.println(u);
    }



}
