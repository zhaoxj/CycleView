package me.leslie.cycleview;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 介绍：不同项的实现ViewModel的基类
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-03-02  14:24
 */

public abstract class BaseCycleViewModel<T> {
    private Context context;
    private List<T> list = new ArrayList<>();
    private CycleView adapter;

    public BaseCycleViewModel() {
    }

    public BaseCycleViewModel(List<T> list) {
        this.list = list;
    }

    public BaseCycleViewModel<T> setContext(Context context) {
        this.context = context;
        return this;
    }

    public final T getItem(int position){
        if (position > -1 && getItemCount() > position){
            return list.get(position);
        }
        return null;
    }

    public BaseCycleViewModel<T> setList(List<T> list) {
        if (null != list){
            this.list.clear();
            this.list.addAll(list);
        }
        return this;
    }

    public BaseCycleViewModel<T> addItem(T t){
        list.add(t);
        return this;
    }

    public BaseCycleViewModel<T> remove(int position){
        if (position > -1 && getItemCount() > position) {
            list.remove(position);
        }
        return this;
    }

    public BaseCycleViewModel<T> remove(T t){
        list.remove(t);
        return this;
    }

    public final List<T> getList(){
        return list;
    }

    public final Context getContext(){
        return context;
    }

    public final BaseCycleViewModel<T> setAdapter(CycleView adapter) {
        this.adapter = adapter;
        return this;
    }

    protected final void setIntrText(String s){
        if (null != adapter){
            adapter.setIntrText(s);
        }
    }

    /**
     * 数据double
     * @return
     */
    public final BaseCycleViewModel<T> onDeoublData(){
        List<T> temp = new ArrayList<>();
        temp.addAll(list);
        list.addAll(temp);
        return this;
    }

    /**
     * 当前ViewModel对应有多少项
     * @return
     */
    public final int getItemCount(){
        return list.size();
    }

    /**
     * 创建View并绑定数据
     * @param position
     * @param t
     * @return
     */
    protected abstract View onCreatView(final int position, final T t);



    /**
     * 当前页面被选中时的处理，如果有页面介绍的，需在该API中调用CycleView的setIntrText()
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
