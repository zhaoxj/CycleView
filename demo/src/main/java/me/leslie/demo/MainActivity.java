package me.leslie.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import me.leslie.cycleview.CycleView;


public class MainActivity extends AppCompatActivity {
    private CycleView iView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Use in xml
        final MViewModel1 model1 = new MViewModel1();
        final MViewModel2 model2 = new MViewModel2();
        final List<String> list1 = new ArrayList();
        list1.add("https://wap.baidu.com/");
        list1.add("https://wap.baidu.com/ssid=414142414e5f4e49414e5c3b/s?word=WSN&ts=8844773&t_kt=0&ie=utf-8&rsv_iqid=2791282430&rsv_t=eec0Wem041Rj05xrf3Ahcmz99J7IU5mrMXpp157FDYdb6PceZsts&sa=ib&rsv_pq=2791282430&rsv_sug4=2039&inputT=532&ss=100");
        final List<TestData> list2 = new ArrayList<>();
        list2.add(new TestData("http://g.hiphotos.baidu.com/image/pic/item/810a19d8bc3eb1350557f4bba41ea8d3fd1f4419.jpg"));
        list2.add(new TestData("http://e.hiphotos.baidu.com/image/pic/item/bd315c6034a85edfb1f0942f4b540923dd5475b9.jpg"));
        list2.add(new TestData("http://f.hiphotos.baidu.com/image/pic/item/9358d109b3de9c8294f9ac836e81800a19d84319.jpg"));
        list2.add(new TestData("http://e.hiphotos.baidu.com/image/pic/item/bd315c6034a85edfb1f0942f4b540923dd5475b9.jpg"));
        list2.add(new TestData("http://f.hiphotos.baidu.com/image/pic/item/9358d109b3de9c8294f9ac836e81800a19d84319.jpg"));
        iView = (CycleView) findViewById(R.id.view);
        iView.setViewModel(model1, model2);
        model1.setList(list1);
        model2.setList(list2);
        iView.notifyDataSetChanged();


        //Use in code
        final LinearLayout layout = (LinearLayout) findViewById(R.id.activity_main);
        final CycleView iView2 = new CycleView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
        layout.addView(iView2, lp);
        iView2.setIndicatorGravity(CycleView.GRAVITY_BOTTOM_LEFT)
                .setAutoPlay(true)
                .setAutoPlayTime(1000)
                .setDisplayIndicator(true)
                .setDisplayIntr(true)
                .setIndicatorDefaultResId(R.drawable.normal)
                .setIndicatorFocusResId(R.drawable.focus)
                .setIntroBackgroundColor(Color.BLUE)
                .setIntroTextColor(Color.RED)
                .setIntroTextSize(12)
                .setViewModel(new MViewModel1(list1), new MViewModel2(list2))
                .notifyDataSetChanged();


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (null != iView){
            iView.startPlay();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != iView){
            iView.stopPlay();
        }
    }
}
