package tv.kuainiu.ui.me.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

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
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.me.thridLogin.LoginApi;
import tv.kuainiu.ui.me.thridLogin.OnLoginListener;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;


/**
 * 账号绑定
 */
public class BindAccountActivity extends BaseActivity {


    public static final String SINA_IS_BIND = "sinaIsBind";
    public static final String QQ_IS_BIND = "qqIsBind";
    public static final String WECHAT_IS_BIND = "wechatIsBind";
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

    public static void intoNewActivity(Context context, boolean sinaIsBind, boolean qqIsBind, boolean wechatIsBind) {
        Intent intent = new Intent(context, BindAccountActivity.class);
        intent.putExtra(SINA_IS_BIND, sinaIsBind);
        intent.putExtra(QQ_IS_BIND, qqIsBind);
        intent.putExtra(WECHAT_IS_BIND, wechatIsBind);
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
        sinaIsBind = getIntent().getBooleanExtra(SINA_IS_BIND, false);
        qqIsBind = getIntent().getBooleanExtra(QQ_IS_BIND, false);
        wechatIsBind = getIntent().getBooleanExtra(WECHAT_IS_BIND, false);
        snackbar = Snackbar.make(tvBindQq, "正在绑定......", Snackbar.LENGTH_LONG);
        initView();
    }

    private void initView() {
        ivSina.setSelected(sinaIsBind);
        ivQq.setSelected(qqIsBind);
        ivWechat.setSelected(wechatIsBind);
        tvBindSina.setText(sinaIsBind ? "去解绑" : "去绑定");
        tvBindQq.setText(qqIsBind ? "去解绑" : "去绑定");
        tvBindWechat.setText(wechatIsBind ? "去解绑" : "去绑定");
    }

    @OnClick({R.id.rl_sina, R.id.rl_wechat, R.id.rl_qq})
    public void onClick(View view) {
        if (isClik) {
            return;
        }
        isClik = true;
        switch (view.getId()) {
            case R.id.rl_sina:
                //新浪微博
                if (!sinaIsBind) {
                    bindAccount(SinaWeibo.NAME);
                } else {
                    unBindAccount(SinaWeibo.NAME, "sina");
                }
                break;
            case R.id.rl_wechat:
                if (!wechatIsBind) {
                    bindAccount(Wechat.NAME);
                } else {
                    unBindAccount(Wechat.NAME, "Wechat");
                }
                break;
            case R.id.rl_qq:
                if (!qqIsBind) {
                    bindAccount(QQ.NAME);
                } else {
                    unBindAccount(QQ.NAME, "qq");
                }
                break;
        }
    }

    private void unBindAccount(final String platform_name, final String platform_id) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this)
                .setTitle(this.getString(R.string.prompt))
                .setMessage("是否确定解绑" + platform_name)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isClik = false;
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("解绑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO 解绑
                        ToastUtils.showToast(BindAccountActivity.this, "解绑平台" + platform_id);
                        isClik = false;

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

    /*
   * 执行第三方登录/注册的方法
   * <p>
   * @param platformName 执行登录/注册的平台名称，如：SinaWeibo.NAME
   */
    private void bindAccount(String platformName) {
        snackbar.show();
        LoginApi api = new LoginApi();
        //设置登陆的平台后执行登陆的方法
        api.setPlatform(platformName);
        api.setOnLoginListener(new OnLoginListener() {
            public void onLogin(String platform_name, HashMap<String, Object> res, Platform mPlatform) {
                BindAccountActivity.this.mPlatform = mPlatform;
                String type = "";
                if (SinaWeibo.NAME.equals(platform_name)) {
                    type = "wb";
                }
                if (QQ.NAME.equals(platform_name)) {
                    type = "qq";
                }
                if (Wechat.NAME.equals(platform_name)) {
                    type = "wx";
                }
                String platform_id = mPlatform.getDb().getUserId();
                String platform_token = mPlatform.getDb().getToken();
                String platform_nickname = mPlatform.getDb().getUserName();
                String platform_avatar = mPlatform.getDb().getUserIcon();
                long platform_expires_in = mPlatform.getDb().getExpiresTime();
                UserHttpRequest.thirdLoginCheck(BindAccountActivity.this, type, platform_token, platform_id, platform_nickname, platform_avatar, platform_expires_in, Action.third_bind);
            }

            @Override
            public void error(Throwable error) {
                snackbar.dismiss();
                isClik = false;
                ToastUtils.showToast(BindAccountActivity.this, error.getMessage());
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
                if (Constant.SUCCEED == event.getCode()) {
                    User user = new Gson().fromJson(event.getData().optString("data"), User.class);
                    MyApplication.setUser(user);
                    ToastUtils.showToast(this, "绑定成功");
                    if (SinaWeibo.NAME.equals(mPlatform.getName())) {
                        sinaIsBind = true;
                    }
                    if (QQ.NAME.equals(mPlatform.getName())) {
                        qqIsBind = true;
                    }
                    if (Wechat.NAME.equals(mPlatform.getName())) {
                        wechatIsBind = true;
                    }
                    initView();
                } else if (-201 == event.getCode()) {
                    ThridAccountVerifyActivity.intoNewActivity(this, mPlatform);
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "第三方绑定失败"));
                }
                break;
        }
    }

}
