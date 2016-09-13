package tv.kuainiu.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by jack on 2016/5/4.
 * 缓存读写
 */
public class CacheManage {
    private static CacheManage instance;

    public static CacheManage getInstance() {
        if (null == instance) {
            instance = new CacheManage();
        }
        return instance;
    }

    /**
     * 保存序列化对象
     *
     * @param str  序列化对象
     * @param file 保存到具体的文件
     */
    public boolean saveStringObject(Context context, String str, String file) {
        long yesterday = System.currentTimeMillis() - 3600000;//删除1小时之前的缓存数据（86400000，一天)
        try {
            clearAppCache(context, yesterday);//每一次缓存保存前都清空1小时之前的缓存，避免用户不知道清空缓存，APP缓存数据过大，浪费用户手机内存空间
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LogUtils.i("CachManage", "saveStringObject_str:" + str);
            LogUtils.i("CachManage", "saveStringObject_file:" + file);
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            try {
                /**
                 * 函数 openFileOutput方法的第一个参数用于指定文件名称，不能包含路径分割符“/”
                 * 参数二为对文件的访问权限，如果希望有多个权限，则写成如下形式： Context.MODE_PRIVATE +
                 * Context.MODE_APPEND 如果文件不存在，Android会自动创建，
                 * 创建的文件保存在/data/data/Activity所在的包/files目录
                 */
                fos = context.openFileOutput(file, Context.MODE_PRIVATE);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(str);
                oos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    oos.close();
                } catch (Exception e) {
                }
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }
        }

    }

    /**
     * 保存序列化对象
     *
     * @param ser  序列化对象
     * @param file 保存到具体的文件
     */
    public boolean saveObject(Serializable ser, String file, Context context) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            /**
             * 函数 openFileOutput方法的第一个参数用于指定文件名称，不能包含路径分割符“/”
             * 参数二为对文件的访问权限，如果希望有多个权限，则写成如下形式： Context.MODE_PRIVATE +
             * Context.MODE_APPEND 如果文件不存在，Android会自动创建，
             * 创建的文件保存在/data/data/Activity所在的包/files目录
             */
            fos = context.openFileOutput(file, context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 读取对象
     *
     * @param file 传入一个文件名
     * @return 返回序列化对象
     */
    public Serializable readObject(Context context, String file) {
        if (!isExistDataCache(context, file))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File data = context.getFileStreamPath(file);
                data.delete();
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 读取对象
     *
     * @param file 传入一个文件名
     * @return 返回序列化对象
     */
    public String readStringObject(Context context, String file) {
        String result = null;
        if (!isExistDataCache(context, file)) {
            return null;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(file);
            ois = new ObjectInputStream(fis);
            result = ois.readObject().toString();
            LogUtils.i("CachManage", "readStringObject_str:" + result);
            LogUtils.i("CachManage", "readStringObject_file:" + file);
        } catch (Exception e) {
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File data = context.getFileStreamPath(file);
                data.delete();
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * 判断缓存是否存在
     *
     * @param cachefile
     * @return
     */
    private boolean isExistDataCache(Context context, String cachefile) {
        boolean exist = false;
        try {
            File data = context.getFileStreamPath(cachefile);
            if (data.exists()) {
                exist = true;
            }
        } catch (Exception e) {

        }
        return exist;
    }

    /**
     * 清理app緩存 注意：CacheManager在4.0环境下可以 高版本已经deprecated 可能报错
     */
    public void clearAppCache(Context context) throws Exception {
        clearAppCache(context, System.currentTimeMillis());
    }

    /**
     * 清理app緩存 注意：CacheManager在4.0环境下可以 高版本已经deprecated 可能报错
     */
    public void clearAppCache(Context context, long time) throws Exception {
        // 清除webview缓存
        /*File file = CacheManager.getCacheFileBaseDir();
        // Log.i("file2","getCacheFileBaseDir:"+file.getPath());
		if (file != null && file.exists() && file.isDirectory()) {
			for (File item : file.listFiles()) {
				item.delete();
			}
			file.delete();
		}*/

        // webview在浏览网页时 会自动产生这2个缓存文件
        if (time < 0) {
            time = System.currentTimeMillis();
        }
        try {
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");
        }catch (Exception e){
            LogUtils.e("CacheManage","clearAppCache",e);
        }

        // 清除文件缓存
        clearCacheFolder(context.getFilesDir(), time);
        // 清除数据缓存
        clearCacheFolder(context.getCacheDir(), time);
        String imageCachePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + File.separator
                + AppUtils.getAppName(context)
                + File.separator
                + "imageCache" + File.separator;
        File imageCatchFile = new File(imageCachePath);
        Log.i("File2", "imageCachePath:" + imageCachePath);
        clearCacheFolder(imageCatchFile, time);

        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            clearCacheFolder(getExternalCacheDir(context),
                    time);
        }

    }

    /**
     * 获取存储卡上的缓存目录
     *
     * @param context
     * @return
     */
    public File getExternalCacheDir(Context context) {
        return context.getExternalCacheDir();
    }

    /**
     * 清除缓存目录
     *
     * @param dir     目录
     * @param curTime 当前系统时间
     * @return
     */
    private int clearCacheFolder(File dir, long curTime) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }


    public String CalculationCacheSize(Context context) {
        String catchSize = "0";
        Long size = 0l;
        File catchFiles = getExternalCacheDir(context);
        try {
            Long catchsize = getFolderSize(catchFiles);
            size += catchsize;
            Log.i("file2", "catchsize:" + String.valueOf(catchsize));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            File file = context.getFilesDir();
            Long catchsize = getFolderSize(file);
            size += catchsize;
            Log.i("file", "catchsize:" + String.valueOf(catchsize));
        } catch (Exception e) {
            e.printStackTrace();
        }
        BigDecimal sizeBig = new BigDecimal(size);
        BigDecimal baseBig = new BigDecimal(1048576);
        BigDecimal resultBig = sizeBig.divide(baseBig, 2,
                BigDecimal.ROUND_HALF_UP);
        catchSize = String.valueOf(resultBig);
        catchSize = catchSize + "M";

        return catchSize;
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long 单位为M
     * @throws Exception
     */
    public long getFolderSize(java.io.File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                Log.i("getFolderSize", "i:" + String.valueOf(i));
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        }
        Log.i("getFolderSize", "getFolderSize:" + String.valueOf(size));
        return size;
    }
}
