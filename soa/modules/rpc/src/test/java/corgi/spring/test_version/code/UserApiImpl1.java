package corgi.spring.test_version.code;

import com.dounine.corgi.rpc.spring.Service;

/**
 * Created by huanghuanlai on 16/9/27.
 */
@Service(version = "1.0.1")
public class UserApiImpl1 implements UserApi {

    @Override
    public String findByUsername(String username) {
        if ("admin".equalsIgnoreCase(username)) {
            return "success";
        }
        return "fail";
    }
}
