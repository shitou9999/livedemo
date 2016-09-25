package tv.kuainiu.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/4/22.
 */
public class ToastUtils {
    private static Toast mToast=null;
    /**
     * 重新Toast
     *
     * @param context
     * @param msg
     * @param duration
     */
    public static void showToast(Context context, String msg, int duration) {
        showToast(context, msg, duration, Gravity.CENTER);
    }

    /**
     * 重新Toast
     *
     * @param context
     * @param msg
     * @param duration
     */
    public static void showToast(Context context, String msg, int duration, int gravity) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), msg, duration);
        } else {
            mToast.setText(msg);
        }
        mToast.setGravity(gravity, 0, 0);
        mToast.show();
    }

    /**
     * 封装Toast，当要弹出多个Toast时，最后弹出的Toast会立刻弹出，不会等待前面的Toast逐个显示完毕才显示最后打的
     *
     * @param content 内容
     * @param context 上下文对象
     */
    public static void showToast(Context context, String content) {
		/*
		 * new Thread(new Runnable() {
		 *
		 * @Override public void run() { Looper.prepare();
		 * Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
		 * Looper.loop(); } }).start();
		 */
        showToast(context, content, Toast.LENGTH_SHORT);
    }

}
