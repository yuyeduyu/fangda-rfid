package com.example.uhfsdkdemo.bean;

/**
 * 作者：lish on 2019-10-11 16:54
 * 描述：
 */
public class SettlementBean {

    //    产品名称
    private String productName;
    //    价格
    private String priceAdjustment;
    //    是否含税
    private String isIncludeTax;
    //    供应商名称  工厂
    private String supplierName;
    //    匹数
    private String numberOfCheckouts;
    //    姆米
    private String mmi;
    //    日期
    private String deliverySupplierDate;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPriceAdjustment() {
        return priceAdjustment;
    }

    public void setPriceAdjustment(String priceAdjustment) {
        this.priceAdjustment = priceAdjustment;
    }

    public String getIsIncludeTax() {
        return isIncludeTax;
    }

    public void setIsIncludeTax(String isIncludeTax) {
        this.isIncludeTax = isIncludeTax;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getNumberOfCheckouts() {
        return numberOfCheckouts;
    }

    public void setNumberOfCheckouts(String numberOfCheckouts) {
        this.numberOfCheckouts = numberOfCheckouts;
    }

    public String getMmi() {
        return mmi;
    }

    public void setMmi(String mmi) {
        this.mmi = mmi;
    }

    public String getDeliverySupplierDate() {
        return deliverySupplierDate;
    }

    public void setDeliverySupplierDate(String deliverySupplierDate) {
        this.deliverySupplierDate = deliverySupplierDate;
    }
}
