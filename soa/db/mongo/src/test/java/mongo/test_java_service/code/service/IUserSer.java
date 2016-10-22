package mongo.test_java_service.code.service;

import com.dounine.corgi.exception.SerException;
import mongo.test_java_service.code.dto.UserDto;
import mongo.test_java_service.code.entity.User;
import com.dounine.corgi.mongo.service.IService;

/**
 * Created by lgq on 16-10-17.
 */
public interface IUserSer extends IService<User,UserDto>{
    User findByUserName(String username) throws SerException;
}
