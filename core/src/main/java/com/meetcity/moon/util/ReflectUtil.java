package com.meetcity.moon.util;

import com.meetcity.moon.spider.template.HtmlField;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wds on 2017/11/24.
 *
 * @author moon
 * <p>
 * 反射赋值和注解相关的工具类
 */
public class ReflectUtil {

    /**
     * 通过反射获取一个类的实例
     *
     * @param className 类名
     */
    public static Object getObjectByReflect(String className) {
        Object o = null;
        try {
            Class clazz = Class.forName(className);
            o = clazz.newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return o;
    }


    /**
     * 获取注解中的值
     *
     * @param clazz 被注解的类
     * @return 返回一个map, K: 类的属性名 V: 注解的值
     */
    public static Map<Object, Object> getResultInfo(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        Map<Object, Object> map = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(HtmlField.class)) {
                HtmlField htmlField = (HtmlField) field.getAnnotation(HtmlField.class);
                String name = field.getName();      //属性名
                String annotation = htmlField.selector();    //注解的内容
                //String slot = field.getType().getTypeName();    //属性类型 ，类似：java.lang.String
                map.put(name, annotation);
                //System.out.println(name + "  :  " + annotation);
            }
        }
        return map;
    }


    /**
     * 给一个实体类赋值
     *
     * @param map  要填充的数据，k:实体的属性名 v:实体的属性值
     * @param bean 要填充的实体
     */
    public static void setFieldValue(Map<Object, Object> map, Object bean) {
        Class<?> cls = bean.getClass();
        Method methods[] = cls.getDeclaredMethods();
        Field fields[] = cls.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fldType = field.getType().getSimpleName();
            String fldSetName = field.getName();
            String setMethod = pareSetName(fldSetName);
            if (!checkMethodExist(methods, setMethod)) {
                continue;
            }
            Object value = map.get(fldSetName);
            //System.out.println(value == null ? "" : value.toString());
            if (StringUtil.isEmpty(setMethod)) {
                continue;
            }
            try {
                Method method = cls.getMethod(setMethod, field.getType());
                System.out.println(method.getName());
                if (value != null) {
                    if ("String".equals(fldType)) {
                        method.invoke(bean, (String) value);
                    } else if ("Double".equals(fldType)) {
                        method.invoke(bean, (Double) value);
                    } else if ("Integer".equals(fldType)) {
                        method.invoke(bean, Integer.valueOf((String) value));
                    } else if ("Date".equals(fldType)) {
                        method.invoke(bean, (Date) value);
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 拼装set方法
     *
     * @param fldName 要拼接的方法名
     */
    private static String pareSetName(String fldName) {
        if (StringUtil.isEmpty(fldName)) {
            return null;
        }
        return "set" + fldName.substring(0, 1).toUpperCase() + fldName.substring(1);
    }


    /**
     * 判断方法是否存在
     *
     * @param methods   一组方法
     * @param setMethod 要判断的方法名
     */
    private static boolean checkMethodExist(Method methods[], String setMethod) {
        if (null != methods) {
            for (Method method : methods) {
                if (setMethod.equals(method.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    //http://blog.csdn.net/xiaofengxiaoling/article/details/7382030


}
