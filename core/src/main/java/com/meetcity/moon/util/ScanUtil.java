package com.meetcity.moon.util;

import java.util.Scanner;

/**
 * Created by moon on 2018/3/12 .
 */
public class ScanUtil {

    public static String scan(String tip){
        System.out.println(tip);
        Scanner scanner = new Scanner(System.in);
        String msg = scanner.nextLine();
        return msg;
    }
}
