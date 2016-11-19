package jta.provider.service;

import jta.provider.entity.User;

/**
 * Created by huanghuanlai on 2016/11/16.
 */
public interface IUserService {

    void register(User user) throws Exception;

    void login(User user) throws Exception;

}
