package com.dounine.corgi.sso.dao;

import java.util.List;

/**
 * Created by huanghuanlai on 16/4/28.
 */
public interface IRepository<T> {

    List<T> getAllObjects();

    void saveObject(T object);

    T getObject(String id);

    void deleteObject(String id);

    void updateObject(T object);

    Long countObject();
}
