package com.dounine.corgi.sso.dao;

import com.dounine.corgi.exception.RepException;
import com.dounine.corgi.utils.GenericsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by huanghuanlai on 16/4/28.
 */
public class RepositoryImpl<T> implements IRepository<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    private Class<T> clazz;

    public RepositoryImpl() {
        clazz = GenericsUtils.getSuperClassGenricType(this.getClass());
    }

    @Override
    public List<T> getAllObjects() throws RepException {
        return mongoTemplate.findAll(clazz);
    }

    @Override
    public void saveObject(T object) throws RepException {
        mongoTemplate.save(object);
    }

    @Override
    public T getObject(String id) throws RepException {
        return mongoTemplate.findById(id, clazz);
    }

    @Override
    public void deleteObject(String id) throws RepException {
        mongoTemplate.remove(new Query(Criteria.where("id").is(id)));
    }

    @Override
    public void updateObject(T object) throws RepException {

    }

    @Override
    public Long countObject() throws RepException {
        return 0L;
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }
}
