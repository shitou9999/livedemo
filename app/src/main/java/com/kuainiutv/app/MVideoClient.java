package com.kuainiutv.app;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import com.easefun.polyvsdk.PolyvSDKClient;
import com.easefun.polyvsdk.SDKUtil;
import com.kuainiutv.command.sevice.PolyvDemoService;
import com.kuainiutv.util.DebugUtils;

import java.io.File;

/**
 * 封装保利SDK初始化,在 Application 初始化时调用 @see{@link #initPolyvCilent()}<p>
 * 建议参考<a href='https://github.com/easefun/polyv-android-sdk-demo/wiki'>Polyv SDK wiki</a>
 * </p>
 *
 * @author nanck on 2016/5/17.
 */
public class MVideoClient {
    private static final String POLYV_SDK_Encrypted_KEY = "k5QQapaCrgwd5Dwdq5Av1VGUXkmfbCgKOg7YinX990j7mpQDkKVoWddyNvfi1CFx5bFoBEjJlow9wbuQzQ3oYi6ytEVMDDC6+avgtprRDMmVv39qN+0RONsYtXOuSfaBIBCsJaJgeLe8VYNB7/kIfw==";
    private static MVideoClient mInstance;
    private Context mContext;

    private MVideoClient(Context context) {
        mContext = context;
    }

    public static synchronized MVideoClient getmInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MVideoClient(context.getApplicationContext());
        }
        return mInstance;
    }

    public void initPolyvCilent() {
        File saveDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            saveDir = new File(Environment.getExternalStorageDirectory().getPath() + "/iguxuandownload");
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            DebugUtils.dd("有SD卡 : " + saveDir.getPath());
        } else {
            DebugUtils.dd("没有SD卡");
        }

        PolyvSDKClient client = PolyvSDKClient.getInstance();

        //设置SDK加密串
        client.setConfig(POLYV_SDK_Encrypted_KEY);
        //下载文件的目录
        if (saveDir != null && saveDir.exists()) {
            client.setDownloadDir(saveDir);
        }
        //初始化数据库服务
        client.initDatabaseService(mContext);
        //启动服务
        client.startService(mContext.getApplicationContext(), PolyvDemoService.class);
    }


    /**
     * 网络方式取得SDK加密串，（推荐）
     */
    private class LoadConfigTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String config = SDKUtil.getUrl2String("http://demo.polyv.net/demo/appkey.php", false);
            if (TextUtils.isEmpty(config)) {
                try {
                    throw new Exception("没有取到数据");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return config;
        }

        @Override
        protected void onPostExecute(String config) {
            PolyvSDKClient client = PolyvSDKClient.getInstance();
            client.setConfig(config);
        }
    }
}
