package com.meetcity.moon.spider.component;

import com.meetcity.moon.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by wds on 2018/1/2.
 *
 * @author moon
 *         <p>
 *         随机代理
 */
@Slf4j
public class MoonUserAgentsConfig {

    private static final MoonUserAgentsConfig moonUserAgentsConfig = new MoonUserAgentsConfig();

    private static List<String> userAgentsPool; //ua 池


    /**
     * userAgent 的路径
     */
    private String uaPath = "core" + System.getProperty("file.separator") +
            "src" + System.getProperty("file.separator") +
            "main" + System.getProperty("file.separator") +
            "resources" + System.getProperty("file.separator") +
            "userAgents";


    private MoonUserAgentsConfig() {
        userAgentsPool = new CopyOnWriteArrayList<>();
        List<String> list = FileUtil.readFileByLines(uaPath);
        log.debug("userAgent file path is : {}", uaPath);
        if (list != null) {
            userAgentsPool.addAll(list);
        }
        log.debug("userAgent init finished");
    }

    /**
     * 获取一个随机UA
     */
    public static String getAnUserAgent() {
        Random random = new Random();
        int index = random.nextInt(userAgentsPool.size()-1);
        return userAgentsPool.get(index);
    }


    public static MoonUserAgentsConfig getMoonUserAgentsConfig() {
        return moonUserAgentsConfig;
    }



}
