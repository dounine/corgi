package com.dounine.corgi.sso.service;

import com.dounine.corgi.sso.dao.IRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by huanghuanlai on 16/4/28.
 */
public class ServiceImpl<T> implements IService<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceImpl.class);

    @Autowired
    private IRepository<T> repository;

    public List<T> getAllObjects() {
        return repository.getAllObjects();
    }

    public void saveObject(T object) {
        repository.saveObject(object);
    }

    public T getObject(String id) {
        return repository.getObject(id);
    }

    public void deleteObject(String id) {
        repository.deleteObject(id);
    }

    public void updateObject(T object) {
        repository.updateObject(object);
    }

    @Override
    public Long countObject() {
        return repository.countObject();
    }

    public IRepository<T> getRepository() {
        return repository;
    }

    public void setRepository(IRepository<T> repository) {
        this.repository = repository;
    }
}
