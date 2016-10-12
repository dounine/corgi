package corgi.spring.test_void.code;

import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.rpc.spring.Service;

/**
 * Created by huanghuanlai on 16/9/27.
 */
@Service(version = "1.0.1")
public class UserApiImpl1 implements UserApi {

    @Override
    public void login(String username) throws SerException {
        if (!"admin".equalsIgnoreCase(username)) {
            throw new SerException(username+" not found");
        }
    }
}
