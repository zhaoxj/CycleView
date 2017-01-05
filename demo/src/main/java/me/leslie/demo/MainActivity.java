package me.leslie.demo;

import android.os.Bundle;
import android.os.Handler;
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
        final LinearLayout layout = (LinearLayout) findViewById(R.id.activity_main);

        final IView iView = (IView) findViewById(R.id.view);
        final List<TestData> list1 = new ArrayList<>();
        final TestData d1_1 = new TestData("http://g.hiphotos.baidu.com/image/pic/item/810a19d8bc3eb1350557f4bba41ea8d3fd1f4419.jpg");
        final TestData d1_2 = new TestData("http://e.hiphotos.baidu.com/image/pic/item/bd315c6034a85edfb1f0942f4b540923dd5475b9.jpg");
        final TestData d1_3 = new TestData("http://f.hiphotos.baidu.com/image/pic/item/9358d109b3de9c8294f9ac836e81800a19d84319.jpg");
        list1.add(d1_1);
        list1.add(d1_2);
        list1.add(d1_3);
        iView.setData(null);

        IView i = new IView(getApplicationContext());
        layout.addView(i, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
        i.setData(list1);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iView.setData(list1);
            }
        }, 3000);



    }
}
