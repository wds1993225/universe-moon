package com.meetcity.moon.spider.component;

import com.meetcity.moon.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wds on 2017/12/21.
 *
 * @author moon
 *         <p>
 *         url过滤
 */
public class UrlFilter {

    List<String> urls;

    private String urlRegex = "(\\.gif|\\.jpg|\\.png|\\.ico|\\.css|\\.sit|\\.eps|\\.wmf|\\.zip|\\.ppt|\\.mpg|\\.xls|\\.gz|\\.rpm|\\.tgz|\\.mov|\\.exe|\\.jpeg|\\.bmp|\\.js)$";

    public List<String> doFilter() {
        if (urls != null && urls.size() > 0) {
            List<String> urlsAfterFilter = new ArrayList<>();
            for (String s : urls) {

            }
        }
        return null;
    }

    /**
     * 判断此url是否为一个资源类文件
     * <p>
     * url为空 也会返回 true
     */
    private boolean isResourceFile(String url) {
        if (StringUtil.isEmpty(url)) {
            return true;
        }
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    public static void main(String[] args) {
        /*Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher("http://.gif.baidu。gif");
        boolean is = matcher.find();
        System.out.println("");*/
    }
}
