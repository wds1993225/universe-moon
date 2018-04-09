package com.meetcity.moon.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wds on 2017/10/30.
 *
 * @author moon
 */
public class StringUtil {

    /**
     * 判断字符串是否为空
     * "null" 也被认为是空
     *
     * @param str
     */
    public static boolean isEmpty(String str) {
        if (str == null || "null".equals(str) || str.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串是否为空
     * "null" 不被认为是空
     *
     * @param str
     */
    public static boolean isEmptyIncludedNull(String str) {
        return str == null || str.length() == 0;
    }


    /**
     * 使用特殊字符填充字符串
     *
     * @param list    要中间填充字符的字符串组
     * @param pattern 要填充的字符
     *                <p>
     *                <p>
     *                如果有转义需要自己做
     *                填充后的样式类似于  { This##is##super##hero }
     */
    public static String fullString(List<String> list, String pattern) {
        StringBuilder stringBuilder = new StringBuilder("");

        if (list == null) {
            return stringBuilder.toString();
        }
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i));
            if (i != list.size() - 1) {             //最后一个字符不填充pattern
                stringBuilder.append(pattern);
            }
        }

        return stringBuilder.toString();
    }


    /**
     * 去空格
     * <p>
     * 样式类似于  AAA   BBBB  CC      DDD
     * 中间的空格数不等
     */
    public static List<String> replaceBlank(String rawText) {
        if (StringUtil.isEmpty(rawText)) {
            return null;
        }
        List<String> list = new ArrayList<>();
        String raws[] = rawText.split(" ");
        for (String raw : raws) {
            if (!raw.equals("")) {
                list.add(raw);
            }
        }
        return list;
    }


    /**
     * 去除类似空格，换行等字符
     * */
    public static String removeNoise(String str){
        if(isEmpty(str)){
            return null;
        }
        return str.replaceAll("\n","")
                .replaceAll("\r","")
                .replaceAll(" ","");
    }

}
