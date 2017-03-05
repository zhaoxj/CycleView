package me.leslie.cycleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * 介绍：CycleView的基类
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-03-02  14:09
 */

public abstract class BaseCycleView extends RelativeLayout implements ICycleView, ViewPager.OnPageChangeListener {
    /**
     * 配置属性
     **/
    private boolean isAutoPlay;
    private boolean isDisplayIntr;
    private boolean isDisplayIndicator;
    private int indicatorDefault = -1;
    private int indicatorFocus = -1;
    private int indicatorGravity = GRAVITY_CENTER;
    private int intrBackgroundColor = Color.BLACK;
    private int introTextColor = Color.WHITE;
    private int introTextSize = 14;
    private int autoPlayTime = 3000;
    private float intrBackAlpha = 0.5f;
    private int anyPadding = 10;


    protected CycleViewPager viewPager;
    protected CycleViewAdapter adapter;
    private LinearLayout indicatorLayout;
    private RelativeLayout bottomLayout;
    private View intrBackLayout;
    private boolean isAutoPlaying;
    private TextView intrText;
    private int realSize;
    private int size;
    private boolean isAdd;
    protected List list;
    private Handler handler;
    private SparseArray<ImageView> indecatorViews;
    private boolean isNotifyDataSetChanged;

    /**
     * 获取具体项的ViewModel
     * @param position
     * @return
     */
    public abstract BaseViewModel getViewModel(final int position);

    public BaseCycleView(Context context, List list) {
        super(context);
        this.list = list;
        init(context, null);
    }


    public BaseCycleView(Context context) {
        super(context);
        init(context, null);
    }

    public BaseCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BaseCycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseCycleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    public BaseCycleView setData(List<?> list) {
        this.list = list;
        return this;
    }


    @Override
    public BaseCycleView add(Object o) {
        if (null == list){
            list = new ArrayList();
        }
        list.add(o);
        return this;
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaseCycleView);
        if (null != a) {
            isAutoPlay = a.getBoolean(R.styleable.BaseCycleView_isAutoPlay, true);
            isDisplayIntr = a.getBoolean(R.styleable.BaseCycleView_isDisplayIntr, true);
            isDisplayIndicator = a.getBoolean(R.styleable.BaseCycleView_isDisplayIndicator, false);
            indicatorDefault = a.getResourceId(R.styleable.BaseCycleView_indicatorDefault, -1);
            indicatorFocus = a.getResourceId(R.styleable.BaseCycleView_indicatorFocus, -1);
            intrBackgroundColor = a.getColor(R.styleable.BaseCycleView_intrBackgroundColor, intrBackgroundColor);
            intrBackAlpha = a.getFloat(R.styleable.BaseCycleView_intrBackgroundAlpha, intrBackAlpha);
            introTextColor = a.getColor(R.styleable.BaseCycleView_introTextColor, introTextColor);
            introTextSize = a.getDimensionPixelSize(R.styleable.BaseCycleView_introTextSize, introTextSize);
            indicatorGravity = a.getInt(R.styleable.BaseCycleView_indicatorGravity, RelativeLayout.CENTER_HORIZONTAL);
            autoPlayTime = a.getInt(R.styleable.BaseCycleView_autoPlayTime, autoPlayTime);
            anyPadding = a.getDimensionPixelSize(R.styleable.BaseCycleView_anyPadding, anyPadding);
            a.recycle();
        }

        viewPager = new CycleViewPager(context);
        viewPager.addOnPageChangeListener(this);
        addView(viewPager, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        handler = new Handler();
        notifyDataSetChanged();
    }

    @Override
    public final BaseCycleView setIndicatorIntro() {
        if (null != bottomLayout) removeView(bottomLayout);
        if (null != indecatorViews) indecatorViews.clear();
        if ((isDisplayIndicator || isDisplayIntr) && null == bottomLayout) {

            //外层
            initBottomLayout();

            //指示器
            initIndicator(false);

            //简介
            initIntr();


            //两大种情况需要重新布局
            reLayout();

        }
        return this;
    }

    @Override
    public BaseCycleView initBottomLayout() {
        if (realSize > 0) {
            intrBackLayout = new LinearLayout(getContext());
            intrBackLayout.setBackgroundColor(intrBackgroundColor);
            intrBackLayout.setAlpha(intrBackAlpha);
            LayoutParams viewLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            addView(intrBackLayout, viewLp);

            bottomLayout = new RelativeLayout(getContext());
            bottomLayout.setId(R.id.bootom_layout);
            bottomLayout.setPadding(anyPadding, anyPadding, anyPadding, anyPadding);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            addView(bottomLayout, lp);
            bottomLayout.setBackgroundColor(Color.TRANSPARENT);
        }
        return this;
    }

