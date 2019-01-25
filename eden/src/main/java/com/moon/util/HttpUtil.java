package com.moon.util;


import com.moon.Exception.HttpUtilException;
import org.apache.bcel.generic.FADD;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 网络请求的工具
 */
public class HttpUtil {

    private static final int CONNECTION_TIME = 1000 * 10;
    private static final int SOCKET_TIME = 1000 * 10;
    private static final int CONNECTION_REQUEST_TIME = 1000 * 10;
    private static final boolean REDIRECT_ENABLE = false;


    /**
     * GET请求
     *
     * @param url 请求URL
     */
    public String get(String url) throws Exception {
        return get(url, null);
    }

    /**
     * GET请求
     *
     * @param url     请求的url
     * @param headers 请求头
     */
    public String get(String url, Map<String, String> headers) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
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


    /**
     * POST请求
     *
     * @param url      请求URL
     * @param paramMap 请求参数
     */
    public String post(String url, Map<String, String> paramMap) throws Exception {
        return post(url, paramMap, null);
    }

    /**
     * POST请求
     *
     * @param url      请求url
     * @param paramMap 请求参数
     * @param headers  请求头
     */
    public String post(String url, Map<String, String> paramMap, Map<String, String> headers) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<>();
        if (paramMap == null) {
            throw new HttpUtilException("params can not be null");
        }
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }

        httpPost.setEntity(new UrlEncodedFormEntity(pairs));
        CloseableHttpResponse response = httpclient.execute(httpPost);

        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } finally {
            response.close();
        }
    }


    public HttpClientBuilder createBuilder(String host, int port) {
        HttpHost proxy = new HttpHost(host, port, "http");
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        HttpClientBuilder builder = HttpClients.custom().setRoutePlanner(routePlanner);
        return builder;
    }


    private SSLConnectionSocketFactory buildSSLConnectionSocketFactory(HttpClientBuildUtil.SSL ssl)
            throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance(ssl.text);
        sslContext.init(null, new TrustManager[]{MyX509TrustManager.INSTANCE}, null);
        return new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
    }
}
