package com.meetcity.moon.spider;


import com.meetcity.moon.spider.schedule.MoonProducer;
import com.meetcity.moon.spider.schedule.MoonTask;
import com.meetcity.moon.spider.schedule.ScheduleCore;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by wds on 2018/2/14 .
 * <p>
 * <p>
 * 爬虫的启动
 * <p>
 * eg:
 * <p>
 * A. 单机爬虫的启动
 * 需要
 * 1，启动爬虫消费线程；
 * 2，向队列中添加任务
 * <p>
 * MoonTask task = new MoonTask("http://www.baidu.com");
 * SpiderStart spiderStart  = new SpiderStart();
 * spiderStart.start();
 * spiderStart.addTask(task);
 * <p>
 * B. 分布式爬虫的启动
 * 需要
 * 启动爬虫消费线程
 * <p>
 * SpiderStart spiderStart = new SpiderStart();
 * spiderStart.start();
 */


@Slf4j
public class SpiderStart {


    private ScheduleCore scheduleCore;      //任务调度器
    private MoonSpiderConfig config;        //配置
    private MoonProducer producer;          //生产器


    public SpiderStart() {
        init();
    }

    /**
     * 初始化程序
     */
    private void init() {
        InitMoonConfig initMoonConfig = new InitMoonConfig();
        initMoonConfig.init();
        config = initMoonConfig.getConfig();
        producer = MoonProducer.getProducer();
    }

    /**
     * 启动消费线程
     */
    public void start() {
        scheduleCore = new ScheduleCore(config);
        scheduleCore.start();
        log.debug("SpiderCore start , spider begin .");
    }

    /**
     * 添加任务
     */
    public void addTask(MoonTask task) {
        if (task == null) {
            return;
        }
        producer.produce(task);

    }

}
