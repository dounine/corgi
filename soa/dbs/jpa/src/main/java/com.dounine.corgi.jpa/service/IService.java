package com.dounine.corgi.jpa.service;


import com.dounine.corgi.jpa.dto.BaseDto;
import com.dounine.corgi.jpa.entity.BaseEntity;
import com.dounine.corgi.jpa.exception.SerException;

import java.util.List;
import java.util.Map;

/**
 * Created by huanghuanlai on 16/9/3.
 */
public interface IService<BE extends BaseEntity, BD extends BaseDto> {

    /**
     * 查询所有数据
     *
     * @return
     */
    List<BE> findAll() throws SerException;

    /**
     * 查询分页数据
     *
     * @param dto
     * @return
     */
    List<BE> findByPage(BD dto) throws SerException;

    /**
     * 查询数据量
     *
     * @param dto
     * @return
     */
    Long count(BD dto) throws SerException;


    BE findOne(BD dto) throws SerException;

    /**
     * 根据条件询对象列表
     *是否分页排序
     * @param dto
     * @return
     */
    List<BE> findByCis(BD dto,Boolean pageAndSort) throws SerException;

    /**
     * 根据条件询对象列表数量
     *
     * @param dto
     * @return
     */
    Long countByCis(BD dto ) throws SerException;

    /**
     * 查询某个对象
     *
     * @param id
     * @return
     */
    BE findById(String id) throws SerException;


    /**
     * 保存对象
     *
     * @param entity
     */
    BE save(BE entity) throws SerException;

    /**
     * 保存对象列表
     *
     * @param entities
     */
    void save(List<BE> entities) throws SerException;

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
    void remove(BE entity) throws SerException;

    /**
     * 删除对象列表
     *
     * @param entities
     */
    void remove(List<BE> entities) throws SerException;


    /**
     * 更新对象
     *
     * @param entity
     */
    void update(BE entity) throws SerException;

    /**
     * 更新对象
     *
     * @param entities
     */
    void update(List<BE> entities) throws SerException;


    boolean exists(String id) throws SerException;

}
