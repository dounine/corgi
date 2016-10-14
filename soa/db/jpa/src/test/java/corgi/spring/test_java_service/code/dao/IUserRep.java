package corgi.spring.test_java_service.code.dao;


import com.dounine.corgi.jpa.dao.MyRep;
import corgi.spring.test_java_service.code.dto.UserDto;
import corgi.spring.test_java_service.code.entity.User;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * Created by huanghuanlai on 16/5/24.
 */
public interface IUserRep extends MyRep<User,UserDto> {
    /**
     * 此处使用的是spring-data-jpa接口,不需要对接口进行实现,jpa可根据命名自动进行数据的查询
     * jpa接口规范：http://docs.spring.io/spring-data/jpa/docs/1.11.0.M1/reference/html/
     * @param username 用户名
     * @return 用户信息
     */
    User findByUsername(String username);

    @QueryHints(value={@QueryHint(name="org.hibernate.cacheable",value="true")})
    List<User> findByNickname(String nickname);
}
