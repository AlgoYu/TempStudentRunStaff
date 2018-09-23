package com.example.xiaoyu.tempstudentrunstaff.entity;


public class RequestTemplate<T> {
    private String action;
    private String key;
    private T data;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
