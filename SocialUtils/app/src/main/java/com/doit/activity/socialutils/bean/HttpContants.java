package com.doit.activity.socialutils.bean;

/**
 * Created by lzh on 2018/7/20.
 */

public class HttpContants {

    //天气10000次
    public static final String weatherInfo = "http://api.jisuapi.com/weather/query?";
    public static final String videoInfo = "http://120.76.205.241:8000/news/cctvplus?";
    //美食
    public static final String home_recommend_new = "https://api.meishi.cc/v8/home_recommend_new.php?";

    //美食
    public static final String foodinfo = "https://newapi.meishi.cc/index/home_feeds_classify?";
//    public static final String foodinfo = "https://newapi.meishi.cc/index/home_feeds_6_9_1?";

    //美食详情
    public static final String foodDetail = "https://newapi.meishi.cc/recipe/detail?source=android&format=json&fc=msjandroid&lat=39.961402&lon=116.449137&cityCode=131&token=faf0ef51ccb28290828cfe4f37dfb159&id=";

    //美食评分
    public static final String foodScore="https://newapi.meishi.cc/Recipe/YingYang?source=android&format=json&fc=msjandroid&lat=39.961373&lon=116.449179&cityCode=131&token=6751882830dc10c56a2e8d9b3ccc1e3d&type=YYList&news_id=";
}
