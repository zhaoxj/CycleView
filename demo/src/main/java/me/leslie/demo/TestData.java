package me.leslie.demo;

/**
 * 介绍：
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-01-03  18:27
 */

public class TestData {
    private String url;


    public TestData(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "TestData{" +
                "url='" + url + '\'' +
                '}';
    }
}
