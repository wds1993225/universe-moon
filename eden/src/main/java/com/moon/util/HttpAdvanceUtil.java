package com.moon.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Map;

public class HttpAdvanceUtil {

    HttpClientBuildUtil buildUtil = new HttpClientBuildUtil();


    /**
     * GET请求
     *
     * @param url     请求的url
     * @param headers 请求头
     */
    public String get(String url, Map<String, String> headers) throws Exception {
        CloseableHttpClient httpclient = buildUtil.create().build();
        HttpRequestBase httpGet = new HttpGet(url);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }

        CloseableHttpResponse response = httpclient.execute(httpGet);
        try {
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } finally {
            response.close();
        }
    }

}
