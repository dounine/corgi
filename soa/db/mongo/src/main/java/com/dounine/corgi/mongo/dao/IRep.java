package com.dounine.corgi.mongo.dao;

import com.dounine.corgi.mongo.dto.BaseDto;
import com.dounine.corgi.mongo.entity.BaseEntity;
import org.springframework.data.mongodb.core.query.Criteria;
import java.util.List;
import java.util.Map;

/**
 * Created by lgq on 16/9/3.
 */
public interface IRep<Entity extends BaseEntity, Dto extends BaseDto> {


    /**
     * 查询所有数据
     *
     * @return
     */
    List<Entity> findAll();


    /**
     * 查询分页数据
     *
     * @param dto
     * @return
     */
    List<Entity> findByPage(Dto dto);


    /**
     * 查询数据量
     *
     * @return
     */
    Long count(Dto dto);

    /**
     * 查询第一个对象
     *
     * @param dto
     * @return
     */
    Entity findOne(Dto dto);


    /**
     * 自定义查询
     *
     * @param criteria
     * @return
     */
    List<Entity> findByCriteria(Criteria criteria);

    /**
     * 自定义查询
     *
     * @param dto
     * @return
     */
    List<Entity> findByCis(Dto dto,boolean pageAndSort);


    /**
     * 查询某个对象
     *
     * @param id
     * @return
     */
    Entity findById(String id);


    /**
     * 保存对象
     *
     * @param entity
     */
    void save(Entity entity);

    /**
     * 保存对象列表
     *
     * @param entities
     */
    void save(List<Entity> entities);

    /**
     * 通过id删除对象
     *
     * @param id
     */
    void remove(String id);

    /**
     * 删除对象
     *
     * @param entity
     */
    void remove(Entity entity);

    /**
     * 删除对象列表
     *
     * @param entities
     */
    void remove(List<Entity> entities);

    /**
     * 更新对象
     *
     * @param entity
     */
    void update(Entity entity);

    /**
     * 更新对象列表
     *
     * @param entities
     */
    void update(List<Entity> entities);

    /**
     * 根据字段条件查询对象列表
     *
     * @param conditions
     */
    public List<Entity> findByCis(Map<String, Object> conditions);


}
