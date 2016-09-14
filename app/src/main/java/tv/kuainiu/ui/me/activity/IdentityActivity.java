package tv.kuainiu.ui.me.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.IGXApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.util.DebugUtils;
import tv.kuainiu.util.MatcherUtils;
import tv.kuainiu.util.NetUtils;


/**
 * 身份认证
 */
public class IdentityActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.et_real_name) EditText mEtRealName;
    @BindView(R.id.et_identity_card) EditText mEtIdentityCard;
    @BindView(R.id.btn_submit) Button mBtnSubmit;

    private String realName;
    private String identityCard;
    private String oldIdentityCard;

    private boolean isButtonEnable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initListener();
//        setMyTitle("身份认证");
        bindDataForView();
    }

    private void bindDataForView() {
        User user = IGXApplication.getInstance().getUser();
        if (user != null) {
            mEtRealName.setText(user.getRealname());
            mEtIdentityCard.setText(user.getIdno());
            oldIdentityCard = user.getIdno();
        }
    }

    protected void initListener() {
        mBtnSubmit.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        realName = mEtRealName.getText().toString().trim();
        identityCard = mEtIdentityCard.getText().toString().trim();
        switch (v.getId()) {
            case R.id.btn_submit:
                if (!isButtonEnable) {
                    return;
                }
                if (TextUtils.isEmpty(realName)) {
                    DebugUtils.showToast(IdentityActivity.this, "姓名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(identityCard)) {
                    DebugUtils.showToast(IdentityActivity.this, "身份证不能为空");
                    return;
                }
                if (!MatcherUtils.is18ByteIdCardComplex(identityCard)) {
                    DebugUtils.showToast(IdentityActivity.this, "无效的身份证格式");
                    return;
                }
                if (!TextUtils.isEmpty(oldIdentityCard) && oldIdentityCard.equals(identityCard)) {
                    DebugUtils.showToast(IdentityActivity.this, "该号码已认证");
                    return;
                }
                if (!NetUtils.isOnline(IdentityActivity.this)) {
                    DebugUtils.showToast(IdentityActivity.this, R.string.toast_not_network);
                    return;
                }
                isButtonEnable = false;
                OKHttpUtils.getInstance().post(IdentityActivity.this, Api.UPDATE_USERINFO, prepareParam(), Action.iden);
                break;
            default:
                break;
        }
    }

    private String prepareParam() {
        Map<String, String> map = new HashMap<>();
        map.put("realname", realName);
        map.put("idno", identityCard);
        return ParamUtil.getParam(map);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        if (Action.iden == event.getAction()) {
            isButtonEnable = true;
            if (event.getCode() == Constant.SUCCEED) {
                DebugUtils.showToast(IdentityActivity.this, "身份认证成功");
                IGXApplication.getInstance().getUser().setIdno(identityCard);
                finish();
            } else {
                DebugUtils.showToastResponse(IdentityActivity.this, event.getMsg());
            }
        }
    }
    @Override protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
