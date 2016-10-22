package com.dounine.corgi.demo.entity.user;

import com.dounine.corgi.entity.BaseEntity;
import com.dounine.corgi.demo.validation.jsr303.PasswordValid;
import com.dounine.corgi.validation.Add;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * Created by huanghuanlai on 16/4/28.
 */
public class User extends BaseEntity {

    public static final int UNAME_MIN_SIZE = 6;
    public static final int UNAME_MAX_SIZE = 20;


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
