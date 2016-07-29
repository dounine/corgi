package corgi.captcha.dao.login;

import corgi.captcha.dao.RepositoryImpl;
import corgi.captcha.entity.user.LoginInfo;
import corgi.captcha.session.user.UserSession;
import org.springframework.stereotype.Repository;

/**
 * Created by huanghuanlai on 16/6/18.
 */
@Repository
public class LoginInfoRepImpl extends RepositoryImpl<LoginInfo> implements  ILoginInfoRep {

    @Override
    public void addLoginInfo(String account, String ip, boolean success) {
        UserSession.putLoginInfo(account,ip,success);
    }
}
