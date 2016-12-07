package corgi.spring.test_void.code;


import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.spring.rpc.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by huanghuanlai on 16/9/27.
 */
@Service(version = "1.0.0")
public class UserApiImpl implements UserApi {

    @Override
    public void login(String username) throws SerException {
        if (!"admin".equals(username)) {
            throw new SerException("not found");
        }
    }
}
