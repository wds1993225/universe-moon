package com.moon.messagequeue;


/**
 * Task TaskQueue
 * <p>
 * 任务队列
 */
public interface TaskQueue {

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
