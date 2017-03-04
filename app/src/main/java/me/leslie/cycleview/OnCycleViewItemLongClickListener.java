package me.leslie.cycleview;

/**
 * 介绍：
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-03-02  14:31
 */

public interface OnCycleViewItemLongClickListener<T> {
    void onLongClick(final int position, final T t);
}
