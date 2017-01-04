package me.leslie.demo;

import me.leslie.cycleview.ICycleData;

/**
 * 介绍：
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-01-03  18:27
 */

public class TestData implements ICycleData {
    private String url;


    public TestData(String url) {
        this.url = url;
    }

    @Override
    public String getImgUrl() {
        return url;
    }
}
