package com.dounine.corgi.sso.service.user;

import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.mongo.service.ServiceImpl;
import com.dounine.corgi.sso.dto.UserDto;
import com.dounine.corgi.sso.entity.user.User;
import com.dounine.corgi.sso.session.TokenUtils;
import com.dounine.corgi.sso.session.UserSession;
import org.springframework.stereotype.Service;

/**
 * Created by huanghuanlai on 16/5/24.
 */
@Service
public class UserSerImpl extends ServiceImpl<User,UserDto> implements IUserSer {

    public boolean verify(String token) throws SerException {
        if(TokenUtils.verify(token)){
            return UserSession.exist(token);
        }
        throw new SerException("token无效");
    }

    @Override
    public String login(User user) throws SerException{
        if(null!=user){
            //if("admin".equals(user.getUsername())&&"admin".equals(user.getPassword())){
                UserSession.removeByUsername(user.getUsername());
                String token = TokenUtils.create("192.168.0.1",user.getUsername());
                UserSession.put(token,user);
                return token;
           // }
        }
        return null;
    }

    @Override
    public void cookieInit(User user) throws SerException {

    }

}
