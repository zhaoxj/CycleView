//package me.leslie.demo;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.util.AttributeSet;
//import android.view.View;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import me.leslie.cycleview.BaseCycleView;
//import me.leslie.cycleview.BaseViewModel;
//
///**
// * 介绍：
// * 作者：xjzhao
// * 邮箱：mr.feeling.heart@gmail.com
// * 时间: 2017-03-02  15:14
// */
//
//public class TestCycleView extends BaseCycleView {
//
//    public TestCycleView(Context context, BaseViewModel... vms) {
//        super(context, vms);
//    }
//
//    public TestCycleView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    /**
//     * 类型1
//     */
//    class Model1 extends BaseViewModel<String>{
//        @Override
//        protected View onCreatView(int position, String s) {
//            WebView view = new WebView(getContext());
//            view.setWebViewClient(new WebViewClient());
//            view.loadUrl(s);
//            return view;
//        }
//
//        @Override
//        public void onPageSelected(int position, String s) {
//            setIntrText(s);
//        }
//
//        @Override
//        protected void onClick(int position, @NonNull String s) {
//
//        }
//
//        @Override
//        protected boolean onLongClick(int position, @NonNull String s) {
//            return false;
//        }
//    }
//
//
//    /**
//     * 类型2
//     */
//    class Model2 extends BaseViewModel<TestData>{
//        @Override
//        protected View onCreatView(int position, TestData data) {
//            ImageView img = new ImageView(getContext());
//            LoadImgUtils.loadImage(img, data.getImgUrl());
//            return img;
//        }
//
//        @Override
//        public void onPageSelected(int position, TestData data) {
//            setIntrText(data.getImgUrl());
//        }
//
//        @Override
//        protected void onClick(int position, TestData testData) {
//            Toast.makeText(getContext(), "点击了: " + position, Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        protected boolean onLongClick(int position, TestData testData) {
//            return false;
//        }
//
//    }
//}
