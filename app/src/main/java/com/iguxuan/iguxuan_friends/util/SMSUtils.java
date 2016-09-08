package com.iguxuan.iguxuan_friends.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.iguxuan.iguxuan_friends.R;
import com.iguxuan.iguxuan_friends.modle.cons.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName SMSUtils
 * @Description 读取验证码短信
 */
public class SMSUtils {
	private Activity _activity;
	private Handler hand;
	public static final int SMS = R.mipmap.ic_launcher;

	public SMSUtils(Activity activity, Handler handler) {
		super();
		try {
			this._activity = activity;
			this.hand = handler;
			ContentObserver c = new ContentObserver(hand) {
				@Override
				public void onChange(boolean selfChange) {
					super.onChange(selfChange);

					Message msg = new Message();
					msg.what = SMS;
					msg.obj = getsmsyzm(_activity);
					hand.sendMessage(msg);
				}
			};
			if (activity != null) {
				activity.getContentResolver().registerContentObserver(
						Uri.parse("content://sms/"), true, c);
			}
		} catch (Exception e) {
			LogUtils.e("SMSUtils", "监听短信异常", e);
		}
	}

	private String getsmsyzm(Activity c) {
		try {
			if (c == null) {
				return null;
			}
			ContentResolver cr = c.getContentResolver();
			Uri uri = Uri.parse("content://sms/inbox");
			String[] projection = new String[] { "address", "person", "body" };

			// String selection = " address='" + Constant.YZM_SEDN_PHONE_NUMBER
			// +
			// "' ";
			// String[] selectionArgs = new String[] {};
			String selection = " body like ? AND address like ? AND date >  "
					+ (System.currentTimeMillis() - 5 * 60 * 1000);// 查询最近5分钟的短信
			String[] selectionArgs = new String[] {
					Constant.YZM_CONTENTS_HEAD + "%",
					Constant.YZM_SEDN_PHONE_NUMBER_HRAD + "%" };
			String sortOrder = "date desc";
			Cursor cur = cr.query(uri, projection, selection, selectionArgs,
					sortOrder);
			if (cur != null && cur.getCount() > 0) {
				cur.moveToFirst();
				String body = cur.getString(cur.getColumnIndex("body"))
						.replaceAll("\n", " ");
				cur.close();
				return getyzm(body, Constant.YZM_LENGTH);
			}
			cur.close();
			return null;
		} catch (Exception e) {
			LogUtils.e("SMSUtils", "监听短信异常，读取短信", e);
			return null;
		}

	}

	/**
	 * 从短信字符窜提取验证码
	 * 
	 * @param body
	 *            短信内容
	 * @param YZMLENGTH
	 *            验证码的长度 一般6位或者4位
	 * @return 接取出来的验证码
	 */
	private String getyzm(String body, int YZMLENGTH) {
		// 首先([a-zA-Z0-9]{YZMLENGTH})是得到一个连续的六位数字字母组合
		// (?<![a-zA-Z0-9])负向断言([0-9]{YZMLENGTH})前面不能有数字
		// (?![a-zA-Z0-9])断言([0-9]{YZMLENGTH})后面不能有数字出现
		try {
			Pattern p = Pattern.compile("(?<![a-zA-Z0-9])([a-zA-Z0-9]{"
					+ Constant.YZM_LENGTH + "})(?![a-zA-Z0-9])");
			Matcher m = p.matcher(body);
			if (m.find()) {
				return m.group(0);
			}
			return null;
		} catch (Exception e) {
			LogUtils.e("SMSUtils", "监听短信异常,正则", e);
			return null;
		}
	}
}
