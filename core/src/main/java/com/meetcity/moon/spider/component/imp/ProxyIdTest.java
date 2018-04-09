package com.meetcity.moon.spider.component.imp;

import com.meetcity.moon.spider.component.ProxyIp;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

/**
 * Created by wds on 2017/11/15.
 *
 * @author moon
 */
public class ProxyIdTest implements ProxyIp {

    @Override
    public void setProxies(List<Proxy> proxyPool) {
       // Proxy proxy3 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("101.68.162.203", 4943));
        Proxy proxy1 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("60.184.184.66", 3536));
        //Proxy proxy2 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("119.90.63.3",3128));

        proxyPool.add(proxy1);
        //proxyPool.add(proxy2);
        //proxyPool.add(proxy3);
    }

}
