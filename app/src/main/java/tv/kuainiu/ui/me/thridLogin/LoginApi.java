package tv.kuainiu.ui.me.thridLogin;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

public class LoginApi implements Callback {
    private static final int MSG_AUTH_CANCEL = 1;
    private static final int MSG_AUTH_ERROR = 2;
    private static final int MSG_AUTH_COMPLETE = 3;

    private OnLoginListener loginListener;
    private String platform;
    private Context context;
    private Handler handler;

    public LoginApi() {
        handler = new Handler(Looper.getMainLooper(), this);
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setOnLoginListener(OnLoginListener login) {
        this.loginListener = login;
    }

    public void loginOut(Context context) {
        this.context = context.getApplicationContext();
        if (platform == null) {
            Message msg = new Message();
            msg.what = MSG_AUTH_ERROR;
            msg.obj = "未指定平台";
            handler.sendMessage(msg);
            return;
        }

        //初始化SDK
        ShareSDK.initSDK(context);
        Platform plat = ShareSDK.getPlatform(platform);
        if (plat == null) {
            Message msg = new Message();
            msg.what = MSG_AUTH_ERROR;
            msg.obj = "平台初始化失败";
            handler.sendMessage(msg);
            return;
        }

        if (plat.isAuthValid()) {
            plat.removeAccount(true);
        }
    }

    public void login(Context context) {
        this.context = context.getApplicationContext();
        if (platform == null) {
            Message msg = new Message();
            msg.what = MSG_AUTH_ERROR;
            msg.obj = "未指定平台";
            handler.sendMessage(msg);
            return;
        }

        //初始化SDK
        ShareSDK.initSDK(context);
        Platform plat = ShareSDK.getPlatform(platform);
        if (plat == null) {
            Message msg = new Message();
            msg.what = MSG_AUTH_ERROR;
            msg.obj = "平台初始化失败";
            handler.sendMessage(msg);
            return;
        }

        if (plat.isAuthValid()) {
            // 过期重新登录
            plat.removeAccount(true);
//            long time=(plat.getDb().getExpiresTime()/1000+plat.getDb().getExpiresIn())*1000;
//            if (time < System.currentTimeMillis()) {
//                plat.removeAccount(true);
//            } else {
//                Message msg = new Message();
//                msg.what = MSG_AUTH_COMPLETE;
//                msg.obj = plat;
//                handler.sendMessage(msg);
//                return;
//            }
        }

        //使用SSO授权，通过客户单授权
        plat.SSOSetting(false);
        plat.setPlatformActionListener(new PlatformActionListener() {
            public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
                if (action == Platform.ACTION_USER_INFOR) {
                    Message msg = new Message();
                    msg.what = MSG_AUTH_COMPLETE;
                    msg.arg2 = action;
                    msg.obj = plat;
                    handler.sendMessage(msg);
                }
            }

            public void onError(Platform plat, int action, Throwable t) {
                if (action == Platform.ACTION_USER_INFOR) {
                    Message msg = new Message();
                    msg.what = MSG_AUTH_ERROR;
                    msg.arg2 = action;
                    msg.obj = t;
                    handler.sendMessage(msg);
                }
            }

            public void onCancel(Platform plat, int action) {
                if (action == Platform.ACTION_USER_INFOR) {
                    Message msg = new Message();
                    msg.what = MSG_AUTH_CANCEL;
                    msg.arg2 = action;
                    msg.obj = plat;
                    handler.sendMessage(msg);
                }
            }
        });
        plat.showUser(null);
    }

    /**
     * 处理操作结果
     */
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_AUTH_CANCEL: {
                // 取消
                if (loginListener != null) {
                    loginListener.cancel();
                }
            }
            break;
            case MSG_AUTH_ERROR: {
                if (msg.obj instanceof String) {
                    if (loginListener != null) {
                        loginListener.error(msg.obj.toString(), null);
                    }
                } else {
                    Throwable t = (Throwable) msg.obj;
                    // 失败
                    if (loginListener != null) {
                        loginListener.error("", t);
                    }
                }

            }
            break;
            case MSG_AUTH_COMPLETE: {
                // 成功
                Platform pPlatform = (Platform) msg.obj;
                if (loginListener != null) {
                    loginListener.onLogin(pPlatform);
                }
            }
            break;
        }
        return false;
    }

}
