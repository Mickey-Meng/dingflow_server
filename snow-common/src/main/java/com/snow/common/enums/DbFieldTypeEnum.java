package com.snow.common.enums;

public enum DbFieldTypeEnum {
    CHAR(1,"char"),
    VARCHAR(1,"varchar"),
    TINYTEXT(1,"tinytext"),
    TEXT(1,"text"),


    DATETIME(2,"datetime"),
    TIME(2,"time"),
    DATE(2,"date"),
    TIMESTAMP(2,"timestamp"),

    TINYINT(3,"tinyint"),
    SMALLINT(3,"smallint"),
    INT(3,"int"),
    INTEGER(3,"integer"),
    BIGINT(3,"bigint"),
    FLOAT(3,"float"),
    DOUBLE(3,"double"),
    DECIMAL(3,"decimal");


    DbFieldTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    //模块
    private final Integer type;
    /**
     * 操作类型 1--新增 2--更新 3--删除 4- 查询 5--发送消息
     */
    private final String name;


    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
