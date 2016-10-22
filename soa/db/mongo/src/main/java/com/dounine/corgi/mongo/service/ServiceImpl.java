package com.dounine.corgi.mongo.service;

import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.mongo.constant.FinalCommons;
import com.dounine.corgi.mongo.dao.IRep;
import com.dounine.corgi.mongo.dto.BaseDto;
import com.dounine.corgi.mongo.entity.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import java.util.List;

/**
 * Created by lgq on 16/9/3.
 */
public class ServiceImpl<Entity extends BaseEntity, Dto extends BaseDto> extends FinalCommons implements IService<Entity, Dto> {

    @Autowired
    protected IRep<Entity, Dto> dao;

    @Override
    public List<Entity> findAll() throws SerException {
        return dao.findAll();
    }

    @Override
    public List<Entity> findByPage(Dto dto) throws SerException {
        return dao.findByPage(dto);
    }

    @Override
    public Long count(Dto dto) throws SerException {
        return dao.count(dto);
    }


    @Override
    public Entity findOne(Dto dto) throws SerException {
        return dao.findOne(dto);
    }

    @Override
    public List<Entity> findByCriteria(Criteria criteria) throws SerException {
        return dao.findByCriteria(criteria);
    }

    @Override
    public List<Entity> findByCis(Dto dto,boolean pageAndSort) throws SerException {
        return dao.findByCis(dto,pageAndSort);
    }

    @Override
    public List<Entity> findByCis(Dto dto) throws SerException {
        return dao.findByCis(dto,false);
    }

    @Override
    public Entity findById(String id) throws SerException {
        return dao.findById(id);
    }

    @Override
    public void save(Entity entity) throws SerException {
        dao.save(entity);
    }

    @Override
    public void save(List<Entity> entities) throws SerException {
        dao.save(entities);
    }

    @Override
    public void remove(String id) throws SerException {
        dao.remove(id);
    }

    @Override
    public void remove(Entity entity) throws SerException {
        dao.remove(entity);
    }

    @Override
    public void remove(List<Entity> entities) throws SerException {
        dao.remove(entities);
    }

    @Override
    public void update(Entity entity) throws SerException {

        if (null != findById(entity.getId())) {
            dao.update(entity);
        } else {
            throw new SerException(entity.getClass().getSimpleName() + "  is not persisted Object !");
        }

    }

    @Override
    public void update(List<Entity> entities) throws SerException {
        for (Entity entity : entities) {
            if (null == findById(entity.getId())) {
                throw new SerException("entities's "+entity.getClass().getSimpleName() + " has Object is not persisted !");
            }
        }
        dao.update(entities);
    }


}
