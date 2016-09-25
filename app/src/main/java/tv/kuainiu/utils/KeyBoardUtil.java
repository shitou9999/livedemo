package tv.kuainiu.utils;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


/**
 * 软件盘控制类
 *
 * @author nanck on 2016/4/9.
 */
public class KeyBoardUtil {
    public static void showKeyBoard(final Context context, final EditText editText) {
        if (null == editText) {
            return;
        }
        editText.requestFocus();
        editText.post(new Runnable() {
            @Override public void run() {
                InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        });
    }

    public static void hideSoftInput(Context context, EditText editText) {
        if (null == editText) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }


    public static boolean isOpen(Activity context) {
        return context.getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED;
    }

    // 2016/4/9 监听软键盘状态的接口 -- 弹起或隐藏
    public interface OnKeyBoardBehaveListener {
        void onShow();

        void onHide();
    }
}
