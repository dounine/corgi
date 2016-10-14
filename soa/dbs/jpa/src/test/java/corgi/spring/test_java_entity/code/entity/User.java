package corgi.spring.test_java_entity.code.entity;

import com.dounine.corgi.jpa.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by huanghuanlai on 16/4/28.
 */
@Entity
@Table(name="test_user")
public class User extends BaseEntity {

    @Column(unique = true,length = 12)
    private String username;
    private String password;
    @OrderBy(value = "age desc ")
    private Integer age;
    private Double money;
    private Float height;
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private LocalDateTime accessTime = LocalDateTime.now();

    /**
     * 只有在双向关联时才会使用mappedBy属性，
     * 只有OneToOne、OneToMany、ManyToMany上才有mappedBy属性，ManyToOne不存在该属性。
     * 使用了mappedBy的一端称为关系目标方（被控方），
     * 另一端为关系拥有方（控制方）。
     * 相应的对象称之为被控对象和主控对象。
     */


    /**一般用many to one 即可
     * User类（主控方）
     *  private UserDetails details;
     * @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
     * @JoinColumn(name = "details_id")
     * @Fetch(FetchMode.JOIN)
     *
     * UserDetails类(被控方)
     * private User user ;
     * @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
     */



    //CascadeType 级联所有,根据需要选择级联更新,级联保存,级联删除....
    @ManyToOne(cascade = {CascadeType.ALL})
    //多对一配置 details_id 为 外键关联id
    @JoinColumn(name = "details_id",nullable=false)
    private UserDetails details;

    //一对多配置  FetchType 是否懒加载, (OneToMany默认懒加载LAZY,ManyToOne为默认加载EAGER)
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE},fetch = FetchType.EAGER)
    @OrderBy(value = "seq ASC")
    private Set<UserInterest> interests;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public LocalDateTime getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(LocalDateTime accessTime) {
        this.accessTime = accessTime;
    }

    public UserDetails getDetails() {
        return details;
    }

    public void setDetails(UserDetails details) {
        this.details = details;
    }

    public Set<UserInterest> getInterests() {
        return interests;
    }

    public void setInterests(Set<UserInterest> interests) {
        this.interests = interests;
    }
}
