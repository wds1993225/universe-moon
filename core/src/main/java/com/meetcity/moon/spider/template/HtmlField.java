package com.meetcity.moon.spider.template;

import java.lang.annotation.*;

/**
 * Created by wds on 2017/11/24.
 *
 * @author moon
 */

@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HtmlField {


    /**
     * 样式选择器
     */
    String selector() default "";

}
