package corgi.spring.test_generalize.code;

import com.dounine.corgi.exception.SerException;

/**
 * Created by huanghuanlai on 16/9/27.
 */

public interface UserApi<T> {

    T login(T username) throws SerException;
}
