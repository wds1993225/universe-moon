package com.meetcity.moon.spider.component.imp;

import com.meetcity.moon.spider.component.RandomNum;

/**
 * Created by wds on 2017/11/8.
 *
 * @author moon
 *         <p>
 *         随机数策略
 */
public class RandomStrategy implements RandomNum {


    @Override
    public long getNum() {
        return 1;
    }


    /**
     * 生成给定长度的随机数
     * 从0 到 长度
     */
    public static int randomInt(int length) {
        return randomInt(0, length);
    }

    /**
     * 生成 min 到 max 之间的随机数，包含min 和max
     *
     * @param min 最小值
     * @param max 最大值
     */
    public static int randomInt(int min, int max) {
        return (int) (min + Math.random() * (max - min + 1));
    }

}
