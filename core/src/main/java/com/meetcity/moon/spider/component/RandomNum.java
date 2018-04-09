package com.meetcity.moon.spider.component;

/**
 * Created by wds on 2017/11/14.
 *
 * 随机数策略接口
 *
 * @see com.meetcity.moon.spider.schedule.ScheduleCore.TaskConsumer 调用此类，作为线程休眠的时间
 *
 *
 *
 * @author moon
 */
public interface RandomNum {

    long getNum();
}
