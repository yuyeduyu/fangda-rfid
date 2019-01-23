package com.example.uhfsdkdemo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：lish on 2019-01-22 15:21
 * 描述：出库记录
 */
public class ChukuResultBean {
    private int pages;
    private int count;
    private List<ChukuBean> list;

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ChukuBean> getList() {
        return list;
    }

    public void setList(List<ChukuBean> list) {
        this.list = list;
    }

    public class ChukuBean implements Serializable{
        private String barcode;
        //    设备名称
        private String deviceName;
        //            扫描时间
        private String scanningTime;
        //    订单编号
        private String orderNo;
        //            日期
        private String timeKct;
        //    品名
        private String productName;
        //            颜色
        private String color;
        //    米数
        private float length;
        //            缩率
        private String shrinkage;
        //    库房
        private String storeroom;
        //            冻结人
        private String frozenMan;

        //    是否出库
        private String isOut;
        //            作废
        private String cancellation;
        //    起始日期
        private String startTime;
        //            截止日期
        private String endTime;
        //    是否手填
        private String isHandFilling;
        //            出库单号
        private String outOrderNo;

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getScanningTime() {
            return scanningTime;
        }

        public void setScanningTime(String scanningTime) {
            this.scanningTime = scanningTime;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getTimeKct() {
            return timeKct;
        }

        public void setTimeKct(String timeKct) {
            this.timeKct = timeKct;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public float getLength() {
            return length;
        }

        public void setLength(float length) {
            this.length = length;
        }

        public String getShrinkage() {
            return shrinkage;
        }

        public void setShrinkage(String shrinkage) {
            this.shrinkage = shrinkage;
        }

        public String getStoreroom() {
            return storeroom;
        }

        public void setStoreroom(String storeroom) {
            this.storeroom = storeroom;
        }

        public String getFrozenMan() {
            return frozenMan;
        }

        public void setFrozenMan(String frozenMan) {
            this.frozenMan = frozenMan;
        }

        public String getIsOut() {
            return isOut;
        }

        public void setIsOut(String isOut) {
            this.isOut = isOut;
        }

        public String getCancellation() {
            return cancellation;
        }

        public void setCancellation(String cancellation) {
            this.cancellation = cancellation;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getIsHandFilling() {
            return isHandFilling;
        }

        public void setIsHandFilling(String isHandFilling) {
            this.isHandFilling = isHandFilling;
        }

        public String getOutOrderNo() {
            return outOrderNo;
        }

        public void setOutOrderNo(String outOrderNo) {
            this.outOrderNo = outOrderNo;
        }
    }
}
