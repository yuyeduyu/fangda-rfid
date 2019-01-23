package com.example.uhfsdkdemo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：lish on 2019-01-23 13:30
 * 描述：
 */
public class DiaoBoResultBean {

    private int pages;
    private int count;
    private List<DiaoBoBean> list;

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

    public List<DiaoBoBean> getList() {
        return list;
    }

    public void setList(List<DiaoBoBean> list) {
        this.list = list;
    }

    public class DiaoBoBean implements Serializable {
        private String barcode;
        //        设备名称
        private String deviceName;
        //        扫描时间
        private String scanningTime;
        //        订单编号
        private String orderNo;
        //        日期
        private String timeKct;
        //        品名
        private String productName;
        //        颜色
        private String color;
        //        米数
        private float length;
        //        缩率
        private String shrinkage;
        //        库房
        private String storeroom;
        //        冻结人
        private String frozenMan;
        private String ExcelServerRCID;
        private String ExcelServerRN;
        private String ExcelServerCN;
        private String ExcelServerRC1;
        private String ExcelServerWIID;
        private String ExcelServerRTID;
        private String ExcelServerCHG;
        //        是否调拨
        private String isAllocation;
        //        起始日期
        private String startTime;
        //        截止日期
        private String endTime;
        //        当前用户
        private String nowUser;
        //        库房
        private String storeroom2;
        //        日期
        private String timeBsi;
        //        是否手填
        private String isHandFilling;
        //        调拨编号
        private String allocationNo;

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

        public String getExcelServerRCID() {
            return ExcelServerRCID;
        }

        public void setExcelServerRCID(String excelServerRCID) {
            ExcelServerRCID = excelServerRCID;
        }

        public String getExcelServerRN() {
            return ExcelServerRN;
        }

        public void setExcelServerRN(String excelServerRN) {
            ExcelServerRN = excelServerRN;
        }

        public String getExcelServerCN() {
            return ExcelServerCN;
        }

        public void setExcelServerCN(String excelServerCN) {
            ExcelServerCN = excelServerCN;
        }

        public String getExcelServerRC1() {
            return ExcelServerRC1;
        }

        public void setExcelServerRC1(String excelServerRC1) {
            ExcelServerRC1 = excelServerRC1;
        }

        public String getExcelServerWIID() {
            return ExcelServerWIID;
        }

        public void setExcelServerWIID(String excelServerWIID) {
            ExcelServerWIID = excelServerWIID;
        }

        public String getExcelServerRTID() {
            return ExcelServerRTID;
        }

        public void setExcelServerRTID(String excelServerRTID) {
            ExcelServerRTID = excelServerRTID;
        }

        public String getExcelServerCHG() {
            return ExcelServerCHG;
        }

        public void setExcelServerCHG(String excelServerCHG) {
            ExcelServerCHG = excelServerCHG;
        }

        public String getIsAllocation() {
            return isAllocation;
        }

        public void setIsAllocation(String isAllocation) {
            this.isAllocation = isAllocation;
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

        public String getNowUser() {
            return nowUser;
        }

        public void setNowUser(String nowUser) {
            this.nowUser = nowUser;
        }

        public String getStoreroom2() {
            return storeroom2;
        }

        public void setStoreroom2(String storeroom2) {
            this.storeroom2 = storeroom2;
        }

        public String getTimeBsi() {
            return timeBsi;
        }

        public void setTimeBsi(String timeBsi) {
            this.timeBsi = timeBsi;
        }

        public String getIsHandFilling() {
            return isHandFilling;
        }

        public void setIsHandFilling(String isHandFilling) {
            this.isHandFilling = isHandFilling;
        }

        public String getAllocationNo() {
            return allocationNo;
        }

        public void setAllocationNo(String allocationNo) {
            this.allocationNo = allocationNo;
        }
    }
}
