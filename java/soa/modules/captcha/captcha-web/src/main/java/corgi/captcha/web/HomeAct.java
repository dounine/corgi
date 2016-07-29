package corgi.captcha.web;

import corgi.captcha.service.login.ILoginInfoSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by huanghuanlai on 16/6/13.
 */
@RestController
@SessionAttributes("currentUser")
public class HomeAct {

    @Autowired
    private ILoginInfoSer loginInfoSer;

    @GetMapping("")
    public ModelAndView home(){
        loginInfoSer.putLoginInfo("admin","192.168.0.1",true);
        return new ModelAndView("index");
    }

}
