package com.meetcity.moon.spider.download;

import com.meetcity.moon.spider.schedule.MoonTask;

/**
 * Created by wds on 2017/11/9.
 * <p>
 * 下载器接口
 *
 * @author moon
 */
public interface MoonDownloader {

    Object download(MoonTask moonTask);

}
