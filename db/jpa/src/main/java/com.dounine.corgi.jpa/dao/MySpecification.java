package com.dounine.corgi.jpa.dao;

import com.dounine.corgi.jpa.dto.BaseDto;
import com.dounine.corgi.jpa.dto.Condition;
import com.dounine.corgi.jpa.entity.BaseEntity;
import com.dounine.corgi.jpa.enums.RepExceptionType;
import com.dounine.corgi.jpa.enums.RestrictionType;
import com.dounine.corgi.jpa.exception.RepException;
import com.dounine.corgi.jpa.utils.PrimitiveUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.lang.reflect.Method;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by lgq on 16-9-30.
 */
public class MySpecification<BE extends BaseEntity, BD extends BaseDto> implements Specification<BE> {

    private static final Pattern PATTERN = Pattern.compile("\\[[a-zA-Z0-9]+\\]");

    private BD dto;

    public MySpecification(BD dto) {
        this.dto = dto;
    }


    @Override
    public Predicate toPredicate(Root<BE> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> preList = null;
        try {
            preList = initPredicates(dto, root, cb,query);
        } catch (RepException e) {
            throw e;
        }

        Predicate[] predicates = preList.toArray(new Predicate[preList.size()]);
        return query.where(predicates).getRestriction();

    }


    private List<Predicate> initPredicates(BD dto, Root<BE> root, CriteriaBuilder cb,CriteriaQuery<?> query) throws RepException {

        List<Predicate> preList = new ArrayList<>(0); //条件列表
        List<Condition> conditions = dto.getConditions() != null ? dto.getConditions() : new ArrayList<>(0);//避免条件列表为空
        Stream<Condition> stream = conditions.stream();
        stream.forEach(model -> {
            Predicate predicate = null;
            Class clazz = PrimitiveUtil.switchType(model.getFieldType()); //得到数据类型
            String field = model.getField(); //字段
            Method[] methods = cb.getClass().getDeclaredMethods();
            Method method = null;
            try {
                for (Method m : methods) {
                    Class<?>[] types = m.getParameterTypes();
                    String name = RestrictionType.getRestrict(model.getRestrict());
                    if (m.getName().equals(name) &&
                            types[types.length - 1] != Expression.class) {
                        method = m;
                        break;
                    }
                }
                RestrictionType type = model.getRestrict();
                switch (type) {
                    case LIKE:
                        predicate = cb.like(root.get(field).as(clazz), "%" + model.getValues()[0] + "%");
                        break;
                    case ISNULL:
                        predicate = cb.isNull(root.get(field).as(clazz));
                        break;
                    case ISNOTNULL:
                        predicate = cb.isNotNull(root.get(field).as(clazz));
                        break;
                    default:
                        Object[] values = PrimitiveUtil.convertValuesByType(model.getValues(), model.getFieldType());
                        if (type == RestrictionType.IN) {
                            predicate = (Predicate) method.invoke(cb, root.get(field).as(clazz), values);
                        } else {
                            predicate = (Predicate) method.invoke(cb, ArrayUtils.add(values, 0, root.get(field).as(clazz)));
                        }
                }


            } catch (Exception e) {
                e.printStackTrace();
                exceptionHandler(e);

            }

            if (null != predicate) {
                preList.add(predicate);
            }
        });

        return preList;
    }

    private RepException exceptionHandler(Exception e) {
        String msg = "";
        RepExceptionType type = RepExceptionType.UNDEFINE;

        if (e instanceof IllegalArgumentException) {
            Matcher matcher = PATTERN.matcher(e.getMessage());
            boolean isFind = matcher.find();
            if (isFind) { //字段不匹配
                msg = StringUtils.substring(matcher.group(), 1, -1);
                type = RepExceptionType.NOT_FIND_FIELD;
            } else if (e.getMessage().indexOf("wrong number of arguments") != -1) { //其他错误
                msg = "wrong number of arguments";
                type = RepExceptionType.ERROR_ARGUMENTS;
            } else if (e instanceof NumberFormatException) {
                msg = e.getMessage();
                type = RepExceptionType.ERROR_NUMBER_FORMAT;
            }
        } else {
            if (e instanceof DateTimeParseException) {
                msg = e.getMessage();
                type = RepExceptionType.ERROR_PARSE_DATE;
            } else {
                msg = e.getMessage();
                type = RepExceptionType.UNDEFINE;
            }

        }
        throw new RepException(type, msg);
    }


    /**
     * 分页及排序
     *
     * @param dto
     * @return
     */
    public PageRequest getPageRequest(BD dto) {
        PageRequest pageRequest = null;
        if (dto.getSorts() != null && dto.getSorts().size() > 0) {
            Sort.Direction dct = Sort.Direction.ASC;
            if (dto.getOrder().equals("desc")) {
                dct = Sort.Direction.DESC;
            }
            pageRequest = new PageRequest(dto.getPage(), dto.getLimit(), new Sort(dct, dto.getSorts())); //分页带排序
        } else {
            pageRequest = new PageRequest(dto.getPage(), dto.getLimit()); //分页不带排序
        }
        return pageRequest;
    }


}
