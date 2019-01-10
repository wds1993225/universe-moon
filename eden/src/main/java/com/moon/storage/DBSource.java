package com.moon.storage;

/**
 * 数据库源
 * <p>
 * 应该支持多种数据库类型
 */
public interface DBSource {

    /**
     * source名称
     */
    public String getSourceName();

    /**
     * source类型
     */
    public DBType getSourceType();


}
