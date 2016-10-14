package com.dounine.corgi.jpa.entity;

import com.dounine.corgi.validation.Del;
import com.dounine.corgi.validation.Edit;
import com.dounine.corgi.validation.Get;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by huanghuanlai on 16/9/3.
 */

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEntity implements Serializable{
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false,length = 36)
    @NotBlank(message = "user id not blank",groups = {Del.class, Edit.class, Get.class})
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
