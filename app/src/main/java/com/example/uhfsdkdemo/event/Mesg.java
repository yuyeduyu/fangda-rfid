package com.example.uhfsdkdemo.event;

/**
 * 作者：lish on 2019-01-23 10:55
 * 描述：
 */
public class Mesg {
    public static String updateChuKuTabCount = "updateChuKuTabCount";
    public static String updatediaoBoTabCount = "updatediaoBoTabCount";
    private String Mes;
    private int count;

    public Mesg(String Mes ){
        this.Mes = Mes;
    }
    public Mesg(String Mes ,int count){
        this.Mes = Mes;
        this.count = count;
    }
    public String getMes() {
        return Mes;
    }

    public void setMes(String mes) {
        Mes = mes;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
