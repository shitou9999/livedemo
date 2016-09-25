package tv.kuainiu.ui.me.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.NetUtils;
import tv.kuainiu.utils.SecurityUtils;


/**
 * 修改密码
 */
public class AlterPasswordActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.et_old_password) EditText mEtOldPassword;
    @BindView(R.id.et_new_password) EditText mEtNewPassword;
    @BindView(R.id.et_affirm_password) EditText mEtAffirmPassword;
    @BindView(R.id.btn_submit) Button mBtnSubmit;

    private String oldPassword;
    private String newPassword;
    private String affirmPassword;

    private final Handler mHideHandler = new Handler();

    /**
     * 延迟响应
     */
    private Runnable mHidePartRunnable = new Runnable() {
        @Override public void run() {
            finish();
//            mBtnSubmit.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_password);
        ButterKnife.bind(this);
        initListener();
    }


    protected void initListener() {
        mBtnSubmit.setOnClickListener(this);
    }

    // account/update_pwd
    @Override public void onClick(View v) {
        oldPassword = mEtOldPassword.getText().toString().trim();
        newPassword = mEtNewPassword.getText().toString().trim();
        affirmPassword = mEtAffirmPassword.getText().toString().trim();
        switch (v.getId()) {
            case R.id.btn_submit:
                if (TextUtils.isEmpty(oldPassword)) {
                    DebugUtils.showToast(AlterPasswordActivity.this, "原密码不能为空");
                    return;
                } else if (TextUtils.isEmpty(newPassword)) {
                    DebugUtils.showToast(AlterPasswordActivity.this, "密码不能为空");
                    return;
                } else if (TextUtils.isEmpty(affirmPassword)) {
                    DebugUtils.showToast(AlterPasswordActivity.this, "请确认密码");
                    return;
                } else if (!newPassword.equals(affirmPassword)) {
                    DebugUtils.showToast(AlterPasswordActivity.this, "两次输入的密码不一致");
                } else if (!NetUtils.isOnline(AlterPasswordActivity.this)) {
                    DebugUtils.showToast(AlterPasswordActivity.this, R.string.toast_not_network);
                    return;
                } else {
                    mBtnSubmit.setEnabled(false);
                    OKHttpUtils.getInstance().post(AlterPasswordActivity.this, Api.UPDATE_PASSWORD, preareParam(), Action.update_password);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 准备请求参数
     *
     * @return
     */
    private String preareParam() {
        Map<String, String> map = new HashMap<>();
        map.put("ori_pwd", SecurityUtils.salt(oldPassword));
        map.put("new_pwd", SecurityUtils.salt(newPassword));
        return ParamUtil.getParam(map);
    }

    /**
     * 修改密码网络接口回调
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        if (Action.update_password == event.getAction()) {
            if (event.getCode() == Constant.SUCCEED) {
                DebugUtils.showToast(AlterPasswordActivity.this, "修改成功");
                mHideHandler.removeCallbacks(mHidePartRunnable);
                mHideHandler.postDelayed(mHidePartRunnable, 1500);
            } else {
                mBtnSubmit.setEnabled(true);
                DebugUtils.showToastResponse(AlterPasswordActivity.this, event.getMsg());
            }
        }
    }
}
