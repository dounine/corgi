package com.dounine.corgi.sso.dao.user;

import com.dounine.corgi.mongo.dao.DaoImpl;
import com.dounine.corgi.sso.dto.UserDto;
import com.dounine.corgi.sso.entity.user.User;
import org.springframework.stereotype.Repository;

/**
 * Created by huanghuanlai on 16/5/24.
 */
@Repository
public class UserDaoImpl extends DaoImpl<User,UserDto> implements IUserDao {

}
