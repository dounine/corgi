package com.dounine.corgi.demo.service.user;

import com.dounine.corgi.demo.entity.user.User;
import com.dounine.corgi.demo.session.TokenUtils;
import com.dounine.corgi.demo.session.UserSession;
import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.spring.rpc.Service;

/**
 * Created by huanghuanlai on 16/5/24.
 */
@Service(version = "1.0.0")
public class UserSerImpl implements IUserSer {

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

    @Override
    public String hello() {
        return "yes,I'm sso impl.";
    }

}
