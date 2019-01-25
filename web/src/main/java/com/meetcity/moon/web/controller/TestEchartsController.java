package com.meetcity.moon.web.controller;


import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Pie;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestEchartsController {


    @CrossOrigin(origins = "http://localhost:63342")
    @RequestMapping("/test")
    public Option test() {


        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> a = new HashMap<String, Object>();
        a.put("NAME", "aaaa");
        a.put("TOTAL",100);
        list.add(a);

        Map<String, Object> b = new HashMap<String, Object>();
        b.put("NAME", "bbb");
        b.put("TOTAL",150);
        list.add(b);

        Map<String, Object> c = new HashMap<String, Object>();
        c.put("NAME", "ccc");
        c.put("TOTAL",130);
        list.add(c);

        Map<String, Object> d = new HashMap<String, Object>();
        d.put("NAME", "aaaa");
        d.put("TOTAL",110);
        list.add(d);


        //创建Option
        Option option = new Option();
        option.title("剔除药品").tooltip(Trigger.axis).legend("金额（元）");
        //横轴为值轴
        option.xAxis(new ValueAxis().boundaryGap(0d, 0.01));
        //创建类目轴
        CategoryAxis category = new CategoryAxis();
        //柱状数据
        Bar bar = new Bar("金额（元）");
        //饼图数据
        Pie pie = new Pie("金额（元）");
        //循环数据
        for (Map<String, Object> objectMap : list) {
            //设置类目
            category.data(objectMap.get("NAME"));
            //类目对应的柱状图
            bar.data(objectMap.get("TOTAL"));
            //饼图数据
            pie.data(new PieData(objectMap.get("NAME").toString(), objectMap.get("TOTAL")));
        }
        //设置类目轴
        option.yAxis(category);
        //饼图的圆心和半径
        pie.center(900, 380).radius(100);
        //设置数据
        option.series(bar, pie);
        //由于药品名字过长，图表距离左侧距离设置180，关于grid可以看ECharts的官方文档
        option.grid().x(180);


        return option;
    }
}
