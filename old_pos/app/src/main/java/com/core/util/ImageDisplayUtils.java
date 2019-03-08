package com.core.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by nongyd on 2017/4/27.
 */

public class ImageDisplayUtils {

    public static void showImage(Context context,String url,ImageView imageView){
        Glide.with(context).load(url).into(imageView);
    }
    public static void showImage(Context context,String url,ImageView imageView,int width,int height){
        Glide.with(context).load(url).apply(new RequestOptions().centerCrop().override(width,height)).into(imageView);
    }

    public static void showImage(Context context,int resourId,ImageView imageView){
        Glide.with(context).load(resourId).apply(new RequestOptions().fitCenter()).into(imageView);
    }
    public static void showImage(Context context,int resourId,ImageView imageView,int width,int height){
        Glide.with(context).load(resourId).apply(new RequestOptions().centerCrop().override(width,height)).into(imageView);
    }
    public static void showCircleImage(Context context,String url,ImageView imageView){
        Glide.with(context).load(url).apply(new RequestOptions().fitCenter().transform(new CropCircleTransformation(context))).into(imageView);
    }
    public static void showCircleImage(Context context,int resourId,ImageView imageView){
        Glide.with(context).load(resourId).apply(new RequestOptions().fitCenter().transform(new CropCircleTransformation(context))).into(imageView);
    }

    /** Created by nongyd on 2017/4/27 11:43.
     * 一般加载大图时不使用内存来缓存
     *  */
    public static void showNoMemoryCache(Context context,String url,ImageView imageView){
        Glide.with(context).load(url).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(imageView);
    }
}
