package com.meetcity.moon.example;

import com.meetcity.moon.spider.loginSpider.MoonRequest;
import com.meetcity.moon.spider.loginSpider.RequestChain;
import com.meetcity.moon.util.DateUtil;
import com.meetcity.moon.util.JSUtil;
import com.meetcity.moon.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by moon on 2018/3/8 .
 */
@Slf4j
public class UACHelper3 {

    private static String jsPath = "core/src/main/resources/js/uac.js";
    private static String phoneNumber = "13121650833";
    private Map<String, String> cookiesMap = new HashMap<>();


    public void start() {


        String time = String.valueOf(DateUtil.getTimeStamp10());
        String configId = getConfigId();
        String cit = JSUtil.executeScript(jsPath, "uuid");
        String ext = JSUtil.executeScript(jsPath, "buildExt");


        cookiesMap.put("_n3fa_cit", cit);
        cookiesMap.put("_n3fa_ext", ext);
        cookiesMap.put("_n3fa_lpvt_" + configId, time);
        cookiesMap.put("_n3fa_lvt_" + configId, time);
        RequestChain requestChain = new RequestChain();
        requestChain.initCookie(cookiesMap);

        MoonRequest unisecid = new MoonRequest() {
            @Override
            public Object process(Object downloadResult, MoonRequest request) {
                return null;
            }
        }.setUrl("https://uac.10010.com/oauth2/genqr?timestamp=" + DateUtil.getTimeStamp())
                .addHeader("Host", "uac.10010.com")
                .addHeader("Referer", "http://uac.10010.com/")
                .load(requestChain);

        MoonRequest checkRelease = new MoonRequest() {
            @Override
            public Object process(Object downloadResult, MoonRequest request) {
                return null;
            }
        }.setUrl("http://uac.10010.com/portal/Service/checkRelease?callback=jQuery17205582105852022494_"
                + DateUtil.getTimeStamp() + "&_=" + DateUtil.getTimeStamp())
                .commonHeader()
                .addHeader("Host", "uac.10010.com")
                .addHeader("Referer", "http://uac.10010.com/")
                .load(requestChain);

        StringBuilder param = new StringBuilder();
        param.append("callback=jQuery172022155537663198466_" + DateUtil.getTimeStamp());
        param.append("&userName=" + phoneNumber);
        param.append("&pwdType=02");
        param.append("&_=" + DateUtil.getTimeStamp());
        MoonRequest checkNeedVerify = new MoonRequest() {
            @Override
            public Object process(Object downloadResult, MoonRequest request) {
                return null;
            }
        }.setUrl("https://uac.10010.com/portal/Service/CheckNeedVerify?" + param)
                .addHeader("Host", "uac.10010.com")
                .addHeader("Referer", "http://uac.10010.com/portal/homeLogin")
                .load(requestChain);


        StringBuilder paramMSG = new StringBuilder();
        paramMSG.append("callback=jQuery172022155537663198466_" + DateUtil.getTimeStamp());
        paramMSG.append("&req_time=" + DateUtil.getTimeStamp());
        paramMSG.append("&mobile=" + phoneNumber);
        paramMSG.append("&_=" + DateUtil.getTimeStamp());
        MoonRequest sendMsg = new MoonRequest() {
            @Override
            public Object process(Object downloadResult, MoonRequest request) {
                System.out.println("请出入短信验证码");
                Scanner scanner = new Scanner(System.in);
                String msg = scanner.nextLine();
                return msg;
            }
        }.setUrl("https://uac.10010.com/portal/Service/SendMSG?" + paramMSG.toString())
                .addHeader("Host", "uac.10010.com")
                .addHeader("Referer", "http://uac.10010.com/portal/homeLogin")
                .load(requestChain);


        MoonRequest mallLogin = new MoonRequest() {
            @Override
            public Object process(Object downloadResult, MoonRequest request) {
                return null;
            }
        }.addHeader("Host", "uac.10010.com")
                .addHeader("Referer", "http://uac.10010.com/portal/homeLogin")
                .load(requestChain);
        StringBuilder mallLoginParam = new StringBuilder();
        mallLoginParam.append("callback=jQuery172022155537663198466_" + DateUtil.getTimeStamp());
        mallLoginParam.append("&req_time=" + DateUtil.getTimeStamp());
        mallLoginParam.append("&redirectURL=" + "http://www.10010.com");
        mallLoginParam.append("&userName=" + phoneNumber);
        mallLoginParam.append("&password=" + String.valueOf(mallLogin.getLastResult()));
        mallLoginParam.append("&pwdType=" + "02");
        mallLoginParam.append("&productType=" + "01");
        mallLoginParam.append("&redirectType=" + "01");
        mallLoginParam.append("&rememberMe=" + "1");
        mallLoginParam.append("&_=" + DateUtil.getTimeStamp());

        mallLogin.setUrl("https://uac.10010.com/portal/Service/MallLogin?" + mallLoginParam.toString());


        MoonRequest checkLogin = new MoonRequest() {
            @Override
            public Object process(Object downloadResult, MoonRequest request) {
                log.info("baseInfo: {}", String.valueOf(downloadResult));
                return "haha";
            }
        }.setUrl("http://www.10010.com/mall/service/check/checklogin/?_=" + DateUtil.getTimeStamp())
                .addHeader("Host", "www.10010.com")
                .addHeader("Origin", "http://www.10010.com")
                .addHeader("Referer", "http://www.10010.com/net5/011/")
                .addParam("jutThird", requestChain.getCookieMap().get("JUT"))
                .load(requestChain);



        requestChain.execute();
    }


    private String getConfigId() {
        String configId = null;

        Map<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        header.put("Accept-Encoding", "Accept-Encoding");
        header.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        header.put("Host", "res.mall.10010.com");
        header.put("Referer", "http://uac.10010.com/");
        header.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");

        try {
            Response response = OkHttpUtil.get("http://res.mall.10010.com/mall/common/js/fa.js?referer=http://uac.10010.com", header);
            String script = response.body().string();
            String funConfig = script.substring(script.indexOf("_config"), script.indexOf("}"));
            configId = funConfig.substring(funConfig.indexOf("id:"), funConfig.indexOf(","))
                    .replace("id:", "")
                    .replace("\"", "");
            System.out.println("configId: " + configId);

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return configId;
    }
}
