package corgi.spring.test_java_entity.code.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.dounine.corgi.jpa.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by lgq on 16-10-25.
 */
@Entity
@Table(name = "test_userInfo")
public class UserInfo extends BaseEntity{

    private String email;
    private String fox;
    private String company; //所在单位
    private String address; //通讯地址
    private String postcodes; //邮编
    private String contact; // 联系人
    private String telephone;//联系电话


    @OneToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name="user_id")
    @JSONField(serialize = false)
    private User user;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFox() {
        return fox;
    }

    public void setFox(String fox) {
        this.fox = fox;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcodes() {
        return postcodes;
    }

    public void setPostcodes(String postcodes) {
        this.postcodes = postcodes;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
