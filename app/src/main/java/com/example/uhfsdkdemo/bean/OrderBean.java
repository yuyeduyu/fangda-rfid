package com.example.uhfsdkdemo.bean;

import android.support.annotation.NonNull;

import com.example.uhfsdkdemo.utils.TimeUtils;

import java.text.ParseException;

/**
 * 作者：lish on 2018-10-31 9:50
 * 描述：
 */
public class OrderBean implements Comparable<OrderBean> {
    // [{"name":"140/弹力缎16","orderNumber":"6.000","riceNumber":0.000,"orderTime":"Aug 20, 2015 12:00:00 AM"
// ,"shippingDate":"Oct 31, 2018 12:00:00 AM"}

//    面料名称      进厂日期      匹数       进厂米数      出货日期
    private String name;
    private String orderTime;
    private double orderNumber;
    private double riceNumber;
    private String shippingDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public double getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(double orderNumber) {
        this.orderNumber = orderNumber;
    }

    public double getRiceNumber() {
        return riceNumber;
    }

    public void setRiceNumber(double riceNumber) {
        this.riceNumber = riceNumber;
    }

    public String getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(String shippingDate) {
        this.shippingDate = shippingDate;
    }

    @Override
    public int compareTo(@NonNull OrderBean o) {
        try {
            return (int) -(TimeUtils.dateToStamp(TimeUtils.parseTime1(orderTime))
                    -TimeUtils.dateToStamp(TimeUtils.parseTime1(o.getOrderTime())));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
