package com.example.uhfsdkdemo.event;

/**
 * Created by fengye on 2017/1/19.
 * email 1040441325@qq.com
 */
public class SearchEvent {
    private String content;//搜索内容

    public SearchEvent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
