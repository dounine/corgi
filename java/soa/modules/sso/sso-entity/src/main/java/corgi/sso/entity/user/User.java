package corgi.sso.entity.user;

import corgi.sso.common.validation.jsr303.PasswordValid;
import corgi.validation.Add;
import corgi.validation.Del;
import corgi.validation.Edit;
import corgi.validation.Get;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Created by huanghuanlai on 16/4/28.
 */
@Document
public class User {

    public static final int UNAME_MIN_SIZE = 6;
    public static final int UNAME_MAX_SIZE = 20;

    @Id
    //validation
    @NotBlank(message = "user id not blank",groups = {Del.class, Edit.class, Get.class})
    private String id;

    //groups 用于CRUD组合使用,当字段有分组时,@Validated(?)占位符内必需有值(Add,Del,Edit,Get)
    @NotBlank(message = "username not blank",groups = {Add.class})
    @Length(message = "username size between {min} the {max}",min = UNAME_MIN_SIZE,max = UNAME_MAX_SIZE,groups = {Add.class})
    private String username;

    @PasswordValid(groups = {Add.class})
    private String password;

    private LocalDateTime accessTime;

    public LocalDateTime getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(LocalDateTime accessTime) {
        this.accessTime = accessTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

}
