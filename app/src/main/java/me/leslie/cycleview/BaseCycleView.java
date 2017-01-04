package me.leslie.cycleview;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 介绍：自动切换View基类
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-01-03  17:14
 */

public abstract class BaseCycleView<T extends ICycleData> extends RelativeLayout implements ICycleView, ViewPager.OnPageChangeListener{

    protected List<T> list;
    protected int size;
    protected int realSize;
    protected Context context;
    protected CycleViewPager viewPager;
    protected CycleViewAdapter adapter;
    private boolean isAutoPlaying;
    private Handler handler;
    private boolean isAddList;
    private LinearLayout indicatorLayout;
    private int lastWidth;
    private int lastHeight;
    private int[] pageIndecatorIds;
    private SparseArray<ImageView> indecatorViews = new SparseArray<>();

    /**
     * 获取并处理每个ITEM
     * @param position
     * @param t
     * @return
     */
    protected abstract View onCreatItemView(int position, @NonNull T t);

    /**
     * 点击Item
     * @param position
     * @param t
     */
    protected abstract void onClickItem(int position, @NonNull T t);

    /**
     * 长按Item
     * @param position
     * @param t
     */
    protected abstract boolean onLongClickItem(int position, @NonNull T t);

    /**
     * 获取选中和非选中的小点资源
     * @return
     */
    protected abstract int[] getPageIndecatorIds();

    public BaseCycleView(Context context) {
        super(context);
        init(context);
    }

    public BaseCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseCycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseCycleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        handler = new Handler();
        this.context = context;
        if (null == context) return;
        viewPager = new CycleViewPager(context);
        viewPager.addOnPageChangeListener(this);
        addView(viewPager, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        indicatorLayout = new LinearLayout(context);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        lp.bottomMargin = 20;
        addView(indicatorLayout, lp);
        pageIndecatorIds = getPageIndecatorIds();
    }

    @Override
    public BaseCycleView<T> setData(List list) {
        if (null == list) {
            list = new ArrayList<>();
        }

        this.list = list;
        if (list.isEmpty()) {
            resize(0, 0);
        } else {
            update();
        }

        realSize = list.size();
        onFlingWhite();
        size = list.size();
        //默认位置给到尺寸的3000倍
        int num = 1000;
        if (size <= 0){
            num = 0;
        } else if (size >= 5) {
            num = 600;
        } else if (size == 4) {
            num = 800;
        } else if (size == 3) {
            num = 1000;
        } else if (size == 2) {
            num = 1500;
        }
        // 设置indicator
        setPageIndicator();
        adapter = null;
        viewPager.setAdapter(adapter = new CycleViewAdapter());
        viewPager.setCurrentItem(size = size * num);
        if (isNeedAutoPlay()) {
            startPlay();
        }
        return this;
    }

    @Override
    public int getAutoPlayTime() {
        return 3000;
    }

    @Override
    public boolean isNeedAutoPlay() {
        return true;
    }

    @Override
    public void resize(int w, int h) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (null != lp){
            lastWidth = lp.width;
            lastHeight = lp.height;
            lp.width = w;
            lp.height = h;
        }
    }

    @Override
    public final void onFlingWhite() {
        if (null != list && 2 == list.size()) {
            list.add(2, list.get(0));
            list.add(3, list.get(1));
            isAddList = true;
        }
    }

    @Override
    public void setPageIndicator() {
        indicatorLayout.removeAllViews();
        indecatorViews.clear();
        if (realSize == 0) return;
        int size = realSize;
        if(isAddList&&realSize==4){
            realSize = size = 2;
        }
        if (isIndecator()) {
            for (int count = 0; count < size; count++) {
                // 翻页指示的点
                ImageView pointView = new ImageView(getContext());
                pointView.setPadding(5, 0, 5, 0);
                if (0 == indecatorViews.size()){
                    pointView.setImageResource(pageIndecatorIds[1]);
                }else {
                    pointView.setImageResource(pageIndecatorIds[0]);
                }
                indecatorViews.put(count, pointView);
                indicatorLayout.addView(pointView);
            }
        }
    }

    @Override
    public T getItem(int position) {
        if (realSize > 0 && list != null && (position % realSize) < list.size()) {
            return list.get(position % realSize);
        }
        return null;
    }

    @Override
    public final void update() {
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (null != lp && 0 == lp.height){
            resize(lastWidth, lastHeight);
        }
    }

    @Override
    public void startPlay() {
        if ((list != null ? list.size() : 0) > 1 && !isAutoPlaying) {
            isAutoPlaying = true;
            handler.postDelayed(autoPlayRunnable, getAutoPlayTime());
        }
    }

    @Override
    public void stopPlay() {
        if ((list != null ? list.size() : 0) > 1) {
            isAutoPlaying = false;
            handler.removeCallbacks(autoPlayRunnable);
        }
    }

    private Runnable autoPlayRunnable = new Runnable() {
        @Override
        public void run() {
            if (null != viewPager) {
                int currentItem = viewPager.getCurrentItem();
                currentItem++;
                if (currentItem >= getMaxCount()) {
                    currentItem = 0;
                } else if (currentItem <= 0) {
                    currentItem = getMaxCount();
                }
                viewPager.setCurrentItem(currentItem, true);
                isAutoPlaying = false;
                startPlay();
            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 设置手指触碰时停止自动切换
        if (isNeedAutoPlay()) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    stopPlay();
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    startPlay();
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int index) {
        if (0 != realSize) {
            onPageChange(index % realSize);
        }
        if (isIndecator()) {
            for (int i = 0; i < realSize; i++) {
                if (i != (index % realSize)){
                    indecatorViews.get(i).setImageResource(pageIndecatorIds[0]);
                }
            }
            indecatorViews.get(index % realSize).setImageResource(pageIndecatorIds[1]);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (isNeedAutoPlay()){
            stopPlay();
        }
    }

    private boolean isIndecator(){
        return pageIndecatorIds != null && pageIndecatorIds.length > 1;
    }

    /**
     * Adapter
     */
    final class CycleViewAdapter extends PagerAdapter implements View.OnClickListener, View.OnLongClickListener{

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = onCreatItemView(position, getItem(position));
            if (null != view) {
                container.addView(view);
                view.setOnClickListener(this);
                view.setOnLongClickListener(this);
            }
            return view;
        }

        @Override
        public int getCount() {
            return getMaxCount();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void onClick(View v) {
            if (null != list && null != viewPager && realSize > 0) {
                int realPosition = viewPager.getCurrentItem() % realSize;
                if (list.size() > realPosition) {
                    onClickItem(realPosition, list.get(realPosition));
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (null != list && null != viewPager && realSize > 0) {
                int realPosition = viewPager.getCurrentItem() % realSize;
                if (list.size() > realPosition) {
                    return onLongClickItem(realPosition, list.get(realPosition));
                }
            }
            return false;
        }
    }

    @Override
    public int getMaxCount() {
        return list.isEmpty() ? 0 : Integer.MAX_VALUE / 2;
    }
}
