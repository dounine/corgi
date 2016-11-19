package jta.provider.dao;

import jta.provider.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.time.LocalDateTime;

/**
 * Created by huanghuanlai on 2016/11/16.
 */
@Repository
public class UserDao implements IUserDao {

    //主库
    @Resource(name = "jdbcTemplateMaster")
    protected JdbcTemplate jdbcTemplateMaster;

    //从库
    @Resource(name = "jdbcTemplateSlave")
    protected JdbcTemplate jdbcTemplateSlave;

    @Transactional(rollbackFor = Exception.class)
    public void save(User user) throws Exception {
        jdbcTemplateMaster.execute("INSERT INTO USER (username) VALUES ('" + user.getUsername() + "')");
        if (true) {
            throw new Exception("DaoException");
        }
        jdbcTemplateSlave.execute("INSERT INTO USER (username) VALUES ('" + user.getUsername() + "')");
    }

    @Override
    public void saveLoginLog(User user) throws Exception {
        jdbcTemplateSlave.execute("INSERT INTO USER_LOGIN_LOG (info) VALUES ('user ==> " + user.getUsername() + " login success.')");
    }

    @Override
    public void saveLoginInfo(User user) throws Exception {
        jdbcTemplateMaster.execute("INSERT INTO USER_LOGIN_INFO (info) VALUES ('user ==> " + user.getUsername() + " " + LocalDateTime.now() + " login, ip:" + InetAddress.getLocalHost() + "')");
    }

}
