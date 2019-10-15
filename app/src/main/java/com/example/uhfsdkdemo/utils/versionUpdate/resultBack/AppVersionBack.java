package com.example.uhfsdkdemo.utils.versionUpdate.resultBack;

/**
 * 作者：lish on 2018-07-24.
 * 描述：
 */

public class AppVersionBack {
//    {"code":200,"mes":"????????","flag":true,"data":"
    private int code;
    private String mes;
    private boolean flag;
    private AppVersion data;

    public AppVersion getData() {
        return data;
    }

    public void setData(AppVersion data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public class AppVersion{
//        "data":"{ "appName":"wxldC", "versionName":"3.2.2", "versionCode":, "des":"1.版本更新 2.添加功能 3.测试数据" } "}
        private String appName;
        private String versionName;
        private int versionCode;
        private String des;

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }
    }
}
