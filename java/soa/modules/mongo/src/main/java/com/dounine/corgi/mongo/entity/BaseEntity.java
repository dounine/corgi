package com.dounine.corgi.mongo.entity;

import com.dounine.corgi.validation.Del;
import com.dounine.corgi.validation.Edit;
import com.dounine.corgi.validation.Get;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * Created by lgq on 16-9-2.
 */
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 6190715959705710245L;

    @Id
    //validation
    @NotBlank(message = "id not blank", groups = {Del.class, Edit.class, Get.class})
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
