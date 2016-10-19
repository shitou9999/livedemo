package tv.kuainiu.ui.me.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import butterknife.BindView;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.ui.BaseActivity;
import tv.kuainiu.umeng.UMEventManager;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.MeasureUtil;
import tv.kuainiu.utils.NetUtils;
import tv.kuainiu.utils.StringUtils;

import static tv.kuainiu.modle.cons.Constant.SUCCEED;


/**
 * 问题反馈
 */
public class FeedbackActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.et_contact)
    EditText mEtContact;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;

    @BindView(R.id.rl_feedback1)
    RelativeLayout mRlFeedback1;
    @BindView(R.id.rl_feedback2)
    RelativeLayout mRlFeedback2;
    @BindView(R.id.rl_feedback3)
    RelativeLayout mRlFeedback3;
    @BindView(R.id.rl_feedback4)
    RelativeLayout mRlFeedback4;
    @BindView(R.id.rl_feedback5)
    RelativeLayout mRlFeedback5;
    @BindView(R.id.rl_feedback6)
    RelativeLayout mRlFeedback6;

    @BindView(R.id.check_feedback1)
    AppCompatCheckBox mCheck1;
    @BindView(R.id.check_feedback2)
    AppCompatCheckBox mCheck2;
    @BindView(R.id.check_feedback3)
    AppCompatCheckBox mCheck3;
    @BindView(R.id.check_feedback4)
    AppCompatCheckBox mCheck4;
    @BindView(R.id.check_feedback5)
    AppCompatCheckBox mCheck5;
    @BindView(R.id.check_feedback6)
    AppCompatCheckBox mCheck6;

    private LinkedList<String> mFeedbackTypeList = new LinkedList<>();
    private String content = "";
    private String contact = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.feedback);
        setTouchHideInput(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFeedbackTypeList.clear();
    }


    @Override
    protected void initContentView(int layoutId) {
        super.initContentView(R.layout.activity_feedback);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mRlFeedback1.setOnClickListener(this);
        mRlFeedback2.setOnClickListener(this);
        mRlFeedback3.setOnClickListener(this);
        mRlFeedback4.setOnClickListener(this);
        mRlFeedback5.setOnClickListener(this);
        mRlFeedback6.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
    }


    private void select(AppCompatCheckBox view) {
        if (view.isChecked() && mFeedbackTypeList.size() > 0) {
            mFeedbackTypeList.remove(mFeedbackTypeList.indexOf(view.getTag() + ","));
        } else {
            mFeedbackTypeList.add(view.getTag() + ",");
        }
        view.setChecked(!view.isChecked());
    }


    /**
     * 准备请求参数
     * </p>
     * type       反馈类型  1首页加载失败 2不能播放视频 3无法缓存下载 4闪退 5评论问题 6其他.<strong>多个以","隔开</strong>
     * content    反馈内容
     * contact    联系方式
     * model      手机型号
     * system_version  操作系统版本
     * app_version     APP版本
     */
    private String prepareParam() {
        if (mFeedbackTypeList == null || mFeedbackTypeList.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String o : mFeedbackTypeList) {
            sb.append(o);
        }
        sb.deleteCharAt(sb.length() - 1);
        DebugUtils.dd("feedback types : " + sb.toString());

        Map<String, String> map = new HashMap<>();
        map.put("type", sb.toString());
        map.put("content", content);
        map.put("contact", contact);
        map.put("model", Build.MANUFACTURER.concat("_").concat(Build.MODEL)); // Build.MANUFACTURER 1.5以后才支持
        map.put("system_version", Build.VERSION.RELEASE);
        map.put("app_version", OKHttpUtils.Utils.getAppVersionName(this));

        String sizeInfo = MeasureUtil.getScreenSize(this)[1] + " * " + MeasureUtil.getScreenSize(this)[0];
        map.put("other_data", "screen : " + sizeInfo);
        return ParamUtil.getParam(map);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddFeedback(HttpEvent event) {
        if (Action.add_feedback == event.getAction()) {
            mBtnSubmit.setEnabled(true);
            if (SUCCEED == event.getCode()) {
                DebugUtils.showToast(this, "提交成功,感谢您的反馈!");
                // 添加自定义事件
                UMEventManager.onEvent(this, UMEventManager.ID_FEEDBACK);
                finish();
            } else {
                DebugUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "提交失败,请重试!"));
            }
        }

    }


    @Override
    public void onClick(View v) {
        contact = mEtContact.getText().toString().trim();
        content = mEtContent.getText().toString().trim();
        switch (v.getId()) {
            case R.id.rl_feedback1:
                select(mCheck1);
                break;
            case R.id.rl_feedback2:
                select(mCheck2);
                break;
            case R.id.rl_feedback3:
                select(mCheck3);
                break;
            case R.id.rl_feedback4:
                select(mCheck4);
                break;
            case R.id.rl_feedback5:
                select(mCheck5);
                break;
            case R.id.rl_feedback6:
                select(mCheck6);
                break;
            case R.id.btn_submit:
                if (mFeedbackTypeList == null || mFeedbackTypeList.size() < 1) {
                    DebugUtils.showToast(this, "请至少选择一个类型");
                    return;
                } else if (!NetUtils.isOnline(this)) {
                    DebugUtils.showToast(this, R.string.toast_not_network);
                    return;
                } else {
//                    prepareParam();
                    OKHttpUtils.getInstance().post(this, Api.ADD_FEEDBACK, prepareParam(), Action.add_feedback);
                    mBtnSubmit.setEnabled(false);
                }
                break;
            default:
                break;
        }
    }


}
