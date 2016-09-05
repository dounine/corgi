package com.dounine.corgi.sso.service;

import com.dounine.corgi.exception.RepException;
import com.dounine.corgi.exception.SerException;
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
    protected IRepository<T> repository;

    public List<T> getAllObjects() throws SerException {
        try {
            return repository.getAllObjects();
        } catch (RepException e) {
            throw new SerException(e.getMessage());
        }
    }

    public void saveObject(T object) throws SerException {
        try {
            repository.saveObject(object);
        } catch (RepException e) {
            throw new SerException(e.getMessage());
        }
    }

    public T getObject(String id) throws SerException {
        try {
            return repository.getObject(id);
        } catch (RepException e) {
            throw new SerException(e.getMessage());
        }
    }

    public void deleteObject(String id) throws SerException {
        try {
            repository.deleteObject(id);
        } catch (RepException e) {
            throw new SerException(e.getMessage());
        }
    }

    public void updateObject(T object) throws SerException {
        try {
            repository.updateObject(object);
        } catch (RepException e) {
            throw new SerException(e.getMessage());
        }
    }

    @Override
    public Long countObject() throws SerException {
        try {
            return repository.countObject();
        } catch (RepException e) {
            throw new SerException(e.getMessage());
        }
    }

    public IRepository<T> getRepository() throws SerException {
        return repository;
    }

    public void setRepository(IRepository<T> repository) throws SerException {
        this.repository = repository;
    }
}
