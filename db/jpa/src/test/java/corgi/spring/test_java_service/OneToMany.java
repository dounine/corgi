package corgi.spring.test_java_service;

import com.alibaba.fastjson.JSON;
import com.dounine.corgi.jpa.exception.SerException;
import corgi.spring.test_java_service.code.ApplicationConfiguration;
import corgi.spring.test_java_service.code.entity.User;
import corgi.spring.test_java_service.code.entity.UserInfo;
import corgi.spring.test_java_service.code.entity.UserInterest;
import corgi.spring.test_java_service.code.service.IUserInterestSer;
import corgi.spring.test_java_service.code.service.IUserSer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by huanghuanlai on 2016/10/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class OneToMany {


    /**
     * one to many Test
     * 一般由多的一方维护关系
     */

    @Autowired
    private IUserSer userSer;
    @Autowired
    private IUserInterestSer interestSer;



    @Before
    public void init()throws SerException {
        if(null==userSer.findByUsername("liguiqin")){
            User user = new User();
            user.setUsername("liguiqin");
            user.setPassword("123456");
        }
    }

    @Test
    public void addUserInterest() throws SerException{
        User user = userSer.findByUsername("liguiqin");
        List<UserInterest> interests = new ArrayList<>(5);
        for(int i=0;i<5;i++){
            UserInterest interest = new UserInterest();
            interest.setName("ga"+i);
            interest.setUser(user);
            interest.setSeq(i);
            interests.add(interest);
        }
        interestSer.save(interests);
        System.out.println(JSON.toJSONString(user));
    }

    @Test
    public void delUserInterest() throws SerException{
        User user = userSer.findByUsername("liguiqin");
        Set<UserInterest> interests = user.getInterests();
        if(null!=interests){
            interestSer.remove(interests);
        }
        System.out.println(JSON.toJSONString(user));
    }


    @Test
    public void updateUserInterest() throws SerException{
        User user = userSer.findByUsername("liguiqin");
        Set<UserInterest> interests = user.getInterests();
        if(null!= interests){
            for(UserInterest interest : interests){
                interest.setName("update"+ new Random().nextInt(999));
            }
        }
        interestSer.update(interests);
        System.out.println(JSON.toJSONString(user));
    }



}
