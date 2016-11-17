package tv.kuainiu.ui.me.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import tv.kuainiu.R;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.me.thridLogin.LoginApi;
import tv.kuainiu.ui.me.thridLogin.OnLoginListener;
import tv.kuainiu.utils.LogUtils;


/**
 * 账号绑定
 */
public class BindAccountActivity extends BaseActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_account);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @OnClick({R.id.rl_sina, R.id.rl_wechat, R.id.rl_qq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_sina:
                //新浪微博
                login(SinaWeibo.NAME);
                break;
            case R.id.rl_wechat:
                login(Wechat.NAME);
                break;
            case R.id.rl_qq:
                login(QQ.NAME);
                break;
        }
    }

    /*
   * 执行第三方登录/注册的方法
   * <p>
   * @param platformName 执行登录/注册的平台名称，如：SinaWeibo.NAME
   */
    private void login(String platformName) {
        LoginApi api = new LoginApi();
        //设置登陆的平台后执行登陆的方法
        api.setPlatform(platformName);
        api.setOnLoginListener(new OnLoginListener() {
            public boolean onLogin(String platform, HashMap<String, Object> res, Platform mPlatform) {
                // TODO 去服务器绑定
                LogUtils.e(TAG, "platformID=" + mPlatform.getDb().getUserId());
                Iterator iter = res.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    Object val = entry.getValue();

                    LogUtils.e(TAG, key + "=" + val.toString());
                }
                return true;
            }

        });
        api.login(this);
    }
}
