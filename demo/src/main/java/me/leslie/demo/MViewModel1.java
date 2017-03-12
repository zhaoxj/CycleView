package me.leslie.demo;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.List;

import me.leslie.cycleview.BaseCycleViewModel;

/**
 * 介绍：
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-03-11  22:46
 */

public class MViewModel1 extends BaseCycleViewModel<String> {

    public MViewModel1() {
    }

    public MViewModel1(List<String> list) {
        super(list);
    }

    @Override
    protected View onCreatView(int position, String s) {
        WebView view = new WebView(getContext());
        view.setWebViewClient(new WebViewClient());
        view.loadUrl(s);
        return view;
    }


    @Override
    public void onPageSelected(int position, String s) {
        setIntrText(s);
    }

    @Override
    protected void onClick(int position, String s) {

    }

    @Override
    protected boolean onLongClick(int position, String s) {
        return false;
    }
}
