package com.moon.util;


import com.moon.Exception.HttpLoaderException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * http 下载工具
 * <p>
 * 不支持代理，使用连接池
 * <p>
 * {@linkplain HttpProxyLoader}
 */
public class HttpLoader {

    public static HttpClientBuilder builder;

    private static PoolingHttpClientConnectionManager connectionManager;

    /**
     * 创建Builder
     * <p>
     * TODO 线程安全
     */
    public HttpLoader instance() {
        if (builder == null) {
            instance(100, 10, null);
        }
        return this;
    }

    /**
     * 配置SSL,配置最大连接数
     *
     * @param maxTotal           最大连接数
     * @param defaultMaxPerRoute 每个路由默认连接数
     */
    private HttpLoader instance(int maxTotal, int defaultMaxPerRoute, SSL ssl) {

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
            throw new HttpLoaderException("ssl配置异常");
        }
    }


    /**
     * GET请求
     *
     * @param requestBuilder 传入请求构建器
     * @return 将请求结果保存在请求构建器中
     */
    public void call(HttpRequestBuilder requestBuilder) {
        try {
            CloseableHttpResponse response = builder.build().execute(requestBuilder.build());
            HttpEntity entity = response.getEntity();
            requestBuilder.httpResult.setResult(EntityUtils.toString(entity));

        } catch (Exception e) {
            throw new HttpLoaderException("get error ", e);
        }
    }

    /**
     * 生成一个Http GET request
     */
    public HttpRequestBase get(String url, Map<String, String> headers) {
        HttpGet get = (HttpGet) HttpRequestBuilder.create(url).header(headers).build();
        try {
            CloseableHttpResponse response = builder.build().execute(get);
            HttpEntity entity = response.getEntity();

        } catch (Exception e) {
            throw new HttpLoaderException("get error ", e);
        }
        return get;
    }


    private SSLConnectionSocketFactory buildSSLConnectionSocketFactory(SSL ssl)
            throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance(ssl.text);
        sslContext.init(null, new TrustManager[]{MyX509TrustManager.INSTANCE}, null);
        return new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
    }

    /**
     * ssl协议类型
     */
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
