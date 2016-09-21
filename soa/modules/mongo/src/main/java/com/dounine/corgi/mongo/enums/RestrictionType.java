package com.dounine.corgi.mongo.enums;

public enum RestrictionType {
    /**
     * 相等
     */
    EQ,//相等
    /**
     * 在什么之间
     */
    BETWEEN,//在什么区间
    /**
     * 模糊
     */
    LIKE,//相似
    /**
     * 在什么范围之间
     */
    IN,//在什么范围之间
    /**
     * 大于
     */
    GT,//大于
    /**
     * 少于
     */
    LT,//少于
    /**
     * 或者
     */
    OR,//或者
    /**
     * 不等于
     */
    NE,//不等于
    ;

    public static RestrictionType valueOf(Object val) {
        String vv = String.valueOf(val);
        switch (vv) {
            case "EQ":
                return RestrictionType.EQ;
            case "BETWEEN":
                return RestrictionType.BETWEEN;
            case "LIKE":
                return RestrictionType.LIKE;
            case "IN":
                return RestrictionType.IN;
            case "GT":
                return RestrictionType.GT;
            case "LT":
                return RestrictionType.LT;
            case "OR":
                return RestrictionType.OR;
            case "NE":
                return RestrictionType.NE;
            default:
                return RestrictionType.EQ;
        }
    }
}
