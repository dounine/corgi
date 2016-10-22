package mongo.test_java_service;

import com.alibaba.fastjson.JSON;
import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.mongo.dto.Condition;
import mongo.test_java_service.code.ApplicationConfiguration;
import mongo.test_java_service.code.dto.UserDto;
import com.dounine.corgi.mongo.enums.DataType;
import com.dounine.corgi.mongo.enums.RestrictionType;
import mongo.test_java_service.code.entity.User;
import mongo.test_java_service.code.entity.UserDetails;
import mongo.test_java_service.code.service.IUserSer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by lgq on 16-10-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class JunitTest {

    @Autowired
    public IUserSer userSer;

    @Before
    public  void init() throws SerException{
        if(null==userSer.findByUserName("liguiqin")){
            User user = new User();
            user.setAge(16);
            user.setUsername("liguiqin");
            user.setPassword("123456");
            UserDetails details = new UserDetails();
            details.setAddress("广州天河");
            details.setCompany("aj");
            details.setTelephone("110");
            user.setDetails(details);
            userSer.save(user);
        }
    }

    /**
     * 添加对象
     * @throws SerException
     */
    @Test
    public  void addUser() throws SerException{
        User user = new User();
        user.setAge(16);
        user.setUsername("gui");
        user.setPassword("123456");
        UserDetails details = new UserDetails();
        details.setAddress("广州");
        details.setCompany("艾佳");
        details.setTelephone("888");
        user.setDetails(details);
        userSer.save(user);
        System.out.println(user);
    }

    /**
     * 按照条件查询对象
     * @throws SerException
     */
    @Test
    public  void findByCis() throws SerException{
        UserDto dto = new UserDto();
        Condition condition = new Condition("username",DataType.STRING);
        condition.setRestrict(RestrictionType.EQ);
        condition.setValues(new String[]{"liguiqin"});
        dto.getConditions().add(condition);
        System.out.println(JSON.toJSONString(userSer.findByCis(dto)));
    }


    /**
     * 批量添加数据
     */
    @Test
    public void addAll() throws Throwable {
        List<User> users = new ArrayList<>(5);
        for (int i = 0; i < 15; i++) {
            User user = new User();
            user.setUsername("test_mongo" + i);
            user.setAge(20 + i);
            user.setPassword("password" + i);
            users.add(user);

        }
        userSer.save(users);
        System.out.println(JSON.toJSONString(users));

    }


    /**
     * 查询分页
     * @throws SerException
     */
    @Test
    public  void findByPage() throws SerException{
        UserDto dto = new UserDto();
//        dto.getConditions().add(condition); 添加条件
        dto.setPage(1);
        List<User> users = userSer.findByPage(dto);
        for(User user : users){
            System.out.println(user.getUsername());
        }
    }


    /**
     * 查询所有
     * @throws SerException
     */
    @Test
    public  void findAll() throws SerException{
        System.out.println(JSON.toJSONString(userSer.findAll()));
    }

    @Test
    public void update() throws SerException {
      User user =   userSer.findByUserName("liguiqin");
      user.setAge(666);
      userSer.update(user);
    }


    /**
     * 批量修改数据
     */
    @Test
    public void updateAll() throws SerException {
        UserDto dto = new UserDto();
        List<User> users = null;
        Condition c = new Condition("username", DataType.STRING);
        c.setValues(new String[]{"test_mongo"});
        c.setRestrict(RestrictionType.LIKE);
        dto.getConditions().add(c);
        users = userSer.findByCis(dto, true);
        for (User user : users) {
            user.setUsername("update" +  new Random().nextInt(9999));
        }
        userSer.update(users);
        for(User user : users){
            System.out.println(user.getUsername());
        }
    }

    @Test
    public void remove() throws SerException {
        User user =   userSer.findByUserName("liguiqin");
        userSer.remove(user);
    }


    @Test
    public void removeAll() throws SerException {
        List<User> users =   userSer.findAll();
        userSer.remove(users);
    }


}
