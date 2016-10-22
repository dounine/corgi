package corgi.spring.test_collection.code;


import com.dounine.corgi.rpc.spring.annotation.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huanghuanlai on 16/9/27.
 */
@Service(version = "1.0.0")
public class UserApiImpl implements UserApi {

    @Override
    public Map<String, String> getMap(String username) {
        Map<String,String> maps = new HashMap<>(2);
        maps.put("you",username);
        maps.put("my","nihao");
        return maps;
    }

    @Override
    public List<String> getList(String username) {
        List<String> list = new ArrayList<>(2);
        list.add(username);
        list.add("nihao");
        return list;
    }
}
