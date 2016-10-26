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
     *   双方共同维护关系 User Role  中间表 UserRole
     *   一般不会直接用manytomany,而是用中间表进行维护关系
     *   即 双向一对多来实现多对多
     */


    @Autowired
    private IUserSer userSer;

    @Test
    public void generateTable(){

    }

    @Test
    public void findUser()throws SerException {
        List<User> users  = userSer.findAll();

        for(User u:users){
            System.out.println(u.getInterests().size());
            System.out.println(u);
        }

    }



}
