package com.example.uhfsdkdemo.bean;

/**
 * 作者：lish on 2018-10-31 12:19
 * 描述：
 */
public class InfoData {
    public static String isLocked = "1";
    public static String unLocked = "0";
    private String name;
    private int id;
    private String color;
    private String length;
    private String lock;

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
