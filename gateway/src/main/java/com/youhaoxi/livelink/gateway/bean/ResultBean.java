package com.youhaoxi.livelink.gateway.bean;

import java.util.HashMap;

public class ResultBean {
    private Integer code;
    private String msg;
    private HashMap data;

    public ResultBean(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public ResultBean setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResultBean setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public HashMap getData() {
        return data;
    }

    public ResultBean setData(HashMap data) {
        this.data = data;
        return this;
    }
}
