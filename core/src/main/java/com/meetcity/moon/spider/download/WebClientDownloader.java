package com.meetcity.moon.spider.download;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.meetcity.moon.spider.schedule.MoonTask;
import com.meetcity.moon.spider.schedule.MoonTaskExecutor;
import com.meetcity.moon.util.WebClientUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by wds on 2017/11/10.
 *
 * @author moon
 */
@Slf4j
public class WebClientDownloader implements MoonDownloader {


    @Override
    public String download(MoonTask moonTask) {
        String url = moonTask.getUrl();
        log.debug("webclient download begin ,url is : {}", url);
        HtmlPage htmlPage = null;
        for (int i = 0; i < 10; i++) {
            htmlPage = WebClientUtil.getPage(url);
            if (htmlPage != null) {
                break;
            }
        }
        log.debug("webclient download end ");
        String result = null;
        if (htmlPage != null) {
            result = htmlPage.asXml();
            log.trace("get page success , url is :{} , response is : {}", url, htmlPage.getWebResponse());
        } else {
            log.trace("get page failed , url is : {} ", url);
        }
        return result;
    }
}
