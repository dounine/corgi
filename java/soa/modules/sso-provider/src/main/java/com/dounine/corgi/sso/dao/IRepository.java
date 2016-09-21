package com.dounine.corgi.sso.dao;

import com.dounine.corgi.exception.RepException;
import com.dounine.corgi.exception.SerException;

import java.util.List;

/**
 * Created by huanghuanlai on 16/4/28.
 */
public interface IRepository<T> {

    List<T> getAllObjects() throws RepException;

    void saveObject(T object) throws RepException;

    T getObject(String id) throws RepException;

    void deleteObject(String id) throws RepException;

    void updateObject(T object) throws RepException;

    Long countObject() throws RepException;

    T findById(String id) throws RepException;
}
