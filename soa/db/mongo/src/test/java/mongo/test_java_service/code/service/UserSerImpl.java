package mongo.test_java_service.code.service;

import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.mongo.dto.Condition;
import com.dounine.corgi.mongo.enums.DataType;
import com.dounine.corgi.mongo.enums.RestrictionType;
import com.dounine.corgi.mongo.service.ServiceImpl;
import mongo.test_java_service.code.dao.IUserRep;
import mongo.test_java_service.code.dto.UserDto;
import mongo.test_java_service.code.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lgq on 16-10-17.
 */
@Service
public class UserSerImpl extends ServiceImpl<User, UserDto> implements IUserSer {
    @Autowired
    private IUserRep userRep;

    @Override
    public User findByUserName(String username) throws SerException {
        //userRep.findByCriteria(Criteria.where("username").is(username));
        UserDto dto = new UserDto();
        Condition cis = new Condition("username", DataType.STRING);
        cis.setValues(new String[]{username});
        cis.setRestrict(RestrictionType.EQ);
        dto.getConditions().add(cis);
        return  userRep.findOne(dto);
    }
}
