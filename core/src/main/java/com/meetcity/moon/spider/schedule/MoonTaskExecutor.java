package com.meetcity.moon.spider.schedule;

import com.meetcity.moon.constant.Constants;
import com.meetcity.moon.spider.MoonSpiderConfig;
import com.meetcity.moon.spider.download.DefaultDownloader;
import com.meetcity.moon.spider.download.MoonDownloader;
import com.meetcity.moon.spider.monitor.MoonMonitor;
import com.meetcity.moon.spider.processor.DefaultProcessor;
import com.meetcity.moon.spider.processor.MoonProcessor;
import com.meetcity.moon.spider.upload.DefaultUploader;
import com.meetcity.moon.spider.upload.MoonUploader;
import com.meetcity.moon.util.ReflectUtil;
import com.meetcity.moon.util.StringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import redis.RedisUtil;

import java.util.Map;

/**
 * Created by wds on 2017/11/8.
 *
 * @author moon
 *         <p>
 *         这个类有不少东西 @。@
 *         <p>
 *         包括下载器，解析器，上传器，请求参数，额外信息，监控，builder
 *         <p>
 *         这个类{@linkplain MoonTaskExecutor#execute()} ()}方法中 ，下载和上传都 已经包含了 循环重试，
 *         所以在编码的时候只需考虑一次请求，失败返回null，就可以了，流程会自动循环重试。
 *         超过{@linkplain MoonTaskExecutor#downloadRetryTime}重试次数后，就会进入监控
 */

@Data
@Slf4j
public class MoonTaskExecutor {


    private MoonDownloader downloader;        //下载器
    private MoonProcessor processor;          //解析器
    private MoonUploader uploader;            //上传器
    private int downloadRetryTime = 10;            //下载重试
    private int uploadloadRetryTime = 10;          //上传重试
    private int retryTime = 10;                                //从队列中取任务重试
    private MoonTask moonTask;                  //请求参数信息


    private MoonMonitor moonMonitor;        //任务的监控
    private String errorMessage;            //错误信息

    private MoonSpiderConfig config;


    /**
     * 传入参数，根据参数携带的信息实例化各种器
     * 各种器为空，使用默认来初始化，不为空，反射实例化
     */
    public MoonTaskExecutor(MoonTask moonTask, MoonSpiderConfig config) {
        this.config = config;
        this.moonTask = moonTask;
        String downloaderName = moonTask.getMoonDownloadName();
        String processorName = moonTask.getMoonProcessorName();
        String uploaderName = moonTask.getMoonProcessorName();
        if (StringUtil.isEmpty(downloaderName)) {
            this.downloader = new DefaultDownloader();
            log.warn("downloader is null , use default downloader , task is : {} ", moonTask);
        } else {
            this.downloader = (MoonDownloader) ReflectUtil.getObjectByReflect(downloaderName);
        }

        if (StringUtil.isEmpty(processorName)) {
            this.processor = new DefaultProcessor();
            log.warn("processor is null , use default processor , task is : {} ", moonTask);
        } else {
            this.processor = (MoonProcessor) ReflectUtil.getObjectByReflect(processorName);
        }

        if (StringUtil.isEmpty(uploaderName)) {
            this.uploader = new DefaultUploader();
            log.warn("uploader is null , use default uploader , task is : {} ", moonTask);
        } else {
            this.uploader = (MoonUploader) ReflectUtil.getObjectByReflect(uploaderName);
        }
    }

    /**
     * 传入参数
     * 如果各种器为空，就使用默认器来初始化
     */
    public MoonTaskExecutor(MoonTask moonTask, MoonDownloader downloader, MoonProcessor processor, MoonUploader uploader, MoonSpiderConfig config) {
        this.config = config;
        this.moonTask = moonTask;
        if (downloader == null) {
            this.downloader = new DefaultDownloader();
            this.moonTask.setMoonDownloadName(DefaultDownloader.class.getName());
        } else {
            this.downloader = downloader;
            this.moonTask.setMoonDownloadName(downloader.getClass().getName());
        }

        if (processor == null) {
            this.processor = new DefaultProcessor();
            this.moonTask.setMoonProcessorName(DefaultProcessor.class.getName());
        } else {
            this.processor = processor;
            this.moonTask.setMoonProcessorName(processor.getClass().getName());
        }

        if (uploader == null) {
            this.uploader = new DefaultUploader();
            this.moonTask.setMoonUploadName(DefaultUploader.class.getName());
        } else {
            this.uploader = uploader;
            this.moonTask.setMoonUploadName(uploader.getClass().getName());
        }

    }


