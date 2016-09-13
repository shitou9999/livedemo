package tv.kuainiu.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * @author nanck on 2016/7/8.
 */
public final class PermissionManager {
    private static final int CALL_PHONE = 0x1;
    private static final int READ_SMS = 0x2;


    /**
     * 必须授予的权限
     */
    public static final String[] MUST_PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.GET_ACCOUNTS
    };

    /**
     * @param permission
     * @param context
     * @return
     */
    public static boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkPermission(Context context, String[] permission) {
        if (permission.length < 1) {
            return false;
        }
        for (String per : permission) {
            if (!checkPermission(context, per)) {
                return false;
            }
        }
        return true;
    }

    /**
     * XXX
     * ActivityCompat.requestPermissions(AboutActivity.this, new String[]{Manifest.permission.CALL_PHONE},
     * CALL_PHONE_REQUEST_CODE);
     */
    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
