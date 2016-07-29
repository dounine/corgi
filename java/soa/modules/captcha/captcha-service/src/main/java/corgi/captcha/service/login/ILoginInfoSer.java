package corgi.captcha.service.login;

import corgi.captcha.entity.user.LoginInfo;
import corgi.captcha.service.IService;

/**
 * Created by huanghuanlai on 16/6/18.
 */
public interface ILoginInfoSer extends IService<LoginInfo> {

    void putLoginInfo(String account,String ip,boolean success);

}
