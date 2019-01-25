package com.moon.util;


import com.moon.Exception.HttpLoaderException;
import com.moon.Exception.HttpUtilException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于构建request
 */
public class HttpRequestBuilder {

    private HttpRequestMethod method;
    private HttpRequestBase request;
    private String url;
    private Map<String, String> headers;


    /**
     * 实例化builder
     *
     * @param url 必须有url
     */
    public static HttpRequestBuilder create(String url) {
        return new HttpRequestBuilder(url);
    }

    private HttpRequestBuilder(String url) {
        if (StringUtils.isEmpty(url)) {
            throw new HttpLoaderException("url can not be null or \"\"");
        }
        this.url = url;
    }

    /**
     * 设置类型
     *
     * @param method 请求类型
     */
    public HttpRequestBuilder method(HttpRequestMethod method) {
        if (method == null) {
            throw new HttpLoaderException("request method can not be null");
        }
        this.method = method;
        if (method == HttpRequestMethod.GET) {
            request = new HttpGet(url);
        }
        if (method == HttpRequestMethod.POST) {
            request = new HttpPost(url);
        }
        if (method == HttpRequestMethod.PUT) {
            request = new HttpPut(url);
        }
        return this;
    }

    /**
     * 设置请求头
     *
     * @param headers 请求头
     */
    public HttpRequestBuilder header(Map<String, String> headers) {
        if (request == null) {
            throw new HttpLoaderException("header need call method(Method method) first");
        }
        this.headers = headers;
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    /**
     * 设置post请求的参数
     *
     * @param postParam 请求参数
     */
    public HttpRequestBuilder postParam(Map<String, String> postParam) {
        if (request == null) {
            throw new HttpLoaderException("postParam need call method(Method method) first");
        }
        if (!method.equals(HttpRequestMethod.POST)) {
            throw new HttpLoaderException(method.toString() + " method is not allowed here");
        }
        if (postParam == null) {
            throw new HttpLoaderException("postParam can not be null");
        }

        List<NameValuePair> pairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : postParam.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        try {
            ((HttpPost) request).setEntity(new UrlEncodedFormEntity(pairs));
        } catch (UnsupportedEncodingException e) {
            throw new HttpLoaderException("UnsupportedEncodingException");
        }
        return this;
    }

    public HttpRequestBase build() {
        return request;
    }

    public HttpResult httpResult;


}
