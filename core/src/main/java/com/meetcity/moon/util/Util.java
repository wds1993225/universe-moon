package com.meetcity.moon.util;

import com.meetcity.moon.constant.Constants;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;

/**
 * Created by wds on 2017/10/30.
 *
 * @author moon
 */
public class Util {

    /**
     * 日志，是否开启日志
     */
    public static void log(String content) {
        if (Constants.isOpenLog) {
            System.out.println(content);
        }
    }


    /**
     * 比较两个长度相等的数组的离散程度
     */
    public static int compareSimilar(Integer[] source, Integer[] des) {
        int score = 0;
        if (source.length == des.length) {
            for (int i = 0; i < source.length; i++) {
                score = score + Math.abs(source[i] - des[i]);
            }
        }
        return score;
    }

    /**
     * 测试一个地址能否ping通
     */
    public static boolean isReachable(String address) {
        int timeOut = 3000;
        boolean status = false;
        try {
            status = InetAddress.getByName(address).isReachable(timeOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }


    /**
     * 获取一个分页
     *
     * @param totalRow 总条数
     * @return 总页数
     */
    public static int getTotalPage(int totalRow) {
        if ((totalRow % 10) == 0) {
            return totalRow / 10;
        } else {
            return (totalRow / 10) + 1;
        }
    }

    @Deprecated
    public static void ping(String address) {
        Runtime runtime = Runtime.getRuntime();
        Process process;
        try {
            process = runtime.exec("ping " + address);
            InputStreamReader reader = new InputStreamReader(process.getInputStream());
            LineNumberReader lineNumberReader = new LineNumberReader(reader);
            String msg = "";
            String line = "";
            while ((line = lineNumberReader.readLine()) != null) {
                System.out.println(line);
                msg = msg + line;
            }
            if (msg.contains("100% loss")) {
                System.out.println("与" + address + "连接不通常");
            } else {
                System.out.println("与" + address + "连接正常");
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {

        }
    }

}
