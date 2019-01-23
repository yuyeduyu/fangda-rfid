package com.example.uhfsdkdemo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 作者：lish on 2018-10-31 16:29
 * 描述：
 */
public class TimeUtils {
    public static String timeType = "yyyy-MM-dd  HH:mm:ss";
    /**
     * @param datdString Thu May 18 2017 00:00:00 GMT+0800 (中国标准时间)
     * @return 年月日;
     */
    public static String parseTime(String datdString) {
        datdString = datdString.replace("GMT", "").replaceAll("\\(.*\\)", "");
        //将字符串转化为date类型，格式2016-10-12
        SimpleDateFormat format = new SimpleDateFormat("MM dd , yyyy HH:mm:ss z", Locale.ENGLISH);
        Date dateTrans = null;
        try {
            dateTrans = format.parse(datdString);
            return new SimpleDateFormat(timeType).format(dateTrans).replace("-","/");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datdString;

    }

    public static String parseTime1(String data){
        if (data==null)
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a",Locale.ENGLISH);
        Date d2 ;
        String dateString = "";
        try {
            d2 = sdf.parse(data);

            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateString = sdf.format(d2);

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        return dateString;
    }
    /*
   * 将时间转换为时间戳
   */
    public static long dateToStamp(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        return date.getTime()/1000;
    }
    /**
     * 时间戳 转换 成timeType样式
     * @Author lish
     * @Date 2019-01-22 17:05
     */
    public static String longToString (long time,String timeType){
        SimpleDateFormat sdf=new SimpleDateFormat(timeType);//这个是你要转成后的时间的格式
        String sd = sdf.format(new Date(time));   // 时间戳转换成时间
        return sd;
    }
}
