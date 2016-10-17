package corgi.spring.test_java_service.code.service;


import com.dounine.corgi.jpa.exception.SerException;
import com.dounine.corgi.jpa.service.IService;
import corgi.spring.test_java_service.code.dto.UserDto;
import corgi.spring.test_java_service.code.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * Created by huanghuanlai on 16/5/24.
 */
public interface IUserSer extends IService<User, UserDto> {

    @Cacheable("serviceCache")
    User findByUsername(String username) throws SerException;

    List<User> findByNickname(String nickname) throws SerException;


}
