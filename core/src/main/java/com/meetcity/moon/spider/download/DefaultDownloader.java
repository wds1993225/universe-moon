package com.meetcity.moon.spider.download;

import com.meetcity.moon.spider.schedule.MoonTask;
import com.meetcity.moon.spider.schedule.MoonTaskExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by wds on 2017/11/9.
 *
 * @author moon
 */
@Slf4j
public class DefaultDownloader implements MoonDownloader {


    @Override
    public Object download(MoonTask moonTask) {
        log.debug("The download is success , moonParam is : {}", moonTask != null ? moonTask.toString() : "null");
        return "success : " + (moonTask != null ? moonTask.toString() : "null");
    }
}
