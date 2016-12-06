package rpc.test_jta.pro;


import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.spring.rpc.Service;
import rpc.test_jta.api.UserApi;

/**
 * Created by huanghuanlai on 16/9/27.
 */
@Service(version = "1.0.2")
public class UserApiImpl2 implements UserApi {

    @Override
    public void login(String username) throws SerException {
        if (!"admin".equals(username)) {
            throw new SerException(username+" not found");
        }
    }
}
