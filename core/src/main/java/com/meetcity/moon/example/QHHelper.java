package com.meetcity.moon.example;

import com.alibaba.fastjson.JSON;
import com.meetcity.moon.util.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by moon on 2018/3/8 .
 */

@Slf4j
public class QHHelper {

    private static String jsPath = "core/src/main/resources/js/qh.js";
    private String phoneNum = "17797144575";
    private String pwd = "208732";


    private Map<String, String> cookieMap = new HashMap<>();
    OkHttpCookieHelper helper;

    public static void main(String[] args) {
        QHHelper qhHelper = new QHHelper();
        qhHelper.start();
    }


    public void start() {


        helper = new OkHttpCookieHelper(cookieMap);

        String pwdAES = JSUtil.executeScript(jsPath, "pwdAES", pwd);

        String ECS_ReqInfo_login1 = JSUtil.executeScript(jsPath, "getECS_ReqInfo_login1", phoneNum);
        //String ECS_ReqInfo_login1 = "U2FsdGVkX1%2FxSK7yg9L1%2BbTV33QkaguXdfsLJtFAMomUagYd%2BzTptRNyQqP6tqH6fRVcDBcLIhWW%2Fv%2Bb6t32DOufIfB0jkm5g%2FtF%2F%2BLuN1A%3D";
        cookieMap.put("ECS_ReqInfo_login1", ECS_ReqInfo_login1);

        //String[] ESC = ECS_ReqInfo_login1.split(";");
        //ECS_ReqInfo_login1 = ESC[0];

        String trkId = JSUtil.executeScript(jsPath, "trkId");
        System.out.println("");

        String lvid = JSUtil.executeScript(jsPath, "getVid");
        System.out.println("");

        String s_fid = JSUtil.executeScript(jsPath, "fid");


        String code_v = getSv();
        String time = code_v.replace("document.cookie=\"code_v=\"+", "").replace("\n", "");

        cookieMap.put("__qc_wId", "234");
        cookieMap.put("pgv_pvid", "5974549854");

        getSvid();        //svid
        cookieMap.put("loginStatus", "non-logined");
        cookieMap.put("lvid", lvid);
        cookieMap.put("nvid", "1");
        cookieMap.put("s_cc", "true");
        cookieMap.put("s_fid", s_fid);
        //svid
        cookieMap.put("trkId", trkId);
        cookieMap.put("code_v", "20170913");
        checkLogin1();
        checkLogin2();
        getCapture();
        String capture = ScanUtil.scan("请输入验证码");
        cookieMap.put("ECS_ReqInfo_login1", ECS_ReqInfo_login1);
        cookieMap.put("trkHmClickCoords", "755%2C372%2C803");
        String result = login(capture, pwdAES);
        redirect((result));
        cookieMap.put("cityCode", "qh");
        cookieMap.put("isLogin", "logined");
        index();

        cookieMap.put("SHOPID_COOKIEID", "10029");
        cookieMap.put("trkHmPageName", "%2Fqh%2F");
        cookieMap.put("trkHmCoords", "290%2C371%2C363%2C401%2C4532");
        cookieMap.put("trkHmCity", "qh");
        cookieMap.put("trkHmLinks", "http%3A%2F%2Fwww.189.cn%2Fdqmh%2Fmy189%2FinitMy189home.do%3Ffastcode%3D00920924");
        cookieMap.put("trkHmClickCoords", "301%2C388%2C4532");
        //cookieMap.put("s_sq", "%5B%5BB%5D%5D");
        cookieMap.put("s_sq", "eshipeship-189-all%3D%2526pid%253D%25252Fqh%25252F%2526pidt%253D1%2526oid%253Dhttp%25253A%25252F%25252Fwww.189.cn%25252Fdqmh%25252Fmy189%25252FinitMy189home.do%25253Ffastcode%25253D00920924%2526ot%253DA");

        getCsrfToken();
        String location = getCsrfToken1();
        auth_token(location);
        System.out.println("");

    }


    private String getSv() {
        String url = "http://webwebfenxi.189.cn:9000/scode/live/sv.js?v=" + DateUtil.getTimeStamp();
        Map<String, String> header = helper.getCommonHeader();
        header.put("Host", "webwebfenxi.189.cn:9000");
        header.put("Referer", "http://login.189.cn/web/login");
        String result = "";


        try {
            Response response = OkHttpUtil.get(url, header);
            result = checkResponse("getSv", response);
            System.out.println("");
        } catch (Throwable t) {
            System.out.println("");
        }
        return result;
    }


