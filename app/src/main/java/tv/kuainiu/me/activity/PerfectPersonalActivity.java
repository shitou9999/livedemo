package tv.kuainiu.me.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.widget.curved.WheelDatePicker;

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
import tv.kuainiu.util.DateUtil;
import tv.kuainiu.util.DebugUtils;
import tv.kuainiu.util.NetUtils;
import tv.kuainiu.widget.dialog.MainDialog;


/**
 * 完善资料
 */
public class PerfectPersonalActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.ll_select_gender) LinearLayout mLlSelectGender;
    @BindView(R.id.ll_date_of_birth) LinearLayout mLlBirth;
    @BindView(R.id.ll_address) LinearLayout mLlAddress;
    @BindView(R.id.et_nickname) EditText mEtNickname;
    @BindView(R.id.et_qq) EditText mEtQQ;
    @BindView(R.id.tv_gender) TextView mTvGender;
    @BindView(R.id.tv_addres) TextView mTvAddress;
    @BindView(R.id.tv_date_of_birth) TextView mTvBirth;
    @BindView(R.id.btn_submit) Button mBtnSubmit;

    private AlertDialog genderDialog;
    private MainDialog birthDialog;
    private String nickName = "";
    private String qq = "";
    private String gender = "";
    private String address = "";
    private String birth = "";
    private String prov = "";
    private String city = "";
    private int padding;
    private int textSize;
    private int itemSpace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_personal);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        initDialogs();
        initDataAndBindView();
        initListener();

        padding = getResources().getDimensionPixelSize(R.dimen.WheelPadding);
        textSize = getResources().getDimensionPixelSize(R.dimen.TextSizeLarge);
        itemSpace = getResources().getDimensionPixelSize(R.dimen.item_space_small);
    }


    private void initDialogs() {
        birthDialog = new MainDialog(this);
        genderDialog = new AlertDialog.Builder(this)
                .setTitle("选择性别")
                .setCancelable(true)
                .setNegativeButton("男", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTvGender.setText("男");
                        genderDialog.dismiss();
                    }
                })
                .setPositiveButton("女", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTvGender.setText("女");
                        genderDialog.dismiss();
                    }
                })
                .create();

    }

    private void initDataAndBindView() {
        try {
            User user = IGXApplication.getUser();
            bindToInitialValue(mEtNickname, "未填写", user.getNickname());
            bindToInitialValue(mEtQQ, "未填写", user.getQq());
            bindToInitialValue(mTvAddress, "未填写", user.getProvince() + "-" + user.getCity());
            bindToInitialValue(mTvGender, "未填写", user.getGender());
            boolean isNullToBirth = TextUtils.isEmpty(user.getBirthday()) || "0000-00-00".equals(user.getBirthday());
            bindToInitialValue(isNullToBirth, mTvBirth, "0000-00-00", user.getBirthday());
        } catch (NullPointerException e) {
            finish();
        }

    }

    private void bindToInitialValue(TextView textView, String hint, String value) {
        bindToInitialValue(TextUtils.isEmpty(value), textView, hint, value);
    }

    private void bindToInitialValue(boolean bool, TextView textView, String hint, String value) {
        if (bool) {
            textView.setHint(hint);
        } else {
            textView.setText(value);
        }
    }

    protected void initListener() {
        mLlSelectGender.setOnClickListener(this);
        mLlBirth.setOnClickListener(this);
        mLlAddress.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        nickName = mEtNickname.getText().toString().trim();
        qq = mEtQQ.getText().toString().trim();
        gender = mTvGender.getText().toString().trim();
        address = mTvAddress.getText().toString().trim();

        if (!TextUtils.isEmpty(address)) {
            String arr[] = address.split("-");
            if (arr.length == 3) {
                prov = arr[0];
                city = arr[1].concat("-").concat(arr[2]);
            } else {
                // TODO: 2016/7/11 This address error 
                // mTvAddress.setText("地址选择出错，请点击重新选择");
            }
        }
        birth = mTvBirth.getText().toString().trim();
        switch (v.getId()) {
            case R.id.ll_select_gender:
                genderDialog.show();
                break;
            case R.id.ll_date_of_birth:
                final WheelDatePicker wheelDatePicker = new WheelDatePicker(this);
                wheelDatePicker.setPadding(padding, 0, padding, 0);
                wheelDatePicker.setTextColor(0xFF757575);
                wheelDatePicker.setCurrentTextColor(0xFF212121);
                wheelDatePicker.setLabelColor(0xFF212121);
                wheelDatePicker.setTextSize(textSize);
                wheelDatePicker.setItemSpace(itemSpace);

                String birthday = IGXApplication.getUser().getBirthday();
                if (TextUtils.isEmpty(birthday) || "0000-00-00".equals(birthday)) {
                    wheelDatePicker.setCurrentDate(1990, 1, 1);
                } else {
                    String[] dates = birthday.split("-");
                    try {
                        int year = Integer.parseInt(dates[0]);
                        int month = Integer.parseInt(dates[1]);
                        int day = Integer.parseInt(dates[2]);
                        wheelDatePicker.setCurrentDate(year, month, day);
                    } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                        wheelDatePicker.setCurrentDate(1990, 1, 1);
                    }
                }
                birthDialog.setContentView(wheelDatePicker);
                birthDialog.setOnButtonClickListener(new MainDialog.OnButtonClickListener() {
                    @Override public void onButtonClick(View v) {
                        birth = birthDialog.getData();
                        mTvBirth.setText(birth);
                        birthDialog.dismiss();

                    }
                });
                birthDialog.show();
                break;
            case R.id.ll_address:
                ChooseRegionActivity.intoNewIntent(this, null);
                break;
            case R.id.btn_submit:
                if (!NetUtils.isOnline(this)) {
                    DebugUtils.showToast(this, R.string.toast_not_network);
                    return;
                }
                if (!isModification()) {
                    Snackbar.make(mBtnSubmit, "您没作任何修改", Snackbar.LENGTH_LONG).show();
                    return;
                }
                OKHttpUtils.getInstance().post(PerfectPersonalActivity.this, Api.UPDATE_USERINFO, prepareParam(), Action.update_userinfo);
                break;
            default:
                break;
        }
    }


    // 判断用户是否作出修改
    private boolean isModification() {
        User user = IGXApplication.getUser();
        String address = user.getProvince() + "-" + user.getCity();
        try {
            if (!user.getNickname().equals(nickName) || !user.getGender().equals(gender) || !user.getQq().equals(qq)
                    || !user.getBirthday().equals(birth) || !address.equals(this.address)) {
                return true;
            }
        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }

    private String prepareParam() {
        Map<String, String> map = new HashMap<>();
        map.put("nickname", nickName);
        map.put("gender", gender);
        map.put("qq", qq);
        map.put("birthday", DateUtil.toStringForTimestamp(birth)); // 生日转换成时间戳
        map.put("province", prov);
        map.put("city", city);
        return ParamUtil.getParam(map);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()){
            case choose_city:
                mTvAddress.setText(event.getMsg());
                break;
            case update_userinfo:
                if (event.getCode() == Constant.SUCCEED) {
                    DebugUtils.showToastResponse(PerfectPersonalActivity.this, "修改成功");
                    User user = IGXApplication.getUser();
                    user.setNickname(nickName);
                    user.setGender(gender);
                    user.setQq(qq);
                    user.setBirthday(birth);
                    user.setProvince(prov);
                    user.setCity(city);
                    IGXApplication.setUser(user);
                    finish();
                } else {
                    DebugUtils.showToastResponse(PerfectPersonalActivity.this, event.getMsg());
                }
                break;
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
