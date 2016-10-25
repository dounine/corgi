package corgi.spring.test_java_service.code.entity;

import com.dounine.corgi.jpa.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by lgq on 16-10-14.
 * 多对多关联中间表 ，一般不会直接用many -to -many
 * 应该用双向many-to-one 来达到多对多关联的目的
 */
@Entity
@Table(name = "test_user_role")
public class UserRole extends BaseEntity {

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "rid")
    private Role role;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
