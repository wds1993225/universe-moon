package com.meetcity.moon.spider.upload;

import com.meetcity.moon.spider.schedule.MoonTask;

/**
 * Created by wds on 2017/11/9.
 * <p>
 * 上传器接口
 *
 * @author moon
 */
public interface MoonUploader {

    Object upload(Object o, MoonTask moonTask);
}
