package me.leslie.cycleview;

import android.support.v4.view.ViewPager;

import java.util.List;

/**
 * 介绍：
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-03-02  14:10
 */

interface ICycleView {
    int GRAVITY_LEFT_CENTER_VERTICAL = 1;
    int GRAVITY_RIGHT_CENTER_VERTICAL = 2;
    int GRAVITY_CENTER = 3;
    int GRAVITY_TOP_LEFT = 4;
    int GRAVITY_TOP_CENTER = 5;
    int GRAVITY_TOP_RIGHT = 6;
    int GRAVITY_BOTTOM_LEFT = 7;
    int GRAVITY_BOTTOM_CENTER = 8;
    int GRAVITY_BOTTOM_RIGHT = 9;


    /**
     * 设置是否可以自动轮播
     * @param isAutoPlay
     * @return
     */
    BaseCycleView setAutoPlay(boolean isAutoPlay);

    /**
     * 设置自动轮播时间
     * @param time
     */
    BaseCycleView setAutoPlayTime(int time);

    /**
     * 设置是否显示底部介绍
     * @param displayIntr
     */
    BaseCycleView setDisplayIntr(boolean displayIntr);

    /**
     * 设置底部文字介绍的背景色
     * @param color
     * @return
     */
    BaseCycleView setIntroBackgroundColor(int color);

    /**
     * 设置是否显示切换指示器
     * @return
     */
    BaseCycleView setDisplayIndicator(boolean display);

    /**
     * 设置指示器默认图
     * @param resId
     * @return
     */
    BaseCycleView setIndicatorDefaultResId(int resId);

    /**
     * 设置指示器选中图
     * @param resId
     * @return
     */
    BaseCycleView setIndicatorFocusResId(int resId);

    /**
     * 设置指示器位置
     * @param gravity {@link #GRAVITY_LEFT_CENTER_VERTICAL}
     *                {@link #GRAVITY_RIGHT_CENTER_VERTICAL}
     *                {@link #GRAVITY_CENTER}
     *                {@link #GRAVITY_TOP_LEFT}
     *                {@link #GRAVITY_TOP_CENTER}
     *                {@link #GRAVITY_TOP_RIGHT}
     *                {@link #GRAVITY_BOTTOM_LEFT}
     *                {@link #GRAVITY_BOTTOM_CENTER}
     *                {@link #GRAVITY_BOTTOM_RIGHT}
     * @return
     */
    BaseCycleView setIndicatorGravity(int gravity);

    /**
     * 设置介绍文字大小
     * @param spSize
     * @return
     */
    BaseCycleView setIntroTextSize(int spSize);

    /**
     * 设置介绍文字颜色
     * @param color
     * @return
     */
    BaseCycleView setIntroTextColor(int color);

    /**
     * 设置数据
     * @param list
     */
    BaseCycleView setData(List<?> list);

    /**
     * 添加数据,添加完成必须调用notifyDataSetChanged()
     * @param o
     * @return
     */
    BaseCycleView add(Object o);


    /**
     * 设置轮播指示器和简介
     * @return
     */
    BaseCycleView setIndicatorIntro();

    /**
     * 初始化底部View
     * @return
     */
    BaseCycleView initBottomLayout();

    /**
     * 初始化指示器
     * @return
     */
    BaseCycleView initIndicator(boolean isRelayout);

    /**
     * 初始化简介
     * @return
     */
    BaseCycleView initIntr();

    /**
     * 设置当前项简介
     * @param s
     */
    void setIntrText(String s);

    /**
     * 设置切换动画效果
     * @param tf
     */
    void setPageTransformer(ViewPager.PageTransformer tf);

    /**
     * 获取设定最大数据
     * @return
     */
    int getMaxCount();

    /**
     * 获取item数据
     * @param position
     * @return
     */
    Object getItem(int position);


    /**
     * 处理两张图片切换时的闪白问题
     */
    void onFlingWhite();

    /**
     * 切换
     * @param index
     */
    void onPageChange(int index);

    /**
     * 开启轮播
     */
    void startPlay();

    /**
     * 停止轮播
     */
    void stopPlay();

    /**
     * 刷新
     */
    void notifyDataSetChanged();

}
