package corgi.spring.test_timeout.code;


import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.rpc.spring.annotation.Method;
import com.dounine.corgi.rpc.spring.annotation.Service;

/**
 * Created by huanghuanlai on 16/9/27.
 */
@Service(version = "1.0.0",timeout = 4000)
public class UserApiImpl implements UserApi {

    @Override
    @Method(timeout = 3000)
    public void login(String username) throws SerException {
//        try {
//            Thread.sleep(600);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        if (!"admin".equals(username)) {
            throw new SerException("not found");
        }
    }
}
