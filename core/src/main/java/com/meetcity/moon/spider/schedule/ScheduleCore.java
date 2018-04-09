package com.meetcity.moon.spider.schedule;

import com.meetcity.moon.spider.MoonSpiderConfig;
import com.meetcity.moon.spider.component.RandomNum;
import com.meetcity.moon.spider.monitor.DefaultMonitor;
import com.meetcity.moon.spider.monitor.MoonMonitor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wds on 2017/11/8.
 * <p>
 * 任务调度中心
 * 职责： 1，创建线程 ； 2， 从队列中取数据，调用开始方法
 *
 * @author moon
 */
@Slf4j
public class ScheduleCore {


    private ExecutorService executor;
    private MoonMonitor moonMonitor;
    private MoonSpiderConfig config;
    private MoonQueue moonQueue;

    public ScheduleCore(MoonSpiderConfig config) {
        this.config = config;
        moonMonitor = config.getMoonMonitor();
        moonQueue = config.getMoonQueue();
    }

    public void start() {

        log.info("ScheduleCore is start ...");
        if (moonMonitor == null) {
            moonMonitor = new DefaultMonitor();
        }
        executor = Executors.newFixedThreadPool(config.getThreadNum());
        for (int i = 0; i < config.getThreadNum(); i++) {
            TaskConsumer taskConsumer = new TaskConsumer();
            executor.execute(taskConsumer);
            log.info("Thread :{} is begin ...", i);
        }


    }

    class TaskConsumer implements Runnable {

        @Override
        public void run() {
            while (true) {
                MoonTask task = null;
                long queueSize = moonQueue.queueSize();
                if (queueSize != 0) {
                    try {
                        RandomNum randomNum = config.getRandomNum();
                        if (randomNum != null) {
                            long time = randomNum.getNum();
                            log.debug("Thread begin sleep , sleep time is : {}", time);
                            Thread.sleep(time);      //这里调用随机数策略，休眠固定或随机时间
                        }
                        log.info("Before consuming , current queue size is {}", queueSize);     //在多线程情况下，尤其是多线程读取redis下，这个值可能有延迟或者不准
                        task = moonQueue.takeTask();
                        if (task != null) {     //使用BlockQueue的take阻塞，task一定不为空
                            MoonTaskExecutor taskExecutor = new MoonTaskExecutor(task, config);
                            moonMonitor.onStart(task);      //监控

                            for (int i = 0; i <= taskExecutor.getRetryTime(); i++) {        //这里循环重试
                                boolean isSuccess = taskExecutor.execute();
                                if (isSuccess) {
                                    break;
                                }
                            }
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                        log.error("Consum task error : {}, task is :{} ", t, task);
                    }
                }
            }
        }
    }

}
