package tv.kuainiu.util;

import android.content.Context;
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
        Glide.with(context).load(imagePath).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    public static void displayImage(Context context, ImageView imageView, String imagePath, int imageId) {
        if(context==null||imageView==null){
            return;
        }
        Glide.with(context).load(imagePath).placeholder(imageId).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    public static void displayImage(Context context, ImageView imageView, int imageId) {
        if(context==null||imageView==null){
            return;
        }
        Glide.with(context).load(imageId).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }
}
