package corgi.spring.test_exception.code;


import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.rpc.spring.annotation.Service;

/**
 * Created by huanghuanlai on 16/9/27.
 */
@Service(version = "1.0.0")
public class UserApiImpl implements UserApi {

    @Override
    public void login(String username) throws SerException {
        if (!"admin".equals(username)) {
            throw new SerException(username+" not found");
        }
    }
}
