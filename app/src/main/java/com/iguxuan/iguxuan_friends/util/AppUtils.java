package com.iguxuan.iguxuan_friends.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

import com.iguxuan.iguxuan_friends.R;

import java.io.File;


/**
 * @ClassName AppUtils
 * @Description 跟App相关的辅助类
 */
public class AppUtils {
	private AppUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本名称信息]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getAppVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序文件夹路径]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getAppFilePath(Context context) {
		String filePath = Environment.getExternalStorageDirectory()
				+ File.separator
				+ StringUtils.replaceNullToEmpty(AppUtils.getAppName(context),
						context.getResources().getString(R.string.app_name))
				+ File.separator;
		return filePath;
	}

	/**
	 * [获取应用程序文件夹路径]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getAppFilePathImage(Context context) {
		String filePath = Environment.getExternalStorageDirectory()
				+ File.separator
				+ StringUtils.replaceNullToEmpty(AppUtils.getAppName(context),
						context.getResources().getString(R.string.app_name))
				+ File.separator + "images" + File.separator;
		return filePath;
	}
	/**
	 * [获取应用程序文件夹路径]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getAppCameraFilePathImage(Context context) {
		String filePath = Environment.getExternalStorageDirectory()
				+ File.separator
				+ StringUtils.replaceNullToEmpty(AppUtils.getAppName(context),
						context.getResources().getString(R.string.app_name))
						+ File.separator + "camera" + File.separator;
		return filePath;
	}

	/**
	 * [获取应用程序文件夹路径]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getAppFilePathAvatar(Context context) {
		String filePath = Environment.getExternalStorageDirectory()
				+ File.separator
				+ StringUtils.replaceNullToEmpty(AppUtils.getAppName(context),
						context.getResources().getString(R.string.app_name))
				+ File.separator + "Avatar" + File.separator;
		return filePath;
	}

	/**
	 * [获取应用程序版本名称信息]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static int getAppVersionCode(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionCode;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * [获取应用程序包名信息]
	 * 
	 * @param context
	 * @return 当前应用的包名
	 */
	public static String getAppPackageName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.packageName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
