package tv.kuainiu.ui.me.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.UserHttpRequest;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.ThirdBind;
import tv.kuainiu.modle.ThirdBindContent;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.ui.me.thridLogin.LoginApi;
import tv.kuainiu.ui.me.thridLogin.OnLoginListener;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;

import static tv.kuainiu.modle.cons.Constant.Q_Q;
import static tv.kuainiu.modle.cons.Constant.SUCCEED;
import static tv.kuainiu.modle.cons.Constant.WB;
import static tv.kuainiu.modle.cons.Constant.WeChat;


/**
 * 账号绑定
 */
public class BindAccountActivity extends AppCompatActivity {


    @BindView(R.id.ivSina)
    ImageView ivSina;
    @BindView(R.id.iv_sina_right)
    ImageView ivSinaRight;
    @BindView(R.id.tvBindSina)
    TextView tvBindSina;
    @BindView(R.id.ivWechat)
    ImageView ivWechat;
    @BindView(R.id.iv_wechat_right)
    ImageView ivWechatRight;
    @BindView(R.id.tvBindWechat)
    TextView tvBindWechat;
    @BindView(R.id.ivQq)
    ImageView ivQq;
    @BindView(R.id.iv_qq_right)
    ImageView ivQqRight;
    @BindView(R.id.tvBindQq)
    TextView tvBindQq;
    private Platform mPlatform;
    private Snackbar snackbar;
    private boolean sinaIsBind;
    private boolean qqIsBind;
    private boolean wechatIsBind;
    private boolean isClik = false;
    private String type = "";

    ThirdBind mThirdBind;
    ThirdBindContent wbThirdBindContent;
    ThirdBindContent wxThirdBindContent;
    ThirdBindContent qqThirdBindContent;
    User user;

    public static void intoNewActivity(Context context) {
        Intent intent = new Intent(context, BindAccountActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_account);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (!MyApplication.isLogin()) {
            finish();
        }
        user = MyApplication.getUser();
        mThirdBind = user.getThird_bind();
        wbThirdBindContent = mThirdBind.getWb();
        wxThirdBindContent = mThirdBind.getWx();
        qqThirdBindContent = mThirdBind.getQq();
        initView();
    }

    private void initView() {
        sinaIsBind = wbThirdBindContent.getBind() != 0;
        qqIsBind = qqThirdBindContent.getBind() != 0;
        wechatIsBind = wxThirdBindContent.getBind() != 0;
        snackbar = Snackbar.make(tvBindQq, "正在绑定......", Snackbar.LENGTH_LONG);
        ivSina.setSelected(sinaIsBind);
        ivQq.setSelected(qqIsBind);
        ivWechat.setSelected(wechatIsBind);
        tvBindSina.setText(sinaIsBind ? wbThirdBindContent.getName() : "去绑定");
        tvBindQq.setText(qqIsBind ? qqThirdBindContent.getName() : "去绑定");
        tvBindWechat.setText(wechatIsBind ? wxThirdBindContent.getName() : "去绑定");
    }

    @OnClick({R.id.rl_sina, R.id.rl_wechat, R.id.rl_qq})
    public void onClick(View view) {
        if (isClik) {
            return;
        }

        switch (view.getId()) {
            case R.id.rl_sina:
                //新浪微博
                isClik = true;
                type = WB;
                if (!sinaIsBind) {
                    bindAccount(SinaWeibo.NAME);
                } else {
                    unBindAccount("微博");
                }
                break;
            case R.id.rl_wechat:
                type = WeChat;
                if (!wechatIsBind) {
                    bindAccount(Wechat.NAME);
                } else {
                    unBindAccount("微信");
                }
                break;
            case R.id.rl_qq:
                isClik = true;
                type = Q_Q;
                if (!qqIsBind) {
                    bindAccount(QQ.NAME);
                } else {
                    unBindAccount("QQ");
                }
                break;
        }
    }

