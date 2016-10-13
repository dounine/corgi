package corgi.spring.test_version.code;


import com.dounine.corgi.rpc.spring.Service;

/**
 * Created by huanghuanlai on 16/9/27.
 */
@Service(version = "1.0.0")
public class UserApiImpl implements UserApi {

    @Override
    public String findByUsername(String username) {
        if ("admin".equals(username)) {
            return "success";
        }
        return "fail";
    }
}
