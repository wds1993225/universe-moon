package com.moon.util;


import com.moon.Exception.HttpUtilException;
import org.apache.http.HttpHost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * HttpClientBuilder的封装类
 * <p>
 * 用于初始化HttpClientBuilder
 * <p>
 * see {@linkplain org.apache.http.impl.client.HttpClientBuilder}
 * <p>
 * <p>
 * 包括4个部分
 * <p>
 * https的支持
 * 代理的支持
 */
public class HttpClientBuildUtil {

    public static HttpClientBuilder builder;

    private static PoolingHttpClientConnectionManager connectionManager;


    /**
     * 创建Builder
     */
    public HttpClientBuildUtil create() {
        if (builder == null) {
            create(100, 10, null);
        }
        return this;
    }

    /**
     * 配置SSL,配置最大连接数
     *
     * @param maxTotal           最大连接数
     * @param defaultMaxPerRoute 每个路由默认连接数
     */
    public HttpClientBuildUtil create(int maxTotal, int defaultMaxPerRoute, SSL ssl) {

        if (builder != null) {      //保证只有一个builder
            return this;
        }
        try {
            if (ssl == null) {      //如果为空，就默认为SSLv3
                ssl = SSL.SSLv3;
            }
            builder = HttpClients.custom();
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                    .<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", buildSSLConnectionSocketFactory(ssl)).build();
            connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            connectionManager.setMaxTotal(maxTotal);
            connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
            builder.setConnectionManager(connectionManager);
            return this;
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new RuntimeException("ssl配置异常");
        }
    }


    /**
     * 设置代理ip
     *
     * @param host 代理ip
     * @param port 代理端口
     */
    public HttpClientBuildUtil proxy(String host, int port) {
        if (builder == null) {
            create();
        }
        HttpHost proxy = new HttpHost(host, port, "http");
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        builder.setRoutePlanner(routePlanner);
        return this;
    }


    /**
     * 构建
     */
    public CloseableHttpClient build() {
        if (builder == null) {
            create();
        }
        return builder.build();
    }


    private SSLConnectionSocketFactory buildSSLConnectionSocketFactory(SSL ssl)
            throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance(ssl.text);
        sslContext.init(null, new TrustManager[]{MyX509TrustManager.INSTANCE}, null);
        return new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
    }

    public enum SSL {

        SSL("SSL"),
        SSLv3("SSLv3"),
        TLSv1("TLSv1"),
        TLSv1_1("TLSv1.1"),
        TLSv1_2("TLSv1.2");

        public final String text;

        SSL(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }



}
