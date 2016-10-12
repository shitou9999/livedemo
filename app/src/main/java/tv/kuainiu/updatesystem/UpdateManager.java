package tv.kuainiu.updatesystem;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpCallBackListener;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.modle.AppVersion;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.ExitUtil;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.NetUtils;
import tv.kuainiu.utils.PreferencesUtils;
import tv.kuainiu.widget.NumberProgressBar;


@SuppressLint("HandlerLeak")
public class UpdateManager {
    /**
     * 服务器版本号
     */
    public static final String SERVER_VERSION_CODE = "ServerVersionCode";
    /**
     * 服务器版本名称
     */
    public static final String SERVER_VERSION_NAME = "ServerVersionName";
    /**
     * 当前程序版本号
     */
    public static final String CURRENT_VERSION_CODE = "CurrentVersionCode";
    /**
     * 当前程序版本名称
     */
    public static final String CURRENT_VERSION_NAME = "CurrentVersionName";

    public static final int CHECK_UPDATE_THREAD_HAS_UPDATE = 100;
    public static final int CHECK_UPDATE_THREAD_HAS_NO_UPDATE = 101;
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 保存解析的JSON信息 */
    // public HashMap<String, String> mHashMap;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    private Dialog xDialog;

    private Context mContext;
    /* 更新进度条 */
    private NumberProgressBar mProgress;
    /**
     * true，关闭当前程序，false不关闭
     */
    public boolean flag = true;

    /**
     * 程序包名
     */
    private String PackageInfo;

    /**
     * 程序App name
     */
    private String apkName;

    /**
     * 服务器 检查地址
     */
    // private String ServerUrl;

    /**
     * 升级的Dialog
     */
    private Dialog dialog;

    private ICheckUpdate icheckupdate;

    /**
     * 获得更新APK的包名
     *
     * @return
     */
    public String getPackageInfo() {
        return PackageInfo;
    }

    /**
     * 设置当前检查升级的项目的的包名
     *
     * @param packageInfo
     */
    public void setPackageInfo(String packageInfo) {
        PackageInfo = packageInfo;
    }

    /**
     * @param icheckupdate
     */
    public void setICheckupdate(ICheckUpdate icheckupdate) {
        this.icheckupdate = icheckupdate;
    }

    public String getApkName() {
        return apkName;
    }

    /**
     * 获取检查更新版本的服务地址
     *
     * @return
     */
    // public String getServerUrl() {
    // return ServerUrl;
    // }

    /**
     * 设置检查版本的服务器url
     *
     * @param serverUrl
     */
    // public void setServerUrl(String serverUrl) {
    // ServerUrl = serverUrl;
    // }

