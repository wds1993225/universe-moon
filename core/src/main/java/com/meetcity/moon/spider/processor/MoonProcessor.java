package com.meetcity.moon.spider.processor;

import com.meetcity.moon.spider.schedule.MoonTask;
import com.meetcity.moon.spider.schedule.MoonTaskExecutor;

/**
 * Created by wds on 2017/11/9.
 * <p>
 * 解析器接口
 *
 * @author moon
 */
public interface MoonProcessor {

    Object processor(Object downloadResult, MoonTask moonTask);
}
