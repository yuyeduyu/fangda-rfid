package com.example.uhfsdkdemo.bean;

/**
 * 作者：lish on 2018-10-30 11:03
 * 描述：
 */
public class RespBean {
    private String info;//门面库
    private String code;//code码
    private String status;// 1，成功 2，失败

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
