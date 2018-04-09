package com.meetcity.moon.spider.schedule;

import com.meetcity.moon.constant.Constants;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by wds on 2017/11/9.
 *
 * @author moon
 *         <p>
 *         <p>
 *         一个任务是最小单位
 *         一个任务包含所有与请求必须的条件
 */

@Data
public class MoonTask implements Serializable {

    private String url;             //请求的url
    private Constants.HttpMethod method;    //请求方式

    private Map<String, String> headers;     //请求头

    private Map<String, String> param;       //请求参数 K-V
    private String json;        //请求参数  Json


    /*这里和分页相关*/
    private String paginationName;          //分页参数 名
    private Integer paginationIndex;        //分页参数 页数
    private PageSplitType pageSplitType;              //拼接类型    kv类型：www.baidu.com?page=1   add类型：www.baidu.com/page_1
    private String suffix;                      //分页参数的后缀，适配Add类型
    //TODO 根据日期采集
    //TODO totalPage
    //TODO redis 去重，通过URL + Param

    /*这里和列表页解析元素相关*/
    private Map<String, String> listParam;
    /*这里和详情解析元素相关*/
    private Map<String, String> detailParam;        //k:解析的selector，v:上传的字段


    private Map<String, Object> uploadParam;     //上传参数

    private Map<String, Object> extraInfo;           //额外的一些参数

    private String moonDownloadName;    //下载器的名字
    private String moonProcessorName;   //解析器的名字
    private String moonUploadName;      //上传器的名字


    private String isRemovalDuplicate;      //是否去重  列表任务不去重，因为一直更新，相同的url可能包含不同item


    public enum PageSplitType {     //配置列表追加方式
        KV("kv"), ADD("add");
        private final String text;

        PageSplitType(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }


    /**
     * 默认GET方法
     */

    public MoonTask() {
    }

    public MoonTask(String url) {
        this(url, Constants.HttpMethod.GET, null);
    }

    public MoonTask(String url, Constants.HttpMethod method, Map<String, String> param) {
        this(url, method, null, param, null);
    }

    public MoonTask(String url, Constants.HttpMethod method, Map<String, String> headers, Map<String, String> param, String json) {
        this(url, method, headers, param, json, null, null, null);
    }

    public MoonTask(String url, Constants.HttpMethod method, Map<String, String> headers, Map<String, String> param, String json,
                    String downloader, String processor, String uploader) {
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.param = param;
        this.json = json;
        this.moonDownloadName = downloader;
        this.moonProcessorName = processor;
        this.moonUploadName = uploader;

    }
}
