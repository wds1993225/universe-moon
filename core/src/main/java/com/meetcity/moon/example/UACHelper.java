package com.meetcity.moon.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meetcity.moon.spider.loginSpider.RequestChain;
import com.meetcity.moon.util.DateUtil;
import com.meetcity.moon.util.JSUtil;
import com.meetcity.moon.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.util.*;

/**
 * Created by moon on 2018/2/27 .
 * <p>
 * 1,请求前先进行基础cookie的组合
 * <p>
 * 包括
 * 由js生成：_n3fa_cit,_n3fa_ext
 * 由代码生成 _n3fa_lpvt_*，_n3fa_lvt_* , * 由代码中获取的字符进行替换
 * <p>
 * <p>
 * 2,执行预先的请求
 * A，（http://uac.10010.com/） 获取参数中的configId {@link #getConfigId()}
 * B，（https://uac.10010.com/oauth2/genqr?timestamp=1519801625286)  获取unisecid {@link #getUnisecid(Map)}
 * C，（http://uac.10010.com/portal/Service/checkRelease?）{@link #checkRelease(Map)}
 * D, (https://uac.10010.com/portal/Service/CheckNeedVerify) 获取ckuuid {@link #checkNeedVerifyAndGetCkuuid(Map)}
 * E, (http://uac.10010.com/portal/Service/CreateImage?t=) 获取uacverifykey {@link #createImageAndUacVerifykey(Map)}
 * <p>
 * 3,发送短信
 * <p>
 * (https://uac.10010.com/portal/Service/SendCkMSG) {@link #sendSMS(Map)}
 * <p>
 * 4,登陆
 */

@Slf4j
public class UACHelper {

    private static String jsPath = "core/src/main/resources/js/uac.js";
    //private static String phoneNumber = "18535722009";
    //private static String password = "789512";

    //private static String phoneNumber = "18518981371";
    //private static String password = "442286";

    private static String phoneNumber = "13121650833";
    private static String password = "220031";


    private Map<String, String> cookiesMap = new HashMap<>();


    public void start() {

        String time = String.valueOf(DateUtil.getTimeStamp10());
        String configId = getConfigId();
        String cit = JSUtil.executeScript(jsPath, "uuid");
        String ext = JSUtil.executeScript(jsPath, "buildExt");

        StringBuilder cookie = new StringBuilder();
        cookie.append("_n3fa_cit=" + cit + ";");
        cookie.append("_n3fa_ext=" + ext + ";");
        cookie.append("_n3fa_lpvt_" + configId + "=" + time + ";");
        cookie.append("_n3fa_lvt_" + configId + "=" + time + ";");

        cookiesMap.put("_n3fa_cit", cit);
        cookiesMap.put("_n3fa_ext", ext);
        cookiesMap.put("_n3fa_lpvt_" + configId, time);
        cookiesMap.put("_n3fa_lvt_" + configId, time);

//        cookie.put("_n3fa_cit", cit);
//        cookie.put("_n3fa_ext", ext);
//        cookie.put("_n3fa_lpvt_" + configId, time);
//        cookie.put("_n3fa_lvt_" + configId, time);
        //Map<String, String> headers = convertCookiesToOkHeader(cookie);
        Map<String, String> headers = combineHeader("cookie", cookie.toString());

        String unisecid = getUnisecid(headers);
        cookie.append("unisecid=" + unisecid + ";");
        headers = combineHeader("cookie", cookie.toString());
        checkRelease(headers);
        String ckuuid = checkNeedVerifyAndGetCkuuid(headers);
        cookie.append("ckuuid=" + ckuuid + ";");
        headers = combineHeader("cookie", cookie.toString());
        String uacverifykey = createImageAndUacVerifykey(headers);
        cookie.append("uacverifykey=" + uacverifykey + ";");
        headers = combineLoginHeader("cookie", cookie.toString());
        sendSMS(headers);
        System.out.println("请出入短信验证码");
        Scanner scanner = new Scanner(System.in);
        String smg = scanner.nextLine();
        String loginKey = loginIn(smg, headers);
        cookie.append(loginKey);
        combineInfoHeaderCookie(cookie);
        headers = combineInfoHeader2("cookie", cookie.toString());
        String jut = getParamFromCookie("JUT", cookie.toString());
        getBaseInfo2(jut, headers);
        headers = combineInfoHeaderForE3("cookie", cookie.toString());
        String e3AndRoute = getBaseInfo3(jut, headers);

        cookie.append(e3AndRoute);
        cookie.append("MII=000100020001;");
        //headers = combineE3Header("cookie", cookie.toString());
        //String e3 = getE32(headers);
        //cookie.append("e3=" + e3 + ";");
        headers = combineHistoryHeader("cookie", cookie.toString());
        List<String> accountList = getHistoryAccount(headers);
        getHistoryBill(accountList, headers);
        headers = combineCallingHeader("cookie", cookie.toString());
        getCalling(headers);


    }


