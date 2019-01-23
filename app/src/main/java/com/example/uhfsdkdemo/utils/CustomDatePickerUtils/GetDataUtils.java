package com.example.uhfsdkdemo.utils.CustomDatePickerUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/27.
 */
public class GetDataUtils {
    /**
     * 获取指定日后 后 dayAddNum 天的 日期
     *
     * @param day       日期，格式为String："2013-9-3";
     * @param dayAddNum 增加天数 格式为int;
     * @return
     */
    public static String getDateStrByMint(String day, int dayAddNum) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date newDate2 = new Date(nowDate.getTime() + (long) dayAddNum * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }

    /**
     * 获取指定日后 后 dayAddNum 天的 日期
     *
     * @param day       日期，格式为String："2013-9-3";
     * @param dayAddNum 增加天数 格式为int;
     * @return
     */
    public static String getDateStrByDay(String day, int dayAddNum) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date newDate2 = new Date(nowDate.getTime() + dayAddNum * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }

    /**
     * 比较2个日期字符串大小
     *
     * @param s1
     * @param s2
     * @return 0 日期相同,1 s1>s2,2 s1<s2
     * @throws Exception
     */
    public static boolean DateCompare(String s1, String s2) throws Exception {
        //设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //得到指定模范的时间
        Date d1 = sdf.parse(s1 + " 00:00:00");
        Date d2 = sdf.parse(s2 + " 00:00:00");

        //比较
        int result = d1.compareTo(d2);
        if (result > 0)
            return true;
        else
            return false;
    }
    /**
     * q日期比较大小
     *
     * @param DATE1
     * @param DATE2
     * @return 返回0 为同一天
     * 返回1 dt1 在dt2前
     * 返回-1 dt1在dt2后
     */
    public static int compare_date(String DATE1, String DATE2,String type) {

        DateFormat df = new SimpleDateFormat(type);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前时间 格式yyyy年MM月dd日    HH:mm:ss
     *
     * @return
     */
    public static String getNowTime(String type) {
        SimpleDateFormat formatter = new SimpleDateFormat(type);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 获取当前时间 格式yyyy年MM月dd日    HH:mm:ss
     *
     * @return
     */
    public static String getNowTime2() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 获取当前时间 格式yyyy年MM月dd日    HH:mm
     *
     * @return
     */
    public static String getNowTime1() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param type      yyyy-MM-dd
     * @return 返回相差天数  -1为计算错误
     */
    public static int differentDaysByMillisecond(String startTime, String endTime, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        try {
            Date date1 = sdf.parse(startTime);
            Date date2 = sdf.parse(endTime);
            int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
            return days;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
    /**
     * 根据日期获取当天0点时间戳
     * @author lish
     * created at 2018-08-13 16:55
     */
    public static long getLongTimeByDay(String day, String type){
        DateFormat df = new SimpleDateFormat(type);
        Date date = null;
        try {
            date = df.parse(day);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            long timestamp = cal.getTimeInMillis();
            return timestamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
    // 使用当前月份,得到上一个月的月份:月份的格式是:yyyy-MM
    public static String getLastDate(String currentDate,int days,String type) {

        SimpleDateFormat sdf = new SimpleDateFormat(type);
        Date date = null;
        try {
            date = sdf.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -days);

        String lastDate = c.get(Calendar.YEAR) + "-"
                + String.format("%02d",c.get(Calendar.MONTH) + 1);
//        LogUtils.e("lastDate",lastDate);
        return lastDate;
    }
}
