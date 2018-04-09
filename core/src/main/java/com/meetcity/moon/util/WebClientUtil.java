package com.meetcity.moon.util;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.meetcity.moon.spider.component.ProxyUtil;
import com.meetcity.moon.spider.component.imp.RandomStrategy;
import com.meetcity.moon.spider.component.ProxyIp;

import java.io.IOException;
import java.net.*;

/**
 * Created by wds on 2017/11/10.
 * <p>
 * 通过webClient获取网页
 *
 * @author moon
 */
public class WebClientUtil {

    private static int TIME_OUT = 50000;
    private static long jsWaitTime =50000;
    public static WebClient webClient;

    private static ProxyIp proxyIp;


    /**
     * 获取一个webClient对象
     */
    public static WebClient getWebClient() {
        WebClient webClient;
        if (ProxyUtil.proxyPool != null && ProxyUtil.proxyPool.size() > 0) {
            int i = RandomStrategy.randomInt(ProxyUtil.proxyPool.size()-1);
            Proxy proxy = ProxyUtil.proxyPool.get(i);
            InetSocketAddress inetAddress = (InetSocketAddress) proxy.address();
            String hostName = inetAddress.getHostName();
            int port = inetAddress.getPort();
            webClient = new WebClient(BrowserVersion.FIREFOX_52, hostName, port);
        } else {
            webClient = new WebClient(BrowserVersion.FIREFOX_52);
        }
        initOption(webClient);
        return webClient;
    }

    /**
     * 获取一个page对象
     */
    public static HtmlPage getPage(String url) {
        HtmlPage page = null;
        try {
            page = getWebClient().getPage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return page;
    }

    /**
     * 初始化webClient配置
     */
    public static void initOption(WebClient webClient) {
        webClient.setJavaScriptTimeout(TIME_OUT);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setTimeout(TIME_OUT);
        JavaScriptEngine engine = new JavaScriptEngine(webClient);
        webClient.setJavaScriptEngine(engine);
        webClient.waitForBackgroundJavaScript(jsWaitTime);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());



    }


    /**
     * 设置代理ip
     *
     * @param proxyIpImp 代理ip的实现类
     */
    public static void setProxyIp(ProxyIp proxyIpImp) {
        if (proxyIpImp != null) {
            proxyIp = proxyIpImp;
        }
    }

    public static void getRequest(String url) {
        URL url1 = null;
        try {
            url1 = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        WebRequest request = new WebRequest(url1);
    }

}
