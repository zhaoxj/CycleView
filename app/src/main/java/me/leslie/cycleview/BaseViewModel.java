package me.leslie.cycleview;

import android.view.View;

/**
 * 介绍：不同项的实现ViewModel的基类
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-03-02  14:24
 */

public abstract class BaseViewModel<T> {

    /**
     * 创建View并绑定数据
     * @param position
     * @param t
     * @return
     */
    protected abstract View onCreatView(final int position, final T t);

    /**
     * 当前页面被选中时的处理，如果有页面介绍的，需在该API中调用BaseCycleView的setIntrText()
     * @param position
     * @param t
     */
    public abstract void onPageSelected(final int position, final T t);

    /**
     * 点击事件
     * @param position
     * @param t
     */
    protected abstract void onClick(final int position, final T t);

    /**
     * 长按事件
     * @param position
     * @param t
     * @return
     */
    protected abstract boolean onLongClick(final int position, final T t);


}
