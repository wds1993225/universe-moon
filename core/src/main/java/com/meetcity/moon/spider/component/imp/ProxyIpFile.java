package com.meetcity.moon.spider.component.imp;

import com.meetcity.moon.spider.component.ProxyIp;
import com.meetcity.moon.util.FileUtil;
import com.meetcity.moon.util.Util;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

/**
 * Created by wds on 2017/11/1.
 *
 * @author moon
 * <p>
 * 从文件中读取ip和端口
 * 文件格式一定要
 * 104.41.49.232:80
 * 63.175.159.29:80
 * 162.105.86.202:8118
 * 37.221.195.215:8118
 * 74.208.110.38:80
 */
public class ProxyIpFile implements ProxyIp {

    private String filePath;

    public ProxyIpFile(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void setProxies(List<Proxy> proxyPool) {
        List<String> list = FileUtil.readFileByLines(filePath);

        if (list == null) {
            return;
        }
        for (String str : list) {
            String[] s = str.split(":");
            if (s.length == 2) {
                if (Util.isReachable(s[0])) {
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(s[0], Integer.valueOf(s[1])));
                    proxyPool.add(proxy);
                }
            }
        }
    }
}
