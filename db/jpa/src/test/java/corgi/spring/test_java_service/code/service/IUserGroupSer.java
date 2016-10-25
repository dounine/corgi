package corgi.spring.test_java_service.code.service;

import com.dounine.corgi.jpa.service.IService;
import corgi.spring.test_java_service.code.dto.UserGroupDto;
import corgi.spring.test_java_service.code.entity.UserGroup;

import java.util.List;

/**
 * Created by lgq on 16-10-25.
 */
public interface IUserGroupSer extends IService<UserGroup, UserGroupDto> {
    UserGroup findByName(String name);
}
