package com.meetcity.moon.spider.monitor;

import com.meetcity.moon.constant.Constants;
import com.meetcity.moon.spider.schedule.MoonTask;
import com.meetcity.moon.spider.schedule.MoonTaskExecutor;
import com.meetcity.moon.util.DateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by wds on 2017/12/6.
 *
 * @author moon
 */
@Slf4j
public class DefaultMonitor implements MoonMonitor {

    private static AtomicLong successTaskCount = new AtomicLong(0);     //成功任务数
    private static AtomicLong failedTaskCount = new AtomicLong(0);      //失败任务数

    private static AtomicLong totalTaskCount = new AtomicLong(0);       //任务总数

    private String beginTime;
    private String endTime;

    private static List<MonitorTaskInternal> monitorQueue = new CopyOnWriteArrayList<>();

    @Override
    public void onStart(MoonTask moonTask) {
        totalTaskCount.getAndIncrement();
        beginTime = DateUtil.parseDate(new Date(), DateUtil.TIME_DEFAULT);
        log.debug("Monitor begin record , begin task is : {} , now is : {}", moonTask, beginTime);
    }

    @Override
    public void onSuccess(MoonTask moonTask) {
        successTaskCount.getAndIncrement();
        log.debug("a task execute success , task is : {} ", moonTask.toString());
    }

    @Override
    public void onFailed(MoonTask moonTask, String errorMessage, Object processData, Constants.MoonPeriod period) {
        MonitorTaskInternal monitorTaskInternal = new MonitorTaskInternal();
        monitorTaskInternal.setMoonTask(moonTask);
        monitorTaskInternal.setErrorMessage(errorMessage);
        monitorTaskInternal.setProcessData(processData);
        monitorTaskInternal.setMoonPeriod(period);
        monitorQueue.add(monitorTaskInternal);
        failedTaskCount.getAndIncrement();
        log.debug("a task execute failed , task is : {} , error message is : {} , process data is : {} , period is : {} ",
                String.valueOf(moonTask), String.valueOf(errorMessage), String.valueOf(processData), period.toString());
    }

    @Override
    public void end(MoonTask moonTask) {
        endTime = DateUtil.parseDate(new Date(), DateUtil.TIME_DEFAULT);
        log.debug("monitor end , now is : {} ", endTime);
    }

    @Override
    public void getTotalCount() {
        log.debug("");
    }

    @Override
    public void getTotalTime() {

    }

    @Override
    public void haveBeanDownloaded(MoonTask moonTask, String RDKey) {
        log.debug("this page have been downloaded , task is : {} , RDKey is : {} ", moonTask, RDKey);
    }

    /**
     * 内嵌的类，用于统计错误的任务
     */
    @Data
    private static class MonitorTaskInternal {

        private MoonTask moonTask;      //任务
        private String errorMessage;        //错误信息
        private Object processData;         //正在处理的数据
        private Constants.MoonPeriod moonPeriod;        //处理时期

    }
}
