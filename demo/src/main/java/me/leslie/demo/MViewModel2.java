package me.leslie.demo;

import android.view.View;
import android.widget.ImageView;

import java.util.List;

import me.leslie.cycleview.BaseCycleViewModel;

/**
 * 介绍：
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-03-11  22:51
 */

public class MViewModel2 extends BaseCycleViewModel<TestData> {

    public MViewModel2() {
    }

    public MViewModel2(List<TestData> list) {
        super(list);
    }

    @Override
    protected View onCreatView(int position, TestData data) {
        ImageView img = new ImageView(getContext());
        LoadImgUtils.loadImage(img, data.getImgUrl());
        return img;
    }

    @Override
    public void onPageSelected(int position, TestData data) {
        setIntrText(data.getImgUrl());
    }

    @Override
    protected void onClick(int position, TestData data) {

    }

    @Override
    protected boolean onLongClick(int position, TestData data) {
        return false;
    }
}
