package com.example.uhfsdkdemo.utils.versionUpdate.resultBack;

import java.util.List;

/**
 * 作者：lish on 2018-08-20.
 * 描述：服务器获取布控目标，数据返回
 */

public class TargetFromNetRuslt {
    private List<Target> data;

    public List<Target> getData() {
        return data;
    }

    public void setData(List<Target> data) {
        this.data = data;
    }

    public class Target{
//        {
//            "name": "告警设备测试",
//                "valueStr": "88-b1-11-a5-1d-bf"
//        },
        private String name;
        private String valueStr;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValueStr() {
            return valueStr;
        }

        public void setValueStr(String valueStr) {
            this.valueStr = valueStr;
        }
    }
}
