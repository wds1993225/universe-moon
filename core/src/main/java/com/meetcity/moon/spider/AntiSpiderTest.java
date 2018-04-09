package com.meetcity.moon.spider;

import com.meetcity.moon.constant.Constants;
import com.meetcity.moon.spider.component.ProxyUtil;
import com.meetcity.moon.spider.component.ProxyIp;
import com.meetcity.moon.util.*;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wds on 2017/10/20.
 *
 * @author moon
 *         <p>
 *         用于测试一个接口或一个网页的爬取频率限制，及各种状态返回
 */
public class AntiSpiderTest {

    private int RESPONSE_SUCCESS = 0;       //响应成功
    private int RESPONSE_FAIL = 0;          //响应失败
    private Map<Integer, Map<String, Integer>> respMap = new HashMap<Integer, Map<String, Integer>>();        //临时存放响应结果；   key：响应状态码，value：Map：k:响应的内容 ，v:相同响应的次数
    private int currentIndex = 0;      //当前的请求次数
    private List<Long> journeys = new ArrayList<Long>();   //耗时

    private final String url;
    private final RequestType requestType;
    private final Map<String, String> param;
    private final Map<String, String> headers;
    private final int requestTime;        //请求的次数
    private final long sleepTime;            //每次请求的休眠时间
    private final String filePath;
    private final String fileName;
    private final ProxyIp proxyIp;      //代理ip池


    public AntiSpiderTest() {
        this(new AntiSpiderTest.Builder());
    }

    AntiSpiderTest(Builder builder) {
        this.url = builder.url;
        this.requestType = builder.requestType;
        this.param = builder.param;
        this.headers = builder.headers;

        requestTime = builder.requestTime;
        this.sleepTime = builder.sleepTime;
        this.filePath = builder.filePath;
        this.fileName = builder.fileName;
        this.proxyIp = builder.proxyIp;

    }

    public void start() {
        Util.log("开始请求...");
        for (int i = 0; i < requestTime; i++) {
            currentIndex = i;
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Response response = sendRequest();
            if (response != null) {
                countResponseState(response);
            }
        }
        Util.log("请求完成.");
        buildReport();
    }

    /**
     * 发送请求
     */
    private Response sendRequest() {
        long startTime = System.currentTimeMillis();
        Response response = getResponse();
        long endTime = System.currentTimeMillis();
        long journey = endTime - startTime;
        journeys.add(journey);      //记录用时
        Util.log("发送请求中... 次数："+currentIndex);
        return response;
    }

    private Response getResponse() {
        if (!StringUtil.isEmpty(url)) {
            if(proxyIp != null){
                proxyIp.setProxies(ProxyUtil.proxyPool);
            }
            switch (requestType) {
                case GET:
                    return OkHttpUtil.get(url,headers);
                case POST:
                    return OkHttpUtil.post(url, param, headers);
            }
        }
        return null;
    }

    /**
     * 统计结果
     */
    private void countResponseState(Response response) {
        if (response != null) {
            int code = response.code();
            if (200 == code) {
                RESPONSE_SUCCESS++;
            } else {
                RESPONSE_FAIL++;
            }
            try {
                recognizeContent(code, response.body() != null ? response.body().string() : null);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * 保存结果
     *
     * @param code    响应码
     * @param content 响应体
     */
    private void recognizeContent(int code, String content) {

        content = !StringUtil.isEmpty(content) ? content : "";

        if (respMap.containsKey(code)) {
            Map<String, Integer> map = respMap.get(code);
            if (map.containsKey(content)) {
                int index = map.get(content);
                map.put(content, ++index);
            } else {
                map.put(content, 1);
                FileUtil.writeToFile(filePath, String.valueOf(code) + "-" + String.valueOf(currentIndex) + ".txt", content);
            }
        } else {
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put(content, 1);
            respMap.put(code, map);
            FileUtil.writeToFile(filePath, String.valueOf(code) + ".txt", content);
        }

    }

    /**
     * 生成报告
     */
    private void buildReport() {
        Util.log("开始生成报告...");
        FileUtil.writeToFile(filePath, fileName, "-----响应统计结果-----");
        FileUtil.writeToFile(filePath, fileName, "URL为："+this.url);
        List<String> list = new ArrayList<String>();
        for (Map.Entry<Integer, Map<String, Integer>> entry : respMap.entrySet()) {     //根据临时存储的数据生成报告，内容：相同响应结果的 不同内容的次数
            int code = entry.getKey();
            Map<String, Integer> map = entry.getValue();
            list.add("响应状态为：" + String.valueOf(code));
            int i = 0;
            for (Map.Entry<String, Integer> innEntry : map.entrySet()) {
                ++i;
                list.add("----类型：" + i + "  次数：" + innEntry.getValue());
            }
            list.add("\r\n");
        }
        String[] s = new String[list.size()];
        list.toArray(s);
        FileUtil.writeToFile(filePath, fileName, s);

        double totalTime = 0;
        double avgTime = 0;
        for (Long l : journeys) {
            totalTime += l;
        }
        if (journeys.size() != 0) {
            avgTime = totalTime / journeys.size();
        }
        String[] content = {"-----总计------"
                , "响应成功的次数： " + RESPONSE_SUCCESS
                , "响应失败的次数： " + RESPONSE_FAIL
                , "总用时： " + totalTime / 1000 + " 秒"
                , "平均用时： " + avgTime / 1000 + " 秒"};
        FileUtil.writeToFile(filePath, fileName, content);
        Util.log("报告生成完成.");

    }

    /**
     * 请求类型
     */
    public enum RequestType {
        GET, POST
    }

    public static final class Builder {

        String url;         //请求的url
        RequestType requestType;        //请求的类型
        Map<String, String> param;      //请求的参数
        Map<String, String> headers;     //请求头

        int requestTime;        //请求的次数
        long sleepTime;              //每次请求的休眠时间
        String filePath;        //保存的文件路径
        String fileName;        //保持的文件名
        ProxyIp proxyIp;        //代理ip池

        public Builder() {
            url = "";
            requestType = RequestType.GET;
            param = new HashMap<String, String>();
            headers = new HashMap<String, String>();
            requestTime = 100;
            sleepTime = 0;
            filePath = Constants.FOLDER_PATH;
            fileName = Constants.FILE_NAME;
            proxyIp = null;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder requestType(RequestType type) {
            this.requestType = type;
            return this;
        }

        public Builder requestParam(Map<String, String> param) {
            if(param != null){
                this.param = param;
            }
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            if(headers != null){
                this.headers = headers;
            }
            return this;
        }

        public Builder requestTime(int requestTime) {
            if (requestTime < 0) {
                this.requestTime = 0;
            }
            this.requestTime = requestTime;
            return this;
        }
        public Builder sleepTime(int sleepTime){
            if(sleepTime < 0){
                this.sleepTime = 0;
            }
            this.sleepTime = sleepTime;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            if (!this.fileName.contains("txt")) {
                this.filePath = this.fileName + ".txt";
            }
            return this;
        }
        public Builder proxyId(ProxyIp proxyIp){
            this.proxyIp = proxyIp;
            return this;
        }

        public AntiSpiderTest build() {
            return new AntiSpiderTest(this);
        }
    }


    //测试一个地址 请求10000次 的响应结果，并将结果保存下来，相同请求的相同结果去掉，相同请求的不同结果保留
    //每次请求的时间间隔，平均间隔，总时间
    //文件 200,200-1,200-2

}
