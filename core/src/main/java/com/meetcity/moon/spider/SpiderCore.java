package com.meetcity.moon.spider;

import com.meetcity.moon.spider.component.imp.DefaultQueue;
import com.meetcity.moon.spider.component.imp.RedisQueue;
import com.meetcity.moon.spider.download.DefaultDownloader;
import com.meetcity.moon.spider.download.MoonDownloader;
import com.meetcity.moon.spider.processor.DefaultProcessor;
import com.meetcity.moon.spider.processor.MoonProcessor;
import com.meetcity.moon.spider.schedule.*;
import com.meetcity.moon.spider.upload.DefaultUploader;
import com.meetcity.moon.spider.upload.MoonUploader;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wds on 2017/11/9.
 * <p>
 * 封装配置文件
 * 将配置文件中的线程休眠策略，代理策略实例化，注入到代码中
 * <p>
 * 此类不再使用
 * 使用{@link SpiderStart}替换
 *
 * @author moon
 */
@Slf4j
@Deprecated
public class SpiderCore {

    private MoonSpiderConfig config;

    private ScheduleCore scheduleCore;      //任务调度器
    private MoonTask param;                //单个的请求参数
    private MoonDownloader moonDownloader;      //下载器
    private MoonProcessor moonProcessor;        //解析器
    private MoonUploader moonUploader;          //上传器

    public SpiderCore() {
    }

    public SpiderCore(MoonTask seedTask) {
        this(seedTask, null, null, null);
    }


    /**
     * 这里使用接口，后面实现是用反射获取类，但是之所以不直接传实现
     * 1，类型校验
     * 2，一组任务要添加的时候不能用同一个下载器，要新实例化
     */
    public SpiderCore(MoonTask seedTask, MoonDownloader moonDownloader, MoonProcessor moonProcessor, MoonUploader moonUploader) {
        this.param = seedTask;
        if (param == null) {
            log.error("param is null ");        //参数为空，停止运行
            return;
        }
        this.moonDownloader = moonDownloader;
        this.moonProcessor = moonProcessor;
        this.moonUploader = moonUploader;
    }


    /**
     * 爬虫启动
     * <p>
     * 实例化config
     * 实例化Producer
     * 启动调度器
     */
    public void start() {

//        config = new MoonSpiderConfig.Builder().moonQueue(new RedisQueue("RedisTaskQueue")).build();
        config = new MoonSpiderConfig.Builder().moonQueue(new DefaultQueue()).build();


        MoonProducer moonProducer = MoonProducer.getProducer();
        moonProducer.setConfig(config);
        moonProducer.produce(param, moonDownloader, moonProcessor, moonUploader);
        scheduleCore = new ScheduleCore(config);
        scheduleCore.start();
        log.debug("SpiderCore start , spider begin .");
    }


    private void readYaml() {

    }
}
