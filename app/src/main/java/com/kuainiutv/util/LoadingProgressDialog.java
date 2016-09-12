package com.kuainiutv.util;

import android.content.Context;

import com.kuainiutv.widget.CustomProgressDialog;


/**
 * @ClassName LoadingProgressDialog
 * @Description 加载等待对话框
 */
public class LoadingProgressDialog {

    private static CustomProgressDialog progressDialog = null;

    public static void startProgressDialog(Context context) {
        startProgressDialog(null, context);
    }

    public static void startProgressDialog(String tips, Context context) {
        try {
            stopProgressDialog();
            if (context == null) {
                return;
            }
            progressDialog = CustomProgressDialog.createDialog(context);
            progressDialog.setMessage(tips);
            progressDialog.setCancelable(false);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setMessage(String tips) {
        try {
            if (progressDialog != null) {
                progressDialog.setMessage(tips);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
