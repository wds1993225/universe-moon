package com.meetcity.moon.spider.monitor;

/**
 * Created by wds on 2017/12/20.
 *
 * @author moon
 *
 *         <p>
 *         用于监控系统异常，例如redis连接异常等
 */
public interface MoonSystemMonitor {

    void componentConnectError(String exception);
}
