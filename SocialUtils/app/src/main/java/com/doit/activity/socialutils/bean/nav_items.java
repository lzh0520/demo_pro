package com.doit.activity.socialutils.bean;

/**
 * Created by lzh on 2018/9/5.
 */

public class nav_items {

    private String title;
    private String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "title=" + title + "\ntype=" + type;
    }

}
