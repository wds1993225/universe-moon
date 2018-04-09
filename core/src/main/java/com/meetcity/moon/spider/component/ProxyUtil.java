package com.meetcity.moon.spider.component;


import com.meetcity.moon.util.OkHttpUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by wds on 2017/11/30.
 *
 * @author moon
 * <p>
 * 用于对代理的预处理，ping和预先请求
 */
public class ProxyUtil {

    public static volatile List<Proxy> proxyPool = new CopyOnWriteArrayList<>();        //使用一个list作为代理池



    public static boolean isUseful(String ip, String port) {
        return isUseful(ip, port, null);
    }

    /**
     * 检查一个代理是否可用
     */
    public static boolean isUseful(String ip, String port, String url) {

        return sentRequest(ip, port, url) != null;

    }


    public static String sentRequest(String ip, String port, String url) {
        if (url == null) {
            url = "http://www.baidu.com";
        }
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2 * 5000, TimeUnit.SECONDS)
                .writeTimeout(2 * 5000, TimeUnit.SECONDS)
                .readTimeout(2 * 5000, TimeUnit.SECONDS)
                .proxySelector(new ProxySelector() {
                    @Override
                    public List<Proxy> select(URI uri) {
                        List<Proxy> proxyList = new ArrayList<Proxy>();
                        proxyList.add(new Proxy(Proxy.Type.HTTP,
                                new InetSocketAddress(ip, Integer.valueOf(port))));
                        return proxyList;
                    }

                    @Override
                    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {

                    }
                }).build();
        try {
            Response response = client.newCall(new Request.Builder().url(url).build()).execute();
            return OkHttpUtil.checkResponse(response,url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
