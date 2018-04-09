package com.meetcity.moon.spider.component.imp;

import com.alibaba.fastjson.JSON;
import com.meetcity.moon.constant.Constants;
import com.meetcity.moon.spider.schedule.MoonTask;
import com.meetcity.moon.spider.schedule.MoonQueue;
import com.meetcity.moon.spider.schedule.MoonTaskExecutor;
import com.meetcity.moon.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import redis.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by wds on 2017/12/22.
 *
 * @author moon
 *         <p>
 *         Redis 实现的任务队列
 */
@Slf4j
public class RedisQueue implements MoonQueue {

    private String queueName = Constants.redisQueueName;

    public RedisQueue(String queueName) {
        this.queueName = StringUtil.isEmpty(queueName) ? Constants.redisQueueName : queueName;
        log.debug("redis queue have been initialized , queueName is : {} ", queueName);
    }

    @Override
    public void addTask(MoonTask moonTask) {
        if (moonTask == null) {
            log.warn("task is null , nothing to do .");
            return;
        }

        String taskStr = JSON.toJSONString(moonTask);
        RedisUtil.RPush(queueName, taskStr);
        log.debug("redis queue add a task , queue name is : {} , task is : {} ", taskStr);
    }

    @Override
    public MoonTask takeTask() {
        MoonTask task = null;
        try {
            String redisTask = RedisUtil.blPop(queueName);
            task = JSON.parseObject(redisTask, MoonTask.class);
            log.debug("redis queue take a task , queue name is : {} , task is : {}", queueName, task);
        } catch (Throwable t) {
            t.printStackTrace();
            log.error("get task error , queueName is : {}  , error is : ", queueName, t);
        }
        return task;
    }

    @Override
    public long queueSize() {
        Long listSize = RedisUtil.lLen(queueName);
        if (listSize == null) {
            log.error("redis error , the value stored at key is not a list ,queue name is : {} ", queueName);
            return 0;
        }
        log.debug("redis list name is : {} , size is : {} ", queueName, listSize);
        return listSize;
    }
}
