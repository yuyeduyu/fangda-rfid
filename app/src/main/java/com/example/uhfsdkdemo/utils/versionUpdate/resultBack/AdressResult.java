package com.example.uhfsdkdemo.utils.versionUpdate.resultBack;

/**
 * 作者：lish on 2018-07-24.
 * 描述：
 */

public class AdressResult {
    private int code;
    private String mes;
    private boolean flag;
    private Adress data;

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

    public Adress getData() {
        return data;
    }

    public void setData(Adress data) {
        this.data = data;
    }

    public class Adress {
        private long ap_mac; //= rs.getLong("ap_mac");
        private String addr; //= rs.getString("addr");
        private String longitude;// = rs.getString("longitude");
        private String latitude; //= rs.getString("latitude");

        public long getAp_mac() {
            return ap_mac;
        }

        public void setAp_mac(long ap_mac) {
            this.ap_mac = ap_mac;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }
    }
}
