package mongo.test_java_service.code.entity;

import com.dounine.corgi.mongo.entity.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

/**
 * Created by lgq on 16-9-4.
 * 用户详情信息
 * 测试多对一关系
 */
@Document
public class UserDetails extends BaseEntity {
    private String company; //所在单位
    private String address; //通讯地址
    private String postcodes; //邮编
    private String contact; // 联系人
    private String telephone;//联系电话
    private String fax; // 传真
    private String email;//邮箱
    private LocalDateTime createTime;//填表日期???

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

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
