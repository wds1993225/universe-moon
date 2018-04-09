package com.meetcity.moon.util;

import com.meetcity.moon.spider.template.HtmlField;
import lombok.Data;

import java.util.Date;

/**
 * Created by wds on 2017/11/24.
 *
 * @author moon
 */
@Data
public class WenShuResult {

    @HtmlField(selector = "xxx")
    private String title;


    @HtmlField(selector = "css.content")
    private String content;

    @HtmlField(selector = "css.date")
    private Date date;


    @HtmlField(selector = "css.date")
    private Integer number;


    @HtmlField(selector = "cas.name.hahaha")
    private String name;

}
