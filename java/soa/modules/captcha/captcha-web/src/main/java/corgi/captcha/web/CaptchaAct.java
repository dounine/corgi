package corgi.captcha.web;

import corgi.captcha.entity.user.LoginInfo;
import corgi.captcha.service.login.ILoginInfoSer;
import corgi.captcha.session.user.UserSession;
import corgi.commons.ResponseText;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by huanghuanlai on 16/6/13.
 */
@RestController
public class CaptchaAct {

    @Autowired
    private ILoginInfoSer loginInfoSer;

    /**
     * 检查是否需要验证码
     * @param account 用户名
     * @param callback jsonp回调函数
     * @param request 请求对象
     * @param response 响应对象
     * @return 响应结果
     */
    @PostMapping("checkNeedCaptcha")
    public ResponseText login(String account, String callback, HttpServletRequest request, HttpServletResponse response){
        boolean callbackFun = StringUtils.isNotBlank(callback);
        StringBuffer sb = new StringBuffer();
        if(callbackFun){
            try {
                sb.append(callback);
                sb.append("({captchaFlag:false})");
                response.getWriter().print(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }else{
            sb.append("{captchaFlag:false}");
        }
        return new ResponseText(sb);
    }

    /**
     * 登录失败调用
     * @param account 用户名
     * @param request 请求对象
     * @param response 响应对象
     * @return 响应结果
     */
    @GetMapping("loginErr")
    public ResponseText loginErr(String account,HttpServletRequest request,HttpServletResponse response){
        UserSession.putLoginInfo(account,"192.168.0.1",false);
        return null;
    }

    @GetMapping("logins")
    public Map<String, List<LoginInfo>> logins(){
        return UserSession.loginInfos();
    }

    /**
     * 登录成功调用
     * @param account 用户名
     * @param request 请求对象
     * @param response 响应对象
     * @return 响应结果
     */
    @GetMapping("loginSucc")
    public ResponseText loginSucc(String account,HttpServletRequest request,HttpServletResponse response){
        UserSession.putLoginInfo(account,"192.168.0.2",true);
        return null;
    }
}
