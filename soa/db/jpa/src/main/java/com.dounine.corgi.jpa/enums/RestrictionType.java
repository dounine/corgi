package com.dounine.corgi.jpa.enums;

/**
 * Created by lgq on 16-10-7.
 */
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
    GTEQ,//大于等于
    LTEQ,;//小于等于


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
            case "GTEQ":
                return RestrictionType.GTEQ;
            case "LTEQ":
                return RestrictionType.LTEQ;
            case "OR":
                return RestrictionType.OR;
            case "NE":
                return RestrictionType.NE;
            default:
                return RestrictionType.EQ;
        }
    }
    public static String getRestrict(RestrictionType type) {
        switch (type) {
            case EQ:
                return "equal";
            case BETWEEN:
                return "between";
            case LIKE:
                return "like";
            case IN:
                return "in";
            case GT:
                return "greaterThan";
            case LT:
                return "lessThan";
            case GTEQ:
                return "greaterThanOrEqualTo";
            case LTEQ:
                return "lessThanOrEqualTo";
            case OR:
                return "or";
            case NE:
                return "ne";
            default:
                return "equal";
        }
    }

}
