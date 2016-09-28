package tv.kuainiu.ui.me.activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonParseException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.Company;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.NetUtils;
import tv.kuainiu.utils.StringUtils;

import static tv.kuainiu.modle.cons.Constant.SUCCEED;


/**
 * 关于
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {
    //    private final int CALL_PHONE_REQUEST_CODE = 0x1;
    @BindView(R.id.tv_version) TextView mTvVersion;
    @BindView(R.id.tv_content) TextView mTvContent;
    @BindView(R.id.tv_wechat) TextView mTvWeChat;
    @BindView(R.id.tv_hotline) TextView mTvHotLine;
    @BindView(R.id.tv_website) TextView mTvWebsite;
    @BindView(R.id.ll_hotline) LinearLayout mLlHotLine;
    @BindView(R.id.ll_website) LinearLayout mLlWebsite;


//    private static final int REQUEST_CODE = 0; // 请求码
//
//    private PermissionsChecker mPermissionsChecker; // 权限检测器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        fetchAboutInfo();
        String version = getResources().getString(R.string.value_version);
        version = String.format(version, OKHttpUtils.Utils.getAppVersionName(this));
        mTvVersion.setText(version);
        initListener();
//        mPermissionsChecker = new PermissionsChecker(this);
    }


    protected void initListener() {
        mLlHotLine.setOnClickListener(this);
        mLlWebsite.setOnClickListener(this);
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_hotline:
                AlertDialog askCallDialog = new AlertDialog.Builder(this).
                        setTitle("拨打电话")
                        .setMessage(mTvHotLine.getText().toString().trim())
                        .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                boolean isPermission = PermissionManager.checkPermission(AboutActivity.this,
//                                        Manifest.permission.CALL_PHONE);
//                                if (!isPermission) {
//                                    if (ActivityCompat.shouldShowRequestPermissionRationale(AboutActivity.this,
//                                            Manifest.permission.CALL_PHONE)) {
//                                        ToastUtils.showToast(AboutActivity.this, "不再询问了");
//                                        // Show an expanation to the user *asynchronously* -- don't block
//                                        // this thread waiting for the user's response! After the user
//                                        // sees the explanation, try again to request the permission.
//
//                                    }
//                                    ActivityCompat.requestPermissions(AboutActivity.this,
//                                            new String[]{Manifest.permission.CALL_PHONE},
//                                            CALL_PHONE_REQUEST_CODE);
//                                } else {
                                call();
//                                }

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                askCallDialog.show();
                break;
            case R.id.ll_website:
                farWebsite();
                break;
        }

    }

//    @Override protected void onResume() {
//        super.onResume();
//        // 缺少权限时, 进入权限配置页面
////        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
////            startPermissionsActivity();
////        }
//    }

    private void fetchAboutInfo() {
        if (!NetUtils.isOnline(this)) {
            return;
        }
        OKHttpUtils.getInstance().syncGet(this, Api.ABOUT_ME, Action.about_me);
    }


    private void call() {
        StringBuilder sb = new StringBuilder(mTvHotLine.getText().toString().trim());
        while (sb.indexOf("-") != -1) {
            sb.deleteCharAt(sb.indexOf("-"));
        }
        try {
            Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + sb.toString()));
            startActivity(call);
        } catch (SecurityException e) {
            intoDial(sb, e);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void intoDial(StringBuilder sb, SecurityException e) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + sb.toString()));
            startActivity(intent);
        } catch (ActivityNotFoundException sub) {
            e.printStackTrace();
            sub.printStackTrace();
//             DebugUtils.showToast(this, "哎呀，程序发生未知错误。");
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
////        DebugUtils.dd("request code : " + requestCode + " grant results[0] : " + grantResults[0]);
////        if (requestCode == CALL_PHONE_REQUEST_CODE) {
////            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                call();
////            } else {
//////                new PermissionDialog(this).show();
////                DebugUtils.showToast(this, "您没有授予权限");
////            }
////        }
//    }

    private void farWebsite() {
        Uri uri = Uri.parse(mTvWebsite.getText().toString().trim());
        try {
            Intent website = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(website);
        } catch (ActivityNotFoundException e) {
            DebugUtils.showToast(this, "没有检测到浏览器");
        }

    }


// ---------------------------------------
//    private void startPermissionsActivity() {
//        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
//        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
//            finish();
//        }
//    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAboutEvent(HttpEvent event) {
        if (Action.about_me == event.getAction()) {
            if (SUCCEED == event.getCode()) {
                try {
                    JSONObject jsonObject = event.getData().optJSONObject("data");
                    DataConverter<Company> dc = new DataConverter<>();
                    Company mCompany = dc.JsonToObject(jsonObject.toString(), Company.class);
                    mTvWeChat.setText(StringUtils.replaceNullToEmpty(mCompany.getWeixin(), "kuainiu"));
                    mTvWebsite.setText(StringUtils.replaceNullToEmpty(mCompany.getWeb_url(), "kuainiu"));

                    String phone = StringUtils.replaceNullToEmpty(mCompany.getPhone(), "010-58295196");
                    SpannableString sp = new SpannableString(phone);
                    sp.setSpan(new UnderlineSpan(), 0, phone.length(), SpannedString.SPAN_COMPOSING);
                    mTvHotLine.setText(sp);

                    String content = StringUtils.replaceNullToEmpty(mCompany.getContent(), "快牛直播聚合资深商务牛人，为您创建价值资讯空间，线上点播，直播，同步查看数据分析，多维度分析揭秘，多视角透析行情；快牛直播，让价值讯息传递变得更简单！");
                    mTvContent.setText(content);

                } catch (JsonParseException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