    @Override
    public final BaseCycleView initIndicator(boolean isRelayout) {
        if (isDisplayIndicator && null != bottomLayout) {

            if (realSize == 0) return this;
            int size = realSize;
            if (isAdd && 4 == realSize) {
                realSize = size = 2;
            }

            if (-1 == indicatorDefault || -1 == indicatorFocus) {
                throw new RuntimeException("Pls add \"dotDefault\" and \"dotFocus\" !");
            }
            indecatorViews = new SparseArray<>();
            indicatorLayout = new LinearLayout(getContext());
            indicatorLayout.setId(R.id.indicator_layout);
            indicatorLayout.setGravity(Gravity.CENTER_VERTICAL);
            bottomLayout.addView(indicatorLayout, getIndicatorLayoutParams(isRelayout));
            for (int count = 0; count < size; count++) {
                // 翻页指示的点
                ImageView pointView = new ImageView(getContext());
                pointView.setPadding(6, 0, 6, 0);
                if (0 == indecatorViews.size()){
                    pointView.setImageResource(indicatorFocus);
                }else {
                    pointView.setImageResource(indicatorDefault);
                }
                indecatorViews.put(count, pointView);
                indicatorLayout.addView(pointView);
            }
        }
        return this;
    }

    private LayoutParams getIndicatorLayoutParams(boolean isRelayout){
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (indicatorGravity){
            case GRAVITY_LEFT_CENTER_VERTICAL:
                lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                break;
            case GRAVITY_RIGHT_CENTER_VERTICAL:
                lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                break;
            case GRAVITY_CENTER: //需要重新布局
                lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                break;
            case GRAVITY_TOP_LEFT:
                break;
            case GRAVITY_TOP_CENTER:
                lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                break;
            case GRAVITY_TOP_RIGHT:
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                break;
            case GRAVITY_BOTTOM_LEFT:  //需要重新布局
                if (isRelayout){
                    lp.addRule(RelativeLayout.BELOW, R.id.intr_text);
                    lp.topMargin = anyPadding;
                }else {
                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                }
                break;
            case GRAVITY_BOTTOM_CENTER:  //需要重新布局
                lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                if (isRelayout) {
                    lp.addRule(RelativeLayout.BELOW, R.id.intr_text);
                    lp.topMargin = anyPadding;
                }else {
                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                }
                break;
            case GRAVITY_BOTTOM_RIGHT:  //需要重新布局
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                if (isRelayout) {
                    lp.addRule(RelativeLayout.BELOW, R.id.intr_text);
                    lp.topMargin = anyPadding;
                }else {
                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                }
                break;
        }
        lp.bottomMargin = anyPadding;
        return lp;
    }


    private LayoutParams getIntrTextLayoutParams(){
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (isDisplayIndicator){
            switch (indicatorGravity){
                case GRAVITY_LEFT_CENTER_VERTICAL:
                    lp.addRule(RelativeLayout.RIGHT_OF, R.id.indicator_layout);
                    lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    lp.leftMargin = anyPadding;
                    intrText.setGravity(Gravity.RIGHT);
                    break;
                case GRAVITY_RIGHT_CENTER_VERTICAL:
                    lp.addRule(RelativeLayout.LEFT_OF, R.id.indicator_layout);
                    lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    lp.rightMargin = anyPadding;
                    intrText.setGravity(Gravity.LEFT);
                    break;
                case GRAVITY_CENTER:
                    lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                    /**
                     * 重新布局
                     */
                    break;
                case GRAVITY_TOP_LEFT:
                case GRAVITY_TOP_CENTER:
                case GRAVITY_TOP_RIGHT:
                    lp.addRule(RelativeLayout.BELOW, R.id.indicator_layout);
                    lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    intrText.setGravity(Gravity.LEFT);
                    break;
                case GRAVITY_BOTTOM_LEFT:
                case GRAVITY_BOTTOM_CENTER:
                case GRAVITY_BOTTOM_RIGHT:
                    /**
                     * 重新布局
                     */
                    break;
            }
        }else {
            lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        }
        return lp;
    }

    private void reLayout(){
        if (isDisplayIndicator && isDisplayIntr && null != bottomLayout && null != indicatorLayout){
            switch (indicatorGravity){
                case GRAVITY_CENTER:
                case GRAVITY_BOTTOM_LEFT:
                case GRAVITY_BOTTOM_CENTER:
                case GRAVITY_BOTTOM_RIGHT:
                    /**
                     * 重新布局
                     */
                    bottomLayout.removeView(indicatorLayout);
                    initIndicator(true);
                    break;
            }
        }
    }

