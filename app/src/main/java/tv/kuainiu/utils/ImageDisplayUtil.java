package tv.kuainiu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by jack on 2016/4/14.
 */
public class ImageDisplayUtil {
    public static void displayImage(Context context, ImageView imageView, String imagePath) {
        if(context==null||imageView==null){
            return;
        }
        try {
        Glide.with(context).load(imagePath).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }catch (Exception e){
            LogUtils.e("ImageDisplayUtil","line 20",e);
        }
    }

    public static void displayImage(Context context, ImageView imageView, String imagePath, int imageId) {
        if(context==null||imageView==null){
            return;
        }
        try {
            Glide.with(context).load(imagePath).placeholder(imageId).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }catch (Exception e){
            LogUtils.e("ImageDisplayUtil","line 31",e);
        }
    }

    public static void displayImage(Context context, ImageView imageView, int imageId) {
        if(context==null||imageView==null){
            return;
        }
        try {
        Glide.with(context).load(imageId).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }catch (Exception e){
            LogUtils.e("ImageDisplayUtil","line 42",e);
        }
    }

    public static void displayImage(Context context, ImageView imageView, Bitmap mDrawable, int error) {
        if(context==null||imageView==null){
            return;
        }
        try {
            Glide.with(context).load(mDrawable).placeholder(error).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }catch (Exception e){
            LogUtils.e("ImageDisplayUtil","line 52",e);
        }
    }
}
