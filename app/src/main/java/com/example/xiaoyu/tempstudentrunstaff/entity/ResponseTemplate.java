package com.example.xiaoyu.tempstudentrunstaff.entity;

public class ResponseTemplate<T> {
    private Boolean succes = false;
    private String message = "暂无附加信息！";
    private T data;

    public Boolean getSucces() {
        return succes;
    }

    public void setSucces(Boolean succes) {
        this.succes = succes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
