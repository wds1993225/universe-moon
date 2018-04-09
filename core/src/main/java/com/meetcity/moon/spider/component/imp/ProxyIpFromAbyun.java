package com.meetcity.moon.spider.component.imp;

import com.meetcity.moon.spider.component.ProxyIp;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.List;

/**
 * Created by wds on 2017/12/7.
 */
public class ProxyIpFromAbyun implements ProxyIp {

    private final String PROXY_AUTHORIZATIONNAME = "SDdXMzlNVUVXTFE4U0I2RDowRTExMUE4OUJGMjkyRDBD";
    private final String PROXY_USERNAME = "H7W39MUEWLQ8SB6D";
    private final String PROXY_PASS = "0E111A89BF292D0C";
    private final String PROXY_URL = "http-dyn.abuyun.com";
    private final int PROXY_PORT = 9020;

    @Override
    public void setProxies(List<Proxy> proxyPool) {
        Authenticator.setDefault(new ProxyAuthenticator(PROXY_USERNAME,PROXY_PASS));
        InetSocketAddress addr = new InetSocketAddress(PROXY_URL, PROXY_PORT);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
        proxyPool.add(proxy);
    }

    public static class ProxyAuthenticator extends Authenticator {
        private String user, password;

        public ProxyAuthenticator(String user, String password) {
            this.user     = user;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, password.toCharArray());
        }
    }


}
