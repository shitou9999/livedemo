package tv.kuainiu.utils;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * @author nanck on 2016/8/2.
 */
public class ImageDisplayUtils {

    public static void display(Activity context, @DrawableRes int resId, ImageView view) {
        Glide.with(context).load(resId).into(view);
    }

    public static void display(Activity context, String url, ImageView view) {
        Glide.with(context).load(url).into(view);
    }

    public static void display(Activity context, String url, ImageView view, @DrawableRes int resId) {
        Glide.with(context).load(url).error(resId).into(view);
    }
}
