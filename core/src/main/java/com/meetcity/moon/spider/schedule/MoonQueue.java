package com.meetcity.moon.spider.schedule;


/**
 * @author moon
 *         <p>
 *         <p>
 *         需要实现的队列接口
 */
public interface MoonQueue {

    void addTask(MoonTask moonTask);

    MoonTask takeTask();

    long queueSize();
}
