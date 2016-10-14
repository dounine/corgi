package corgi.spring.test_java_service;

import com.alibaba.fastjson.JSON;
import com.dounine.corgi.jpa.dto.Condition;
import com.dounine.corgi.jpa.enums.DataType;
import com.dounine.corgi.jpa.enums.RestrictionType;
import com.dounine.corgi.jpa.exception.SerException;
import corgi.spring.test_java_service.code.ApplicationConfiguration;
import corgi.spring.test_java_service.code.dto.UserDto;
import corgi.spring.test_java_service.code.entity.User;
import corgi.spring.test_java_service.code.entity.UserDetails;
import corgi.spring.test_java_service.code.entity.UserInterest;
import corgi.spring.test_java_service.code.service.IUserSer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by huanghuanlai on 2016/10/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =ApplicationConfiguration.class)
public class JunitTest {



    @Test
    public void start(){

    }

    @Autowired
    private IUserSer userSer;

    /**
     * 插入对象
     */
    @Test
    public void addUser() throws SerException {
        User user = new User();
        user.setUsername("liguiqin");
        user.setPassword("123456");
        user.setMoney(5000.0);
        userSer.save(user);
        System.out.println(JSON.toJSONString(user));
    }

    /**
     * 保存对象,(保存 many-to-one,one-to-many )
     */
    @Test
    public void add() throws SerException {
        User user = new User();
        user.setUsername("liguiqin66");
        user.setAge(77);
        user.setPassword("7777755");

        UserDetails details = new UserDetails();
        details.setEmail("liguiqin@qq.com");
        details.setAddress("广州");
        user.setDetails(details);

        Set<UserInterest> interests = new HashSet<>(1);
        UserInterest interest = new UserInterest();
        interest.setSeq(1);
        interest.setName("cpmputer");
        interest.setUser(user);
        interest.setCreateTime(LocalDateTime.now());
        interests.add(interest);
        user.setInterests(interests);
        userSer.save(user);
        System.out.println(JSON.toJSONString(user));
    }


    /**
     * 分页查询,可带条件及排序
     */
    @Test
    public void findByPage() throws SerException {
        UserDto dto = new UserDto();
       // List<User> users = userSer.findByPage(dto);
    //    System.out.println(JSON.toJSONString(users));
    }


    //条件查询
    @Test
    public void findByCis() throws SerException {
        UserDto dto = new UserDto();
        Condition condition = new Condition();
        String[] between =
                new String[]{"1", "99"};
        condition.setField("age");
        condition.setValues(between);
        condition.setFieldType(DataType.INT);
        condition.setRestrict(RestrictionType.BETWEEN);
        dto.getConditions().add(condition);
        //List<User> users = userSer.findByCis(dto, true); //按条件查询并分页
     //   System.out.println(JSON.toJSONString(users));
    }


    /**
     * 更新对象
     */
    @Test
    public void update() throws SerException {

        User user = userSer.findByUsername("liguiqin");
        user.setPassword("666 this is a pass");
        user.getDetails().setTelephone("1999999");
        //userSer.update(user);
        System.out.println(JSON.toJSONString(user));
    }

    /**
     * 删除对象
     */
    @Test
    public void remove() throws Throwable {
        User user = userSer.findByUsername("liguiqin");
       // userSer.remove(user.getId());
        System.out.println("remove user success!");
    }

    /**
     * 模糊查询
     */
    @Test
    public void findByLike() throws Throwable {
        UserDto dto = new UserDto();
        Condition c = new Condition("username", DataType.STRING);
        c.setValues(new String[]{"gui"});
        c.setRestrict(RestrictionType.LIKE);
        dto.getConditions().add(c);
      //  User user = userSer.findOne(dto);
        //System.out.println(JSON.toJSONString(user));
    }

    /**
     * 批量添加数据
     */
    @Test
    public void addAll() throws Throwable {
        List<User> users = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setUsername("testName" + i);
            user.setAge(20 + i);
            user.setPassword("password" + i);
            users.add(user);

        }
        //userSer.save(users);
        System.out.println(JSON.toJSONString(users));

    }

    /**
     * 批量修改数据
     */
    @Test
    public void updateAll() throws Throwable {
        UserDto dto = new UserDto();
        addAll();
        List<User> users = null;
        Condition c = new Condition("username", DataType.STRING);
        c.setValues(new String[]{"testName"});
        c.setRestrict(RestrictionType.LIKE);
        dto.getConditions().add(c);
     //   users = userSer.findByCis(dto, true);
        int i = 0;
        for (User user : users) {
            user.setUsername("updateName" + i++);
        }
     //   userSer.update(users);
        System.out.println(JSON.toJSONString(users));

    }



    /**
     * 数据缓存
     */
    @Test
    public void cache() throws Throwable {
        String name = "liguiqin";
        userSer.findByNickname(name);
        userSer.findByNickname(name);
        userSer.findByNickname(name);
        userSer.findByNickname(name);
    }


}
