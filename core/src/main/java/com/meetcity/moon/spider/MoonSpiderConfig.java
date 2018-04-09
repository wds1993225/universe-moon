package com.meetcity.moon.spider;

import com.meetcity.moon.spider.component.MoonUserAgentsConfig;
import com.meetcity.moon.spider.monitor.DefaultMonitor;
import com.meetcity.moon.spider.schedule.MoonQueue;
import com.meetcity.moon.spider.component.ProxyIp;
import com.meetcity.moon.spider.component.ProxyUtil;
import com.meetcity.moon.spider.component.RandomNum;
import com.meetcity.moon.spider.monitor.MoonMonitor;
import com.meetcity.moon.spider.component.imp.DefaultQueue;
import com.meetcity.moon.util.OkHttpUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by wds on 2017/11/20.
 *
 * @author moon
 *         <p>
 *         <p>
 *         爬虫的配置类
 */

@Data
public class MoonSpiderConfig {


    /**
     * 爬虫的线程数
     */
    private final int threadNum;

    /**
     * 阻塞队列的最大长度，默认 10 * 1000
     */
    private final long queueMaxLength;

    /**
     * 是否开启redis去重
     */
    private final boolean useRemoveDuplicate;

    /**
     * redis去重中，使用SET类型去重，SET的名字
     */
    private final String removalDuplicateSETName;

    /**
     * 随机数，用于决定一个爬虫任务休眠几秒
     */
    private final RandomNum randomNum;

    /**
     * 代理IP
     */
    private final ProxyIp proxyIp;

    /**
     * 是否使用随机user-agent
     */
    private final boolean isUseUserAgent;


    /**
     * 监控器
     */
    private final MoonMonitor moonMonitor;


    /**
     * 任务队列的名字
     */
    private final String moonQueueName;
    /**
     * 任务队列
     */
    private final MoonQueue moonQueue;


    public MoonSpiderConfig() {
        this(new MoonSpiderConfig.Builder());
    }

    public MoonSpiderConfig(MoonSpiderConfig.Builder builder) {

        this.threadNum = builder.threadNum;
        this.queueMaxLength = builder.queueMaxLength;
        this.useRemoveDuplicate = builder.useRemoveDuplicate;
        this.removalDuplicateSETName = builder.removalDuplicateSETName;
        this.randomNum = builder.randomNum;
        this.proxyIp = builder.proxyIp;
        this.isUseUserAgent = builder.isUseUserAgent;
        this.moonMonitor = builder.moonMonitor;
        this.moonQueueName = builder.moonQueueName;
        this.moonQueue = builder.moonQueue;

    }

    public static final class Builder implements Serializable {


        private int threadNum;

        private long queueMaxLength;

        private boolean useRemoveDuplicate;

        private String removalDuplicateSETName;

        private RandomNum randomNum;

        private ProxyIp proxyIp;

        private boolean isUseUserAgent;

        private MoonMonitor moonMonitor;

        private String moonQueueName;

        private MoonQueue moonQueue;

        public Builder() {

            threadNum = 1;
            queueMaxLength = 10 * 1000;
            useRemoveDuplicate = false;
            removalDuplicateSETName = "redisRemovalDuplicateSET";
            randomNum = null;
            proxyIp = null;
            moonMonitor = new DefaultMonitor();
            moonQueueName = "RedisTaskQueue";
            moonQueue = new DefaultQueue();
            isUseUserAgent = true;
        }

        public Builder threadNum(int threadNum) {
            this.threadNum = threadNum;
            return this;
        }

        public Builder queueMaxLength(long queueMaxLength) {
            this.queueMaxLength = queueMaxLength;
            return this;
        }

        public Builder useRemoveDuplicate(boolean useRemoveDuplicate) {
            this.useRemoveDuplicate = useRemoveDuplicate;
            return this;
        }

        public Builder removalDuplicateSETName(String removalDuplicateSETName) {
            this.removalDuplicateSETName = removalDuplicateSETName;
            return this;
        }

        public Builder randomNum(RandomNum randomNum) {
            this.randomNum = randomNum;
            return this;
        }

        /**
         * 设置代理ip后，需要将本地代理ip池注入其中，统一管理ip
         **/
        public Builder ProxyIp(ProxyIp proxyIp) {
            this.proxyIp = proxyIp;
            if (proxyIp != null) {
                proxyIp.setProxies(ProxyUtil.proxyPool);
            }
            return this;
        }

        public Builder userAgent(boolean isUse) {
            this.isUseUserAgent = isUse;
            OkHttpUtil.isUseUA = this.isUseUserAgent;
            return this;
        }

        public Builder moonMonitor(MoonMonitor moonMonitor) {
            this.moonMonitor = moonMonitor;
            return this;
        }

        public Builder moonQueueName(String moonQueueName) {
            this.moonQueueName = moonQueueName;
            return this;
        }

        public Builder moonQueue(MoonQueue moonQueue) {
            this.moonQueue = moonQueue;
            return this;
        }

        public MoonSpiderConfig build() {
            return new MoonSpiderConfig(this);
        }
    }

}
