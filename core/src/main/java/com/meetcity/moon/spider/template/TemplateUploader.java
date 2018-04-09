package com.meetcity.moon.spider.template;

import com.meetcity.moon.spider.schedule.MoonTask;
import com.meetcity.moon.spider.upload.MoonUploader;

/**
 * Created by wds on 2017/11/29.
 *
 * @author moon
 */
public class TemplateUploader implements MoonUploader {


    @Override
    public Object upload(Object o, MoonTask moonTask) {
        System.out.println("");
        return "haha";
    }
}
