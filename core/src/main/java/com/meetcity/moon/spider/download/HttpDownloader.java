package com.meetcity.moon.spider.download;

import com.meetcity.moon.constant.Constants;
import com.meetcity.moon.spider.schedule.MoonTask;
import com.meetcity.moon.spider.schedule.MoonTaskExecutor;
import com.meetcity.moon.util.OkHttpUtil;
import com.meetcity.moon.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.util.Map;

/**
 * Created by wds on 2017/11/9.
 *
 * @author moon
 */
@Slf4j
public class HttpDownloader implements MoonDownloader {

    @Override
    public String download(MoonTask moonTask) {
        if (moonTask == null) {
            return null;
        }
        log.info("HttpDownloader get page begin , param is {}", moonTask);
        Constants.HttpMethod method = moonTask.getMethod();    //请求的方法类型
        String url = moonTask.getUrl();                        //请求的url
        Map<String, String> param = moonTask.getParam();        //请求的参数
        Map<String, String> headers = moonTask.getHeaders();    //请求头
        String json = moonTask.getJson();                      //请求的JSON类型的参数
        Response response = null;
        switch (method) {
            case GET:
                response = OkHttpUtil.get(url, headers);
                break;
            case POST:
                if (param != null) {
                    response = OkHttpUtil.post(url, param, headers);
                } else {
                    response = OkHttpUtil.postJson(url, json, headers);
                }
                break;
            case PUT:
                break;

        }
        log.info("HttpDownloader get page end ");
        log.debug("Response is :{}", response != null ? response : " 响应为空.");
        if (response != null && response.body() != null || response.isSuccessful()) {
            try {
                String data = response.body().string();
                log.trace("Download data is :{}", data);
                return data;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
