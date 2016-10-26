package corgi.spring.test_java_entity.code.entity;

import com.dounine.corgi.jpa.entity.BaseEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
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

    @Column(unique = true, length = 12)
    private String username;
    private String password;
    @OrderBy(value = "age desc ")
    private Integer age;
    @Column(name = "money", nullable = true, precision = 12, scale = 2)//12位数字可保留两位小数，可以为空
    private Double money;
    private Float height;
    private String nickname;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime accessTime = LocalDateTime.now();

    //optional 属性 是定义该关联类是否必须存在,值为false 时，关联类双方都必须存在(inner join) true是为left join

    //CascadeType.PERSIST（级联新建）、CascadeType.REMOVE（级联删除）、CascadeType.REFRESH（级联刷新）、CascadeType.MERGE（级联更新）中选择一个或多个。还有一个选择是使用CascadeType.ALL，表示选择全部四项。    @ManyToOne(cascade = {CascadeType.ALL})

    /** one-to-one
     * 除了 many to one 其他关联关系都有mappedBy属性，使用mappedBy的一端为主控方，
     * 主控方来维持对象关系
     * mappedBy = "user" User 为主控方，维持userInfo（被控方关系）
     * private UserInfo userInfo;
     */
    @OneToOne(optional = true, cascade = CascadeType.ALL, mappedBy = "user")
    private UserInfo userInfo;


    /**
     * many-to-one
     */
    @ManyToOne(cascade = {CascadeType.ALL})
    //多对一配置 group_id作为外键关联映射表的主键列关联
    //ManyToOne 指定 many 一方是不能独立存在的，否则存在孤儿数据
    @JoinColumn(name = "group_id", nullable = true)
    private UserGroup group;

    //OneToMany(cascade = CascadeType.ALL, mappedBy = "oneId")//指向多的那方的pojo的关联外键字段
    //private Set<Many> manys;
    //一对多配置  FetchType 是否懒加载, (OneToMany默认懒加载LAZY,ManyToOne为默认加载EAGER)一般由多的一方维护关系
    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @OrderBy(value = "seq ASC")
    private Set<UserInterest> interests;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<UserRole> userRoles;

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

    public Set<UserInterest> getInterests() {
        return interests;
    }

    public void setInterests(Set<UserInterest> interests) {
        this.interests = interests;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
