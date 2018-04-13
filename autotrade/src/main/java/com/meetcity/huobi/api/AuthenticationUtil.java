package com.meetcity.huobi.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by moon on 2018/4/13 .
 * <p>
 * 火币pro的授权API
 */
public class AuthenticationUtil {


    public enum RequestMethod {     //配置列表追加方式
        GET("get"), POST("post");
        private final String text;

        RequestMethod(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }


    /**
     * */
    public String signGETParam(String... params) {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("AccessKeyId=" + Constants.AccessKeyId);
        stringBuilder.append("&SignatureMethod=HmacSHA256");
        stringBuilder.append("&SignatureVersion=2");
        stringBuilder.append("&Timestamp=" + getTimeStrap());
        for (String sb : params) {
            stringBuilder.append(sb);
        }
        return stringBuilder.toString();

    }

    public String sign(Map<String,String> paramMap, RequestMethod method, String path) {
        StringBuilder signBody = new StringBuilder("");
        if (method == RequestMethod.GET) {        //请求方法
            signBody.append("GET\n");
        } else {
            signBody.append("POST\n");
        }
        signBody.append("api.huobi.pro\n");     //访问地址
        signBody.append(path + "\n");             //访问路径
        return "";
    }

    private String getTimeStrap() {
return "";
    }


    public static void main(String[] args) {
        List<String> lis = new ArrayList<String>();
        lis.add("AccessKeyId=e2xxxxxx-99xxxxxx-84xxxxxx-7xxxx");
        lis.add("order-id=1234567890");
        lis.add("SignatureMethod=HmacSHA256");
        lis.add("SignatureVersion=2");
        lis.add("Timestamp=2017-05-11T15%3A19%3A30");
        Collections.sort(lis);
        System.out.println("");
    }
}
