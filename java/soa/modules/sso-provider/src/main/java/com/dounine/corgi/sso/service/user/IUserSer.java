package com.dounine.corgi.sso.service.user;


import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.sso.entity.user.User;
import com.dounine.corgi.sso.service.IService;

/**
 * Created by huanghuanlai on 16/5/24.
 */
public interface IUserSer extends IService<User> {

    boolean verify(String token) throws SerException;

    String login(User user) throws SerException;

    void cookieInit(User user) throws SerException;
}
