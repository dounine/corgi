package corgi.spring.test_rpcmethod.code;


import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.spring.rpc.RpcMethod;
import com.dounine.corgi.spring.rpc.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by huanghuanlai on 16/9/27.
 */
@Service(version = "1.0.0")
public class UserApiImpl implements UserApi {

    @Override
    @RpcMethod(timeout = 1000)
    public void login(String username) throws SerException {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!"admin".equals(username)) {
            throw new SerException("not found");
        }
    }
}
