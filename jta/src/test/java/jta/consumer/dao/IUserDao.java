package jta.consumer.dao;

import jta.consumer.entity.User;

/**
 * Created by huanghuanlai on 2016/11/16.
 */
public interface IUserDao {
    void save(User user) throws Exception;

    void saveLoginLog(User user) throws Exception;

    void saveLoginInfo(User user) throws Exception;
}
