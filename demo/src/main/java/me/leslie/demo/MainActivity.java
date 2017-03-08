package me.leslie.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //注意，数据顺序修改时记得修改下TestCycleView的getViewModel()，demo用了0和4两个位置使用Web的ViewModel

        //Use in xml
        final TestCycleView iView = (TestCycleView) findViewById(R.id.view);
        iView
                .add("https://wap.baidu.com/")
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


    }
}
