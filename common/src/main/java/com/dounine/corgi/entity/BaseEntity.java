package com.dounine.corgi.entity;

import java.io.Serializable;

/**
 * Created by lgq on 16-9-2.
 */
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 6190715959705710245L;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
