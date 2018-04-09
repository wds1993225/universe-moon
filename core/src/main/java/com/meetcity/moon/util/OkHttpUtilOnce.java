package com.meetcity.moon.util;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.meetcity.moon.util.OkHttpUtil.DEFAULT_CONNECT_TIMEOUT_MILLIS;
import static com.meetcity.moon.util.OkHttpUtil.DEFAULT_READ_TIMEOUT_MILLIS;
import static com.meetcity.moon.util.OkHttpUtil.DEFAULT_WRITE_TIMEOUT_MILLIS;


/**
 * Created by wds on 2017/10/19.
 *
 * @author moon
 * <p>
 * 用于生产一个普通的请求，不使用单例，不使用代理
 * <p>
 * 适用于 [一次请求] 和 [上传操作]
 */
public class OkHttpUtilOnce {



    public static String get(String url) {
        if (StringUtil.isEmpty(url)) {
            return null;
        }
        try {
            Response response = getOkHttpClient().newCall(OkHttpUtil.getRequest(url,null)).execute();
            return OkHttpUtil.checkResponse(response,url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 执行Put请求
     */
    public static Response putJSON(String url, String jsonData) {
        try {
            Response response = getOkHttpClient().newCall(new Request.Builder()
                    .url(url)
                    .addHeader("content-type", "application/json;charset:utf-8")
                    .put(RequestBody.create(MediaType.parse("application/json;charset:utf-8"), jsonData))
                    .build()).execute();
            return response;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }


    /**
     * 返回一个OkHttpClient
     */
    private static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder().connectTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.SECONDS)
                .build();
    }
}