    private void unBindAccount(final String platform_name) {
        snackbar = Snackbar.make(tvBindQq, "正在解绑......", Snackbar.LENGTH_LONG);
        snackbar.show();
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this)
                .setTitle(this.getString(R.string.prompt))
                .setMessage("是否确定解绑" + platform_name)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("解绑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //解绑
                        thirdUnbindAccount(type);
                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isClik = false;
                        snackbar.dismiss();
                    }
                });

        mBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isClik = false;
            }
        });
        mBuilder.create().show();
    }

    private void thirdUnbindAccount(String platform_name) {
        UserHttpRequest.thirdUnBindAccount(BindAccountActivity.this, platform_name, Action.third_unbind);
    }

    /*
   * 执行第三方登录/注册的方法
   * <p>
   * @param platformName 执行登录/注册的平台名称，如：SinaWeibo.NAME
   */
    private void bindAccount(String platformName) {
        snackbar = Snackbar.make(tvBindQq, "正在绑定......", Snackbar.LENGTH_LONG);
        snackbar.show();
        LoginApi api = new LoginApi();
        //设置登陆的平台后执行登陆的方法
        api.setPlatform(platformName);
        api.setOnLoginListener(new OnLoginListener() {
            public void onLogin(Platform mPlatform) {
                BindAccountActivity.this.mPlatform = mPlatform;
                String platform_id = mPlatform.getDb().getUserId();
                String platform_token = mPlatform.getDb().getToken();
                String platform_nickname = mPlatform.getDb().getUserName();
                String platform_avatar = mPlatform.getDb().getUserIcon();
                long platform_expires_in = mPlatform.getDb().getExpiresTime() / 1000 + mPlatform.getDb().getExpiresIn();
                UserHttpRequest.thirdLoginCheck(BindAccountActivity.this, type, platform_token, platform_id, platform_nickname, platform_avatar, platform_expires_in, Action.third_bind);
            }

            @Override
            public void error(String errorMsg, Throwable error) {
                snackbar.dismiss();
                isClik = false;
                ToastUtils.showToast(BindAccountActivity.this, error == null ? errorMsg : error.getMessage());
            }

            @Override
            public void cancel() {
                snackbar.dismiss();
                isClik = false;
                ToastUtils.showToast(BindAccountActivity.this, "取消绑定");
            }
        });
        api.login(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case third_bind:
                isClik = false;
                snackbar.dismiss();
                if (SUCCEED == event.getCode()) {
//                    User user = new Gson().fromJson(event.getData().optString("data"), User.class);
//                    MyApplication.setUser(user);
                    ToastUtils.showToast(this, "绑定成功");
                    databind(true);
                } else if (-201 == event.getCode()) {
                    ThridAccountVerifyActivity.intoNewActivity(this, mPlatform);
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "第三方绑定失败"));
                }
                break;
            case third_unbind:
                isClik = false;
                snackbar.dismiss();
                if (SUCCEED == event.getCode()) {
                    databind(false);
                    ToastUtils.showToast(this, "解绑成功");
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "第三方解绑失败"));
                }
                break;
        }
    }

    private void databind(boolean isBind) {

        String platformName = "";

        if (WB.equals(type)) {
            wbThirdBindContent.setBind(isBind ? 1 : 0);
            wbThirdBindContent.setName(mPlatform == null ? "" : mPlatform.getDb().getUserName());
            platformName = SinaWeibo.NAME;
        } else if (Q_Q.equals(type)) {
            qqThirdBindContent.setBind(isBind ? 1 : 0);
            qqThirdBindContent.setName(mPlatform == null ? "" : mPlatform.getDb().getUserName());
            platformName = QQ.NAME;
        } else if (WeChat.equals(type)) {
            wxThirdBindContent.setBind(isBind ? 1 : 0);
            wxThirdBindContent.setName(mPlatform == null ? "" : mPlatform.getDb().getUserName());
            platformName = Wechat.NAME;
        }
        if (!TextUtils.isEmpty(type)) {
            mThirdBind.setQq(qqThirdBindContent);
            mThirdBind.setWb(wbThirdBindContent);
            mThirdBind.setWx(wxThirdBindContent);
            user.setThird_bind(mThirdBind);

            mPlatform = null;
            initView();
            if (!isBind) {
                LoginApi api = new LoginApi();
                //设置登陆的平台后执行登陆的方法
                api.setPlatform(platformName);
                api.loginOut(this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.setUser(user);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
