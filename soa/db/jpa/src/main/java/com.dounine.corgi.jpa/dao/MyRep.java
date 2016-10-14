package com.dounine.corgi.jpa.dao;

import com.dounine.corgi.jpa.dto.BaseDto;
import com.dounine.corgi.jpa.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

/**
 * Created by huanghuanlai on 16/9/3.
 */
@NoRepositoryBean
public interface MyRep<BE extends BaseEntity,BD extends BaseDto> extends JpaRepository<BE,String>
        ,JpaSpecificationExecutor<BE> {

    default BE findById(String id){
        return findOne(id);
    }

}
