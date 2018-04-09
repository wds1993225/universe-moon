package com.meetcity.moon.spider.template;

import com.meetcity.moon.constant.Constants;
import com.meetcity.moon.spider.download.MoonDownloader;
import com.meetcity.moon.spider.schedule.MoonTask;
import com.meetcity.moon.spider.schedule.MoonTaskExecutor;
import com.meetcity.moon.util.OkHttpUtil;
import com.meetcity.moon.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.util.Map;


/**
 * 列表类型 的模板下载器
 */
@Slf4j
public class TemplateListDownloader implements MoonDownloader {


    @Override
    public Object download(MoonTask moonTask) {
        if (moonTask == null) {
            return null;
        }

        String pageName = moonTask.getPaginationName();        //分页名
        Integer pageIndex = moonTask.getPaginationIndex();     //分页数
        if (StringUtil.isEmpty(pageName) && pageIndex == null) {
            return null;
        }
        log.info("HttpDownloader get page begin , task is : {}", moonTask);
        Constants.HttpMethod method = moonTask.getMethod();    //请求的方法类型
        String url = moonTask.getUrl();                        //请求的url
        Map<String, String> param = moonTask.getParam();        //请求的参数
        Map<String, String> headers = moonTask.getHeaders();    //请求头
        String json = moonTask.getJson();                      //请求的JSON类型的参数
        Response response = null;
        switch (method) {
            case GET:
                MoonTask.PageSplitType type = moonTask.getPageSplitType();
                if (type == null || type == MoonTask.PageSplitType.KV) {
                    url = url + "&" + pageName + "=" + pageIndex;       //这里将分页参数拼入链接中
                } else if (type == MoonTask.PageSplitType.ADD) {
                    url = url + pageName + pageIndex + (moonTask.getSuffix() != null ? moonTask.getSuffix() : "");
                }
                response = OkHttpUtil.get(url, headers);
                break;
            case POST:
                if (param != null) {
                    param.put(pageName, String.valueOf(pageIndex));      //这里将分页参数加入请求参数中
                    response = OkHttpUtil.post(url, param, headers);
                } else {
                    response = OkHttpUtil.postJson(url, json, headers);
                }
                break;
            case PUT:
                break;

        }
        log.info("HttpDownloader get page end ");
        log.debug("url is :{} , Response is :{} ", url, response != null ? response : " 响应为空.");
        if (response != null && response.body() != null && response.isSuccessful()) {
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
