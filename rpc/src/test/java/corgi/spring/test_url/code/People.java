package corgi.spring.test_url.code;

import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.rpc.spring.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huanghuanlai on 16/9/28.
 */
@Service
public class People {

    @Autowired(version = "1.0.0",url = "localhost:7777")
    UserApi userApi;

    public String login(String username){
        try {
            userApi.login(username);
            return "success";
        } catch (SerException e) {
            e.printStackTrace();
        }
        return "fail";
    }


}
