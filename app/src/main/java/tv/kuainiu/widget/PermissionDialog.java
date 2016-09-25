package tv.kuainiu.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import tv.kuainiu.R;
import tv.kuainiu.utils.ExitUtil;


/**
 * @author nanck on 2016/7/12.
 */
public class PermissionDialog {
    private static final int REQUEST_CODE = 1;
    private Activity mContext;


    public PermissionDialog(Activity context) {
        mContext = context;
    }


    private void requestAlertWindowPermission(Activity context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivityForResult(intent, REQUEST_CODE);
    }

    public void show() {
        show("", false);
    }

    public void show(@StringRes int resID) {
        show(mContext.getResources().getString(resID), false);
    }

    public void show(@StringRes int resID, boolean isExit) {
        show(mContext.getResources().getString(resID), isExit);
    }

    public void show(CharSequence charSequence, final boolean isExit) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext)
                .setTitle(R.string.permission_tip_dialog_title)
                .setMessage(charSequence)
                .setCancelable(false)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (isExit) {
                            ExitUtil.getInstance().exit();
                        }
                    }
                })

                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestAlertWindowPermission(mContext);
                        dialog.dismiss();
                    }
                });
        mBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override public void onDismiss(DialogInterface dialog) {
            }
        });
        mBuilder.create().show();
    }


}
