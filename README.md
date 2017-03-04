# CycleView
一个可选择自动循环的View，不受数据结构限制，可用setData()传递任何数据结构的list，也可以通过add()添加任何结构的单项数据。还可以设置底部指示器，当前项介绍，多种位置调整等。所有操作最终都需要调用notifyDataSetChanged()使其生效。

# Usage
```Java
public class TestCycleView extends BaseCycleView {


    public TestCycleView(Context context, List list) {
        super(context, list);
    }

    public TestCycleView(Context context) {
        super(context);
    }

    public TestCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestCycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestCycleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public BaseViewModel getViewModel(int position) {
        if (0 == position || 4 == position){
            return new Model1();
        }else {
            return new Model2();
        }
    }


    /**
     * 类型1
     */
    class Model1 extends BaseViewModel<String>{
        @Override
        protected View onCreatView(int position, String s) {
            WebView view = new WebView(getContext());
            view.setWebViewClient(new WebViewClient());
            view.loadUrl(s);
            return view;
        }

        @Override
        public void onPageSelected(int position, String s) {
            setIntrText(s);
        }

        @Override
        protected void onClick(int position, @NonNull String s) {

        }

        @Override
        protected boolean onLongClick(int position, @NonNull String s) {
            return false;
        }
    }


    /**
     * 类型2
     */
    class Model2 extends BaseViewModel<TestData>{
        @Override
        protected View onCreatView(int position, TestData data) {
            ImageView img = new ImageView(getContext());
            LoadImgUtils.loadImage(img, data.getImgUrl());
            return img;
        }

        @Override
        public void onPageSelected(int position, TestData data) {
            setIntrText(data.getImgUrl());
        }

        @Override
        protected void onClick(int position, TestData testData) {
            Toast.makeText(getContext(), "点击了: " + position, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected boolean onLongClick(int position, TestData testData) {
            return false;
        }

    }
    
    
    //Use in xml
        final TestCycleView iView = (TestCycleView) findViewById(R.id.view);
        iView.add("https://wap.baidu.com/")
                .add(new TestData("http://g.hiphotos.baidu.com/image/pic/item/810a19d8bc3eb1350557f4bba41ea8d3fd1f4419.jpg"))
                .add(new TestData("http://e.hiphotos.baidu.com/image/pic/item/bd315c6034a85edfb1f0942f4b540923dd5475b9.jpg"))
                .add(new TestData("http://f.hiphotos.baidu.com/image/pic/item/9358d109b3de9c8294f9ac836e81800a19d84319.jpg"))
                .add("https://wap.baidu.com/ssid=414142414e5f4e49414e5c3b/s?word=WSN&ts=8844773&t_kt=0&ie=utf-8&rsv_iqid=2791282430&rsv_t=eec0Wem041Rj05xrf3Ahcmz99J7IU5mrMXpp157FDYdb6PceZsts&sa=ib&rsv_pq=2791282430&rsv_sug4=2039&inputT=532&ss=100")
                .add(new TestData("http://e.hiphotos.baidu.com/image/pic/item/bd315c6034a85edfb1f0942f4b540923dd5475b9.jpg"))
                .add(new TestData("http://f.hiphotos.baidu.com/image/pic/item/9358d109b3de9c8294f9ac836e81800a19d84319.jpg"))
                .notifyDataSetChanged();



        //Use in code
        List list = new ArrayList();
        list.add("https://wap.baidu.com/");
        list.add(new TestData("http://g.hiphotos.baidu.com/image/pic/item/810a19d8bc3eb1350557f4bba41ea8d3fd1f4419.jpg"));
        list.add(new TestData("http://e.hiphotos.baidu.com/image/pic/item/bd315c6034a85edfb1f0942f4b540923dd5475b9.jpg"));
        list.add(new TestData("http://f.hiphotos.baidu.com/image/pic/item/9358d109b3de9c8294f9ac836e81800a19d84319.jpg"));
        list.add("https://wap.baidu.com/ssid=414142414e5f4e49414e5c3b/s?word=WSN&ts=8844773&t_kt=0&ie=utf-8&rsv_iqid=2791282430&rsv_t=eec0Wem041Rj05xrf3Ahcmz99J7IU5mrMXpp157FDYdb6PceZsts&sa=ib&rsv_pq=2791282430&rsv_sug4=2039&inputT=532&ss=100");
        list.add(new TestData("http://e.hiphotos.baidu.com/image/pic/item/bd315c6034a85edfb1f0942f4b540923dd5475b9.jpg"));
        list.add(new TestData("http://f.hiphotos.baidu.com/image/pic/item/9358d109b3de9c8294f9ac836e81800a19d84319.jpg"));
        final LinearLayout layout = (LinearLayout) findViewById(R.id.activity_main);
        final TestCycleView iView2 = new TestCycleView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
        layout.addView(iView2, lp);
        iView2.setIndicatorGravity(TestCycleView.GRAVITY_LEFT_CENTER_VERTICAL)
                .setAutoPlay(true)
                .setAutoPlayTime(1000)
                .setDisplayIndicator(true)
                .setDisplayIntr(true)
                .setIndicatorDefaultResId(R.drawable.normal)
                .setIndicatorFocusResId(R.drawable.focus)
                .setIntroBackgroundColor(Color.BLUE)
                .setIntroTextColor(Color.RED)
                .setIntroTextSize(12)
                .setData(list)
                .notifyDataSetChanged();
    
    
    
    
    
    
    
    <declare-styleable name="BaseCycleView">
        <!--是否可以自动轮播 默认可以-->
        <attr name="isAutoPlay" format="boolean"/>

        <!--自动轮播时间 默认3000ms-->
        <attr name="autoPlayTime" format="integer"/>

        <!--是否显示底部介绍 默认有-->
        <attr name="isDisplayIntr" format="boolean"/>

        <!--介绍文字大小 默认14sp-->
        <attr name="introTextSize" format="integer"/>

        <!--介绍文字颜色 默认白色-->
        <attr name="introTextColor" format="color"/>

        <!--底部介绍背景色 默认黑色，0.5透明度-->
        <attr name="intrBackgroundColor" format="color"/>

        <!--底部介绍背景色 默认黑色，0.5透明度-->
        <attr name="intrBackgroundAlpha" format="float"/>

        <!--是否有切换指示器 默认无-->
        <attr name="isDisplayIndicator" format="boolean"/>

        <!--切换指示器未选中状态图-->
        <attr name="indicatorDefault" format="reference"/>

        <!--切换指示器选中状态图-->
        <attr name="indicatorFocus" format="reference"/>

        <!--切换指示器的位置 默认在中间-->
        <attr name="indicatorGravity" format="enum">
            <enum name="left_center_vertical" value="1"/>
            <enum name="right_center_vertical" value="2"/>
            <enum name="center" value="3"/>
            <enum name="top_left" value="4"/>
            <enum name="top_center" value="5"/>
            <enum name="top_right" value="6"/>
            <enum name="bottom_left" value="7"/>
            <enum name="bottom_center" value="8"/>
            <enum name="bottom_right" value="9"/>
        </attr>

        <!--底部指示器，文字，底部背景等的padding-->
        <attr name="anyPadding" format="dimension"/>

    </declare-styleable>
    
```

# Demo
![image](https://github.com/zhaoxj/CycleView/blob/master/csreencast/1.gif)
