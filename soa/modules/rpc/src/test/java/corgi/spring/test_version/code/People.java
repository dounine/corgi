package corgi.spring.test_version.code;

import com.dounine.corgi.rpc.spring.Reference;
import org.springframework.stereotype.Service;

/**
 * Created by huanghuanlai on 16/9/28.
 */
@Service
public class People {

    @Reference(version = "1.0.0")
    UserApi userApi;

    public String login(String username){
        return userApi.findByUsername(username);
    }


}
