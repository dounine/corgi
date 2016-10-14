package com.dounine.corgi.mongo.service;

import com.dounine.corgi.exception.SerException;
import com.dounine.corgi.mongo.constant.FinalCommons;
import com.dounine.corgi.mongo.dao.IDao;
import com.dounine.corgi.mongo.dto.BaseDto;
import com.dounine.corgi.mongo.dto.SearchJson;
import com.dounine.corgi.mongo.entity.BaseEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by lgq on 16/9/3.
 */
public class ServiceImpl<Entity extends BaseEntity, Dto extends BaseDto> extends FinalCommons implements IService<Entity, Dto> {

    @Autowired
    protected IDao<Entity, Dto> dao;
    private static final Logger CONSOLE = LoggerFactory.getLogger(ServiceImpl.class);


    @Override
    public List<Entity> findAll() throws SerException {
        return dao.findAll();
    }

    @Override
    public List<Entity> findByPage(Dto dto) throws SerException {
        Query query = new Query();
        query = init_sort(query, dto);
        query = init_search(query, dto);
        query.skip(dto.getSkip());
        query.limit(dto.getLimit());
        if (null != dto.getCriteria()) {
            query.addCriteria(dto.getCriteria());
        }
        return dao.findByPage(query);
    }

    @Override
    public Long count(Dto dto) throws SerException {
        Query query = new Query();
        query = init_search(query, dto);
        if (null != dto.getCriteria()) {
            query.addCriteria(dto.getCriteria());
        }
        return dao.count(query);
    }


    @Override
    public Entity findOne(Map<String, Object> conditions) throws SerException {
        return dao.findOne(conditions);
    }

    @Override
    public List<Entity> findByCriteria(Criteria criteria) throws SerException {
        return dao.findByCriteria(criteria);
    }


    @Override
    public Entity findById(String id) throws SerException {
        return dao.findById(id);
    }

    @Override
    public List<Entity> findByIn(String field, List<String> values) throws SerException {
        return dao.findByIn(field, values);
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
    public void remove(List<Entity> entities) {
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

    @Override
    public void UpdateByCis(Entity entity, Map<String, Object> conditions) throws SerException {
        dao.UpdateByCis(entity, conditions);
    }

    @Override
    public void removeByCis(Map<String, Object> conditions) throws SerException {
        dao.removeByCis(conditions);
    }

    @Override
    public List<Entity> findByCis(Map<String, Object> conditions) throws SerException {
        return dao.findByCis(conditions);
    }

    @Override
    public long countByCis(Map<String, Object> conditions) throws SerException {
        return dao.countByCis(conditions);
    }

    @Override
    public List<Entity> findByFuzzy(Map<String, Object> conditions) throws SerException {
        return dao.findByFuzzy(conditions);
    }


    private Query init_search(Query query, Dto dto) {

        List<SearchJson> searchJsons = dto.getSearchJsons();
        Stream<SearchJson> stream = searchJsons.stream();
        stream.forEach(model -> {
            String[] value = model.getSearchField();
            switch (model.getSearchName()) {
                case EQ:
                    query.addCriteria(Criteria.where(value[0]).is(switchSearchType(value[1], value[2])));
                    break;
                case LIKE:
                    if (DATE.equals(String.valueOf(value[1]))) {
                        if (null == value[2] || (null != value[2] && StringUtils.isBlank(value[2].toString()))) break;
                    }
                    query.addCriteria(Criteria.where(value[0]).regex(switchSearchType(value[1], value[2]).toString()));
                    break;
                case BETWEEN:
                    if (DATE.equals(String.valueOf(value[1]))) {
                        if (null == value[2] || (null != value[2] && StringUtils.isBlank(value[2].toString()))) break;
                    }
                    query.addCriteria(Criteria.where(value[0]).gt(switchSearchType(value[1], value[2])).lt(switchSearchType(value[1], value[3])));
                    break;
                case GT:
                    if (DATE.equals(String.valueOf(value[1])) || INT.equals(String.valueOf(value[1])) || "float".equals(String.valueOf(value[1]))) {
                        if (null == value[2] || (null != value[2] && StringUtils.isBlank(value[2].toString()))) break;
                    }
                    query.addCriteria(Criteria.where(value[0]).gt(switchSearchType(value[1], value[2])));
                    break;
                case LT:
                    if (DATE.equals(String.valueOf(value[1])) || INT.equals(String.valueOf(value[1])) || "float".equals(String.valueOf(value[1]))) {
                        if (null == value[2] || (null != value[2] && StringUtils.isBlank(value[2].toString()))) break;
                    }
                    query.addCriteria(Criteria.where(value[0]).lt(switchSearchType(value[1], value[2])));
                    break;
                case IN:
                    if (DATE.equals(String.valueOf(value[1])) || INT.equals(String.valueOf(value[1])) || "float".equals(String.valueOf(value[1]))) {
                        if (null == value[2] || (null != value[2] && StringUtils.isBlank(value[2].toString()))) break;
                    }
                    Object[] objects = value;
                    query.addCriteria(Criteria.where(value[0]).in(objects));
                    break;
                default:
                    break;
            }

        });
        return query;
    }

    private Query init_sort(Query query, Dto dto) {
        if (null != dto.getSort() && dto.getSort().size() > 0) {
            Sort.Direction direction = Sort.Direction.DESC;
            if (dto.getOrder().equals(ASC)) {
                direction = Sort.Direction.ASC;
            }
            query.with(new Sort(direction, dto.getSort()));
        }
        return query;
    }

    public Object switchSearchType(Object type, Object value) {
        Object field_value = null;
        if (StringUtils.isNotBlank(String.valueOf(value))) {
            switch (String.valueOf(type)) {
                case STRING:
                    field_value = String.valueOf(value);
                    break;
                case DATE:
                    field_value = LocalDate.parse(String.valueOf(value), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    break;
                case TIME:
                    field_value = LocalTime.parse(String.valueOf(value), DateTimeFormatter.ofPattern("HH:mm:ss"));
                    break;
                case DATETIME:
                    field_value = LocalDateTime.parse(String.valueOf(value), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    break;
                case INT:
                    field_value = NumberUtils.createInteger(value.toString());
                    break;
                case LONG:
                    field_value = NumberUtils.createLong(value.toString());
                    break;
                case FLOAT:
                    field_value = NumberUtils.createFloat(value.toString());
                    break;
                case DOUBLE:
                    field_value = NumberUtils.createDouble(value.toString());
                    break;
                default:
                    field_value = value;
                    CONSOLE.info("value type not defined：" + value);
                    break;
            }
        }
        return field_value;
    }

}
