# CycleView
一个可选择自动循环的View.

1.不受数据结构限制，可用setData()传递任何数据结构的list，也可以通过add()添加任何结构的单项数据，也可两者混用;   
2.可配置是否自动轮播;   
3.可以设置底部指示器，当前项介绍，介绍项背景，文字等。多种位置样式可配置等;   
4.一种类型定义一个ViewModel，内部实现重用机制;   
5.手指触碰到View时停止自动轮播，有API供锁屏时停止自动轮播，开屏时开启;   
6.所有set最终都需要调用notifyDataSetChanged()使其生效。
    
# Usage
```Java
public class TestCycleView extends BaseCycleView {

    public TestCycleView(Context context) {
        super(context);
    }

    //xml中使用时必须实现该构造器
    public TestCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
    
    
    
```

# Demo
![image](https://github.com/zhaoxj/CycleView/blob/master/csreencast/1.gif)
