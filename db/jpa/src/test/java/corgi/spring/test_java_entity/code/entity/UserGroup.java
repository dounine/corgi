package corgi.spring.test_java_entity.code.entity;

import com.dounine.corgi.jpa.entity.BaseEntity;
import com.dounine.corgi.jpa.enums.Status;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by lgq on 16-10-25.
 * many to one
 * 多个用户属于一个用户组
 */
@Entity
@Table(name = "test_user_group")
public class UserGroup extends BaseEntity{
    @Column(unique = true)
    private String name ;
    private LocalDateTime createTime;
    private Status status;

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
}
