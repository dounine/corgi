package corgi.sso.service.user;

import corgi.exception.SerException;
import corgi.sso.entity.user.User;
import corgi.sso.service.ServiceImpl;
import corgi.sso.session.user.TokenUtils;
import corgi.sso.session.user.UserSession;
import org.springframework.stereotype.Service;

/**
 * Created by huanghuanlai on 16/5/24.
 */
@Service
public class UserSerImpl extends ServiceImpl<User> implements IUserSer {

    public boolean verify(String token){
        if(TokenUtils.verify(token)){
            return UserSession.exist(token);
        }
        throw new SerException("token无效");
    }

    @Override
    public String login(User user) {
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
    public void cookieInit(User user) {

    }

}
