package corgi.spring.test_void.code;

import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.spring.rpc.Reference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by huanghuanlai on 16/9/28.
 */
@Service
public class People {

    @Reference(version = "1.0.0")
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
