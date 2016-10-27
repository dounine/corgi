package corgi.spring.test_java_service.code.entity;

import com.dounine.corgi.jpa.entity.BaseEntity;
import com.dounine.corgi.jpa.enums.Status;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by lgq on 16-10-14.
 */
@Entity
@Table(name="test_role")
public class Role extends BaseEntity {
    private String name; //角色名
    private LocalDateTime createTime; //创建时间
    private Status status;

    @OneToMany(mappedBy = "role", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Set<UserRole> userRoles;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
}
