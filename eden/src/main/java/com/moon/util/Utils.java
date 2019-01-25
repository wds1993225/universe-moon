package com.moon.util;


import java.util.UUID;

/**
 * 一些工具的合集
 */
public class Utils {


    /**
     * 返回一个uuid
     * <p>
     * 并且uuid为小写
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().toLowerCase();
    }
}
