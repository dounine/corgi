package rpc.test_jta.code;

import com.dounine.corgi.context.RpcContext;
import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.spring.rpc.Reference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpc.test_jta.api.UserApi;

/**
 * Created by huanghuanlai on 16/9/28.
 */
@Service
public class People {

    @Reference(version = "1.0.0",timeout = 4000)
    UserApi userApi;

    @Transactional
    public String login(String username) throws SerException{
        RpcContext.put("nihao", 1100);
        userApi.login(username);
        if (true) {
            throw new RuntimeException("this is a [ try exception ]");
        }
        userApi.login(username);
        return "success";
    }

}
