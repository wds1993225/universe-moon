package com.meetcity.moon.util;

import com.alibaba.fastjson.*;
import com.jayway.jsonpath.JsonPath;

import java.util.*;

/**
 * Created by wds on 2017/12/5.
 *
 * @author moon
 */
public class JSONUtil {


    /**
     * fastJson -- JSONPath # read()方法不支持 $.store.book.*这样的语法
     * jayway --JsonPath # read() 方法支持 以上的语法
     * */


    /**
     * 这个方法是为了 记住 new TypeReference<List<Map<String,Object>>>(){} 的写法
     * <p>
     * 不常用
     */
    public List<Map<String, Object>> parseToList(String str) {
        List<Map<String, Object>> listMap = JSON.parseObject(str, new TypeReference<List<Map<String, Object>>>() {
        });
        return listMap;
    }


    /**
     * 解析列表页Json
     */
    public static List<Object> parseListJson(String rawJson, String jsonPath) {
        List<Object> list = null;
        if (jsonPath.contains(".*")) {
            list = JsonPath.read(rawJson, jsonPath);
        }
        return list;
    }

    /**
     * 解析内容json
     */
    public static Map<String, Object> parseDetailJson(String rawJson, Map<String, Object> pathMap) {
        for (Map.Entry<String,Object> entry : pathMap.entrySet()){
            String uploadPath = entry.getKey();
            String selectPath = String.valueOf(entry.getValue());
            pathMap.put(uploadPath,JsonPath.read(rawJson,selectPath));
        }
        return pathMap;
    }


    /**
     * 用于将原始json，映射规则，相解析出需要的字段
     * 原始map为 k:上传的映射字段，v：选择器
     * <p>
     * 如果不包含  .*，则用解析出的值替换选择器
     * 如果选择器中包含  .*， 则认为是一个list中的数据，则循环替换
     * <p>
     * 不应该分成列表和详情，这里做了区分，通过是否有  .* 来区分
     *
     * @这个不完善
     */
    public static List<Map<String, Object>> getJsonList(String rawJson, Map<String, Object> map) {   //map:k:upload   v:selector

        Map<String, String> tempMap = new HashMap<>();
        String parentPath = null;
        List<Map<String, Object>> uploadList = new ArrayList<>();

        JSONObject rawJsonObject = JSON.parseObject(rawJson);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String uploadPath = entry.getKey();
            String jsonPath = String.valueOf(entry.getValue());
            if (jsonPath.contains(".*")) {
                String[] allPath = new String[2];
                allPath = jsonPath.split("\\.\\*");
                parentPath = allPath[0];
                String childPath = allPath[1];
                if (childPath.startsWith(".")) {
                    childPath = childPath.replaceFirst("\\.", "");
                }
                //tempMap.put(uploadPath, childPath);
                tempMap.put(childPath, uploadPath);     //tempmap  k:upload   v:selector
                System.out.println("");

            } else {
                Object o = JsonPath.read(jsonPath, jsonPath);
                //Object o = rawJsonObject.get(jsonPath);
                map.put(uploadPath, String.valueOf(o));
                uploadList.add(map);
                System.out.println("");
            }
        }

        if (!StringUtil.isEmpty(parentPath)) {      //处理list类型
            Object parentObject = JsonPath.read(rawJson, parentPath);
            JSONArray parentArray = JSON.parseArray(String.valueOf(parentObject));
            for (Object item : parentArray) {
                Map<String, Object> uploadItemMap = new HashMap<>();

                Map<String, Object> itemMap = JSON.parseObject(String.valueOf(item), Map.class);
                for (Map.Entry<String, String> temEntry : tempMap.entrySet()) {
                    String childPath = temEntry.getKey();
                    String uploadPath = temEntry.getValue();
                    //Object parseData = itemMap.get(childPath);
                    Object parseData = JsonPath.read(String.valueOf(item), "$." + childPath);

                    uploadItemMap.put(uploadPath, parseData);
                }
                uploadList.add(uploadItemMap);

            }
        }
        return uploadList;

    }


    /**
     * 传入json字符串，及关键字，查找次此 关键字的jsonPath
     *
     * @param rawJson 原始json
     * @param keyWord 查找的关键字
     */
    public static List<String> jsonPath(String rawJson, String keyWord) {
        List<String> pathList = new ArrayList<>();
        Object o = JSON.parse(rawJson);
        jsonPath(o, keyWord, "$", pathList);
        return pathList;
    }


    /**
     * @param lastLevelPath 每次递归保存的上一级全路径
     * @param pathList      全局保存路径
     */
    private static void jsonPath(Object rawObject, String keyWord, String lastLevelPath, List<String> pathList) {

        if (rawObject instanceof List) {            //类型为list，递归第一个元素
            List list = (List) rawObject;
            if (list.size() >= 0) {
                Object item = list.get(0);
                jsonPath(item, keyWord, lastLevelPath + ".*", pathList);
            }

        }

        if (!(rawObject instanceof Map)) {      //不为list，不为map，返回
            return;
        }

        Map<Object, Object> map = JSON.parseObject(String.valueOf(rawObject), Map.class);

        if (map.containsKey(keyWord)) {
            pathList.add(lastLevelPath + "." + keyWord);      //如果包含关键字，加入路径
        }
        for (Map.Entry<Object, Object> entry : map.entrySet()) {    //继续递归，遍历整个树
            Object key = entry.getKey();
            Object value = entry.getValue();
            jsonPath(value, keyWord, lastLevelPath + "." + key, pathList);
        }

    }

}
