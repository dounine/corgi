package com.dounine.corgi.mongo.dao;

import com.dounine.corgi.mongo.dto.BaseDto;
import com.dounine.corgi.mongo.dto.Condition;
import com.dounine.corgi.mongo.entity.BaseEntity;
import com.dounine.corgi.mongo.enums.DataType;
import com.dounine.corgi.mongo.enums.RestrictionType;
import com.dounine.corgi.utils.GenericsUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
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
public class RepImpl<Entity extends BaseEntity, Dto extends BaseDto> implements IRep<Entity, Dto> {

    @Autowired
    protected MongoTemplate mongoTemplate;

    protected Class<Entity> clazz;

    private static final Logger CONSOLE = LoggerFactory.getLogger(RepImpl.class);


    public RepImpl() {
        clazz = GenericsUtils.getSuperClassGenricType(this.getClass());
    }

    @Override
    public List<Entity> findAll() {
        return mongoTemplate.findAll(clazz);
    }

    @Override
    public List<Entity> findByPage(Dto dto) {
        Query query = new Query();
        query = init_sort(query, dto);
        query = init_search(query, dto);
        query.skip(dto.getSkip());
        query.limit(dto.getLimit());
        return mongoTemplate.find(query, clazz);
    }


    @Override
    public Long count(Dto dto) {
        Query query = new Query();
        query = init_search(query, dto);
        return mongoTemplate.count(query, clazz);
    }


    @Override
    public Entity findOne(Dto dto) {
        Query query = new Query();
        query = init_search(query, dto);
        return mongoTemplate.findOne(query, clazz);
    }

    @Override
    public List<Entity> findByCriteria(Criteria criteria) {
        Query query = new Query();
        query.addCriteria(criteria);
        return mongoTemplate.find(query, clazz);
    }

    @Override
    public List<Entity> findByCis(Dto dto, boolean pageAndSort) {
        Query query = new Query();
        query = init_search(query, dto);
        if (pageAndSort) {
            query = init_sort(query, dto);
            query.skip(dto.getSkip());
            query.limit(dto.getLimit());
        }
        return mongoTemplate.find(query, clazz);
    }


    @Override
    public Entity findById(String id) {
        Entity entity = mongoTemplate.findById(id, clazz);
        return mongoTemplate.findById(id, clazz);
    }

    @Override
    public void save(Entity entity) {

        mongoTemplate.insert(entity);
    }

    @Override
    public void save(List<Entity> entities) {
        mongoTemplate.insert(entities, clazz);
    }

    @Override
    public void remove(String id) {
        mongoTemplate.remove(new Query(Criteria.where("id").is(id)), clazz);
    }

    @Override
    public void remove(Entity entity) {
        mongoTemplate.remove(entity);
    }

    @Override
    public void remove(List<Entity> entities) {
        Stream<Entity> stream = entities.stream();
        stream.forEach(entity -> {
            mongoTemplate.remove(entity);
        });
    }


    @Override
    public void update(Entity entity) {
        mongoTemplate.save(entity);
    }


    @Override
    public void update(List<Entity> entities) {
        Stream<Entity> stream = entities.stream();
        stream.forEach(entity -> {
            mongoTemplate.save(entity);
        });

    }

    @Override
    public List<Entity> findByCis(Map<String, Object> conditions) {
        Query query = new Query();
        if (null != conditions && conditions.size() > 0) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
            }
        }
        return mongoTemplate.find(query, clazz);
    }


    private Query init_search(Query query, Dto dto) {
        List<Condition> conditions = dto.getConditions();
        Stream<Condition> stream = conditions.stream();
        stream.forEach(model -> {
            String[] values = model.getValues();
            RestrictionType restrict = model.getRestrict();
            DataType dataType = model.getFieldType();
            String field = model.getField();

            switch (restrict) {
                case EQ:
                    query.addCriteria(Criteria.where(field).is(switchSearchType(dataType, values[0])));
                    break;
                case LIKE:
                    if (dataType.equals(DataType.STRING)) {
                        query.addCriteria(Criteria.where(field).regex(switchSearchType(dataType, values[0]).toString()));
                    }
                    break;
                case BETWEEN:
                    if (null != values && values.length == 2) {
                        query.addCriteria(Criteria.where(field).gt(switchSearchType(dataType, values[0])).lt(switchSearchType(dataType, values[1])));
                    }
                    break;
                case GT:
                    query.addCriteria(Criteria.where(field).gt(switchSearchType(dataType, values[0])));
                    break;
                case GTEQ:
                    query.addCriteria(Criteria.where(field).gte(switchSearchType(dataType, values[0])));
                    break;
                case LTEQ:
                    query.addCriteria(Criteria.where(field).lte(switchSearchType(dataType, values[0])));
                    break;
                case LT:
                    query.addCriteria(Criteria.where(field).lt(switchSearchType(dataType, values[0])));
                    break;
                case IN:
                    Object[] objects = values;
                    query.addCriteria(Criteria.where(field).in(objects));
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
            if (dto.getOrder().equals("asc")) {
                direction = Sort.Direction.ASC;
            }
            query.with(new Sort(direction, dto.getSort()));
        }
        return query;
    }

    public Object switchSearchType(DataType type, Object value) {
        Object field_value = null;
        if (StringUtils.isNotBlank(String.valueOf(value))) {

            switch (type) {
                case STRING:
                    field_value = String.valueOf(value);
                    break;
                case LOCALDATE:
                    field_value = LocalDate.parse(String.valueOf(value), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    break;
                case LOCALTIME:
                    field_value = LocalTime.parse(String.valueOf(value), DateTimeFormatter.ofPattern("HH:mm:ss"));
                    break;
                case LOCALDATETIME:
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
                case BOOLEAN:
                    field_value = Boolean.parseBoolean(value.toString());
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
