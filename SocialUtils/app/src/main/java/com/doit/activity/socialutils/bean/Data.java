package com.doit.activity.socialutils.bean;

import java.util.List;

/**
 * Created by lzh on 2018/9/5.
 */

public class Data {


    private String hello_msg;
    private List<nav_items> nav_items;

    public String getHello_msg() {
        return hello_msg;
    }

    public void setHello_msg(String hello_msg) {
        this.hello_msg = hello_msg;
    }


    public List<nav_items> getNav_items() {
        return nav_items;
    }

    public void setNav_items(List<nav_items> nav_items) {
        this.nav_items = nav_items;
    }

    @Override
    public String toString() {
        return "hello_msg=" + hello_msg;
    }
}
