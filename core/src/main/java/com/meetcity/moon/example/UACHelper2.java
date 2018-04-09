package com.meetcity.moon.example;

import com.meetcity.moon.util.DateUtil;
import com.meetcity.moon.util.JSUtil;
import com.meetcity.moon.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.util.*;

/**
 * 请求中pwdType为02
 */
@Slf4j
public class UACHelper2 {


    private static String jsPath = "core/src/main/resources/js/uac.js";


    private static String phoneNumber = "00";
    //private static String password = "00";


    private Map<String, String> cookiesMap = new HashMap<>();


    public void start() {

        String time = String.valueOf(DateUtil.getTimeStamp10());
        String configId = getConfigId();
        System.out.println("");


        String cit = JSUtil.executeScript(jsPath, "uuid");
        String ext = JSUtil.executeScript(jsPath, "buildExt");


        cookiesMap.put("_n3fa_cit", cit);
        cookiesMap.put("_n3fa_ext", ext);
        cookiesMap.put("_n3fa_lpvt_" + configId, time);
        cookiesMap.put("_n3fa_lvt_" + configId, time);
        getUnisecid();
        checkRelease();
        checkNeedVerify();
        sendMsg();
        System.out.println("请出入短信验证码");
        Scanner scanner = new Scanner(System.in);
        String msg = scanner.nextLine();
        mallLogin(msg);
        checkLogin();       //获取基本信息
        //nowDate();
        //getHistoryBill();
        queryUserInfo();

        //------------开始第二页
        //cookiesMap.put("mallflag", "null");
        hallLoginForExchangetJut();
        checkLoginForE3();
        queryHistoryAccount();
        //getHistoryBill();
        getCalling();


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


    private void getUnisecid() {
        String unisecid = null;
        Map<String, String> header = getCommonHeader();
        header.put("Host", "uac.10010.com");
        header.put("Referer", "http://uac.10010.com/");

        try {
            Response response = OkHttpUtil.get("https://uac.10010.com/oauth2/genqr?timestamp=" + DateUtil.getTimeStamp(),
                    header);
            String cookieRaw = response.header("Set-Cookie");
            if (cookieRaw != null && cookieRaw.contains(";")) {
                String[] cookies = cookieRaw.split(";");
                for (String cookie : cookies) {
                    if (cookie.contains("unisecid")) {
                        unisecid = cookie.replace("unisecid=", "");
                        cookiesMap.put("unisecid", unisecid);
                        log.info("cookie has increased unisecid : {}", unisecid);
                    }
                }
            } else {
                log.warn("获取的请求头中没有cookie，无法获取unisecid");
            }
            //System.out.println("unisecid: " + unisecid);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.out.println("");

    }

    private void checkRelease() {
        String url = "http://uac.10010.com/portal/Service/checkRelease?callback=jQuery17205582105852022494_"
                + DateUtil.getTimeStamp() + "&_=" + DateUtil.getTimeStamp();

        Map<String, String> header = getCommonHeader();
        header.put("Host", "uac.10010.com");
        header.put("Referer", "http://uac.10010.com/");
        try {
            Response response = OkHttpUtil.get(url, header);
            checkResponse("checkRelease", response);
            System.out.println();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void checkNeedVerify() {
        StringBuilder param = new StringBuilder();
        param.append("callback=jQuery172022155537663198466_" + DateUtil.getTimeStamp());
        param.append("&userName=" + phoneNumber);
        param.append("&pwdType=02");
        param.append("&_=" + DateUtil.getTimeStamp());
        String url = "https://uac.10010.com/portal/Service/CheckNeedVerify?";

        Map<String, String> header = getCommonHeader();
        header.put("Host", "uac.10010.com");
        header.put("Referer", "http://uac.10010.com/portal/homeLogin");

        try {
            Response response = OkHttpUtil.get(url + param.toString(), header);
            checkResponse("checkNeedVerify", response);
            System.out.println("");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    /**
     * 发送短信
     */
    private void sendMsg() {
        StringBuilder param = new StringBuilder();
        param.append("callback=jQuery172022155537663198466_" + DateUtil.getTimeStamp());
        param.append("&req_time=" + DateUtil.getTimeStamp());
        param.append("&mobile=" + phoneNumber);
        param.append("&_=" + DateUtil.getTimeStamp());
        String url = "https://uac.10010.com/portal/Service/SendMSG?";

        Map<String, String> header = getCommonHeader();
        header.put("Host", "uac.10010.com");
        header.put("Referer", "http://uac.10010.com/portal/homeLogin");

        try {
            Response response = OkHttpUtil.get(url + param.toString(), header);
            checkResponse("sendMsg", response);
            System.out.println("");
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    private void mallLogin(String msg) {
        StringBuilder param = new StringBuilder();
        param.append("callback=jQuery172022155537663198466_" + DateUtil.getTimeStamp());
        param.append("&req_time=" + DateUtil.getTimeStamp());
        param.append("&redirectURL=" + "http://www.10010.com");
        param.append("&userName=" + phoneNumber);
        param.append("&password=" + msg);
        param.append("&pwdType=" + "02");
        param.append("&productType=" + "01");
        param.append("&redirectType=" + "01");
        param.append("&rememberMe=" + "1");
        param.append("&_=" + DateUtil.getTimeStamp());

        String url = "https://uac.10010.com/portal/Service/MallLogin?";

        Map<String, String> header = getCommonHeader();
        header.put("Host", "uac.10010.com");
        header.put("Referer", "http://uac.10010.com/portal/homeLogin");
        try {
            Response response = OkHttpUtil.get(url + param.toString(), header);

            List<String> keyList = Arrays.asList("JUT", "_uop_id", "piw", "ckuuid");
            Map<String, String> map = getCookiesFromResponse(response, keyList);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                cookiesMap.put(entry.getKey(), entry.getValue());
            }
            checkResponse("mallLogin", response);
            System.out.println("");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 获取基本信息
     */
    private void checkLogin() {
        String url = "http://www.10010.com/mall/service/check/checklogin/?_=" + DateUtil.getTimeStamp();
        Map<String, String> header = getCommonHeader();
        header.put("Host", "www.10010.com");
        header.put("Origin", "http://www.10010.com");
        header.put("Referer", "http://www.10010.com/net5/011/");

        Map<String, String> param = new HashMap<>();
        param.put("jutThird", cookiesMap.get("JUT"));

        try {
            Response response = OkHttpUtil.post(url, param, header);
            checkResponse("checkLogin", response);
            List<String> list = Arrays.asList("mall", "route");
            Map<String, String> map = getCookiesFromResponse(response, list);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                cookiesMap.put(entry.getKey(), entry.getValue());
            }
            System.out.println("");
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }


    /**
     * 获取e3的参数
     */
    private void nowDate() {
        String url = "http://iservice.10010.com/e3/static/query/nowDate?_=" + DateUtil.getTimeStamp();
        Map<String, String> header = getCommonHeader();
        header.put("Host", "iservice.10010.com");
        header.put("Origin", "http://iservice.10010.com");
        header.put("Referer", "http://iservice.10010.com/e4/");
        try {
            Response response = OkHttpUtil.post(url, null, header);
            List<String> keyList = Arrays.asList("route", "e3");
            Map<String, String> map = getCookiesFromResponse(response, keyList);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                cookiesMap.put(entry.getKey(), entry.getValue());
            }
            checkResponse("nowDate", response);
            System.out.println("");
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }


    /**
     * 测试，使用e3 cookie获取基本信息
     */
    private void searchPerInfo() {
        String url = "http://iservice.10010.com/e3/static/query/searchPerInfo/?_=" + DateUtil.getTimeStamp();
        Map<String, String> header = getCommonHeader();
        header.put("Origin", "http://iservice.10010.com");
        header.put("Referer", "http://iservice.10010.com/e4/index_server.html");
        header.put("Host", "iservice.10010.com");

        try {
            Response response = OkHttpUtil.post(url, null, header);
            checkResponse("searchPerInfo", response);
            System.out.println("");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    /**
     * 查询历史记录数量
     */
    private void queryHistoryAccount() {
        String param = "&accessURL=http://iservice.10010.com/e4/skip.html?menuCode=000100020001&menuCode=000100020001&menuid=000100020001";
        String url = "http://iservice.10010.com/e3/static/query/queryHistoryAccount?_=" + DateUtil.getTimeStamp();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("querytype", "0001");
        paramMap.put("querycode", "0001");
        paramMap.put("billdate", "");

        Map<String, String> header = getCommonHeader();
        header.put("Host", "iservice.10010.com");
        header.put("Origin", "http://iservice.10010.com");
        header.put("Referer", "http://iservice.10010.com/e4/query/basic/history_list.html?menuId=000100020001");

        try {
            Response response = OkHttpUtil.post(url + param, paramMap, header);
            checkResponse("queryHistoryAccount", response);
            System.out.println("");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    /**
     * 获取账单信息
     */
    private void getHistoryBill() {

        String param = "&accessURL=http://iservice.10010.com/e4/skip.html?menuCode=000100020001&menuCode=000100020001&menuid=000100020001";
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("querytype", "0001");
            paramMap.put("querycode", "0001");
            paramMap.put("billdate", "201801");
            paramMap.put("flag", "1");


            Map<String, String> header = getCommonHeader();
            header.put("Host", "iservice.10010.com");
            header.put("Referer", "http://iservice.10010.com/e4/query/basic/history_list.html?menuId=000100020001");
            header.put("Origin", "http://iservice.10010.com");

            Response response = OkHttpUtil.post("http://iservice.10010.com/e3/static/query/queryHistoryBill?_=" + String.valueOf(DateUtil.getTimeStamp()) + param, paramMap, header);
            String result = response.body().string();
            System.out.println("bill: " + result);
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    /**
     * 登陆用于交换 jut
     */
    private void hallLoginForExchangetJut() {
        String url = "http://uac.10010.com/portal/hallLogin";
        Map<String, String> header = getCommonHeader();
        header.put("Host", "uac.10010.com");
        header.put("Referer", "http://www.10010.com/net5/011/");

        try {
            Response response = OkHttpUtil.get(url, header);
            Map<String, String> map = getCookiesFromResponse(response, Arrays.asList("JUT"));
            cookiesMap.put("JUT", map.get("JUT"));
            checkResponse("hallLoginForExchangetJut", response);
            System.out.println("");
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    private void checkLoginForE3() {
        String url = "http://iservice.10010.com/e3/static/check/checklogin/?_=" + DateUtil.getTimeStamp();
        Map<String, String> header = getCommonHeader();
        header.put("Host", "iservice.10010.com");
        header.put("Origin", "http://iservice.10010.com");
        header.put("Referer", "http://iservice.10010.com/e4/skip.html?menuCode=000100020001");

        try {
            Response response = OkHttpUtil.post(url, null, header);
            Map<String, String> mapForE3 = getCookiesFromResponse(response, Arrays.asList("route", "e3"));

            for (Map.Entry<String, String> entry : mapForE3.entrySet()) {
                cookiesMap.put(entry.getKey(), entry.getValue());
            }
            checkResponse("checkLoginForE3", response);
            System.out.println("");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    /**
     * 请求用户信息
     */
    private void queryUserInfo() {

        String url = "http://www.10010.com/mall/service/query/userinfoquery?_=" + DateUtil.getTimeStamp();
        addExtraHeader();   //这里在添加些cookies
        Map<String, String> header = getCommonHeader();
        header.put("Host", "www.10010.com");
        header.put("Origin", "http://www.10010.com");
        header.put("Referer", "http://www.10010.com/net5/011/");

        try {
            Response response = OkHttpUtil.post(url, null, header);
            checkResponse("queryUserInfo", response);
            System.out.println("");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    /**
     * 获取账单信息
     */
    private void getCalling() {

        String url = "http://iservice.10010.com/e3/static/query/callDetail?_=" + DateUtil.getTimeStamp()
                + "&accessURL=http://iservice.10010.com/e4/query/bill/call_dan-iframe.html&menuid=000100030001";

        cookiesMap.put("MII", "000100030001");

        Map<String, String> header = getCommonHeader();
        header.put("Host", "iservice.10010.com");
        header.put("Referer", "http://iservice.10010.com/e4/query/bill/call_dan-iframe.html");
        header.put("Origin", "http://iservice.10010.com");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("pageNo", "1");
        paramMap.put("pageSize", "20");
        paramMap.put("beginDate", "20180301");
        paramMap.put("endDate", "20180304");

        try {
            Response response = OkHttpUtil.post(url, paramMap, header);
            checkResponse("getCalling", response);
            System.out.println("");
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }


    @Deprecated
    private void searchPerInfoAndE3() {
        String url = "http://iservice.10010.com/e3/static/query/searchPerInfo/?_=" + DateUtil.getTimeStamp();
        Map<String, String> header = getCommonHeader();
        header.put("Host", "iservice.10010.com");
        header.put("Origin", "http://iservice.10010.com");
        header.put("Referer", "http://iservice.10010.com/e4/skip.html?menuCode=000100020001");

        try {
            Response response = OkHttpUtil.post(url, null, header);
            Map<String, String> mapForE3 = getCookiesFromResponse(response, Arrays.asList("route", "e3"));

            for (Map.Entry<String, String> entry : mapForE3.entrySet()) {
                cookiesMap.put(entry.getKey(), entry.getValue());
            }
            checkResponse("searchPerInfoAndE3", response);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    /**
     * 返回通用的header
     */
    private Map<String, String> getCommonHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
        header.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        header.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");

        header.put("cookie", cookieMapToString(cookiesMap));
        return header;
    }

    /**
     * 添加部分cookies
     */
    private void addExtraHeader() {
        cookiesMap.put("citycode", "110");
        cookiesMap.put("ckuuid", "");
        cookiesMap.put("mallcity", "11|110");
        cookiesMap.put("userprocode", "011");
        cookiesMap.put("loginflag", "true");
        cookiesMap.put("mallflag", "0");
        cookiesMap.put("MII", "000100020001");
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
            if (cookie.contains("=")) {
                String key = cookie.substring(0, cookie.indexOf("="));
                String value = cookie.substring(cookie.indexOf("=") + 1);
                cookiesMap.put(key, value);
            }
        }
        return cookiesMap;
    }


    /**
     *  检查响应结果，只适用与调用后没有对结果有任何操作对请求
     *
     * @param methodName 方法名，用于打印
     * @param response   响应结果
     */
    private void checkResponse(String methodName, Response response) {
        try {
            String result = response.body().string();
            if (response.isSuccessful()) {
                log.info(methodName + " is successful");
                log.info(methodName + " result is : {}", result);
            } else {
                log.info(methodName + " failed");
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private Map<String, String> getCookiesFromResponse(Response response, List<String> keyList) {

        Map<String, String> cookieMap = new HashMap<>();        //最终返回对map

        List<String> cookieList = response.headers("Set-Cookie");
        //okHttp返回对header中样式如下
        /**
         *
         * set-cookie:
         * [domain=hahha;path=/;aaa=a]
         * [name=li;pad=top]
         *
         * 这样一组
         * */
        if (cookieList != null) {
            for (String setCookie : cookieList) {
                Map<String, String> covertCookieMap = cookieStringToMap(setCookie);     //这样仍然是O(n^3)的时间复杂度，但是量小
                for (String key : keyList) {
                    if (covertCookieMap.containsKey(key)) {
                        cookieMap.put(key, covertCookieMap.get(key));
                    }
                }
            }
        } else {
            log.warn("获取的请求头中没有cookie，无法获");
        }
        for (String key : keyList) {
            if (!cookieMap.containsKey(key)) {
                log.warn("there is not {} in response cookie", key);
            }
        }

        return cookieMap;

    }
}
