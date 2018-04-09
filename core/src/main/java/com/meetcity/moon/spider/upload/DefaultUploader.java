package com.meetcity.moon.spider.upload;

import com.meetcity.moon.spider.schedule.MoonTask;
import com.meetcity.moon.spider.schedule.MoonTaskExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by wds on 2017/11/9.
 *
 * @author moon
 */
@Slf4j
public class DefaultUploader implements MoonUploader {
    @Override
    public Object upload(Object o, MoonTask moonTask) {
        log.debug("The upload is success , task is : {}", moonTask != null ? moonTask.toString() : "null");
        return "success : " + (moonTask != null ? moonTask.toString() : "null");
    }
}
