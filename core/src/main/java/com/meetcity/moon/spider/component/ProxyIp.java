package com.meetcity.moon.spider.component;

import com.meetcity.moon.spider.SpiderCore;
import com.meetcity.moon.spider.component.ProxyUtil;

import java.net.Proxy;
import java.util.List;

/**
 * Created by wds on 2017/10/31.
 *
 * @author moon
 *
 * 代理ip的接口，{@linkplain SpiderCore}中会调用
 * {@link #setProxies(List)}方法，设置代理ip
 *
 * 里面传入的是@{@link ProxyUtil#proxyPool}
 *
 */
public interface ProxyIp {

    /*  public static String[] getIp(){
          return new String[]{"104.41.49.232","80"};
      }*/
    void setProxies(List<Proxy> proxyPool);

}
