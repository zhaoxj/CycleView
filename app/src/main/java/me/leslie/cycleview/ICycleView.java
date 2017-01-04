package me.leslie.cycleview;

import java.util.List;

/**
 * 介绍：
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-01-03  17:21
 */

public interface ICycleView<T extends ICycleData> {

    /**
     * 获取设定最大数据
     * @return
     */
    int getMaxCount();

    /**
     * 重新设定宽高参数
     * @param w
     * @param h
     */
    void resize(int w, int h);

    /**
     * 设置数据
     * @param list
     */
    BaseCycleView<T> setData(List<T> list);

    /**
     * 获取item数据
     * @param position
     * @return
     */
    T getItem(int position);

    /**
     * 是否开启自动播放
     * @return
     */
    boolean isNeedAutoPlay();

    /**
     * 自动播放间隔时间
     * @return
     */
    int getAutoPlayTime();

    /**
     * 处理两张图片切换时的闪白问题
     */
    void onFlingWhite();

    /**
     * 指示器设置
     */
    void setPageIndicator();

    /**
     * 切换
     * @param index
     */
    void onPageChange(int index);

    /**
     * 刷新
     */
    void update();

    /**
     * 开启轮播
     */
    void startPlay();

    /**
     * 停止轮播
     */
    void stopPlay();
}
