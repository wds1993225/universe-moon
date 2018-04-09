package com.meetcity.moon.spider.template;

import com.meetcity.moon.spider.processor.MoonProcessor;
import com.meetcity.moon.spider.schedule.MoonTask;
import com.meetcity.moon.spider.schedule.MoonTaskExecutor;
import com.meetcity.moon.util.ReflectUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * Created by wds on 2017/11/29.
 *
 * @author moon
 */
public abstract class TemplateDetailProcessor<T> implements MoonProcessor {

    public abstract Class<T> setResult();


    @Override
    public Object processor(Object downloadResult, MoonTask moonTask) {
        Class<T> tClass = setResult();
        Map<Object, Object> map = ReflectUtil.getResultInfo(tClass);
        if (map == null || downloadResult == null || moonTask == null) {
            return null;
        }
        Document document = Jsoup.parse(String.valueOf(downloadResult));
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            String txt = document.select(String.valueOf(value)).text();
            map.put(key, txt);
        }
        ParameterizedType parameterizedType = (ParameterizedType)tClass.getClass().getGenericSuperclass();
        Class<T> clazz = (Class<T>)parameterizedType.getActualTypeArguments()[0];
        Object o = null;
        try {
            o =  clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ReflectUtil.setFieldValue(map,o);
        return map;
    }
}
