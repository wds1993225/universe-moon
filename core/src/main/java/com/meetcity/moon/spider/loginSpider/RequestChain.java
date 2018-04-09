package com.meetcity.moon.spider.loginSpider;

import com.alibaba.fastjson.JSON;
import com.meetcity.moon.spider.schedule.ScheduleCore;
import com.meetcity.moon.util.OkHttpUtil;
import com.meetcity.moon.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by moon on 2018/3/7.
 * <p>
 * <p>
 * 使用流程：
 * 1，{@link #initCookie(Map)}如果有需要初始化的cookie，先传入 . not necessary.
 * 2，{@link #addRequest(MoonRequest)}填入需要的链式请求的request
 * 3，{@link #execute()}执行
 */

@Slf4j
public class RequestChain {


    private Map<String, String> cookieMap = new HashMap<>();
    private List<MoonRequest> requestList = new ArrayList<>();


    private BlockingQueue<MoonRequest> requestBlockingQueue;
    private int THREAD_NUM = 10;
    private ExecutorService executor;
    private boolean IS_THREAD_FINISH = false;  //线程的状态


    public static void main(String[] args) {
        MoonRequest request = new MoonRequest() {
            @Override
            public Object process(Object lastResult, MoonRequest request) {
                System.out.println("hahah");
                return "result";
            }
        };
        request.setUrl("urllllll").setJsonData("666a777");
        log.info("this is : [{}]", request.getUrl());
        System.out.println("");
    }


    /**
     * 初始化第一次cookie
     */
    public void initCookie(Map<String, String> initCookies) {
        if (initCookies == null) {
            log.info("init cookie is null");
            return;
        }
        for (Map.Entry<String, String> entry : initCookies.entrySet()) {
            cookieMap.put(entry.getKey(), entry.getValue());
        }
    }


    /**
     * 添加请求
     *
     * @param request 自定义的一个请求封装类型
     */
    public void addRequest(MoonRequest request) {
        log.info("new request add . request is : {}", JSON.toJSONString(request));
        requestList.add(request);
    }


    /**
     * 一些可以并发执行的请求
     */
    public void addCurrentRequest(BlockingQueue<MoonRequest> requestBlockingQueue) {
        if (requestList == null) {
            return;
        }
        this.requestBlockingQueue = requestBlockingQueue;
        executor = Executors.newFixedThreadPool(THREAD_NUM);


    }


    /**
     * 执行开始
     */
    public void execute() {

        log.info("requestList size is : {} ", requestList.size());

        for (int i = 0; i < requestList.size(); i++) {
            MoonRequest request = requestList.get(i);
            Object downloadResult = download(request);
            Object processResult = process(downloadResult, request);

            if ((i + 1) != requestList.size()) {        //最后一个后面没有了
                MoonRequest nextRequest = requestList.get(i + 1);
                nextRequest.setLastResult(processResult);
            }
        }

        if(requestBlockingQueue.isEmpty()){
            return;
        }
        for (int i = 0; i < THREAD_NUM; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    while (!IS_THREAD_FINISH) {
                        //TODO 这里需要测试一下
                        try {
                            MoonRequest request = requestBlockingQueue.poll();
                            if (request == null) {
                                IS_THREAD_FINISH = true;
                                continue;
                            }
                            Object downloadResult = download(request);
                            process(downloadResult, request);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            });
            log.info("Thread :{} is begin ...", i);
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

        return header;
    }

    /**
     * 获取一个默认的请求头
     */
    public Map<String, String> getDefaultHeader(String origin, String host, String referer, String cookie) {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
        //map.put("Accept-Encoding", "gzip, deflate");
        header.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        header.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
        header.put("X-Requested-With", "XMLHttpRequest");

        if (!StringUtil.isEmpty(origin)) {
            header.put("Origin", origin);
        }
        if (!StringUtil.isEmpty(host)) {
            header.put("Host", host);
        }
        if (!StringUtil.isEmpty(referer)) {
            header.put("Referer", referer);
        }

        if (!StringUtil.isEmpty(cookie)) {
            header.put("cookie", cookie);
        }
        return header;
    }


    /**
     * 实际执行下载
     *
     * @return 下载的结果
     */
    private Object download(MoonRequest request) {
        String downloadResult = null;       //处理结果，不是下载结果
        if (request == null) {
            log.error("request must not be null");
            return null;
        }
        log.info("download begin , request is : {} ", JSON.toJSONString(request));
        MoonRequest.Method method = request.getMethod();    //请求的方法类型
        String url = request.getUrl();                        //请求的url
        Map<String, String> param = request.getParam();        //请求的参数
        Map<String, String> header = request.getHeader();    //请求头
        String jsonData = request.getJsonData();                      //请求的JSON类型的参数

        header.put("cookie", cookieMapToString(cookieMap));     //将之前的cookie传入

        Response response = null;
        if (method == null) {
            return null;
        }
        switch (method) {
            case GET:
                response = OkHttpUtil.get(url, header);
                break;
            case POST:
                if (!StringUtil.isEmpty(jsonData)) {
                    response = OkHttpUtil.postJson(url, jsonData, header);
                } else {
                    response = OkHttpUtil.post(url, param, header);
                }
                break;
            case PUT:
                break;
        }

        log.debug("Response is :{}", response != null ? response : " 响应为空.");
        if (response == null) {
            log.warn("response is null");
            return null;
        }
        if (response.body() != null || response.isSuccessful()) {
            try {
                downloadResult = response.body().string();
                log.info("download success , result is : {} ", JSON.toJSONString(downloadResult));
                addCookiesFromResponse(response);       //这里将请求头中的cookie放入整个cookieMap中
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            log.warn("response error , request is : {} ", JSON.toJSONString(request));
        }
        return downloadResult;
    }


    /**
     * 处理解析结果
     */
    private Object process(Object downloadResult, MoonRequest request) {
        Object processResult = request.process(downloadResult, request);
        log.info("process result is :{}", JSON.toJSONString(processResult));
        return processResult;
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

    /**
     * okHttp返回对header中样式如下
     * set-cookie:
     * [domain=hahha;path=/;aaa=a]
     * [name=li;pad=top]
     * <p>
     * 这样一组
     * 兼容okHttp的header
     */
    private void addCookiesFromResponse(Response response) {

        List<String> cookieList = response.headers("Set-Cookie");

        if (cookieList != null) {
            for (String cookies : cookieList) {

                String[] cookieArray = cookies.split(";");
                for (String cookie : cookieArray) {
                    if (cookie.contains("=")) {
                        String key = cookie.substring(0, cookie.indexOf("="));
                        String value = cookie.substring(cookie.indexOf("=") + 1);
                        if (cookieMap.containsKey(key)) {
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
     * 获取cookieMap
     */
    public Map<String, String> getCookieMap() {
        return cookieMap;
    }
}
