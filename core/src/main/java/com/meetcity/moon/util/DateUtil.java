package com.meetcity.moon.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wds on 2017/11/13.
 * <p>
 * 日期的工具类
 *
 * @author moon
 */
public class DateUtil {

    public static String TIME_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static String TIME_DAY = "yyyy-MM-dd";


    /**
     * 将 String 类型的日期解析成 Date 类型的
     * <p>
     * String ---> Date
     *
     * @param strDate 传入的日期
     * @param pattern 日期格式      yyyy-MM-dd HH:mm:ss
     */
    public static Date parseDate(String strDate, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = null;
        if (StringUtil.isEmpty(strDate) || StringUtil.isEmpty(pattern)) {
            return null;
        }
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将 Date 类型的日期解析成 String类型的日期
     * <p>
     * Date ---> String
     *
     * @param date    要转化的日期
     * @param pattern 日期格式   yyyy-MM-dd HH:mm:ss
     */
    public static String parseDate(Date date, String pattern) {
        if (date == null || StringUtil.isEmpty(pattern)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }


    /**
     * 将时间戳转化成string日期
     */
    public static String parseDate(Long timeStrap, String pattern) {
        if (timeStrap == null || StringUtil.isEmpty(pattern)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(timeStrap);
    }


    /**
     * 判断前一个日期是否比后一个日期小
     * <p>
     * 相同日期也返回false
     *
     * @param startDate 前一个日期
     * @param endDate   后一个日期
     * @param pattern   两个日期的格式
     */
    public static boolean isSmall(String startDate, String endDate, String pattern) {
        Date start = parseDate(startDate, pattern);
        Date end = parseDate(endDate, pattern);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);

        int result = startCalendar.compareTo(endCalendar);
        return result < 0;
    }

    /**
     * 日期的类型
     * <p>
     * 天，月，年
     */
    public enum DateAtom {
        DAY, MONTH, YEAR
    }

    /**
     * 生成一组连续的日期
     *
     * @param startDate  开始日期
     * @param endDate    结束日期
     * @param dateFormat 日期格式
     * @param atom       日期间隔(天，月，年)
     */
    public static List<String> createContinueDate(String startDate, String endDate, String dateFormat, DateAtom atom) {
        if (StringUtil.isEmpty(startDate) || StringUtil.isEmpty(endDate) || StringUtil.isEmpty(dateFormat)) {
            return null;
        }
        List<String> list = new ArrayList<>();
        Date start = parseDate(startDate, dateFormat);
        Date end = parseDate(endDate, dateFormat);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);

        switch (atom) {
            case DAY:
                while (endCalendar.compareTo(startCalendar) > 0) {
                    startCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    if (endCalendar.compareTo(startCalendar) > 0) {
                        list.add(parseDate(startCalendar.getTime(), dateFormat));
                    }
                }
                break;
            case MONTH:
                while (endCalendar.compareTo(startCalendar) > 0) {
                    startCalendar.add(Calendar.MONTH, 1);
                    if (endCalendar.compareTo(startCalendar) > 0) {     //这里加条件，避免 1-10 ,累加后,成7-10,会再加一次成了8-20,但是却比7-20
                        list.add(parseDate(startCalendar.getTime(), dateFormat));
                    }
                }
                break;
            case YEAR:
                while (endCalendar.compareTo(startCalendar) > 0) {
                    startCalendar.add(Calendar.YEAR, 1);
                    if (endCalendar.compareTo(startCalendar) > 0) {
                        list.add(parseDate(startCalendar.getTime(), dateFormat));
                    }
                }
                break;
        }
        return list;
    }


    /**
     * 判断一个日期字符串 是否符合传入的 格式
     * <p>
     * 默认使用 {严格模式}
     *
     * @param dateStr 日期
     * @param pattern 格式
     */
    public static boolean isConformDateFormat(String dateStr, String pattern) {
        return isConformDateFormat(dateStr, pattern, true);
    }

    /**
     * 判断一个字符串 是否符合传入的格式
     *
     * @param dateStr  日期
     * @param pattern  格式
     * @param isStrict 是否是严格模式
     *                 严格模式：会判断传入的日期的字符串长度和格式的字符串长度是否相等
     *                 避免：日期{ 2017-09-01 11:00:22 } 使用 { yyyy-MM-dd }格式， 仍然能解析出日期
     */
    public static boolean isConformDateFormat(String dateStr, String pattern, boolean isStrict) {

        if (isStrict) {
            return parseDate(dateStr, pattern) != null && dateStr.length() == pattern.length();
        } else {
            return parseDate(dateStr, pattern) != null;
        }

    }

    /**
     * 创建与传入日期间隔 几天的 日期
     *
     * @param startDate 今天的日期
     * @param pattern   格式
     * @param interval  间隔
     */
    public static String createDateAndBefore(String startDate, String pattern, int interval) {
        String beforeDate;
        if (StringUtil.isEmpty(startDate) || StringUtil.isEmpty(pattern)) {
            return null;
        }
        Date start = parseDate(startDate, pattern);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        startCalendar.add(Calendar.DAY_OF_MONTH, interval);
        beforeDate = parseDate(startCalendar.getTime(), pattern);
        return beforeDate;
    }


    /**
     * 获取时间戳
     * 13位的
     */
    public static Long getTimeStamp() {
        return System.currentTimeMillis();
    }

    /**
     * 获取时间戳
     * 10位的
     */
    public static Long getTimeStamp10() {
        return System.currentTimeMillis() / 1000;
    }

    public static void main(String[] args) {
        String tim = parseDate(System.currentTimeMillis(), TIME_DEFAULT);


        boolean t = isSmall("2017-09-07", "2017-09-08", "yyyy-MM-dd");
        System.out.println("");
    }
}
