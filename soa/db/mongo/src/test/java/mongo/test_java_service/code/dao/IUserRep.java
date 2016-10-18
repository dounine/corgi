package mongo.test_java_service.code.dao;

import mongo.test_java_service.code.dto.UserDto;
import mongo.test_java_service.code.entity.User;
import com.dounine.corgi.mongo.dao.IRep;

/**
 * Created by lgq on 16-10-17.
 */
public interface IUserRep extends IRep<User,UserDto> {

}
