package tv.kuainiu.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.widget.TextView;

import tv.kuainiu.MyApplication;
import tv.kuainiu.R;


/**
 * Created by jack on 2016/5/9.
 */
public class TextColorUtil {
    /**
     * true 读过（灰色），false 未读过（黑色）
     *
     * @param context
     * @param textview
     * @param isRead
     */
    public static void setIsRead(Context context, TextView textview, boolean isRead) {
        if (isRead) {
            textview.setTextColor(generateColor(R.color.colorGrey500));
        } else {
            textview.setTextColor(generateColor(R.color.colorGrey800));
        }
    }


    public static int generateColor(@ColorRes int id) {
        Resources res = MyApplication.getInstance().getResources();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return res.getColor(id, null);
        } else {
            return res.getColor(id);
        }
    }


    public static int getBackgroundColorForJpType(String jpType) {
        int color;
        switch (jpType) {
            case "早盘":
            case "盘前":
                color = TextColorUtil.generateColor(R.color.colorZaopan);
                break;
            case "午盘":
            case "盘中":
                color = TextColorUtil.generateColor(R.color.colorWupan);
                break;
            case "晚盘":
            case "解盘":
            case "盘后":
                color = TextColorUtil.generateColor(R.color.colorWanpan);
                break;
            default:
                color = TextColorUtil.generateColor(R.color.translate);
                break;
        }
        return color;
    }
}
