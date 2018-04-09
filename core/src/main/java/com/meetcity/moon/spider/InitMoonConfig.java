package com.meetcity.moon.spider;

import com.meetcity.moon.spider.component.ProxyIp;
import com.meetcity.moon.spider.component.RandomNum;
import com.meetcity.moon.spider.monitor.MoonMonitor;
import com.meetcity.moon.spider.schedule.MoonQueue;
import com.meetcity.moon.util.YamlUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 *
 * Created by wds on 2018/2/14 .
 *
 *
 * 爬虫的配置
 *
 * 从配置文件读取
 *
 */


@Slf4j
public class InitMoonConfig {

    private String configFilePath = "core/src/main/resources/MoonCrawel.yml";
    private MoonSpiderConfig config;


    public void init() {
        Map<String, Object> map = YamlUtil.readYaml(configFilePath);
        if (map == null) {
            config = new MoonSpiderConfig.Builder().build();
            log.warn("config read error , use default config");

        } else {
            MoonSpiderConfig.Builder builder = new MoonSpiderConfig.Builder();
            initBuild(map, builder);
            config = builder.build();
        }

    }


    /**
     * 初始化build
     */
    private void initBuild(Map<String, Object> configMap, MoonSpiderConfig.Builder builder) {
        try {
            //爬虫的线程数
            int threadNum = (int) configMap.get("threadNum");

            //阻塞队列的最大长度，默认 10 * 1000
            long queueMaxLength = (long) configMap.get("queueMaxLength");

            //是否开启redis去重
            boolean useRemoveDuplicate = (boolean) configMap.get("useRemoveDuplicate");

            //redis去重中，使用SET类型去重，SET的名字
            String removalDuplicateSETName = (String) configMap.get("removalDuplicateSETName");

            //随机数，用于决定一个爬虫任务休眠几秒
            RandomNum randomNum;

            //代理IP
            ProxyIp proxyIp;

            //是否使用随机user-agent
            boolean isUseUserAgent = (boolean) configMap.get("isUseUserAgent");

            //监控器
            MoonMonitor moonMonitor;

            //任务队列的名字
            String moonQueueName = (String) configMap.get("moonQueueName");

            //任务队列
            MoonQueue moonQueue;

            builder.threadNum(threadNum)
                    .queueMaxLength(queueMaxLength)
                    .useRemoveDuplicate(useRemoveDuplicate)
                    .removalDuplicateSETName(removalDuplicateSETName)
                    .userAgent(isUseUserAgent)
                    .moonQueueName(moonQueueName);
        } catch (Throwable t) {
            log.error("get config from map error , errror is : ", t);
        }

    }

    public MoonSpiderConfig getConfig(){
        return config;
    }


}