    @Override
    public final BaseCycleView initIntr() {
        if (isDisplayIntr && null != bottomLayout) {
            intrText = new TextView(getContext());
            intrText.setId(R.id.intr_text);
            intrText.setTextSize(TypedValue.COMPLEX_UNIT_SP, introTextSize);
            intrText.setTextColor(introTextColor);
            intrText.setSingleLine();
            intrText.setEllipsize(TextUtils.TruncateAt.END);
            bottomLayout.addView(intrText, getIntrTextLayoutParams());
        }


        LayoutParams backLp;
        if (null != intrBackLayout && null != bottomLayout && null != (backLp = (LayoutParams) intrBackLayout.getLayoutParams())){
            backLp.addRule(RelativeLayout.ALIGN_TOP, R.id.bootom_layout);
            backLp.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.bootom_layout);
        }
        return this;
    }

    @Override
    public final void onFlingWhite() {
        if (null != list && 2 == list.size()) {
            list.add(2, list.get(0));
            list.add(3, list.get(1));
            isAdd = true;
        }
    }

    @Override
    public void onPageChange(int index) {

    }

    @Override
    public void startPlay() {
        if ((list != null ? list.size() : 0) > 1 && !isAutoPlaying) {
            isAutoPlaying = true;
            handler.postDelayed(autoPlayRunnable, autoPlayTime);
        }
    }

    @Override
    public void stopPlay() {
        if ((list != null ? list.size() : 0) > 1 && isAutoPlaying) {
            isAutoPlaying = false;
            handler.removeCallbacks(autoPlayRunnable);
        }
    }


    @Override
    public void setPageTransformer(ViewPager.PageTransformer tf) {

    }

    @Override
    public int getMaxCount() {
        return null != list && !list.isEmpty() ? Integer.MAX_VALUE / 2 : 0;
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int index) {
        if (0 != realSize) {
            onPageChange(index % realSize);
            if (isDisplayIndicator && null != indecatorViews) {
                for (int i = 0; i < realSize; i++) {
                    if (i != (index % realSize)) {
                        indecatorViews.get(i).setImageResource(indicatorFocus);
                    }
                }
                indecatorViews.get(index % realSize).setImageResource(indicatorDefault);
            }

            if (null != adapter){
                adapter.onPageSelected(index);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    @Override
    public final Object getItem(final int position) {
        return null != list && position > -1 && list.size() > position ? list.get(position) : null;
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
        if (isAutoPlay) {
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
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (isAutoPlay) {
            stopPlay();
        }
    }

    @Override
    public void notifyDataSetChanged() {
        if (null == list) {
            list = new ArrayList<>();
        }
        realSize = list.size();
        onFlingWhite();
        size = list.size();
        int num = 10000;
        if (size <= 0) {
            num = 0;
        } else if (size >= 5) {
            num = 2000;
        } else if (size == 4) {
            num = 2500;
        } else if (size == 3) {
            num = 7000;
        } else if (size == 2) {
            num = 5000;
        }
        setIndicatorIntro();
        adapter = null;
        isNotifyDataSetChanged = true;
        viewPager.setAdapter(adapter = new CycleViewAdapter());
        viewPager.setCurrentItem(size = size * num);
        if (isAutoPlay) {
            startPlay();
        }
    }

    @Override
    public void setIntrText(String s) {
        if (null == s) s = "";
        if (null != intrText) intrText.setText(s);
    }

    @Override
    public BaseCycleView setAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
        return this;
    }

    @Override
    public BaseCycleView setAutoPlayTime(int time) {
        this.autoPlayTime = time;
        return this;
    }

    @Override
    public BaseCycleView setDisplayIntr(boolean displayIntr) {
        this.isDisplayIntr = displayIntr;
        return this;
    }

    @Override
    public BaseCycleView setIntroBackgroundColor(int color) {
        this.introTextColor = color;
        return this;
    }

    @Override
    public BaseCycleView setDisplayIndicator(boolean display) {
        this.isDisplayIndicator = display;
        return this;
    }

    @Override
    public BaseCycleView setIndicatorDefaultResId(int resId) {
        this.indicatorDefault = resId;
        return this;
    }

    @Override
    public BaseCycleView setIndicatorFocusResId(int resId) {
        this.indicatorFocus = resId;
        return this;
    }

    @Override
    public BaseCycleView setIndicatorGravity(int gravity) {
        this.indicatorGravity = gravity;
        return this;
    }

    @Override
    public BaseCycleView setIntroTextSize(int spSize) {
        this.introTextSize = spSize;
        return this;
    }

    @Override
    public BaseCycleView setIntroTextColor(int color) {
        this.introTextColor = color;
        return this;
    }

    /**
     * Adapter
     */
    final class CycleViewAdapter extends PagerAdapter {
        private SparseArray<BaseViewModel> viewModels;

        public CycleViewAdapter() {
            viewModels = new SparseArray<>();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = null;
            if (0 != realSize){
                final int realPosition = position % realSize;
                BaseViewModel viewModel = viewModels.get(realPosition);
                if (null == viewModel) {
                    viewModel = getViewModel(realPosition);
                    viewModels.put(realPosition, viewModel);
                }
                if (null != viewModel && null != (view = viewModel.onCreatView(realPosition, getItem(realPosition)))) {
                    container.addView(view);
                    final BaseViewModel finalViewModel = viewModel;
                    view.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalViewModel.onClick(realPosition , getItem(realPosition));
                        }
                    });
                    view.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            return finalViewModel.onLongClick(realPosition, getItem(realPosition));
                        }
                    });
                }
                if (isNotifyDataSetChanged && 0 == realPosition){
                    isNotifyDataSetChanged = false;
                    onPageSelected(0);
                }
            }
            return view;
        }

        public void onPageSelected(int position){
            if (0 != realSize) {
                final int realPosition = position % realSize;
                BaseViewModel viewModel = null;
                if (null != (viewModel = viewModels.get(realPosition))){
                    viewModel.onPageSelected(realPosition, getItem(realPosition));
                }
            }
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

    }
}

