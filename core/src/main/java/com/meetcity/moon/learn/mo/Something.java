package com.meetcity.moon.learn.mo;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wds on 2017/11/13.
 * <p>
 * 用于一些代码的测试，自己不确定能否执行通
 *
 * @author moon
 */
public class Something {


    /**
     * while循环中处理异常，被捕获后，还能继续往下走
     */
    public void testCircleException() {
        int i = 5;
        while (true) {
            try {
                Thread.sleep(1000);
                --i;
                System.out.println("高呼德玛西亚" + i);
                System.out.println("hahah" + (5 / i));
                System.out.println("___" + i);

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }
    }


    /**
     * 字段为空，map 转json 后，里面是null，取值没有，key也没有
     */
    public void map() {

        Map<String, Object> hahaha = new HashMap<>();
        hahaha.put("nihai", null);
        hahaha.put("hfinaf", "fsfsf");
        hahaha.put("fsfsf", "fsfsffs");
        String asfsdf = JSON.toJSONString(hahaha);
        System.out.println("");
    }

    /**
     * 对于替换 { \" } 这个字符来说，replace和replaceAll一样
     */
    public void regex() {
        String a = "div.s_classlist_text div.s_classlist_text_l a[target=\\\"_self\\\"]";
        String b = a.replace("\"", "");
        String c = a.replaceAll("\"", "");

        System.out.println("");
    }


    /**
     * boolean 封装
     */
    public void b() {
        Map<String,Object> map = new HashMap<>();
        map.put("Moon",true);
        boolean i = (Boolean) map.get("Moon");
        System.out.println("");
    }


    private Double A;     // 网报车价
    private Double B;       //开票价
    private Double C;       //必要费用
    private Double D;       //贷款数
    private Double E;       //优惠车价

    public void cucut() {
        D = B * 0.7;
        Double 月供 = D * 0.042977;
        Double 利息 = 月供 * 24 - D;

    }
}
