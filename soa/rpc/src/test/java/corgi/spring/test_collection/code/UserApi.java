package corgi.spring.test_collection.code;

import java.util.List;
import java.util.Map;

/**
 * Created by huanghuanlai on 16/9/27.
 */

public interface UserApi {

    Map<String,String> getMap(String username);

    List<String> getList(String username);
}
