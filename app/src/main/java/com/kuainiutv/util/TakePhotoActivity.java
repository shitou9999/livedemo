package com.kuainiutv.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;

import com.kuainiutv.ui.activity.BaseActivity;

import java.io.File;

/**
 * @ClassName TakePhotoActivity
 * @Description 拍照
 */
public class TakePhotoActivity extends BaseActivity {
	private String mImageFilePath;
	public static final String IMAGEFILEPATH = "ImageFilePath";
	public final static String IMAGE_PATH = "com.kuainiutv.utils_TakePhotoActivity_image_path";
	static Activity mContext;
	public final static int GET_IMAGE_REQ = 5000;
	private Uri imageFileUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 判断 activity被销毁后 有没有数据被保存下来
		if (savedInstanceState != null) {

			mImageFilePath = savedInstanceState.getString(IMAGEFILEPATH);

			File mFile = new File(mImageFilePath);
			if (mFile!=null && mFile.exists()) {
				Intent rsl = new Intent();
				rsl.putExtra(IMAGE_PATH, mImageFilePath);
				setResult(Activity.RESULT_OK, rsl);
				LogUtils.i("TakePhotoActivity", "123___" + "图片拍摄成功");
				finish();
			} else {
				finish();
				LogUtils.i("TakePhotoActivity", "123___" + "图片拍摄失败");
			}
		}

		mContext = this;
		if (savedInstanceState == null) {
			initialUI();
		}

	}

	public void initialUI() {
		if (!"vivo".equalsIgnoreCase(Build.MANUFACTURER)) {
			showCamera();
		} else {
			// 根据时间生成 后缀为 .jpg 的图片
			long ts = System.currentTimeMillis();
			mImageFilePath = getImagePath() + ts + ".jpg";
			File out = new File(mImageFilePath);
			showCamera(out);
		}
		// showCamera();
	}

	private void showCamera() {
		imageFileUri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,
				new ContentValues());

		// Intent intent = new Intent(
		// android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		//
		// intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
		// imageFileUri);
		//
		// startActivityForResult(intent, TAKE_PICTURE);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri); // set
		startActivityForResult(intent, GET_IMAGE_REQ);
	}

	private void showCamera(File out) {
		imageFileUri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,
				new ContentValues());

		// Intent intent = new Intent(
		// android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		//
		// intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
		// imageFileUri);
		//
		// startActivityForResult(intent, TAKE_PICTURE);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out)); // set
		startActivityForResult(intent, GET_IMAGE_REQ);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if (GET_IMAGE_REQ == requestCode && resultCode == Activity.RESULT_OK) {

			Intent rsl = new Intent();
			if (!"vivo".equalsIgnoreCase(Build.MANUFACTURER)) {
				if (mImageFilePath == null || mImageFilePath.length() <= 0) {
					mImageFilePath = convertFileUri(mContext, imageFileUri);
				}
			}
			rsl.putExtra(IMAGE_PATH, mImageFilePath);
			mContext.setResult(Activity.RESULT_OK, rsl);
			mContext.finish();

		} else {
			mContext.finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (!"vivo".equalsIgnoreCase(Build.MANUFACTURER)){
			outState.putString(IMAGEFILEPATH, convertFileUri(mContext, imageFileUri));
		}else{
			outState.putString(IMAGEFILEPATH, mImageFilePath);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

	}

	public static String getImagePath() {
		String filePath = AppUtils.getAppCameraFilePathImage(mContext);
		File file = new File(filePath);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		file = null;
		return filePath;
	}

	public String convertFileUri(Context mContext, Uri uri) {
		String filePath;
		try {
			if (uri != null && "content".equals(uri.getScheme())) {
				Cursor cursor = mContext
						.getContentResolver()
						.query(uri,
								new String[] { MediaStore.Images.ImageColumns.DATA },
								null, null, null);
				cursor.moveToFirst();
				filePath = cursor.getString(0);
				cursor.close();
			} else {
				// filePath = uri.getPath();
				filePath = "";
			}
		} catch (Exception e) {
			LogUtils.e("TakePhotoActivity", "convertFileUri失败", e);
			filePath = "";
		}
		// return Uri.parse("file://" + filePath);
		return filePath;
	}
}
