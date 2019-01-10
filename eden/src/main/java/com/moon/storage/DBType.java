package com.moon.storage;


/**
 * 数据库类型 枚举
 * <p>
 * <p>
 * 目前支持:
 * <p>
 * MongoDB
 * ElasticSearch
 * HDFS
 */
public enum DBType {


    MongoDB("MongoDB"), ElasticSearch("ElasticSearch"), HDFS("HDFS");

    private final String text;

    DBType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
