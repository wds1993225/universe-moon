package com.meetcity.moon.spider.component.imp;

import com.meetcity.moon.constant.Constants;
import com.meetcity.moon.spider.schedule.MoonTask;
import com.meetcity.moon.spider.schedule.MoonQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * Created by wds on 2017/11/8.
 * <p>
 * 任务队列
 *
 * @author moon
 */

@Slf4j
public class DefaultQueue implements MoonQueue {


    public static volatile BlockingQueue<MoonTask> taskQueue = new LinkedBlockingQueue<>(Constants.queueMaxLength);
    public static volatile BlockingDeque<MoonTask> taskDuque = new LinkedBlockingDeque<>(Constants.queueMaxLength);     //双端阻塞队列


    /**
     * add | 队列满 直接抛出异常
     * offer | 插入成功返回true
     * put | 队列满 阻塞，直到插入
     */


    /**
     * 向队列中添加任务（参数）
     */
    @Override
    public void addTask(MoonTask moonTask) {
        try {
            taskQueue.add(moonTask);
            log.debug("Add task to queue , task is : {} , queue size is : {}", moonTask, size());
        } catch (Throwable t) {
            log.error("Add task error :", t);
            t.printStackTrace();
        }
    }

    /**
     * 取走排在首位的元素，
     * 队列为空 就阻塞等待，直到有数据
     */
    @Override
    public MoonTask takeTask() {
        MoonTask moonTask = null;
        try {
            moonTask = taskQueue.take();
            log.debug("get an task , task is : {} ", moonTask);
        } catch (Throwable t) {
            t.printStackTrace();
            log.error("take task error : ", t);
        }
        return moonTask;
    }

    @Override
    public long queueSize() {
        return taskQueue.size();
    }

    public static void putTask(MoonTask moonTask) {
        try {
            taskQueue.put(moonTask);
        } catch (InterruptedException e) {
            log.error("Put task error :", e);
            e.printStackTrace();
        }
    }

    public static boolean offerTask(MoonTask moonTask) {
        boolean isAdded = false;
        isAdded = taskQueue.offer(moonTask);
        if (isAdded) {
            log.debug("Offer task to queue");
        } else {
            log.debug("Can't offer task to queue,queue is full");
        }
        return isAdded;
    }


    /**
     * 取走排在首位的元素
     * 队列为空就等待 一定时间 ，取不到就返回null
     *
     * @param second 阻塞等待的时间，秒
     */
    public static MoonTask pollTask(long second) {
        try {
            return taskQueue.poll(second, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 返回队列大小
     */
    public static int size() {
        return taskQueue.size();
    }


}