    public boolean execute() {
        moonMonitor = config.getMoonMonitor();
        if (StringUtil.isEmpty(moonTask.getUrl())) {
            log.error("url can not be null , task is : {}", moonTask);
            return false;
        }

        String removalDuplicateData = duplicateRemoval();
        //TODO some questions here, 如果采集了前100页，第101页有问题，再次传入第一页，任务是不会往后走的。
        if (config != null && config.isUseRemoveDuplicate()) {    //配置中 配置是否去重
            String md5 = DigestUtils.md5Hex(removalDuplicateData);     //redis存 MD5后的数据，打印和上报 都使用原始数据
            boolean isExist = RedisUtil.isExistInSET(
                    StringUtil.isEmpty(config.getRemovalDuplicateSETName()) ?
                            Constants.removalDuplicateSETName : config.getRemovalDuplicateSETName(), md5);
            if (isExist) {
                log.warn("this page have downloaded before, removalDuplicateData is : {} ",
                        removalDuplicateData);
                moonMonitor.haveBeanDownloaded(moonTask, removalDuplicateData);
                return true;        //此任务不再执行
            }
        }

        Object downloadResult = null;
        Object processorResult = null;
        Object uploadResult = null;

        /*执行下载，循环重试*/
        for (int i = 0; i < downloadRetryTime; i++) {
            try {
                downloadResult = downloader.download(moonTask);         //这里try catch住，避免出异常后抛到ScheduleCore 中的catch中，导致任务丢失
                if (null != downloadResult) {
                    log.debug("download success , result is : {} ", String.valueOf(downloadResult));
                    break;
                }
            } catch (Throwable t) {
                t.printStackTrace();
                errorMessage = t.getMessage();
                log.warn("download exception , download result is : {} , exception is : ", String.valueOf(downloadResult), t);
            }

            log.warn("download begin circle , param is : {} , retry time is : {}", String.valueOf(moonTask), i);       //这里有容错
        }

        /* 执行解析， 不包含循环重试，一般解析出问题 重试是解决不了的，主要是获取的网页格式与解析器不相符造成的
         * 造成这种原因 ：1，网页有反爬限制，返回了类似于访客系统的拦截页面，导致解析失败
         * 2，返回的那个网页有特殊的一两个格式，在目标网站的数据库中那个字段出现的概率很小，但是前端做了处理，在分析网站的时候没法分析全
         * */
        if (downloadResult != null) {
            try {
                processorResult = processor.processor(downloadResult, moonTask);
                log.debug("process success , result is : {}", String.valueOf(processorResult));
            } catch (Throwable t) {
                t.printStackTrace();
                errorMessage = t.getMessage();
                log.error("processor exception , download result is : {} , task is : {} ", String.valueOf(downloadResult), String.valueOf(moonTask));
            }
        } else {
            moonMonitor.onFailed(moonTask, errorMessage, null, Constants.MoonPeriod.DOWNLOAD);    //在执行 解析 过程中，发现 下载 结果为空，说明下载有问题，监控到下载层
        }

        /*执行上传，循环重试*/
        if (processorResult != null) {
            for (int i = 0; i < uploadloadRetryTime; i++) {
                try {
                    uploadResult = uploader.upload(processorResult, moonTask);
                    if (null != uploadResult) {
                        log.debug("upload success , result is : {} ", String.valueOf(uploadResult));
                        break;
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                    errorMessage = t.getMessage();
                    log.warn("upload exception , process result is : {} , exception is : ", String.valueOf(processorResult), t);
                }

                log.warn("upload begin circle , process result is : {} , retry time is : {}", String.valueOf(processorResult), i);      //这里有容错
            }
        } else {
            moonMonitor.onFailed(moonTask, errorMessage, downloadResult, Constants.MoonPeriod.PROCESS);     //在执行 上传 过程中，发现 解析 结果为空，说明解析有问题，监控到解析层
        }


        if (uploadResult != null) {
            log.debug("Has upload data , totalNum is :{}", Constants.TOTAL_UPLOAD_NUM.getAndIncrement());
            moonMonitor.onSuccess(moonTask);
            recordToRedis(removalDuplicateData);
            //TODO 上传--监控 ，判断上传任务的类型，如果为默认类型，则只打印，不报警
            return true;
        } else {
            moonMonitor.onFailed(moonTask, errorMessage, processorResult, Constants.MoonPeriod.UPLOAD);      //在执行完整个流程后，发现 上传 结果为空，说明上传有问题，监控到上传层
            return false;
        }
    }


    /**
     * 返回一个用于redis排重的key
     * <p>
     * key使用 {url + 请求参数 + 请求参数(json)}  组合
     *
     * @return 返回一个 MD5 加密后的数据
     **/
    private String duplicateRemoval() {
        StringBuilder redisKey = new StringBuilder("");
        String url = moonTask.getUrl();
        Map<String, String> params = moonTask.getParam();
        String json = moonTask.getJson();

        redisKey.append(url == null ? "" : url);
        redisKey.append(json == null ? "" : json);
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                redisKey.append(entry.getKey() + ":" + entry.getValue());
            }
        }

        log.debug("duplicateRemoval key is : {} ", redisKey.toString());
        return redisKey.toString();
    }

    /**
     * 将采集后的url等记录到redis
     * <p>
     * 用于去重
     */
    private void recordToRedis(String removalDuplicateData) {
        if (config != null && config.isUseRemoveDuplicate()) {
            String md5 = DigestUtils.md5Hex(removalDuplicateData);     //redis存 MD5后的数据，打印和上报 都使用原始数据
            RedisUtil.setADD(StringUtil.isEmpty(config.getRemovalDuplicateSETName()) ?
                    Constants.removalDuplicateSETName : config.getRemovalDuplicateSETName(), md5);
        }

    }
}
