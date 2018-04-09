package com.meetcity.moon.util;

import com.meetcity.moon.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

/**
 * Created by wds on 2017/11/27.
 *
 * @author moon
 *         <p>
 *         这个类在{@linkplain OkHttpUtil#get(String)}上又包了一层
 *         <p>
 *         里面加入了循环重试
 *         <p>
 *         将异常捕获在工具类中，直接返回String类型，有助于代码简洁
 */
@Slf4j
public class OkHttpUtilCircle {


    /**
     * get请求
     *
     * @param url 请求的url
     */
    public static String get(String url) {
        return get(url, null);
    }

    public static String post(String url, Map<String, String> requestParam) {
        return post(url, requestParam, null);
    }

    public static String postJson(String url, String JsonData) {
        return postJson(url, JsonData, null);
    }


    /**
     * get请求
     *
     * @param url     请求url
     * @param headers 请求头
     * @return 请求结果已经转换成String
     */
    public static String get(String url, Map<String, String> headers) {

        String data = null;
        for (int i = 0; i < Constants.RETRY_TIMES; i++) {
            Response response = OkHttpUtil.get(url, headers);
            data = OkHttpUtil.checkResponse(response, url);
            if (data != null) {
                break;
            }
        }
        return data;
    }


    /**
     * 获取一个POST请求的call
     *
     * @param url               请求的url
     * @param requestParameters 请求参数
     * @param headers           带有请求头的post请求
     */
    public static String post(String url, Map<String, String> requestParameters, Map<String, String> headers) {
        String data = null;
        for (int i = 0; i < Constants.RETRY_TIMES; i++) {
            Response response = OkHttpUtil.post(url, requestParameters, headers);
            data = OkHttpUtil.checkResponse(response, url);
            if (data != null) {
                break;
            }
        }
        return data;
    }

    /**
     * post 一个Json参数的请求
     *
     * @param url      请求的url
     * @param jsonData 参数，json类型
     * @param headers  请求头
     */
    public static String postJson(String url, String jsonData, Map<String, String> headers) {
        String data = null;
        for (int i = 0; i < Constants.RETRY_TIMES; i++) {
            Response response = OkHttpUtil.postJson(url, jsonData, headers);
            data = OkHttpUtil.checkResponse(response, url);
            if (data != null) {
                break;
            }
        }
        return data;
    }


    /**
     * put 一个参数为JSON 类型的请求
     *
     * @param url      请求的url
     * @param jsonData 请求参数 JSON类型
     */
    public static String putJson(String url, String jsonData) {
        String data = null;
        for (int i = 0; i < Constants.RETRY_TIMES; i++) {
            Response response = OkHttpUtil.putJson(url, jsonData);
            data = OkHttpUtil.checkResponse(response, url);
            if (data != null) {
                break;
            }
        }
        return data;
    }





}
