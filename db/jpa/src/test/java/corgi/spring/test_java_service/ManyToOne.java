package corgi.spring.test_java_service;

import com.alibaba.fastjson.JSON;
import com.dounine.corgi.jpa.dto.Condition;
import com.dounine.corgi.jpa.enums.DataType;
import com.dounine.corgi.jpa.enums.RestrictionType;
import com.dounine.corgi.jpa.exception.SerException;
import corgi.spring.test_java_service.code.ApplicationConfiguration;
import corgi.spring.test_java_service.code.dto.UserDto;
import corgi.spring.test_java_service.code.dto.UserGroupDto;
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
public class ManyToOne {
    /**
     *   双方共同维护关系
     *   ManyToOne 指定 many 一方是不能独立存在的，否则存在孤儿数据
     *  一般一个group都要存在用户
     */

    @Autowired
    private IUserSer userSer;

    @Autowired
    private IUserGroupSer userGroupSer;

    @Before
    public void initGroup() throws SerException {

        if(0==userGroupSer.findAll().size()){
            UserGroup group1 = new UserGroup();
            group1.setName("用户组1");
            group1.setCreateTime(LocalDateTime.now());
            UserGroup group2 = new UserGroup();
            group2.setName("用户组2");
            group2.setCreateTime(LocalDateTime.now());
            userGroupSer.save(Arrays.asList(group1,group2));
        }

        if(null==userSer.findByUsername("liguiqin77")){
            User user = new User();
            user.setUsername("liguiqin77");
            user.setPassword("123456");
            user.setMoney(5000.0);
            userSer.save(user);
        }
    }


    /**
     * 设置用户组到用户
     */
    @Test
    public void addGroupForUser() throws SerException {
        User user =  userSer.findByUsername("liguiqin77");
        user.setPassword("123456");
        user.setMoney(5000.0);
        UserGroup group =userGroupSer.findByName("用户组2");
        user.setGroup(group);
        userSer.update(user);
        System.out.println(JSON.toJSONString(user));
    }



    /**
     * 删除被引用的用户组
     */
    @Test
    public void delGroupForUser() throws SerException {
        UserDto dto = new UserDto();
        Condition condition = new Condition("group.name",DataType.STRING);
        condition.setRestrict(RestrictionType.EQ);
        condition.setValues(new String[]{"用户组2"});
        List<User> users =  userSer.findByCis(dto); //查询所有用户组2 的用户
        for(User user :users){
            user.setGroup(null);
        }
        userSer.update(users);

        UserGroup group =userGroupSer.findByName("用户组2");
        userGroupSer.remove(group);
    }


}
