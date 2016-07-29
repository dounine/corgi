package corgi.sso.web.user;

import corgi.commons.ResponseText;
import corgi.sso.service.user.IUserSer;
import corgi.sso.session.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by huanghuanlai on 16/5/24.
 */
@RestController
@RequestMapping("user")
public class UserAct {

    @Autowired
    private IUserSer iUserSer;

    @RequestMapping(value = "online",method = RequestMethod.GET)
    public ResponseText list(){
        return new ResponseText(UserSession.sessions());
    }
}
