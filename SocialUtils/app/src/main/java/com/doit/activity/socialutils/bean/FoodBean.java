package com.doit.activity.socialutils.bean;

/**
 * Created by lzh on 2018/9/5.
 */

public class FoodBean {

    private String code;
    private String msg;
    private Data data;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "code=" + code + "\nmsg=" + msg;
    }
}
