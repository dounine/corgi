package corgi.spring.test_java_service;

import com.alibaba.fastjson.JSON;
import com.dounine.corgi.jpa.exception.SerException;
import corgi.spring.test_java_service.code.ApplicationConfiguration;
import corgi.spring.test_java_service.code.entity.User;
import corgi.spring.test_java_service.code.entity.UserInfo;
import corgi.spring.test_java_service.code.service.IUserInterestSer;
import corgi.spring.test_java_service.code.service.IUserSer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by huanghuanlai on 2016/10/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class OneToOne {

    /**
     * 主控方来维持对象关系
     * mappedBy = "user" User 为主控方，维持userInfo（被控方关系）
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
        }
    }

    @Test
    public void addUserInfo() throws SerException{
        User user = userSer.findByUsername("liguiqin");
        UserInfo info = user.getUserInfo();
        if(null==info){
            info = new UserInfo();
        }
        info.setEmail("xinaml@qq.com");
        info.setFox("1122");
        info.setUser(user);
        user.setUserInfo(info);
        userSer.update(user);
        System.out.println(JSON.toJSONString(user));
    }




}
