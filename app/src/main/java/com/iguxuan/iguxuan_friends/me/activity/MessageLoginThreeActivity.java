package com.iguxuan.iguxuan_friends.me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.iguxuan.iguxuan_friends.IGXApplication;
import com.iguxuan.iguxuan_friends.MainActivity;
import com.iguxuan.iguxuan_friends.R;
import com.iguxuan.iguxuan_friends.command.http.Api;
import com.iguxuan.iguxuan_friends.command.http.core.OKHttpUtils;
import com.iguxuan.iguxuan_friends.command.http.core.ParamUtil;
import com.iguxuan.iguxuan_friends.event.HttpEvent;
import com.iguxuan.iguxuan_friends.modle.User;
import com.iguxuan.iguxuan_friends.modle.cons.Action;
import com.iguxuan.iguxuan_friends.modle.cons.Constant;
import com.iguxuan.iguxuan_friends.ui.activity.BaseActivity;
import com.iguxuan.iguxuan_friends.util.DebugUtils;
import com.iguxuan.iguxuan_friends.util.NetUtils;
import com.iguxuan.iguxuan_friends.util.PreferencesUtils;
import com.iguxuan.iguxuan_friends.util.SecurityUtils;
import com.iguxuan.iguxuan_friends.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 设置验证码
 */
public class MessageLoginThreeActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.et_new_password) EditText mEtNewPassword;
    @BindView(R.id.et_affirm_password) EditText mEtAffirmPassword;
    @BindView(R.id.btn_submit) Button mBtnSubmit;

    private String mRegionCode;
    private String mPhoneNumber;
    private String mCheckCode;
    private String newPassword;
    private String affirmPassword;


    private void initData(Intent intent) {
        if (intent != null) {
            mRegionCode = intent.getStringExtra("area");
            mPhoneNumber = intent.getStringExtra("phone");
            mCheckCode = intent.getStringExtra("check_code");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_login_three);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initListener();
    }

    protected void initListener() {
        mBtnSubmit.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        newPassword = mEtNewPassword.getText().toString().trim();
        affirmPassword = mEtAffirmPassword.getText().toString().trim();
        switch (v.getId()) {
            case R.id.btn_submit:
                if (TextUtils.isEmpty(newPassword)) {
                    DebugUtils.showToast(MessageLoginThreeActivity.this, "密码不能为空");
                    return;
                } else if (TextUtils.isEmpty(affirmPassword)) {
                    DebugUtils.showToast(MessageLoginThreeActivity.this, "请确认密码");
                    return;
                } else if (!newPassword.equals(affirmPassword)) {
                    DebugUtils.showToast(MessageLoginThreeActivity.this, "两次输入的密码不一致");
                } else if (!NetUtils.isOnline(MessageLoginThreeActivity.this)) {
                    DebugUtils.showToast(MessageLoginThreeActivity.this, R.string.toast_not_network);
                    return;
                } else {
                    mBtnSubmit.setEnabled(false);
                    OKHttpUtils.getInstance().post(MessageLoginThreeActivity.this, Api.TEST_DNS_API_HOST_V21, Api.SMS_LOGING_REG, preareParam(), Action.sms_login_reg);
                }
        }
    }

    /**
     * 准备请求参数
     *
     * @return
     */

//    phone，area，code，password，rpassword
    private String preareParam() {
        Map<String, String> map = new HashMap<>();
        map.put("phone", mPhoneNumber);
        map.put("area", mRegionCode);
        map.put("code", mCheckCode);
        map.put("password", SecurityUtils.salt(newPassword));
        map.put("rpassword", SecurityUtils.salt(affirmPassword));
        return ParamUtil.getParam(map);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(HttpEvent event) {
        if (Action.sms_login_reg == event.getAction()) {
            if (Constant.SUCCEED == event.getCode()) {
                try {
                    String tempString = event.getData().optString("data");
                    JSONObject object = new JSONObject(tempString);
                    User user = new User();
                    if (object.has("user_id")) {
                        user.setUser_id(object.optString("user_id"));
                    }
                    if (object.has("phone")) {
                        user.setPhone(object.optString("phone"));
                    }
                    if (object.has("area")) {
                        user.setArea(object.optString("area"));
                    }

                    PreferencesUtils.putString(this, Constant.KEY_PHONE, SecurityUtils.DESUtil.en(Api.PUBLIC_KEY, user.getPhone()));
                    PreferencesUtils.putString(this, Constant.KEY_AREA, SecurityUtils.DESUtil.en(Api.PUBLIC_KEY, user.getArea()));
//                    user.setNickname("");
                    IGXApplication.setUser(user);

                    ToastUtils.showToast(this, "注册成功");
                    startActivity(new Intent(this, MainActivity.class));

                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                DebugUtils.showToastResponse(this, event.getMsg());
            }
        }
    }
    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
