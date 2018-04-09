package com.meetcity.moon.spider.loginSpider;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by moon on 2018/3/7 .
 */
@Data
public abstract class MoonRequest {


    private Method method;  //  请求方式
    private String url;    //请求的url
    private Map<String, String> param;   //请求的参数
    private Map<String, String> header;  //请求头
    private String jsonData;        //put类型的请求，请求体  ATTENTION：如果此对象不为空，则使用postJson类型，会忽略param参数

    private Object lastResult;      //上一个请求的处理结果，只有在链式调用中的第二个请求才有

    public MoonRequest() {
        method = Method.GET;        //默认get
        header = new HashMap<>();   //初始化一组常用请求头

        header.put("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
        header.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        header.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");
    }

    public enum Method {     //配置列表追加方式
        GET("get"), POST("post'"), PUT("put"), DELETE("delete");
        private final String text;

        Method(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }


    /**
     * 使用build模式构建请求
     */


    public MoonRequest setMethod(Method method) {
        this.method = method;
        return this;
    }

    public MoonRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    public MoonRequest setParam(Map<String, String> param) {
        this.param = param;
        return this;
    }

    public MoonRequest addParam(String key, String value) {
        if (param == null) {
            param = new HashMap<>();
        }
        param.put(key, value);
        return this;
    }

    public MoonRequest setHeader(Map<String, String> header) {
        this.header = header;
        return this;
    }

    public MoonRequest setJSON(String jsonData) {
        this.jsonData = jsonData;
        return this;
    }

    public MoonRequest addHeader(String key, String value) {
        if (header == null) {
            header = new HashMap<>();
        }
        header.put(key, value);
        return this;
    }

    public MoonRequest commonHeader() {
        if (header == null) {
            header = new HashMap<>();
        }
        header.put("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
        header.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        header.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");

        return this;
    }

    public MoonRequest load(RequestChain requestChain) {
        if (requestChain != null) {
            requestChain.addRequest(this);
        }
        return this;
    }


    public abstract Object process(Object downloadResult, MoonRequest request);


}
