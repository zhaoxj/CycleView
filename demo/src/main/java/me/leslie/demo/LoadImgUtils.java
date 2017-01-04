package me.leslie.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


/**
 * 加载图片工具类
 *
 * 作者：xjzhao
 * 时间：2015-03-20 下午3:01
 */
public class LoadImgUtils {

    /**
     * 加载本地图片
     *
     * @param img
     * @param url
     */
    public static void loadImage(final ImageView img, final String url) {
        if (null != img) {
            if (!TextUtils.isEmpty(url)) {
                Ion.with(img)
                        .fitXY()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .fadeIn(false)
                        .load(url);
            } else {
                img.setImageResource(R.mipmap.ic_launcher);
            }
        }
    }

    /**
     * 加载本地大图加载
     *
     * @param img
     * @param url
     */
    public static void loadImageDeepZoom(final ImageView img, final String url) {
        if (null != img) {
            if (!TextUtils.isEmpty(url)) {
                Ion.with(img)
                        .centerCrop()
                        .deepZoom()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .fadeIn(false)
                        .load(url);
            } else {
                img.setImageResource(R.mipmap.ic_launcher);
            }
        }
    }


    public static void loadBitmap(Context context, final ImageView img, final String url) {
        if (null != img) {
            if (!TextUtils.isEmpty(url)) {
                Ion.getInstance(context, "img")
                        .build(context)
                        .load(url)
                        .asBitmap()
                        .setCallback(new FutureCallback<Bitmap>() {
                            @Override
                            public void onCompleted(Exception e, Bitmap result) {
                                if (null != result) {
                                    img.setImageBitmap(result);
                                } else {
                                    img.setImageResource(R.mipmap.ic_launcher);
                                }
                            }
                        });

            } else {
                img.setImageResource(R.mipmap.ic_launcher);
            }
        }
    }
}
