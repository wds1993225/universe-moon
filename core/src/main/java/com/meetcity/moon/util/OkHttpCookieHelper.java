package com.meetcity.moon.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by moon on 2018/3/12 .
 */

@Slf4j
public class OkHttpCookieHelper {

    private Map<String, String> cookieMap;

    public OkHttpCookieHelper(Map<String, String> cookieMap) {
        this.cookieMap = cookieMap;
    }

    public Map<String, String> getCommonHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        //header.put("Accept-Encoding", "gzip, deflate");
        header.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        header.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");
        header.put("cookie", cookieMapToString(cookieMap));

        return header;
    }

    public void addCookiesFromResponse(Response response) {
        if (response == null) {
            log.error("response is null");
            return;
        }
        List<String> cookieList = response.headers("Set-Cookie");

        if (cookieList != null) {
            for (String cookies : cookieList) {

                String[] cookieArray = cookies.split(";");
                for (String cookie : cookieArray) {
                    if (cookie.contains("=")) {
                        String key = cookie.substring(0, cookie.indexOf("=")).replace("\n","").replaceAll("\n","");
                        String value = cookie.substring(cookie.indexOf("=") + 1).replace("\n","").replaceAll("\n","");
                        if (cookieMap.containsKey(key)) {
                            System.out.println("");
                            cookieMap.put(key, value); //这里直接存入全局cookie对象中
                            log.info("cookie key : [{}] already in cookieStore " +
                                    ",[{}] will be replaced by [{}]", key, cookieMap.get(key), value);
                            continue;
                        }
                        cookieMap.put(key, value); //这里直接存入全局cookie对象中
                        log.info("a new cookie add to cookieStore , key is : [{}] ,value is : [{}]", key, value);
                    }
                }
            }
        } else {
            log.warn("获取的请求头中没有cookie，无法获");
        }
    }

    /**
     * 将Map类型的cookies转化成String类型的cookies
     * <p>
     * 兼容okHttp的header
     */
    private String cookieMapToString(Map<String, String> cookieMap) {
        StringBuilder cookiesString = new StringBuilder();
        if (cookieMap != null) {
            for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
                cookiesString.append(entry.getKey() + "=" + entry.getValue() + ";");
            }
        }
        return cookiesString.toString();
    }

    private static String encodeHeadInfo( String headInfo ) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0, length = headInfo.length(); i < length; i++) {
            char c = headInfo.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                stringBuffer.append( String.format ("\\u%04x", (int)c) );
            } else {
                stringBuffer.append(c);
            }
        }
        return stringBuffer.toString();
    }
}
