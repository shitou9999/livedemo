package tv.kuainiu.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;

import tv.kuainiu.R;
import tv.kuainiu.ui.me.activity.LoginActivity;


/**
 * @author nanck on 2016/4/14.
 */
public class LoginPromptDialog {
    private Context mContext;
    private CallBack mCallBack;

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public LoginPromptDialog(Context context) {
        mContext = context;
    }


    public void show() {
        Resources res = mContext.getResources();
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext)
                .setTitle(res.getString(R.string.prompt))
                .setMessage(res.getString(R.string.dialog_login_prompt))

                .setNegativeButton(res.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mCallBack != null) {
                            mCallBack.onCancel(dialog, which);
                        }
                        dialog.dismiss();
                    }
                })

                .setPositiveButton(res.getString(R.string.login), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mCallBack != null) {
                            mCallBack.onLogin(dialog, which);
                        } else {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            mContext.startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                });
        mBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override public void onDismiss(DialogInterface dialog) {
                if (mCallBack != null) {
                    mCallBack.onDismiss(dialog);
                }
            }
        });
        mBuilder.create().show();
    }


    public interface CallBack {
        void onCancel(DialogInterface dialog, int which);

        void onLogin(DialogInterface dialog, int which);

        void onDismiss(DialogInterface dialog);
    }


}
