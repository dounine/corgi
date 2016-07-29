package corgi.captcha.entity.user;

import java.time.LocalDateTime;

/**
 * Created by huanghuanlai on 16/6/18.
 */
public class LoginInfo {

    private String info;
    private String account;
    private LocalDateTime createTime;
    private boolean success = true;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
