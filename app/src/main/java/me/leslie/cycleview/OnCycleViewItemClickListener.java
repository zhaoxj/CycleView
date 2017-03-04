package me.leslie.cycleview;

/**
 * 介绍：
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-03-02  14:30
 */

public interface OnCycleViewItemClickListener<T> {

    void onClick(final int position, final T t);
}