    /**
     * 获取svid
     */
    private void getSvid() {
        String url = "http://webwebfenxi.189.cn:9000/scode/live/ct189.js?v=" + DateUtil.getTimeStamp();
        Map<String, String> header = helper.getCommonHeader();
        header.put("Host", "webwebfenxi.189.cn:9000");
        header.put("Referer", "http://login.189.cn/web/login");
        try {
            Response response = OkHttpUtil.get(url, header);
            //helper.addCookiesFromResponse(response);
            checkResponse("getSvid", response);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void checkLogin1() {
        String url = "http://login.189.cn/web/login/ajax";
        Map<String, String> header = helper.getCommonHeader();
        header.put("Host", "login.189.cn");
        header.put("Origin", "http://login.189.cn");
        header.put("Referer", "http://login.189.cn/web/login");

        Map<String, String> param = new HashMap<>();
        param.put("m", "checkphone");
        param.put("phone", phoneNum);

        try {
            Response response = OkHttpUtil.post(url, param, header);
            checkResponse("checkLogin1", response);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void checkLogin2() {
        String url = "http://login.189.cn/web/login/ajax";
        Map<String, String> header = helper.getCommonHeader();
        header.put("Host", "login.189.cn");
        header.put("Origin", "http://login.189.cn");
        header.put("Referer", "http://login.189.cn/web/login");

        Map<String, String> param = new HashMap<>();
        param.put("m", "captcha");
        param.put("account", phoneNum);
        param.put("uType", "201");
        param.put("ProvinceID", "29");
        try {
            Response response = OkHttpUtil.post(url, param, header);
            checkResponse("checkLogin2", response);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    private void getCapture() {
        String url = "http://login.189.cn/web/captcha?undefined&source=login&width=100&height=37&0.7663637604889146";
        Map<String, String> header = helper.getCommonHeader();
        //header.put("Host", "login.189.cn");
        //header.put("Referer", "http://login.189.cn/web/login");
        header.put("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
        try {
            Response response = OkHttpUtil.get(url, header);
            helper.addCookiesFromResponse(response);
            //checkResponse("getCapture", response);
            //String result = response.body().string();
            //generateImage(result,"/Users/moon/MoonWorkspace/capture.jpg");
            InputStream inputStream = response.body().byteStream();
            FileUtil.readStreamToFile(inputStream, "/Users/moon/MoonWorkspace", "capture1.png");
            System.out.println();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    private String login(String capture, String pwdAES) {

        String result = "";
        String url = "http://login.189.cn/web/login";
        Map<String, String> header = helper.getCommonHeader();
        header.put("Host", "login.189.cn");
        header.put("Origin", "http://login.189.cn");
        header.put("Referer", "http://login.189.cn/web/login");
        header.put("Upgrade-Insecure-Requests", "1");

        Map<String, String> param = new HashMap<>();
        param.put("Account", phoneNum);
        param.put("UType", "201");
        param.put("ProvinceID", "29");
        param.put("RandomFlag", "0");
        param.put("Password", pwdAES);
        param.put("Captcha", capture);
        try {
            Response response = OkHttpUtil.post(url, param, header);
            //helper.addCookiesFromResponse(response);
            Response priorResponse = response.priorResponse();      //这里是重定向
            if (priorResponse != null) {
                if (priorResponse.code() == 302) {
                    result = priorResponse.headers("Location").get(0);
                    helper.addCookiesFromResponse(priorResponse);
                } else {
                    log.error("login error , response is :[{}]", JSON.toJSONString(priorResponse));
                }
            }
           /* if (response.code() == 302) {
                String result = response.headers("Location").get(0);
                helper.addCookiesFromResponse(response);
            } else {
                log.error("login error , response is :[{}]", JSON.toJSONString(response));
            }*/
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;

    }

    private void redirect(String url) {
        Map<String, String> header = helper.getCommonHeader();
        header.put("Host", "www.189.cn");
        header.put("Referer", "http://login.189.cn/web/login");
        try {
            Response response = OkHttpUtil.get(url, header);
            if (response.priorResponse() != null) {
                if (response.priorResponse().priorResponse() != null) {
                    Response priorResponse = response.priorResponse().priorResponse();
                    checkResponse("redirect", priorResponse);
                }
            }
            //checkResponse("redirect", response);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    private void index() {

        String url = "http://www.189.cn/login/index.do";
        Map<String, String> header = helper.getCommonHeader();
        header.put("Host", "www.189.cn");
        header.put("Referer", "http://www.189.cn/html/login/index.html");

        try {
            Response response = OkHttpUtil.get(url, header);
            log.info("index :[{}]",JSON.toJSONString(response.body().string()));
            checkResponse("index", response);
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }


    private void getCsrfToken() {
        String url = "http://www.189.cn/dqmh/ssoLink.do?method=linkTo&platNo=10029&toStUrl=http://qh.189.cn/service/bill/fee.action?type=bill&csrftoken=QQ_OPEN_TOKEN&fastcode=00920924&cityCode=qh";
        Map<String, String> header = helper.getCommonHeader();
        header.put("Referer", "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00920924");
        header.put("Host", "www.189.cn");


        try {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new MoonIntercept())
                    .build();



            Response response = client.newCall(OkHttpUtil.getRequest(url, header)).execute();
            System.out.println("");
        } catch (Throwable t) {
            t.printStackTrace();
        }




        try {
            Response response = OkHttpUtil.get(url, header);
            System.out.println("");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    private String getCsrfToken1() {

        String result = "";

        String url = "http://www.189.cn/login/sso/ecs.do?method=linkTo&platNo=10029&toStUrl=http://qh.189.cn/service/bill/fee.action?type=bill&csrftoken=QQ_OPEN_TOKEN&fastcode=00920924&cityCode=qh";
        Map<String, String> header = helper.getCommonHeader();
        header.put("Referer", "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00920924");
        header.put("Host", "www.189.cn");


        try {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new MoonIntercept())
                    .build();



            Response response = client.newCall(OkHttpUtil.getRequest(url, header)).execute();
            System.out.println("");
        } catch (Throwable t) {
            t.printStackTrace();
        }


        try {
            Response response = OkHttpUtil.get(url, header);
            if (response != null) {
                if (response.priorResponse() != null) {
                    if (response.priorResponse().priorResponse() != null) {
                        if (response.priorResponse().priorResponse().priorResponse() != null) {
                            Response priorResponse = response.priorResponse().priorResponse().priorResponse();
                            result = priorResponse.headers("Location").get(0);
                        }

                    }
                }
            }
            //checkResponse("getCsrfToken1",response);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }


    private void auth_token(String url) {
        Map<String, String> header = helper.getCommonHeader();
        header.put("Referer", "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00920924");
        header.put("Host", "qh.189.cn");

        try {
            Response response = OkHttpUtil.get(url, header);
            System.out.println("");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    private void personWebJession() {
        String url = "http://qh.189.cn/service/bill/fee.action?type=bill&csrftoken=QQ_OPEN_TOKEN&fastcode=00920924&cityCode=qh&SSOURL=http%3A%2F%2Fqh.189.cn%2Fservice%2Fbill%2Ffee.action%3Ftype%3Dbill%26csrftoken%3DQQ_OPEN_TOKEN%26fastcode%3D00920924%26cityCode%3Dqh";
        Map<String, String> header = helper.getCommonHeader();
        header.put("Referer", "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00920924");
        header.put("Host", "qh.189.cn");
        try {
            Response response = OkHttpUtil.get(url, header);
            checkResponse("personWebJession", response);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    /**
     *  检查响应结果，只适用与调用后没有对结果有任何操作对请求
     *
     * @param methodName 方法名，用于打印
     * @param response   响应结果
     */
    private String checkResponse(String methodName, Response response) {
        String result = "";
        try {
            //result = response.body().string();
            helper.addCookiesFromResponse(response);
            if (response.isSuccessful()) {
                log.info(methodName + " is successful");
                log.info(methodName + " result is : [{}]", result);
            } else {
                log.info(methodName + " failed");
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }


    public static boolean generateImage(String imgStr, String path) {
        if (imgStr == null)
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
// 解密
            byte[] b = decoder.decodeBuffer(imgStr);
// 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }




}
