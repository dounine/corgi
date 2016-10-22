package corgi.spring.test_generalize.code;


import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.rpc.spring.annotation.Service;

/**
 * Created by huanghuanlai on 16/9/27.
 */
@Service(version = "1.0.0")
public class UserApiImpl<T> implements UserApi<T> {

    @Override
    public T login(T username) throws SerException {
        if (!"admin".equals(username)) {
            throw new SerException("not found");
        }
        return (T)"success";
    }
}
