package com.dounine.corgi.mongo.service;

import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.mongo.dto.BaseDto;
import com.dounine.corgi.mongo.entity.BaseEntity;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

/**
 * Created by lgq on 16/9/3.
 */
public interface IService<Entity extends BaseEntity, Dto extends BaseDto> {

    /**
     * 查询所有数据
     *
     * @return
     */
    List<Entity> findAll() throws SerException;

    /**
     * 查询分页数据
     *
     * @param dto
     * @return
     */
    List<Entity> findByPage(Dto dto) throws SerException;


    /**
     * 查询数据量
     *
     * @param dto
     * @return
     */
    Long count(Dto dto) throws SerException;


    /**
     * 条件查询
     * 分页及排序
     * @param dto
     * @return
     * @throws SerException
     */
    List<Entity> findByCis(Dto dto,boolean pageAndSort)throws SerException;

    /**
     * 条件查询
     * 默认不分页及排序
     * @param dto
     * @return
     * @throws SerException
     */
    List<Entity> findByCis(Dto dto)throws SerException;


    /**
     * 查询第一个对象
     *
     * @param dto
     * @return
     */
    Entity findOne(Dto dto) throws SerException;


    /**
     * 自定义查询
     *
     * @param criteria
     * @return
     */
    List<Entity> findByCriteria(Criteria criteria) throws SerException;


    /**
     * 查询某个对象
     *
     * @param id
     * @return
     */
    Entity findById(String id) throws SerException;


    /**
     * 保存对象
     *
     * @param entity
     */
    void save(Entity entity) throws SerException;

    /**
     * 保存对象列表
     *
     * @param entities
     */
    void save(List<Entity> entities) throws SerException;

    /**
     * 通过id删除对象
     *
     * @param id
     */
    void remove(String id) throws SerException;

    /**
     * 删除对象
     *
     * @param entity
     */
    void remove(Entity entity) throws SerException;

    /**
     * 删除对象列表
     *
     * @param entities
     */
    void remove(List<Entity> entities)throws SerException;


    /**
     * 更新对象
     *
     * @param entity
     */
    void update(Entity entity) throws SerException;

    /**
     * 更新对象列表
     *
     * @param entities
     */
    void update(List<Entity> entities)throws SerException;

}
