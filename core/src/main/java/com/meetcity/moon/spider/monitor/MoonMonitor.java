package com.meetcity.moon.spider.monitor;

import com.meetcity.moon.constant.Constants;
import com.meetcity.moon.spider.schedule.MoonTask;
import com.meetcity.moon.spider.schedule.MoonTaskExecutor;

import java.io.Serializable;

/**
 * Created by wds on 2017/12/6.
 *
 * @author moon
 */
public interface MoonMonitor extends Serializable{

    /**
     * 任务开始
     */
    public void onStart(MoonTask moonTask);

    /**
     * 任务成功
     */
    public void onSuccess(MoonTask moonTask);

    /**
     * 任务失败
     *
     * @param moonTask     任务
     * @param errorMessage 错误信息
     * @param processData  处理内容
     * @param period       处理时期
     */
    public void onFailed(MoonTask moonTask, String errorMessage, Object processData, Constants.MoonPeriod period);

    /**
     * 任务结束
     */
    public void end(MoonTask moonTask);

    /**
     * 获取总数
     */
    public void getTotalCount();

    /**
     * 获取总时间
     */
    public void getTotalTime();

    /**
     * 曾经下载过
     *
     * @param moonTask 下载的任务
     * @param RDKey    redis里去重的Hash（MD5之前的值）
     */
    public void haveBeanDownloaded(MoonTask moonTask, String RDKey);


}
