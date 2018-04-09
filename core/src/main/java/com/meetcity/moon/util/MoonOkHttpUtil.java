package com.meetcity.moon.util;

import com.meetcity.moon.spider.component.ProxyPool;
import com.meetcity.moon.spider.component.ProxyUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by wds on 2018/1/3.
 *
 * @author moon
 * <p>
 * 爬虫网络请求工具
 * <p>
 * 1，User agent
 * 2，代理Ip
 * 3，cookie池
 */

@Slf4j
public class MoonOkHttpUtil {

    private static final long DEFAULT_READ_TIMEOUT_MILLIS = 8 * 1000;          //默认 读 超时时间
    private static final long DEFAULT_WRITE_TIMEOUT_MILLIS = 8 * 1000;         //默认 写 超时时间
    private static final long DEFAULT_CONNECT_TIMEOUT_MILLIS = 8 * 1000;       //默认 连接 超时时间

    private static ProxyPool proxyPool ;
    private static volatile List<Proxy> proxyList = new CopyOnWriteArrayList<>();


    public static OkHttpClient.Builder getClientBuilder(Map<String, String> cookies) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.SECONDS);
        if (cookies != null) {
            clientBuilder.cookieJar(setCookie(cookies));
        }
        if (proxyPool != null && proxyPool.getIpList().size() > 0) {
            clientBuilder.proxySelector(new ProxySelector() {
                @Override
                public List<Proxy> select(URI uri) {
                       /* List<Proxy> proxyList = new ArrayList<Proxy>();
                        final String host = uri.getHost();
                        if (host.startsWith("127.0.0.1")) {
                            proxyList.add(Proxy.NO_PROXY);
                        } else {
                            // Add proxy
                            proxyList.add(new Proxy(Proxy.Type.HTTP,
                                    new InetSocketAddress(ProxyIp.getIp()[0], Integer.valueOf(ProxyIp.getIp()[1]))))  ;
                        }*/
                    return ProxyUtil.proxyPool;     //这里的代理ip池 是一个list ， 其他实现向其中加数据
                }

                @Override
                public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {

                }
            });
            //builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyIp.getIp()[0],Integer.valueOf(ProxyIp.getIp()[1]))));


            return clientBuilder;


        }
        return null;


    }


    /**
     * 管理cookie
     */
    private static CookieJar setCookie(Map<String, String> cookies) {
        final List<Cookie> list = new ArrayList<Cookie>();
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            list.add(new Cookie.Builder().name(entry.getKey()).value(entry.getValue()).domain("").build());
        }
        return new CookieJar() {

            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

            }

            public List<Cookie> loadForRequest(HttpUrl url) {
                return list;
            }
        };
    }

    public void setProxyPool(ProxyPool proxyPool){
        this.proxyPool = proxyPool;
    }

}



