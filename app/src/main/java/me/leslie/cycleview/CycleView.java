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


/**
 * 介绍：CycleView的基类
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-03-02  14:09
 */

public class CycleView extends RelativeLayout implements ICycleView, ViewPager.OnPageChangeListener {
    /**
     * 配置属性
     **/
    private boolean isCycle;
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
    private boolean isAdd;
    private Handler handler;
    private SparseArray<ImageView> indicatorViews = new SparseArray<>();
    private boolean isNotifyDataSetChanged;

    private SparseArray<Vm> vmSparseArray = new SparseArray<>();
    private BaseCycleViewModel[] vms;
    private int count;

    public CycleView setViewModel(BaseCycleViewModel... vms){
        this.vms = vms;
        return this;
    }

    public CycleView(Context context) {
        super(context);
        init(context, null);
    }

    public CycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CycleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaseCycleView);
        if (null != a) {
            isCycle = a.getBoolean(R.styleable.BaseCycleView_isCycle, true);
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
        handler = new Handler();
    }

    @Override
    public final CycleView setIndicatorIntro() {
        if (null != indicatorViews) indicatorViews.clear();
        if ((isDisplayIndicator || isDisplayIntr)) {

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
    public CycleView initBottomLayout() {
        if (count > 0 && null == bottomLayout) {
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
    public final CycleView initIndicator(boolean isRelayout) {
        if (isDisplayIndicator && null != bottomLayout) {

            if (count == 0) return this;
            int size = count;
            if (isAdd && 4 == count) {
                size = 2;
            }

            if (-1 == indicatorDefault || -1 == indicatorFocus) {
                throw new RuntimeException("Pls add \"dotDefault\" and \"dotFocus\" !");
            }
            if (null == indicatorLayout){
                indicatorLayout = new LinearLayout(getContext());
                bottomLayout.addView(indicatorLayout, getIndicatorLayoutParams(isRelayout));
                indicatorLayout.setId(R.id.indicator_layout);
                indicatorLayout.setGravity(Gravity.CENTER_VERTICAL);
            }else {
                indicatorLayout.removeAllViews();
                indicatorViews.clear();
            }
            for (int i = 0; i < size; i++) {
                // 翻页指示的点
                ImageView pointView = new ImageView(getContext());
                pointView.setPadding(6, 0, 6, 0);
                if (0 == indicatorViews.size()){
                    pointView.setImageResource(indicatorFocus);
                }else {
                    pointView.setImageResource(indicatorDefault);
                }
                indicatorViews.put(i, pointView);
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
                    indicatorLayout = null;
                    initIndicator(true);
                    break;
            }
        }
    }

    @Override
    public final CycleView initIntr() {
        if (isDisplayIntr && null != bottomLayout && null == intrText) {
            intrText = new TextView(getContext());
            intrText.setId(R.id.intr_text);
            intrText.setTextSize(TypedValue.COMPLEX_UNIT_SP, introTextSize);
            intrText.setTextColor(introTextColor);
            intrText.setSingleLine();
            intrText.setEllipsize(TextUtils.TruncateAt.END);
            bottomLayout.addView(intrText, getIntrTextLayoutParams());

            LayoutParams backLp;
            if (null != intrBackLayout && null != bottomLayout && null != (backLp = (LayoutParams) intrBackLayout.getLayoutParams())){
                backLp.addRule(RelativeLayout.ALIGN_TOP, R.id.bootom_layout);
                backLp.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.bootom_layout);
            }
        }
        return this;
    }

    @Override
    public final void onFlingWhite() {
        if (isCycle && null != vmSparseArray) {
            if (1 == vmSparseArray.size()){
                vmSparseArray.get(0).getViewModel().onDeoublData();
            }else if (2 == vmSparseArray.size()){
                Vm vm1 = vmSparseArray.get(0);
                Vm vm2 = vmSparseArray.get(1);
                vmSparseArray.put(2, vm1.setStartPosition(count));
                vmSparseArray.put(3, vm2.setStartPosition(count + vm1.getViewModel().getItemCount()));
            }
            isAdd = true;
        }
    }

    @Override
    public void onPageChange(int index) {

    }

    @Override
    public void startPlay() {
        if (count > 1 && !isAutoPlaying) {
            isAutoPlaying = true;
            handler.postDelayed(autoPlayRunnable, autoPlayTime);
        }
    }

    @Override
    public void stopPlay() {
        if (count > 1 && isAutoPlaying) {
            isAutoPlaying = false;
            handler.removeCallbacks(autoPlayRunnable);
        }
    }


    @Override
    public void setPageTransformer(ViewPager.PageTransformer tf) {

    }

    @Override
    public int getMaxCount() {
        if (isCycle) {
            //循环情况下如果单张不让可滑动，这里改为：count > 1 ? Integer.MAX_VALUE / 2 : 1;
            return count > 0 ? Integer.MAX_VALUE / 2 : 0;
        }else {
            return count;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int index) {
        if (0 != count) {
            onPageChange(index % count);
            if (isDisplayIndicator && null != indicatorViews) {
                for (int i = 0; i < count; i++) {
                    if (i != (index % count)) {
                        indicatorViews.get(i).setImageResource(indicatorDefault);
                    }
                }
                indicatorViews.get(index % count).setImageResource(indicatorFocus);
            }

            if (null != adapter){
                adapter.onPageSelected(index);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

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
        resize();

        //如果是无限循环的，刷新是必须全部重新布局，因为数量太大，直接更新会出现卡顿
        if ((isCycle && null != viewPager) || null == viewPager){
            removeAllViews();
            viewPager = null;
            bottomLayout = null;
            intrText = null;
            indicatorLayout = null;
            viewPager = new CycleViewPager(getContext());
            viewPager.addOnPageChangeListener(this);
            viewPager.setAdapter(adapter = new CycleViewAdapter());
            addView(viewPager, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        setIndicatorIntro();
        if (!isCycle) {
            adapter.notifyDataSetChanged();
        }
        isNotifyDataSetChanged = true;
        int cycleStart = count != 0 ? (int) (Math.floor(Integer.MAX_VALUE / 4 / count) * count) : 0;
        viewPager.setCurrentItem(isCycle ? cycleStart : 0);
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
    public CycleView setCycle(boolean cycle) {
        isCycle = cycle;
        return this;
    }

    @Override
    public CycleView setAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
        return this;
    }

    @Override
    public CycleView setAutoPlayTime(int time) {
        this.autoPlayTime = time;
        return this;
    }

    @Override
    public CycleView setDisplayIntr(boolean displayIntr) {
        this.isDisplayIntr = displayIntr;
        return this;
    }

    @Override
    public CycleView setIntroBackgroundColor(int color) {
        this.intrBackgroundColor = color;
        if (null != intrBackLayout){
            intrBackLayout.setBackgroundColor(introTextColor);
        }
        return this;
    }

    @Override
    public CycleView setDisplayIndicator(boolean display) {
        this.isDisplayIndicator = display;
        return this;
    }

    @Override
    public CycleView setIndicatorDefaultResId(int resId) {
        this.indicatorDefault = resId;
        return this;
    }

    @Override
    public CycleView setIndicatorFocusResId(int resId) {
        this.indicatorFocus = resId;
        return this;
    }

    @Override
    public CycleView setIndicatorGravity(int gravity) {
        this.indicatorGravity = gravity;
        setIndicatorIntro();
        return this;
    }

    @Override
    public CycleView setIntroTextSize(int spSize) {
        this.introTextSize = spSize;
        if (null != intrText){
            intrText.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize);
        }
        return this;
    }

    @Override
    public CycleView setIntroTextColor(int color) {
        this.introTextColor = color;
        if (null != intrText){
            intrText.setTextColor(introTextColor);
        }
        return this;
    }

    public Vm getVm(final int position){
        int size = vmSparseArray.size();
        int total = 0;
        int lastTotal = total;
        Vm vm;
        for (int i = 0; i < size; i++) {
            total += (vm = vmSparseArray.get(i)).getViewModel().getItemCount();
            if (position >= lastTotal && position < total) {
                return vm;
            }
            lastTotal = total;
        }
        return null;
    }

    private void resize(){
        vmSparseArray.clear();
        count = 0;
        int length;
        if (null != vms && (length = vms.length) > 0) {
            BaseCycleViewModel viewModel;
            for (int i = 0; i < length; i++) {
                if (null != (viewModel = vms[i])) {
                    vmSparseArray.put(i, new Vm(viewModel.setContext(getContext()).setAdapter(this), count));
                    count += viewModel.getItemCount();
                }
            }
        }
    }

    /**
     * Adapter
     */
    final class CycleViewAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = null;
            if (0 != count){
                final int realPosition = position % count;
                Vm vm = getVm(realPosition);
                if (null != vm){
                    BaseCycleViewModel viewModel = vm.getViewModel();
                    final int positionInViewModel = realPosition - vm.getStartPosition();
                    if (null != viewModel && null != (view = viewModel.onCreatView(positionInViewModel, viewModel.getItem(positionInViewModel)))) {
                        container.addView(view);
                        final BaseCycleViewModel finalViewModel = viewModel;
                        view.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finalViewModel.onClick(positionInViewModel , finalViewModel.getItem(positionInViewModel));
                            }
                        });
                        view.setOnLongClickListener(new OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                return finalViewModel.onLongClick(positionInViewModel, finalViewModel.getItem(positionInViewModel));
                            }
                        });
                    }
                    if (isNotifyDataSetChanged && 0 == realPosition){
                        isNotifyDataSetChanged = false;
                        onPageSelected(0);
                    }
                }

            }
            return view;
        }

        final void onPageSelected(int position){
            if (0 != count) {
                int realPosition = position % count;
                Vm vm = getVm(realPosition);
                if (null != vm){
                    BaseCycleViewModel viewModel = vm.getViewModel();
                    int positionInViewModel = realPosition - vm.getStartPosition();
                    if (null != viewModel){
                        viewModel.onPageSelected(positionInViewModel, viewModel.getItem(positionInViewModel));
                    }
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

