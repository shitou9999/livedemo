package com.iguxuan.iguxuan_friends.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.iguxuan.iguxuan_friends.command.http.core.ParamUtil;
import com.iguxuan.iguxuan_friends.modle.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by guxuan on 2016/2/29.
 */
public class FileUtils {
    public static final String FILE_USER = "ugg";

    /**
     *
     * @param context
     * @param user
     * @return
     */
    public static boolean saveUser(Context context, User user) {
        boolean flag;
        if (user != null) {
            String json = new Gson().toJson(user);
            json = SecurityUtils.DESUtil.en(ParamUtil.getKey(), json);
            flag = PreferencesUtils.putString(context, FILE_USER, json);
        } else {
            flag = PreferencesUtils.putString(context, FILE_USER, "");
        }
        return flag;
    }

    public static User readUser(Context context) {
        User user = null;

        String old = PreferencesUtils.getString(context, FILE_USER, "");
        if (!TextUtils.isEmpty(old)) {
            try {
                DebugUtils.dd("old " + old);
                String json = SecurityUtils.DESUtil.de(ParamUtil.getKey(), old);
                user = new Gson().fromJson(json, User.class);
            } catch (Exception e) {
                LogUtils.e("FileUtils", "读取本地用户对象转换失败", e);
                saveUser(context, null);//读取本地的读不了就置空
            }
        }
        return user;

    }

    /**
     * 将Bitmap 图片保存到本地路径，并返回路径
     *
     * @param c        资源类型，参照  MultimediaContentType 枚举，根据此类型，保存时可自动归类
     * @param fileName 文件名称
     * @param bitmap   图片
     * @return
     */
    public static String saveFile(Context c, String fileName, Bitmap bitmap) {
        return saveFile(c, "", fileName, bitmap);
    }

    public static String saveFile(Context c, String filePath, String fileName, Bitmap bitmap) {
        byte[] bytes = bitmapToBytes(bitmap);
        return saveFile(c, filePath, fileName, bytes);
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static String saveFile(Context c, String filePath, String fileName, byte[] bytes) {
        String fileFullName = "";
        FileOutputStream fos = null;
        String dateFolder = new SimpleDateFormat("yyyyMMdd", Locale.CHINA)
                .format(new Date());
        try {
            String suffix = "";
            if (filePath == null || filePath.trim().length() == 0) {
                filePath = Environment.getExternalStorageDirectory() + "/JiaXT/" + dateFolder + "/";
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(filePath, fileName + suffix);
            fileFullName = fullFile.getPath();
            fos = new FileOutputStream(new File(filePath, fileName + suffix));
            fos.write(bytes);
        } catch (Exception e) {
            fileFullName = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    fileFullName = "";
                }
            }
        }
        return fileFullName;
    }

}


