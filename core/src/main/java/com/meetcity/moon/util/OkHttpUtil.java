package com.meetcity.moon.util;

import com.meetcity.moon.spider.component.MoonUserAgentsConfig;
import com.meetcity.moon.spider.component.ProxyIp;
import com.meetcity.moon.spider.component.ProxyUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wds on 2017/10/19.
 *
 * @author moon
 */
@Slf4j
public class OkHttpUtil {

    public static final long DEFAULT_READ_TIMEOUT_MILLIS = 8 * 1000;          //默认 读 超时时间
    public static final long DEFAULT_WRITE_TIMEOUT_MILLIS = 8 * 1000;         //默认 写 超时时间
    public static final long DEFAULT_CONNECT_TIMEOUT_MILLIS = 8 * 1000;       //默认 连接 超时时间


    private static OkHttpClient okHttpClient;
    private static OkHttpClient.Builder clientBuilder;
    private static ProxyIp proxyIp;


    /**
     * 执行一个GET请求
     * 返回失败返回null
     */
    public static Response get(String url) {
        return get(url, null);
    }

    public static Response get(String url, Map<String, String> headers) {
        Response response = null;
        try {
            response = getOkHttpClient().newCall(getRequest(url, headers)).execute();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return response;
    }

    /**
     * 获取一个POST请求的call
     *
     * @param url               请求的地址
     * @param requestParameters 请求参数
     */
    public static Response post(String url, Map<String, String> requestParameters) {
        return post(url, requestParameters, null);
    }

    /**
     * 获取一个POST请求的call
     *
     * @param headers 带有请求头的post请求
     */
    public static Response post(String url, Map<String, String> requestParameters, Map<String, String> headers) {
        Response response = null;
        try {
            response = getOkHttpClient().newCall(postRequest(url, requestParameters, headers)).execute();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return response;
    }


    /**
     * 获取一个POST请求的call
     */
    public static Response postJson(String url, String jsonData) {
        return postJson(url, jsonData, null);
    }

    public static Response postJson(String url, String jsonData, Map<String, String> headers) {
        Response response = null;
        try {
            response = getOkHttpClient().newCall(postJsonRequest(url, jsonData, headers)).execute();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return response;
    }

    /**
     * 获取一个put请求的call
     */
    public static Response put(String url, Map<String, String> requestParameters) {
        Response response = null;
        try {
            response = getOkHttpClient().newCall(putRequest(url, requestParameters)).execute();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return response;
    }

    /**
     * 获取一个put请求的call
     */
    public static Response putJson(String url, String jsonData) {
        Response response = null;
        try {
            response = getOkHttpClient().newCall(putJsonRequest(url, jsonData)).execute();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return response;
    }


    /**
     * 获取一个GET类型的请求
     *
     * @param url 请求的url
     */
    public static Request getRequest(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.get();
        setHeaderAndUA(builder, headers);
        return builder.build();
    }

    /**
     * 获取一个POST的请求
     *
     * @param url               请求的url
     * @param requestParameters 请求参数，Map类型
     * @param headers           添加请求头
     */
    public static Request postRequest(String url, Map<String, String> requestParameters, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.post(addParamToBuilder(requestParameters).build());
        setHeaderAndUA(builder, headers);
        return builder.build();
    }

    /**
     * 获取一个POST的请求
     *
     * @param url      请求的url
     * @param jsonData 请求参数，Json类型
     * @param headers  请求头
     */
    public static Request postJsonRequest(String url, String jsonData, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        //builder.addHeader("content-type", "application/json");
        builder.post(RequestBody.create(MediaType.parse("application/json"), jsonData));
        setHeaderAndUA(builder, headers);
        return builder.build();
    }

    /**
     * 获取一个PUT的请求
     *
     * @param url               请求的url
     * @param requestParameters 请求参数，Map类型
     */
    public static Request putRequest(String url, Map<String, String> requestParameters) {
        return new Request.Builder()
                .url(url)
                .put(addParamToBuilder(requestParameters).build())
                .build();
    }

    /**
     * 获取一个PUT的请求
     *
     * @param url      请求的url
     * @param jsonData 请求参数，String类型
     */
    public static Request putJsonRequest(String url, String jsonData) {
        return new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/json;charset:utf-8")
                .put(RequestBody.create(MediaType.parse("application/json;charset:utf-8"), jsonData))
                .build();
    }


    /**
     * 获取一个OkHttpClient
     */
    public static OkHttpClient getOkHttpClient() {
        return getOkHttpClient(null);
    }

    public static OkHttpClient getOkHttpClient(Map<String, String> cookies) {
        if (okHttpClient == null) {
            okHttpClient = getClientBuilder(cookies).build();
        }
        return okHttpClient;
    }

    /**
     * 获取client的builder
     *
     * @param cookies 要转入的cookies
     */
    public static OkHttpClient.Builder getClientBuilder(Map<String, String> cookies) {
        if (clientBuilder == null) {
            clientBuilder = new OkHttpClient.Builder();
            clientBuilder.connectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.SECONDS);
            if (cookies != null) {
                clientBuilder.cookieJar(setCookie(cookies));
            }
            if (ProxyUtil.proxyPool != null && ProxyUtil.proxyPool.size() > 0) {
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
            }
        }

        return clientBuilder;
    }

    /**
     * 将Map转换为OKHttp需要的FormBody
     *
     * @param requestParamMap 需要的请求参数 ，Map类型
     */
    private static FormBody.Builder addParamToBuilder(Map<String, String> requestParamMap) {
        FormBody.Builder formBody = new FormBody.Builder();
        if (requestParamMap != null) {
            for (Map.Entry<String, String> entry : requestParamMap.entrySet()) {
                if (entry != null) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key != null && value != null) {
                        formBody.add(key, value);
                    }
                }
            }
        }
        return formBody;
    }

    /**
     * 将map转换为okhttp3需要的header
     */
    private static Headers setHeaders(Map<String, String> headersParams) {
        Headers.Builder builder = new Headers.Builder();
        if (headersParams != null) {
            for (Map.Entry<String, String> entry : headersParams.entrySet()) {
                if (entry != null) {
                    builder.add(entry.getKey(), entry.getValue());
                }
            }
        }
        return builder.build();
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


    /**
     * 检查响应结果
     *
     * @param response 响应结果
     * @param url      请求url ，用于打印
     */
    public static String checkResponse(Response response, String url) {
        String data = null;
        if (response != null && response.body() != null && response.isSuccessful()) {
            try {
                data = response.body().string();
                log.info("Response success , url is : {} , response data is : {}", url, data);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("Response exception , url is : {} ", url);
            }
        } else {
            log.warn("Response error , url is : {} , response is : {}", url, response);
        }
        closeResponse(response);
        return data;
    }


    public static boolean isUseUA = false;       //是否使用UA，这里实现的不好，网络请求工具和爬虫配置耦合

    /**
     * 设置请求头和UA
     */
    private static void setHeaderAndUA(Request.Builder builder, Map<String, String> header) {

        if (header != null) {
            if (MoonUserAgentsConfig.getMoonUserAgentsConfig() != null && isUseUA) {       //配置不为空，请求头装入ua
                header.put("User-Agent", MoonUserAgentsConfig.getAnUserAgent());
            }
            builder.headers(setHeaders(header));

        } else {
            if (MoonUserAgentsConfig.getMoonUserAgentsConfig() != null && isUseUA) {
                Map<String, String> newHeader = new HashMap<>();
                newHeader.put("User-Agent", MoonUserAgentsConfig.getAnUserAgent());
                builder.headers(setHeaders(newHeader));
            }
        }

    }


    /**
     * 关闭response，防止内存泄漏
     */
    private static void closeResponse(Response response) {
        if (response != null) {
            response.close();
        }
    }

}