    public void moon() {


        String time = String.valueOf(DateUtil.getTimeStamp10());
        String configId = getConfigId();
        String cit = JSUtil.executeScript(jsPath, "uuid");
        String ext = JSUtil.executeScript(jsPath, "buildExt");

        StringBuilder cookie = new StringBuilder();
        cookie.append("_n3fa_cit=" + cit + ";");
        cookie.append("_n3fa_ext=" + ext + ";");
        cookie.append("_n3fa_lpvt_" + configId + "=" + time + ";");
        cookie.append("_n3fa_lvt_" + configId + "=" + time + ";");

        //headers = combineLoginHeader("cookie", cookie.toString());

        RequestChain requestChain = new RequestChain();
        Map<String, String> firstHead = requestChain.getDefaultHeader(null
                , "uac.10010.com", "http://uac.10010.com/", cookie.toString());
/*

        requestChain.addRequest(OkHttpUtil.getRequest("https://uac.10010.com/oauth2/genqr?timestamp="
                + getTimeStamp(), firstHead));

        requestChain.addRequest(OkHttpUtil.getRequest("http://uac.10010.com/portal/Service/checkRelease?callback=jQuery17204178198980987604_"
                + DateUtil.getTimeStamp() + "&_=" + DateUtil.getTimeStamp(), firstHead));


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("callback=jQuery17204214728109522312_" + DateUtil.getTimeStamp());
        stringBuilder.append("&userName=" + phoneNumber);
        stringBuilder.append("&pwdType=01");
        stringBuilder.append("&_=" + DateUtil.getTimeStamp());
        requestChain.addRequest(OkHttpUtil.getRequest("https://uac.10010.com/portal/Service/CheckNeedVerify?"
                + stringBuilder.toString(), firstHead));

        requestChain.addRequest(OkHttpUtil.getRequest("http://uac.10010.com/portal/Service/CreateImage?t="
                + DateUtil.getTimeStamp(), firstHead));

        requestChain.execute();
*/

        System.out.println("");
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


    private String getUnisecid(Map<String, String> headers) {
        String unisecid = null;

        try {
            Response response = OkHttpUtil.get("https://uac.10010.com/oauth2/genqr?timestamp=" + getTimeStamp(),
                    headers);
            String cookieRaw = response.header("Set-Cookie");
            if (cookieRaw != null && cookieRaw.contains(";")) {
                String[] cookies = cookieRaw.split(";");
                for (String cookie : cookies) {
                    if (cookie.contains("unisecid")) {
                        unisecid = cookie.replace("unisecid=", "");
                    }
                }
            } else {
                log.warn("获取的请求头中没有cookie，无法获取unisecid");
            }
            System.out.println("unisecid: " + unisecid);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return unisecid;
    }


    private void checkRelease(Map<String, String> header) {

        try {
            Response response = OkHttpUtil.get(
                    "http://uac.10010.com/portal/Service/checkRelease?callback=jQuery17204178198980987604_" + DateUtil.getTimeStamp() + "&_=" + DateUtil.getTimeStamp(), header);

            String result = response.body().string();
            System.out.println("checkRelease: " + result);
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    private String checkNeedVerifyAndGetCkuuid(Map<String, String> header) {

        String ckuuid = null;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("callback=jQuery17204214728109522312_" + DateUtil.getTimeStamp());
        stringBuilder.append("&userName=" + phoneNumber);
        stringBuilder.append("&pwdType=01");
        stringBuilder.append("&_=" + DateUtil.getTimeStamp());
        try {
            Response response = OkHttpUtil.get("https://uac.10010.com/portal/Service/CheckNeedVerify?" + stringBuilder.toString(), header);
            String cookieRaw = response.header("Set-Cookie");

            if (cookieRaw != null && cookieRaw.contains(";")) {
                String[] cookies = cookieRaw.split(";");
                for (String cookie : cookies) {
                    if (cookie.contains("ckuuid")) {
                        ckuuid = cookie.replace("ckuuid=", "");
                    }
                }
            } else {
                log.warn("获取的请求头中没有cookie，无法获取unisecid");
            }
            String result = response.body().string();

            System.out.println("ckuuid: " + ckuuid);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return ckuuid;
    }

    private String createImageAndUacVerifykey(Map<String, String> header) {

        String uacVerifyKey = null;
        try {
            Response response = OkHttpUtil.get("http://uac.10010.com/portal/Service/CreateImage?t=" + DateUtil.getTimeStamp(), header);
            String cookieRaw = response.header("Set-Cookie");

            if (cookieRaw != null && cookieRaw.contains(";")) {
                String[] cookies = cookieRaw.split(";");
                for (String cookie : cookies) {
                    if (cookie.contains("uacverifykey")) {
                        uacVerifyKey = cookie.replace("uacverifykey=", "");
                    }
                }
            } else {
                log.warn("获取的请求头中没有cookie，无法获取unisecid");
            }
            String result = response.body().string();
            System.out.println("uacVerifyKey: " + uacVerifyKey);

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return uacVerifyKey;
    }


    /**
     * 发送短信
     */
    private void sendSMS(Map<String, String> header) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DateUtil.getTimeStamp());
        stringBuilder.append("&req_time=" + DateUtil.getTimeStamp());
        stringBuilder.append("&mobile=" + phoneNumber);
        stringBuilder.append("&_=" + DateUtil.getTimeStamp());
        try {
            Response response = OkHttpUtil.get("https://uac.10010.com/portal/Service/SendCkMSG?callback=jQuery17204214728109522312_" + stringBuilder.toString()
                    , header);
            String result = response.body().string();
            if (result.contains("7098")) {
                System.out.println("系统忙，请稍后再试");
                System.out.println("当日随机码发送上限");
                System.exit(0);
            }
            System.out.println("sms:" + result);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    /**
     * 登陆
     * <p>
     * 返回新的三个cookie
     * <p>
     * JUT
     * _uop_id
     * piw
     */
    private String loginIn(String smsNumber, Map<String, String> header) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DateUtil.getTimeStamp());
        stringBuilder.append("&req_time=" + DateUtil.getTimeStamp());
        stringBuilder.append("&redirectURL=http://www.10010.com");
        stringBuilder.append("&userName=" + phoneNumber);
        stringBuilder.append("&password=" + password);
        stringBuilder.append("&pwdType=01&productType=01&redirectType=01&rememberMe=1");
        stringBuilder.append("&verifyCKCode=" + smsNumber);
        stringBuilder.append("&_=" + DateUtil.getTimeStamp());

        StringBuilder verifyCookie = new StringBuilder();
        try {
            Response response = OkHttpUtil.get("https://uac.10010.com/portal/Service/MallLogin?callback=jQuery17204214728109522312_" + stringBuilder.toString(), header);
            //String cookieRaw = response.header("Set-Cookie");
            List<String> cookieList = response.headers("Set-Cookie");

            if (cookieList != null) {

                for (String setCookie : cookieList) {
                    String[] cookies = setCookie.split(";");
                    for (String cookie : cookies) {
                        if (cookie.contains("JUT")) {
                            String JUT = cookie.replace("JUT=", "");
                            verifyCookie.append("JUT=" + JUT + ";");
                        }
                        if (cookie.contains("_uop_id")) {
                            String _uop_id = cookie.replace("_uop_id=", "");
                            verifyCookie.append("_uop_id=" + _uop_id + ";");
                        }
                        if (cookie.contains("piw")) {
                            String piw = cookie.replace("piw=", "");
                            verifyCookie.append("piw=" + piw + ";");
                        }
                    }
                }

            } else {
                log.warn("获取的请求头中没有cookie，无法获");
            }

            String result = response.body().string();
            System.out.println("verifyCookie: " + verifyCookie.toString());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return verifyCookie.toString();
    }


    /**
     * 返回一个cookie中的参数JUT
     */
    private String getJutAndMallLogin(Map<String, String> header) {
        StringBuilder jut = new StringBuilder();
        try {
            Response response = OkHttpUtil.get("http://uac.10010.com/portal/hallLogin");
            List<String> cookieList = response.headers("Set-Cookie");

            if (cookieList != null) {

                for (String setCookie : cookieList) {
                    String[] cookies = setCookie.split(";");
                    for (String cookie : cookies) {
                        if (cookie.contains("JUT")) {
                            String JUT = cookie.replace("JUT=", "");
                            jut.append("JUT=" + JUT + ";");
                        }
                    }
                }

            } else {
                log.warn("获取的请求头中没有cookie，无法获");
            }


        } catch (Throwable t) {
            t.printStackTrace();
        }
        return jut.toString();
    }


    /**
     * 获取基本信息
     */
    private void getBaseInfo(Map<String, String> header) {

        String param = String.valueOf(DateUtil.getTimeStamp());
        param = param + "&accessURL=http://iservice.10010.com/e4/query/basic/personal_xx_iframe.html?menuCode=000800010001";
        try {
            Response response = OkHttpUtil.post("http://iservice.10010.com/e3/static/transact/serviceOpenCloseQuery_15?_=" + param, null, header);
            String result = response.body().string();
            System.out.println("");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    /**
     * 获取基本信息
     */
    private String getBaseInfo2(String param, Map<String, String> header) {

        Map<String, String> paramMap = new HashMap<>();
        StringBuilder cookie = new StringBuilder();
        paramMap.put("jutThird", param);
        try {
            Response response = OkHttpUtil.post("http://www.10010.com/mall/service/check/checklogin/?_=" + DateUtil.getTimeStamp(), paramMap, header);
            String result = response.body().string();
            System.out.println("baseInfo" + result);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return cookie.toString();
    }

    /**
     * 获取基本信息
     */
    private String getBaseInfo3(String param, Map<String, String> header) {

        Map<String, String> paramMap = new HashMap<>();
        StringBuilder cookie = new StringBuilder();
        paramMap.put("jutThird", param);
        try {
            Response response = OkHttpUtil.post("http://iservice.10010.com/e3/static/check/checklogin/?_=" + DateUtil.getTimeStamp(), paramMap, header);
            String e3 = getParamFromCookies("e3", response);
            String route = getParamFromCookies("route", response);
            String result = response.body().string();
            cookie.append(e3);
            cookie.append(route);
            System.out.println("baseInfo" + result);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return cookie.toString();
    }

    /**
     * 获取账单月份
     *
     * @return 账单月份
     */
    private List<String> getHistoryAccount(Map<String, String> header) {

        List<String> accountList = new ArrayList<>();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("querytype", "0001");
        paramMap.put("querycode", "0001");
        paramMap.put("billdate", "");


        String param = String.valueOf(DateUtil.getTimeStamp()) + "&accessURL=http://iservice.10010.com/e4/skip.html?menuCode=000100020001&menuCode=000100020001&menuid=000100020001";
        try {
            Response response = OkHttpUtil.post("http://iservice.10010.com/e3/static/query/queryHistoryAccount?_=" + param, paramMap, header);
            String result = response.body().string();
            JSONObject resultObj = JSON.parseObject(result);
            JSONArray accountPeriod = resultObj.getJSONArray("accountPeriod");
            for (Object timeObj : accountPeriod) {
                String queryDate = JSON.parseObject(String.valueOf(timeObj)).getString("queryDate");
                accountList.add(queryDate);
            }
            for (String ha : accountList) {
                System.out.println("HistoryAccount: " + ha);
            }
            System.out.println("HistoryAccount: end");
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return accountList;
    }

    /**
     * 获取账单信息
     *
     * @param monthAccount 账单月份
     */
    private void getHistoryBill(List<String> monthAccount, Map<String, String> header) {

        String param = "&accessURL=http://iservice.10010.com/e4/skip.html?menuCode=000100020001&menuCode=000100020001&menuid=000100020001";
        try {
            for (String month : monthAccount) {
                Map<String, String> paramMap = new HashMap<>();
                paramMap.put("querytype", "0001");
                paramMap.put("querycode", "0001");
                paramMap.put("billdate", month);
                paramMap.put("flag", "1");
                Response response = OkHttpUtil.post("http://iservice.10010.com/e3/static/query/queryHistoryBill?_=" + String.valueOf(DateUtil.getTimeStamp()) + param, paramMap, header);
                String result = response.body().string();
                System.out.println("bill: " + result);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }


    /**
     * 获取账单时会校验
     */
    private String getE3(Map<String, String> header) {

        StringBuilder e3 = new StringBuilder();

        try {
            Response response = OkHttpUtil.get("http://iservice.10010.com/e3/static/common/mall_info?callback=jsonp" + DateUtil.getTimeStamp(), header);
            String result = response.body().string();

            List<String> cookieList = response.headers("Set-Cookie");


            if (cookieList != null) {

                for (String setCookie : cookieList) {
                    String[] cookies = setCookie.split(";");
                    for (String cookie : cookies) {
                        if (cookie.contains("e3")) {
                            String JUT = cookie.replace("e3=", "");
                            e3.append("e3=" + JUT + ";");
                        }
                        if (cookie.contains("route")) {
                            String _uop_id = cookie.replace("route=", "");
                            e3.append("route=" + _uop_id + ";");
                        }
                    }
                }

            } else {
                log.warn("获取的请求头中没有cookie，无法获");
            }


        } catch (Throwable t) {
            t.printStackTrace();
        }
        return e3.toString();
    }


    private String getE32(Map<String, String> header) {

        StringBuilder e3 = new StringBuilder();
        try {
            Response response = OkHttpUtil.post("http://iservice.10010.com/e3/static/query/nowDate?_=" + DateUtil.getTimeStamp(), null, header);
            List<String> cookieList = response.headers("Set-Cookie");

            if (cookieList != null) {

                for (String setCookie : cookieList) {
                    String[] cookies = setCookie.split(";");
                    for (String cookie : cookies) {
                        if (cookie.contains("e3")) {
                            String JUT = cookie.replace("e3=", "");
                            e3.append("e3=" + JUT + ";");
                        }
                        if (cookie.contains("route")) {
                            String _uop_id = cookie.replace("route=", "");
                            e3.append("route=" + _uop_id + ";");
                        }
                    }
                }

            } else {
                log.warn("获取的请求头中没有cookie，无法获");
            }
            System.out.println("e3：" + e3.toString());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return e3.toString();
    }


    /**
     * 这里获取通话详单信息
     * 在获取之前会需要短信验证码，后试验发现获取短信验证码只是界面层的要求，和实际内容请求并无联系
     */
    private void getCalling(Map<String, String> header) {

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("pageNo", "1");
        paramMap.put("pageSize", "20");
        paramMap.put("beginDate", "20180301");
        paramMap.put("endDate", "20180304");

        String url = "http://iservice.10010.com/e3/static/query/callDetail?_=" + DateUtil.getTimeStamp() + "&accessURL=http://iservice.10010.com/e4/query/bill/call_dan-iframe.html&menuid=000100030001";
        try {
            Response response = OkHttpUtil.post(url, paramMap, header);
            String result = response.body().string();
            System.out.println("bill: " + result);
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }


    /**
     * 获取时间戳
     * 10位的
     */
    private String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }


    /**
     * 将请求map类型的cookie转化成okHttp3中需要的请求头类型
     */
    private Map<String, String> convertCookiesToOkHeader(Map<String, String> cookie) {
        Map<String, String> headers = new HashMap<>();       //请求头
        StringBuilder headerCookies = new StringBuilder();       //cookie
        for (Map.Entry<String, String> entry : cookie.entrySet()) {
            headerCookies.append(entry.getKey() + "=" + entry.getValue() + ";");
        }

        headers.put("cookie", headerCookies.toString());
        return headers;
    }

    private Map<String, String> combineHeader(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
        //map.put("Accept-Encoding", "gzip, deflate");
        map.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        map.put("Host", "uac.10010.com");
        map.put("Referer", "http://uac.10010.com/");
        map.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
        map.put("X-Requested-With", "XMLHttpRequest");
        map.put(key, value);
        return map;

    }

    private Map<String, String> combineLoginHeader(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
        //map.put("Accept-Encoding", "gzip, deflate");
        map.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        map.put("Host", "uac.10010.com");
        map.put("Referer", "http://uac.10010.com/portal/homeLogin");
        map.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
        map.put("X-Requested-With", "XMLHttpRequest");
        map.put(key, value);
        return map;
    }

    private Map<String, String> combineInfoHeader(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
        //map.put("Accept-Encoding", "gzip, deflate");
        map.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        map.put("Host", "iservice.10010.com");
        map.put("Referer", "http://iservice.10010.com/e4/query/basic/personal_xx.html?menuId=");
        map.put("Origin", "http://iservice.10010.com");
        map.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
        map.put("X-Requested-With", "XMLHttpRequest");
        map.put(key, value);
        return map;
    }

    private Map<String, String> combineInfoHeader2(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json, text/javascript, */*; q=0.01");
        //map.put("Accept-Encoding", "gzip, deflate");
        map.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        map.put("Host", "www.10010.com");
        map.put("Referer", "http://www.10010.com/net5/011/");
        map.put("Origin", "http://www.10010.com");
        map.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
        map.put("X-Requested-With", "XMLHttpRequest");
        map.put(key, value);
        return map;
    }


    private Map<String, String> combineInfoHeaderForE3(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json, text/javascript, */*; q=0.01");
        //map.put("Accept-Encoding", "gzip, deflate");
        map.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        map.put("Host", "iservice.10010.com");
        map.put("Referer", "http://iservice.10010.com/e4/skip.html?menuCode=000100020001");
        map.put("Origin", "http://iservice.10010.com");
        map.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
        map.put("X-Requested-With", "XMLHttpRequest");
        map.put(key, value);
        return map;
    }


    private void combineInfoHeaderCookie(StringBuilder cookies) {
        cookies.append("citycode=110;");
        cookies.append("mallcity=11|110;");
        cookies.append("mallflag=0;");
        cookies.append("loginflag=true;");
        cookies.append("userprocode=011;");

    }

    private Map<String, String> combineE3Header(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json, text/javascript, */*; q=0.01");
        //map.put("Accept-Encoding", "gzip, deflate");
        map.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        map.put("Host", "iservice.10010.com");
        map.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        map.put("Referer", "http://iservice.10010.com/e4/");
        map.put("Origin", "http://iservice.10010.com");
        map.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
        map.put("X-Requested-With", "XMLHttpRequest");
        map.put(key, value);
        return map;

    }

    private Map<String, String> combineHistoryHeader(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json, text/javascript, */*; q=0.01");
        //map.put("Accept-Encoding", "gzip, deflate");
        map.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        map.put("Host", "iservice.10010.com");
        map.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        map.put("Referer", "http://iservice.10010.com/e4/query/basic/history_list.html?menuId=000100020001");
        map.put("Origin", "http://iservice.10010.com");
        map.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
        map.put("X-Requested-With", "XMLHttpRequest");
        map.put(key, value);
        return map;
    }

    private Map<String, String> combineCallingHeader(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json, text/javascript, */*; q=0.01");
        //map.put("Accept-Encoding", "gzip, deflate");
        map.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        map.put("Host", "iservice.10010.com");
        map.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        map.put("Referer", "http://iservice.10010.com/e4/query/bill/call_dan-iframe.html");
        map.put("Origin", "http://iservice.10010.com");
        map.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
        map.put("X-Requested-With", "XMLHttpRequest");
        if (key.equals("cookie")) {
            value = value.replace("000100020001", "000100030001");
        }
        map.put(key, value);
        return map;
    }


    private String getParamFromCookie(String paramName, String cookies) {
        String paramValue = null;
        String[] cookieArry = cookies.split(";");
        for (String cookie : cookieArry) {
            if (cookie.contains(paramName)) {
                paramValue = cookie.replace(paramName + "=", "");
            }
        }
        return paramValue;
    }


    /**
     * 将Map类型的cookies转化成String类型的cookies
     */
    private String cookieMapToString(Map<String, String> cookiesMap) {
        StringBuilder cookiesString = new StringBuilder();
        if (cookiesMap != null) {
            for (Map.Entry<String, String> entry : cookiesMap.entrySet()) {
                cookiesString.append(entry.getKey() + "=" + entry.getValue() + ";");
            }
        }
        return cookiesString.toString();
    }

    /**
     * 将String 类型的cookies 转化成Map类型的cookies
     */
    private Map<String, String> cookieStringToMap(String cookiesString) {
        String[] cookieArray = cookiesString.split(";");
        Map<String, String> cookiesMap = new HashMap<>();
        for (String cookie : cookieArray) {
            String[] co = cookie.split("=");
            cookiesMap.put(co[0], co[1]);
        }
        return cookiesMap;
    }

    private String getParamFromCookies(String key, Response response) {

        StringBuilder stringBuilder = new StringBuilder();
        if (response != null) {
            List<String> cookieList = response.headers("Set-Cookie");

            if (cookieList != null) {

                for (String setCookie : cookieList) {
                    String[] cookies = setCookie.split(";");
                    for (String cookie : cookies) {
                        if (cookie.contains(key)) {
                            String a = cookie.replace(key + "=", "");
                            stringBuilder.append(key + "=" + a + ";");
                        }
                    }
                }
            } else {
                log.warn("获取的请求头中没有cookie，无法获");
            }

        } else {
            log.error("response is null");
        }
        return stringBuilder.toString();

    }
}
