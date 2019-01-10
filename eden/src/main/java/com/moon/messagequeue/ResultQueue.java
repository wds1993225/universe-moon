package com.moon.messagequeue;


public interface ResultQueue {

    /**
     * 添加任务
     */
    void addTask();

    /**
     * 获取任务
     */
    Object takeTask();

    /**
     * 任务队列大小
     */
    long queueSize();

}
