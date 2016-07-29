package corgi.sso.service.user;


import corgi.sso.entity.user.User;
import corgi.sso.service.IService;

/**
 * Created by huanghuanlai on 16/5/24.
 */
public interface IUserSer extends IService<User> {

    boolean verify(String token);

    String login(User user);

    void cookieInit(User user);
}
