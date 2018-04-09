package com.meetcity.moon.spider.processor;

import com.meetcity.moon.spider.schedule.MoonTask;
import com.meetcity.moon.spider.schedule.MoonTaskExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by wds on 2017/11/9.
 *
 * @author moon
 */
@Slf4j
public class DefaultProcessor implements MoonProcessor {
    @Override
    public Object processor(Object o, MoonTask moonTask) {
        log.debug("The processor is success , moonParam is : {}", moonTask != null ? moonTask.toString() : "null");
        return "success : " + (moonTask != null ? moonTask.toString() : "null");
    }
}
