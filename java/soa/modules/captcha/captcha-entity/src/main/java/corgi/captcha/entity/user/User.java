package corgi.captcha.entity.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Created by huanghuanlai on 16/4/28.
 */
@Document
public class User {

    @Id
    private String id;
    private String username;
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

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean p = false;
        if(null==obj){
            p = false;
        }else if(obj==this){
            p = true;
        }
        if(p){
            User user = (User) obj;
            if(user.getUsername().equals(username)){
                p = true;
            }
        }
        return p;
    }
}
