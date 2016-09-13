package tv.kuainiu.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import tv.kuainiu.IGXApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;

/**
 * @ClassName CrashHandler
 * @Description UncaughtExceptionHandler：线程未捕获异常控制器是用来处理未捕获异常的。
 * 如果程序出现了未捕获异常默认情况下则会出现强行关闭对话框 实现该接口并注册为程序中的默认未捕获异常处理
 * 这样当未捕获异常发生时，就可以做些异常处理操作 例如：收集异常信息，发送错误报告 等。
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 */
public class CrashHandler implements UncaughtExceptionHandler {
    /**
     * Debug LogUtils Tag
     */
    public static final String TAG = "CrashHandler";
    /**
     * 是否开启日志输出, 在Debug状态下开启, 在Release状态下关闭以提升程序性能
     */
    public static final boolean DEBUG = IGXApplication.IsDegbug;
    public static final boolean IsUpUpload = true;
    /**
     * CrashHandler实例
     */
    private static CrashHandler INSTANCE;
    /**
     * 程序的Context对象
     */
    private Context mContext;
    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * 用来存储设备信息和异常信息
     */
    private Map<String, String> infos = new HashMap<String, String>();

    /**
     * 错误报告文件的扩展名
     */
    private static final String CRASH_REPORTER_EXTENSION = ".txt";

    /**
     * 异常信息保存到文件路径
     */
    private static String FileSavePath;
    // 用于格式化日期,作为日志文件名的一部分
    private SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd HH.mm.ss.SSS", Locale.CHINA);

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (INSTANCE == null)
            INSTANCE = new CrashHandler();
        return INSTANCE;
    }

    public void init(Context ctx) {
        init(ctx, null);
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */
    public void init(Context ctx, String filespath) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        FileSavePath = filespath == null ? SDCardUtils.getSDCardPath()
                + AppUtils.getAppName(mContext) + File.separator : filespath;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            // Sleep一会后结束程序
            // 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                LogUtils.e(TAG, "Error : ", e);
            }
            // System.exit(0);
            ExitUtil.getInstance().exit();
            // Intent intent = new Intent(mContext, BuyersMainActivity.class);
            // mContext.startActivity(intent);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }
        // 收集设备信息
        collectCrashDeviceInfo(mContext);
        // 保存错误报告文件
        saveCrashInfoToFile(ex);
        // 发送错误报告到服务器
        // sendCrashReportsToServer(mContext);
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                // Toast 显示需要出现在一个线程的消息队列中
                Looper.prepare();
                Toast.makeText(mContext, "程序出现未知错误", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();

        return true;
    }

    /**
     * 收集程序崩溃的设备信息
     *
     * @param ctx
     */
    public void collectCrashDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                LogUtils.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }


    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return
     */
    public String saveCrashInfoToFile(Throwable ex) {
        LogUtils.e(TAG, "程序崩溃", ex);
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + CRASH_REPORTER_EXTENSION;
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                File dir = new File(FileSavePath);

                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(FileSavePath
                        + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            LogUtils.e(TAG, "an error occured while writing file...", e);
        }
        return null;

    }

    /**
     * 把错误报告发送给服务器,包含新产生的和以前没发送的.
     *
     * @param ctx
     */
    private void sendCrashReportsToServer(Context ctx) {
        String[] crFiles = getCrashReportFiles(ctx);
        if (crFiles != null && crFiles.length > 0) {
            TreeSet<String> sortedFiles = new TreeSet<String>();
            sortedFiles.addAll(Arrays.asList(crFiles));

            for (String fileName : sortedFiles) {
                File cr = new File(CrashHandler.FileSavePath, fileName);
                postReport(cr, ctx);
                // 不是debug模式发送报告后删除错误日子日志文件
                // if (!DEBUG) {
                // cr.delete();// 删除已发送的报告
                // }

            }
        }
    }

    /**
     * 获取错误报告文件名
     *
     * @param ctx
     * @return
     */
    private String[] getCrashReportFiles(Context ctx) {
        // File filesDir = ctx.getFilesDir();
        File filesDir = new File(FileSavePath);
        // 实现FilenameFilter接口的类实例可用于过滤器文件名
        FilenameFilter filter = new FilenameFilter() {
            // accept(File dir, String name)
            // 测试指定文件是否应该包含在某一文件列表中。
            public boolean accept(File dir, String name) {
                return name.endsWith(CRASH_REPORTER_EXTENSION);
            }
        };
        // list(FilenameFilter filter)
        // 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中满足指定过滤器的文件和目录
        return filesDir.list(filter);
    }

    private void postReport(final File file, Context ctx) {
        // if (!IsUpUpload)
        // return;
        // RequestParams params = new RequestParams();
        // try {
        // if (!Utils.isEmpty(ServerConfig.CRASH_REPORTER_EXTENSION_URL)) {
        // params.put("LogUtils", file);
        // 使用HTTP Post 发送错误报告到服务器
        // HttpUtil.post(ServerConfig.CRASH_REPORTER_EXTENSION_URL,
        // params, new AsyncHttpResponseHandler() {
        //
        // @Override
        // public void onSuccess(int statusCode,
        // Header[] arg1, byte[] arg2) {
        // try {
        // if (!DEBUG)
        // file.delete();
        // } catch (Exception e) {
        // LogUtils.e(TAG, "上传成功后删除已上传日志文件异常", e);
        // }
        // LogUtils.i(TAG, "上传异常日志成功," + new String(arg2));
        //
        // }
        //
        // @Override
        // public void onFailure(int arg0, Header[] arg1,
        // byte[] arg2, Throwable arg3) {
        // LogUtils.e(TAG, "上传异常日志失败", arg3);
        // }
        //
        // });
        // } else if (DEBUG) {
        // Toast.makeText(ctx, "上传错误日志服务器地址未填写", Toast.LENGTH_LONG).show();
        // LogUtils.i(TAG, "上传错误日志服务器地址未填写");
        // }
        // } catch (FileNotFoundException e) {
        // LogUtils.e(TAG, "上传错误日志文件不存在", e);
        // }

    }

    /**
     * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
     */
    public void sendPreviousReportsToServer() {
        sendCrashReportsToServer(mContext);
    }


}