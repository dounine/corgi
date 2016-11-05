package corgi.spring.test_zk.code;


import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.spring.rpc.Service;

/**
 * Created by huanghuanlai on 16/9/27.
 */
@Service(version = "1.0.1")
public class UserApiImpl1 implements UserApi {

    @Override
    public void login(String username) throws SerException {
        if (!"admin".equals(username)) {
            throw new SerException("not found");
        }
    }
}
