package corgi.spring.test_collection.code;

import com.dounine.corgi.spring.rpc.Reference;
import com.dounine.corgi.spring.rpc.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by huanghuanlai on 16/9/28.
 */
@Service
public class People {

    @Reference(version = "1.0.0")
    UserApi userApi;

    public Map<String,String> getMap(String username){
        return userApi.getMap(username);
    }

    public List<String> getList(String username){
        return userApi.getList(username);
    }

}
