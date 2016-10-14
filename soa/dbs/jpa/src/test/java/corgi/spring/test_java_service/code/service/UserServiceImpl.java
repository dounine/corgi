package corgi.spring.test_java_service.code.service;

import com.dounine.corgi.jpa.exception.SerException;
import com.dounine.corgi.jpa.service.ServiceImpl;
import corgi.spring.test_java_service.code.dao.IUserRep;
import corgi.spring.test_java_service.code.dto.UserDto;
import corgi.spring.test_java_service.code.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by lgq on 16-10-13.
 */
@Service
public class UserServiceImpl extends ServiceImpl<User, UserDto> implements IUserSer {

    @Autowired
    private IUserRep userRep;

    @PostConstruct
    public void start(){
        System.out.println("UserServiceImpl is init!");
    }

    @Override
    public User findByUsername(String username) throws SerException {
        return userRep.findByUsername(username);
    }

    @Override
    public List<User> findByNickname(String nickname) throws SerException {
        return userRep.findByNickname(nickname);
    }
}
