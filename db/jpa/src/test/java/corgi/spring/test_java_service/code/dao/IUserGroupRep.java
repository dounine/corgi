package corgi.spring.test_java_service.code.dao;


import com.dounine.corgi.jpa.dao.MyRep;
import corgi.spring.test_java_service.code.dto.UserDto;
import corgi.spring.test_java_service.code.dto.UserGroupDto;
import corgi.spring.test_java_service.code.entity.User;
import corgi.spring.test_java_service.code.entity.UserGroup;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * Created by huanghuanlai on 16/5/24.
 */
public interface IUserGroupRep extends MyRep<UserGroup,UserGroupDto> {

    /**

     * @param name
     * @return
     */

    UserGroup findByName(String name);
}
