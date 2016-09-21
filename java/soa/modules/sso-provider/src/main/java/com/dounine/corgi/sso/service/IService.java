package com.dounine.corgi.sso.service;

import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.sso.entity.user.User;

import java.util.List;

/**
 * Created by huanghuanlai on 16/4/28.
 */
public interface IService<T> {

    List<T> getAllObjects() throws SerException;

    void saveObject(T object) throws SerException;

    T getObject(String id) throws SerException;

    void deleteObject(String id) throws SerException;

    void updateObject(T object) throws SerException;

    Long countObject() throws SerException;

    T findById(String id) throws SerException;
}
