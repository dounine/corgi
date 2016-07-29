package corgi.captcha.service.login;

import corgi.captcha.dao.login.ILoginInfoRep;
import corgi.captcha.entity.user.LoginInfo;
import corgi.captcha.service.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huanghuanlai on 16/6/18.
 */
@Service
public class LoginInfoSerImpl extends ServiceImpl<LoginInfo> implements ILoginInfoSer {

    @Autowired
    private ILoginInfoRep loginInfoRep;

    @Override
    public void putLoginInfo(String account, String ip, boolean success) {
        loginInfoRep.addLoginInfo(account,ip,success);
    }
}
