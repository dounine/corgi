package corgi.spring.test_java_entity.code.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.dounine.corgi.jpa.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by lgq on 16-10-6.
 * 测试一对多关系
 */
@Entity
@Table(name="test_user_interest")
public class UserInterest extends BaseEntity {
    private String name;
    private Integer seq;
    private LocalDateTime createTime;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id")
    @JSONField(serialize = false)
    private User user;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
