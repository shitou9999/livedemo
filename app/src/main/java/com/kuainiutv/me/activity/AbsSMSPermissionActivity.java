package com.kuainiutv.me.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.kuainiutv.R;
import com.kuainiutv.ui.activity.BaseActivity;


/**
 */
public abstract class AbsSMSPermissionActivity extends BaseActivity {
    protected static final int SMS_REQUEST_CODE = 10;
    private boolean isStartGrant = false;

    @Override protected void onResume() {
        super.onResume();
        if (isStartGrant) return;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            requestSMSPermission();
        } else {
            awarded();
        }
    }

    private void requestSMSPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
            new AlertDialog.Builder(this)
                    .setTitle("权限释义")
                    .setCancelable(false)
                    .setMessage(R.string.permission_tip_dialog_message_sms)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AbsSMSPermissionActivity.this,
                                    new String[]{Manifest.permission.READ_SMS},
                                    SMS_REQUEST_CODE);
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS},
                    SMS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (SMS_REQUEST_CODE == requestCode) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                awarded();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        isStartGrant = true;
    }


    protected abstract void awarded();


}
