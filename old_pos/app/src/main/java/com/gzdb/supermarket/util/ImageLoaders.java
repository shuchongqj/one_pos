package com.gzdb.supermarket.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by zhumg on 8/17.
 */
public class ImageLoaders {

    private static DisplayImageOptions options;
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPoolSize(3)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(50 * 1024 * 1024))

                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(
                        new BaseImageDownloader(context, 5 * 1000, 30 * 1000))

                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO) // Remove
                // for
                // release
                // app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

    }
    public static class MyListener implements ImageLoadingListener {
        private Context context;

        public MyListener(Context context) {
            this.context = context;
        }

        @Override
        public void onLoadingCancelled(String arg0, View arg1) {

        }

        @Override
        public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {

        }

        @Override
        public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
            System.out.println("=========");
            System.out.println(arg0);
            System.out.println(arg2);
            System.out.println("=========");
            try {
                if (arg0.contains("_index")) {
                    arg0 = arg0.replace("_index", "");
                    if (arg1 != null)
                        ImageLoader.getInstance().displayImage(arg0, (ImageView) arg1, getOptions(context), this);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void onLoadingStarted(String arg0, View arg1) {
            // TODO Auto-generated method stub

        }

    }

    public static void display(Context context, ImageView imageView, String uri) {
        if (imageView == null || uri == null || uri.equals("")) {
            ImageLoader.getInstance().displayImage(uri, imageView, getOptions(context), new MyListener(context));
            return;
        }
        if (!uri.equals(imageView.getTag())) {
            imageView.setTag(uri);
            ImageLoader.getInstance().displayImage(uri, imageView, getOptions(context), new MyListener(context));
        }
    }

    public static void display(Context context, ImageView imageView, String uri,int defaultImg) {
        DisplayImageOptions options = new  DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImg) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(defaultImg)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(defaultImg) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(false)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                // .decodingOptions(android.graphics.BitmapFactory.Options
                // decodingOptions)//设置图片的解码配置
                // .delayBeforeLoading(int delayInMillis)//int
                // delayInMillis为你设置的下载前的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)

                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                // .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
                .build();
        if (imageView == null || uri == null || uri.equals("")) {
            ImageLoader.getInstance().displayImage(uri, imageView, options, new MyListener(context));
            return;
        }
        if (!uri.equals(imageView.getTag())) {
            imageView.setTag(uri);
            ImageLoader.getInstance().displayImage(uri, imageView, options, new MyListener(context));
        }
    }

    public static void display(Context context, ImageView imageView, String uri, ImageLoadingListener ImageLoadingListener) {
        if (imageView == null || uri == null || uri.equals("")) {
            ImageLoader.getInstance().displayImage(uri, imageView, getOptions(context), ImageLoadingListener);
            return;
        }
        if (!uri.equals(imageView.getTag())) {
            imageView.setTag(uri);
            ImageLoader.getInstance().displayImage(uri, imageView, getOptions(context), ImageLoadingListener);
        }
    }

    private static DisplayImageOptions getOptions(Context context) {
        if (options == null) {
            options = getBitmapOptions(context);
        }
        return options;
    }

    private static DisplayImageOptions getBitmapOptions(Context context) {
        return new DisplayImageOptions.Builder()//
                //.showImageOnLoading(R.drawable.pic2) // 设置图片在下载期间显示的图片
//				.showImageForEmptyUri(R.drawable.pic2)// 设置图片Uri为空或是错误的时候显示的图片
//				.showImageOnFail(R.drawable.pic2) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(false)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                // .decodingOptions(android.graphics.BitmapFactory.Options
                // decodingOptions)//设置图片的解码配置
                // .delayBeforeLoading(int delayInMillis)//int
                // delayInMillis为你设置的下载前的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)

                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                // .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
                .build();// 构建完成
    }

}
