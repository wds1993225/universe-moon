package com.meetcity.moon.constant;

import lombok.Data;

import javax.ws.rs.DELETE;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wds on 2017/10/19.
 *
 * @author moon
 */
public class Constants {

    /**
     * 文件路径定义
     * 反爬接口测试
     */
    public static String FILE_PATH = "D:/train/file.txt";        //文件路径
    public static String FOLDER_PATH = "D:/crawlTest/";     //文件夹路径
    public static String FILE_NAME = "报告.txt";      //文件名

    public static String[] alphabet = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"      //小写字母表
            , "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    public static boolean isOpenLog = true;     //是否打开日志

    public static int RETRY_TIMES = 3;    //重试次数

    /**
     * 爬虫相关
     */

    public static int queueMaxLength = 1000000;        //队列默认长度 100 0000

    public static String removalDuplicateSETName = "RDSET";     //redis 去重的SET名

    public static String redisQueueName = "RDQueue";            //redis 任务队列 名


    public enum HttpMethod {
        GET("get"), POST("post"), PUT("put"), DELETE("delete");

        private final String text;

        HttpMethod(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }


    /**
     * 监控相关
     */
    public static volatile AtomicInteger TOTAL_UPLOAD_NUM = new AtomicInteger(1);  //上传的总数

    /**
     * 任务过程
     * 下载，解析，上传
     */
    public static enum MoonPeriod {

        DOWNLOAD("download"), PROCESS("process"), UPLOAD("upload");

        private final String text;

        private MoonPeriod(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }


}
