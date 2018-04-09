package com.meetcity.moon.spider.component;

import java.util.List;

/**
 * Created by moon on 2018/2/19 .
 * <p>
 * Ip代理池，使用类为{@link com.meetcity.moon.util.MoonOkHttpUtil}
 * 用于默认不切换ip
 */

public interface ProxyPool {

    List<int[]> getIpList();
}
