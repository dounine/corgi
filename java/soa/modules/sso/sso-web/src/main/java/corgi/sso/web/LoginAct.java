package corgi.sso.web;

import corgi.commons.ResponseText;
import corgi.jsonp.Callback;
import corgi.response.ResponseContext;
import corgi.sso.entity.user.User;
import corgi.sso.service.user.IUserSer;
import corgi.sso.session.user.UserSession;
import corgi.validation.Add;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huanghuanlai on 16/6/13.
 */
@RestController
public class LoginAct{

    @Autowired
    private IUserSer userSer;

    @GetMapping("all")
    public List<User> all(){
        return userSer.getAllObjects();
    }

    @GetMapping("")
    public ModelAndView home(){
        return new ModelAndView("index");
    }

    @GetMapping("add")
    public void add(){
        User user = new User();
        user.setAccessTime(LocalDateTime.now());
        user.setPassword("1234");
        user.setUsername("hhl");
        userSer.saveObject(user);
    }

    @PostMapping("login")
    public ResponseText login(@Validated(Add.class) User user, BindingResult result,@Callback String callback){
        boolean callbackFun = StringUtils.isNotBlank(callback);
        ResponseText rt = null;
        String token = userSer.login(user);
        StringBuffer sb = new StringBuffer();
        if(StringUtils.isNotBlank(token)){
            if(callbackFun){
                sb.append(callback);
                sb.append(String.format("({token:\"%s\"})",token));
                ResponseContext.writeData(sb);
            }else{
                rt = new ResponseText();
                rt.setData(String.format("{token:\"%s\"}",token));
                Cookie tokenCookie = new Cookie("token",token);
                ResponseContext.get().addCookie(tokenCookie);
            }
        }else{
            if(callbackFun){
                sb.append(callback);
                sb.append("({msg:\"LOGIN-0002\"})");
                ResponseContext.writeData(sb);
            }else{
                rt = new ResponseText();
                rt.setErrno(2);
                rt.setMsg("LOGIN-0002");
            }
        }
        return rt;
    }

    @GetMapping(value = "{token}/verify")
    public ResponseText verify(@PathVariable String token){
        return new ResponseText(String.format("{verify:%s}",UserSession.verify(token)));
    }

    @GetMapping(value = "verify")
    public ResponseText verifyC(@CookieValue String token){
        return verify(token);
    }

    @GetMapping("status")
    public List<String> status(){
        List<String> status = new ArrayList<>(0);
        status.add("LOGIN-0001:paramters not valided");
        status.add("LOGIN-0002:login failed");

        return status;
    }

    public IUserSer getUserSer() {
        return userSer;
    }

    public void setUserSer(IUserSer userSer) {
        this.userSer = userSer;
    }
}
