package me.leslie.demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import me.leslie.cycleview.BaseCycleView;

/**
 * 介绍：
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-01-03  18:17
 */

public class IView extends BaseCycleView<TestData> {

    public IView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreatItemView(int position, @NonNull TestData test) {
        final ImageView img = new ImageView(context);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        String imgUrl = test.getImgUrl();
        if (!TextUtils.isEmpty(imgUrl)) {
            LoadImgUtils.loadImage(img, imgUrl);
        }
        return img;
    }

    public IView(Context context) {
        super(context);
    }

    @Override
    public void onClickItem(int position, @NonNull TestData test) {
        Toast.makeText(getContext(), "点击了第 : " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClickItem(int position, @NonNull TestData test) {
        Toast.makeText(getContext(), "长按第 : " + position, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected int[] getPageIndecatorIds() {
        return new int[]{R.drawable.slide_indicator_focus, R.drawable.slide_indicator_normal};
    }

    @Override
    public void onPageChange(int index) {

    }

}
