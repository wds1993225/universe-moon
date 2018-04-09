package com.meetcity.moon.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wds on 2017/10/31.
 *
 * @author moon
 */
public class ImageUtil {

    /**
     * 返回图片的alpha 值
     */
    public static Integer[] readImage(String filePath) {
        int[] abgr = new int[4];
        File file = new File(filePath);
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = bi.getWidth();
        int height = bi.getHeight();
        int minx = bi.getMinX();
        int miny = bi.getMinY();
        List<Integer> list = new ArrayList<Integer>();
        for (int i = minx; i < width; i++) {
            for (int j = miny; j < height; j++) {
                int pixel = bi.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
                abgr[0] = ((pixel & 0xff000000) >> 24);
                //abgr[1] = ((pixel & 0xff0000) >> 16);
                //abgr[2] = ((pixel & 0xff00) >> 8);
                //abgr[3] = (pixel & 0xff);
                list.add(abgr[0]);
            }
        }
        Integer[] integers = new Integer[list.size()];
        list.toArray(integers);
        return integers;
    }

    public static Map<Integer, Integer[]> map = new HashMap<Integer, Integer[]>();

    public static void readPic(String filePath) {
        Integer[] wh = imageWH(filePath);
        if (wh != null && wh.length == 2) {
            if(wh[0]==7){
                for (int i = 0; i < 10; i++) {
                    map.put(i, readImage("D:/train/moon/numpic/" + i + ".png"));
                }
            } else {
                for(int i = 10; i < 13 ; i++){
                    map.put(i,readImage("D:/train/moon/dou/"+i+".png"));
                }
            }
        }

        Integer[] des = readImage(filePath);
        int score = 10000;
        int num = 0;
        for (Map.Entry<Integer, Integer[]> entry : map.entrySet()) {
            int n = Util.compareSimilar(entry.getValue(), des);
            System.out.println("偏移量：" + n + " 数字：" + entry.getKey());
            if (n < score) {
                score = n;
                num = entry.getKey();
            }
        }
        System.out.println("结果是:" + num);

    }

    /**
     * 返回图片的宽高
     * [0] 宽
     * [1] 高
     */
    public static Integer[] imageWH(String filePath) {
        File file = new File(filePath);
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = bi.getWidth();
        int height = bi.getHeight();
        return new Integer[]{width, height};
    }

}