    /**
     * 设置下载后文件的保存名称,不要加.apk后缀
     *
     * @param apkName
     */
    @SuppressLint("HandlerLeak")
    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };
    private int versionCode;
    protected AppVersion version;

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate() {
        isUpdate(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case CHECK_UPDATE_THREAD_HAS_UPDATE:
                        // 显示提示对话框
                        showNoticeDialog();
                        break;
                    case CHECK_UPDATE_THREAD_HAS_NO_UPDATE:
                        if (icheckupdate != null) {
                            icheckupdate.result(false);
                        }

                        break;
                }
                // Intent intentNew = new Intent(); // Itent就是我们要发送的内容
                // intentNew.putExtra(CONSTANT.UPDATA_APP_ACTION,
                // CONSTANT.UPDATA_APP_NEW);
                // intentNew.setAction(CONSTANT.UPDATA_APP_ACTION); //
                // 设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
                // mContext.sendBroadcast(intentNew); // 发送广播
            }

        });
    }

    public void checkUpdate(Handler handler) {
        if (null != handler) {
            isUpdate(handler);
        }
    }

    /**
     * 检查软件是否有更新版本
     *
     * @return
     */
    private void isUpdate(final Handler handler) {
        versionCode = getVersionCode(mContext, getPackageInfo());
        PreferencesUtils.putInt(mContext, CURRENT_VERSION_CODE, versionCode);
        PreferencesUtils.putString(mContext, CURRENT_VERSION_NAME,
                getVersionName(mContext, getPackageInfo()));

//		CheckUpdateVersionThread thread = new CheckUpdateVersionThread();
//		thread.setClientSign(0);// 0 android；1 ios；
//		thread.setAppVersion(versionCode);
//		thread.setContext(mContext);
//		thread.settListener(new NetApiThreadListener() {
//
//			@Override
//			public void onNetApiSuccess(String tKey, Object obj) {
//				if (obj != null) {
//					version = (AppVersion) obj;
//					// version = new AppVersion();
//					// version.setLoad_Url("");
//					// version.setParameter1("");
//					// version.setParameter2("");
//					// version.setParameter3("");
//					// version.setType(1);
//					// version.setVersion_code(10);
//					// version.setVersion_dec("1、僵尸叔叔\n2、介绍的还是看");
//					// version.setVersion_name("3.23");
//					if (version.getVersion_code() > versionCode) {
//						PreferencesUtils.putInt(mContext, SERVER_VERSION_CODE,
//								version.getVersion_code());
//						PreferencesUtils.putString(mContext,
//								SERVER_VERSION_NAME, version.getVersion_name());
//						Message msg = new Message();
//						msg.what = CHECK_UPDATE_THREAD_HAS_UPDATE;
//						handler.sendMessage(msg);
//					} else {
//						Message msg = new Message();
//						msg.what = CHECK_UPDATE_THREAD_HAS_NO_UPDATE;
//						handler.sendMessage(msg);
//					}
//				} else {
//					Message msg = new Message();
//					msg.what = CHECK_UPDATE_THREAD_HAS_NO_UPDATE;
//					handler.sendMessage(msg);
//				}
//			}
//
//			@Override
//			public void onNetApiStart(String tKey) {
//
//			}
//
//			@Override
//			public void onNetApiFinish(String tKey) {
//
//			}
//
//			@Override
//			public void onNetApiFail(String tKey, int returnCode) {
//				Message msg = new Message();
//				msg.what = CHECK_UPDATE_THREAD_HAS_NO_UPDATE;
//				handler.sendMessage(msg);
//			}
//		});
//		thread.start();


        if (!NetUtils.isOnline(mContext)) {
            LogUtils.i("Update Log", "可用的网络连接，请检查后重试");
            return;
        }
        String url = Api.TEST_DNS_API_HOST.concat(Api.VERSION);
        OkHttpClient mClient = new OkHttpClient();

        Map<String, String> map = new HashMap<>();
        map.put("type", "android");
        map.put("version", String.valueOf(versionCode));

        RequestBody body = new FormBody.Builder().add("data", ParamUtil.getParam(map)).build();
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", OKHttpUtils.testUserAgent(mContext))
                .post(body)
                .build();
        mClient.newCall(request).enqueue(new OKHttpCallBackListener() {
            @Override
            public void onSucceed(int code, String message, JSONObject data) {
                LogUtils.i("hhhhkjhkj", "data=" + data);
                try {
                    if (data != null && data.has("status") && 0 == data.getInt("status") && data.has("data")) {
                        DataConverter<AppVersion> dataConverter = new DataConverter<AppVersion>();
                        version = dataConverter.JsonToObject(data.getString("data"), AppVersion.class);
                        // version = new AppVersion();
                        // version.setLoad_Url("");
                        // version.setParameter1("");
                        // version.setParameter2("");
                        // version.setParameter3("");
                        // version.setType(1);
                        // version.setVersion_code(10);
                        // version.setVersion_dec("1、僵尸叔叔\n2、介绍的还是看");
                        // version.setVersion_name("3.23");
                        if (version.is_update==1) {
                            PreferencesUtils.putInt(mContext, SERVER_VERSION_CODE,
                                    version.getLatest_version());
                            PreferencesUtils.putString(mContext,
                                    SERVER_VERSION_NAME, version.getVersion_name());
                            Message msg = new Message();
                            msg.what = CHECK_UPDATE_THREAD_HAS_UPDATE;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = new Message();
                            msg.what = CHECK_UPDATE_THREAD_HAS_NO_UPDATE;
                            handler.sendMessage(msg);
                        }
                    } else {
                        Message msg = new Message();
                        msg.what = CHECK_UPDATE_THREAD_HAS_NO_UPDATE;
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = CHECK_UPDATE_THREAD_HAS_NO_UPDATE;
                    handler.sendMessage(msg);
                }

            }

            @Override
            public void onFailure(int code, String mssgage) {
                LogUtils.i("hhhhkjhkj", "msg=" + mssgage);
                Message msg = new Message();
                msg.what = CHECK_UPDATE_THREAD_HAS_NO_UPDATE;
                handler.sendMessage(msg);
            }

            @Override
            public void onNull(int code) {
                LogUtils.i("hhhhkjhkj", "code=" + code);
                Message msg = new Message();
                msg.what = CHECK_UPDATE_THREAD_HAS_NO_UPDATE;
                handler.sendMessage(msg);
            }
        });

    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context, String packageName) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(
                    packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
            LogUtils.printStackTrace(e);
        }
        return versionCode;

    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context, String packageName) {
        String versionname = "1.0";
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionname = context.getPackageManager().getPackageInfo(
                    packageName, 0).versionName;
        } catch (NameNotFoundException e) {
            LogUtils.printStackTrace(e);
        }
        return versionname;

    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {

        dialog = new SystemUpdateDialog(mContext, R.style.system_update_dialog,
                new SystemUpdateDialog.LeaveMeetingDialogListener() {

                    @Override
                    public void onClick(View view) {
                        if (view.getId() == R.id.dialog_button_ok) {
                            PreferencesUtils.putBoolean(mContext,
                                    Constant.IS_FIRST_OPENED_APP, true);
                            dialog.dismiss();
                            // 显示下载对话框
                            showDownloadDialog();

                        }
                        if (view.getId() == R.id.dialog_button_cancel) {
                            dialog.dismiss();
                            Constant.IS_FIRIST = false;
                        }

                    }
                }, version);
        dialog.setCancelable(version.getIs_force_update() == Constant.MUST_UPDATE ? false
                : true);
        dialog.show();

    }

    /**
     * 显示软件下载对话框
     */
    public void showDownloadDialog() {
        // 构造软件下载对话框
        xDialog = new DownLoadDialog(mContext,
                R.style.system_update_download_dialog,
                new DownLoadDialog.XDialog() {

                    @Override
                    public void onClick(View view) {
                        xDialog.dismiss();
                        // 设置取消状态
                        cancelUpdate = true;

                        if (version != null
                                && Constant.MUST_UPDATE == version.getIs_force_update()) {// 强制更新
                            Constant.IS_FIRIST = true;
                            ExitUtil.getInstance().closes();
                        }
                    }
                });
        // builder.setName("正在下载");
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(
                R.layout.common_system_update_download_dialog, null);
        mProgress = (NumberProgressBar) v.findViewById(R.id.update_progress);

        xDialog.setContentView(v);
        // 取消更新
        // builder.setNegativeButton("取消", new OnClickListener() {
        // @Override
        // public void onClick(DialogInterface dialog, int which) {
        // dialog.dismiss();
        // // 设置取消状态
        // cancelUpdate = true;
        // }
        // });

        // mDownloadDialog = builder.create();
        xDialog.setCancelable(false);
        xDialog.show();
        // 现在文件
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory()
                            + "/";
                    mSavePath = sdpath + "download";
                    URL url = new URL(version.getLoad_url());
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath,
                            version.getVersion_name() + ".apk");
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                } else {
                    Toast.makeText(mContext, "SD卡不存在或不SD卡不可写",
                            Toast.LENGTH_LONG).show();
                }
            } catch (MalformedURLException e) {
                LogUtils.printStackTrace(e);
            } catch (IOException e) {
                LogUtils.printStackTrace(e);
            }
            // 取消下载对话框显示
            xDialog.dismiss();
        }
    }

    ;

    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkfile = new File(mSavePath, version.getVersion_name() + ".apk");
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        mContext.startActivity(i);
        if (flag) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public interface ICheckUpdate {
        public void result(boolean isnew);
    }
}
