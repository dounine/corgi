package com.dounine.corgi.demo.service.user;


import com.dounine.corgi.demo.entity.user.User;
import com.dounine.corgi.exception.SerException;

/**
 * Created by huanghuanlai on 16/5/24.
 */
public interface IUserSer {

    boolean verify(String token) throws SerException;

    String login(User user) throws SerException;

    void cookieInit(User user) throws SerException;

    String hello();
}
