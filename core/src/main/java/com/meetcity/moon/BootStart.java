package com.meetcity.moon;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meetcity.moon.example.UACHelper;
import com.meetcity.moon.spider.SpiderCore;
import com.meetcity.moon.spider.download.HttpDownloader;
import com.meetcity.moon.spider.schedule.MoonTask;
import com.meetcity.moon.util.JSUtil;
import com.meetcity.moon.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import redis.RedisUtil;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class BootStart {

    public static String getMonthStart(Calendar date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        date.set(Calendar.DAY_OF_MONTH, 1);
        return format.format(date.getTime());
    }

    public static List<String> getMonthStartDayEndCurrent(final int count) {
        List<String> list = new ArrayList<>();
        Calendar cal1 = Calendar.getInstance();
        for (int i = 0; i < count; ++i) {
            list.add(getMonthStart(cal1));
            cal1.add(Calendar.MONTH, -1);
        }
        return list;
    }

    public static void main11111(String[] args) {

        MoonTask moonParam = new MoonTask("http://www.baidu.com");
        SpiderCore spiderCore = new SpiderCore(moonParam, new HttpDownloader(), null, null);
        spiderCore.start();
    }

    public static void main(String[] args) {
        int afsf = (384) & 64;
        System.out.println();

        List<String> list = getMonthStartDayEndCurrent(6);
        for(int i = 0;i<list.size() ;i++){
            System.out.println(list.get(i));
        }
        System.out.println("");
        String a = RedisUtil.getString("haha");
        System.out.println("");

        String bb = "{\"attachment\":[{\"name\":\"startTimeReal_Ch_zh\",\"value\":\"2018年4月1日\"},{\"name\":\"endTimeReal_Ch_zh\",\"value\":\"4月4日\"},{\"name\":\"nowReal\",\"value\":\"2018年4月4日\"},{\"name\":\"yearmonthlist\",\"value\":[{\"name\":\"11\",\"value\":\"201711\"},{\"name\":\"12\",\"value\":\"201712\"},{\"name\":\"1\",\"value\":\"201801\"},{\"name\":\"2\",\"value\":\"201802\"},{\"name\":\"3\",\"value\":\"201803\"},{\"name\":\"4\",\"value\":\"201804\"}]},{\"name\":\"selected\",\"value\":\"201804\"},{\"name\":\"startTimeReal\",\"value\":\"20180401000000\"},{\"name\":\"endTimeReal\",\"value\":\"20180404151434\"},{\"name\":\"isChange\",\"value\":\"\"},{\"name\":\"cityCode\",\"value\":\"DG\"}],\"content\":{\"leftTimes\":\"-2\",\"successful\":false,\"totalTimes\":\"5\"},\"type\":\"initPage\"}";
        JSONObject jb = JSON.parseObject(bb);
        JSONObject aaa = jb.getJSONObject("content");
        if (aaa != null) {
            if ("false".equals(aaa.getString("successful"))) {
                System.out.println("");
            }
            ;
        }
        System.out.println("");

    }


    public static void main11(String[] args) throws Exception {


        UACHelper helper = new UACHelper();
        helper.start();
        try {
            Response response = OkHttpUtil.get("https://uac.10010.com/portal/Service/CheckNeedVerify?callback=jQuery1720759029309307317_1519737698574&userName=13121650833&pwdType=01&_=1519737713443");
            String result = response.body().string();
            System.out.printf("");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


}
