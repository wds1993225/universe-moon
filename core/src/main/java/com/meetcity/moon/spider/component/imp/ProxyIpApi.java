package com.meetcity.moon.spider.component.imp;

import com.meetcity.moon.spider.component.ProxyIp;
import com.meetcity.moon.util.OkHttpUtilOnce;
import lombok.extern.slf4j.Slf4j;

import java.net.*;
import java.util.List;


/**
 * Created by wds on 2017/11/30.
 *
 * @author moon
 */
@Slf4j
public class ProxyIpApi implements ProxyIp {

    @Override
    public void setProxies(List<Proxy> proxyPool) {
        getIpFromKuaiDaiLi(proxyPool);
    }

    private String url = "http://dev.kuaidaili.com/api/getproxy/?orderid=988651737011298&num=50&b_pcchrome=1&b_pcie=1&b_pcff=1&protocol=1&method=1&an_an=1&an_ha=1&sep=1";


    public void getIpFromKuaiDaiLi(List<Proxy> list) {
        try {
//            while (true) {
            String data = OkHttpUtilOnce.get(url);
            log.debug("Ip is : {}",data);
            //String data = OkHttpUtilCircle.get(url);
            if (data == null) {
                //continue;
                return;
            }
            String[] datas = data.split("\r\n");
            for (String str : datas) {
                String[] ip = str.split(":");
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip[0], Integer.valueOf(ip[1])));
                list.add(list.size(), proxy);
                   /* if (ProxyUtil.isUseful(ip[0], ip[1], null)) {
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip[0], Integer.valueOf(ip[1])));
                        ProxyUtil.proxyList.add(ProxyUtil.proxyList.size(),proxy);
                    }*/
            }
//                Thread.sleep(10 * 1000 * 60);
//            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

}
