package mongo.test_java_service.code.dao;

import mongo.test_java_service.code.dto.UserDto;
import mongo.test_java_service.code.entity.User;
import com.dounine.corgi.mongo.dao.RepImpl;
import org.springframework.stereotype.Repository;

/**
 * Created by lgq on 16-10-17.
 */
@Repository
public class UserRepImpl extends RepImpl<User,UserDto> implements IUserRep {

}
