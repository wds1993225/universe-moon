package com.meetcity.moon.spider.schedule;

import com.meetcity.moon.spider.MoonSpiderConfig;
import com.meetcity.moon.spider.download.MoonDownloader;
import com.meetcity.moon.spider.processor.MoonProcessor;
import com.meetcity.moon.spider.upload.MoonUploader;
import com.meetcity.moon.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by wds on 2017/11/9.
 *
 * @author moon
 */
@Slf4j
public class MoonProducer {

    private MoonSpiderConfig config;
    private static MoonQueue moonQueue;

    private static MoonProducer producer = null;

    /**
     * 这里只能实例化一次，其他地方不能调用此方法，这样写虽然有问题，折中
     */
    private MoonProducer() {

    }

    public void produce(MoonTask task) {
        moonQueue.addTask(task);
    }

    /**
     * 生产任务，推荐此构造方法，可以校验类型
     */
    public void produce(MoonTask task, MoonDownloader downloader, MoonProcessor processor, MoonUploader uploader) {

        task.setMoonDownloadName(downloader == null ? null : downloader.getClass().getName());
        task.setMoonProcessorName(processor == null ? null : processor.getClass().getName());
        task.setMoonUploadName(uploader == null ? null : uploader.getClass().getName());
        moonQueue.addTask(task);            //任务加入到队列中
        log.info("new task add to queue,param is {}", task);
    }


    public void produce(List<MoonTask> tasks, String downloaderName, String processorName, String uploaderName) {
        log.info("Producer is producing task ...");
        for (MoonTask task : tasks) {
            task.setMoonDownloadName(downloaderName);
            task.setMoonProcessorName(processorName);
            task.setMoonUploadName(uploaderName);
            moonQueue.addTask(task);            //任务加入到队列中
            log.info("New Task add to queue,task is {}", task);
        }

    }

    public void setConfig(MoonSpiderConfig config){
        this.config = config;
        moonQueue = config.getMoonQueue();
        producer = this;
    }

    /**
     * 其他地方生成任务要调用此方法
     */
    public static MoonProducer getProducer() {
        return producer;
    }


    private static MoonDownloader getDownloader(String downloadName) {
        return (MoonDownloader) ReflectUtil.getObjectByReflect(downloadName);
    }

    private static MoonProcessor getProcessor(String processorName) {
        return (MoonProcessor) ReflectUtil.getObjectByReflect(processorName);
    }

    private static MoonUploader getUploader(String uploaderName) {
        return (MoonUploader) ReflectUtil.getObjectByReflect(uploaderName);
    }


}
