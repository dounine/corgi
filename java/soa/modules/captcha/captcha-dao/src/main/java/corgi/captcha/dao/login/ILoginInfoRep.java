package corgi.captcha.dao.login;

import corgi.captcha.dao.IRepository;
import corgi.captcha.entity.user.LoginInfo;

/**
 * Created by huanghuanlai on 16/6/18.
 */
public interface ILoginInfoRep extends IRepository<LoginInfo> {

    void addLoginInfo(String account,String ip,boolean success);

}
