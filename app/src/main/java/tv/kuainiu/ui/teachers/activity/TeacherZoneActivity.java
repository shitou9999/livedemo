package tv.kuainiu.ui.teachers.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.app.OnItemClickListener;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.TeacherHttpUtil;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.LiveInfo;
import tv.kuainiu.modle.TeacherInfo;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.friends.fragment.CustomViewPointFragment;
import tv.kuainiu.ui.friends.fragment.TabMajorFragment;
import tv.kuainiu.ui.liveold.PlayLiveActivity;
import tv.kuainiu.ui.liveold.model.LiveParameter;
import tv.kuainiu.ui.me.appointment.fragment.MyLiveHistoryFragment;
import tv.kuainiu.ui.teachers.fragment.AppointFragment;
import tv.kuainiu.ui.teachers.fragment.MyLivePlanFragment;
import tv.kuainiu.ui.teachers.fragment.VideoFragment;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.ScreenUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.TitleBarView;
import tv.kuainiu.widget.dialog.LoginPromptDialog;

import static tv.kuainiu.R.id.tv_follow_button;
import static tv.kuainiu.R.id.tv_follow_number;

/**
 * 老师专区
 */
public class TeacherZoneActivity extends BaseActivity implements OnItemClickListener {

    public static final String ID = "ID";
    @BindView(R.id.tbv_title)
    TitleBarView mTbvTitle;
    @BindView(R.id.ivBanner)
    ImageView ivBanner;
    @BindView(R.id.ci_avatar)
    CircleImageView ciAvatar;
    @BindView(R.id.ivIsVip)
    ImageView ivIsVip;
    @BindView(R.id.rl_avatar)
    RelativeLayout rlAvatar;
    @BindView(R.id.tvTheme)
    TextView tvTheme;
    @BindView(R.id.tvTeacherName)
    TextView tvTeacherName;
    @BindView(tv_follow_button)
    TextView tvFollowButton;
    @BindView(tv_follow_number)
    TextView tvFollowNumber;
    @BindView(R.id.rl_top_navigation)
    RelativeLayout rlTopNavigation;
    @BindView(R.id.tab_fragment_major)
    TabLayout tabFragmentMajor;
    @BindView(R.id.vpTeacherZone)
    ViewPager vpTeacherZone;
    @BindView(R.id.ll_fragment_friends_main_news_info)
    LinearLayout llFragmentFriendsMainNewsInfo;
    @BindView(R.id.tvLiningTitle)
    TextView tvLiningTitle;
    @BindView(R.id.llLiningTitle)
    LinearLayout llLiningTitle;
    private String[] tabNames = new String[]{"动态", "观点", "解盘", "直播计划", "直播回看"};
    int selectedIndex = 0;
    private String teacherid = "";
    private String user_id = "";
    private TeacherInfo teacherInfo;
    private List<BaseFragment> mBaseFragments = new ArrayList<>();
    public List<LiveInfo> mLiveItemList = new ArrayList<>();

    public static void intoNewIntent(Activity context, String id) {
        Intent intent = new Intent(context, TeacherZoneActivity.class);
        intent.putExtra(ID, id);
        context.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_zone_pre);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        teacherid = getIntent().getStringExtra(ID);
        user_id = MyApplication.getUser() == null ? "" : MyApplication.getUser().getUser_id();
        if (TextUtils.isEmpty(teacherid)) {
            ToastUtils.showToast(this, "未获取到老师");
            finish();
        }
        initView();
        initData();
        initFragment();
        vpTeacherZone.setAdapter(new TabMajorFragment.SimpleViewPager(getSupportFragmentManager(), mBaseFragments, tabNames));
        tabFragmentMajor.setupWithViewPager(vpTeacherZone);
    }

    private void initView() {
        int screenWidth = ScreenUtils.getScreenWidth(this);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(screenWidth, (int) (screenWidth / 7 * 3));
        ivBanner.setLayoutParams(lp);
    }


    private void initFragment() {
        mBaseFragments.clear();
        mBaseFragments.add(CustomViewPointFragment.newInstance(true, teacherid));
        mBaseFragments.add(AppointFragment.newInstance(true, teacherid));
        mBaseFragments.add(VideoFragment.newInstance(true, teacherid));
        mBaseFragments.add(MyLivePlanFragment.newInstance(teacherid));
        mBaseFragments.add(MyLiveHistoryFragment.newInstance(teacherid));
    }

    private void initData() {
        TeacherHttpUtil.fetchTeacherDetails(this, new TeacherHttpUtil.ParamBuilder(teacherid, user_id), Action.teacher_fg_fetch_detail);
        getLivingData();
    }

    private void getLivingData() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", MyApplication.getUser() == null ? "" : MyApplication.getUser().getUser_id());
        map.put("teacher_id", teacherid);
        map.put("live_type", "1");
        map.put("page", String.valueOf(1));
        OKHttpUtils.getInstance().syncGet(this, Api.my_live_list + ParamUtil.getParamForGet(map), Action.my_live_list);
    }

    @OnClick({R.id.tv_follow_button})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_follow_button:
                if (!MyApplication.isLogin()) {
                    new LoginPromptDialog(this).show();
                    return;
                }
                if (teacherInfo != null) {
                    addFollow(teacherInfo.getIs_follow(), teacherid);
                }
                break;
        }
    }

    // 添加 or 取消关注
    private void addFollow(int is_follow, String id) {
        if (Constant.FOLLOWED == is_follow) {
            TeacherHttpUtil.delFollowForTeacherId(this, id, Action.teacher_zone_fg_del_follow);
        } else {
            TeacherHttpUtil.addFollowForTeacherID(this, id, Action.teacher_zone_fg_add_follow);
        }
    }

    private void teacherInfoDataBind(TeacherInfo teacherInfo) {
        mTbvTitle.setText(StringUtils.replaceNullToEmpty(teacherInfo.getNickname()));
        ImageDisplayUtil.displayImage(this, ciAvatar, StringUtils.replaceNullToEmpty(teacherInfo.getAvatar()), R.mipmap.default_avatar);
        ImageDisplayUtil.displayImage(this, ivBanner, StringUtils.replaceNullToEmpty(teacherInfo.getBanner()));
        tvTheme.setText(StringUtils.replaceNullToEmpty(teacherInfo.getSlogan()));
        tvTeacherName.setText(StringUtils.replaceNullToEmpty(teacherInfo.getNickname()));
        tvFollowNumber.setText(String.format(Locale.CHINA, "%s人关注", StringUtils.getDecimal(teacherInfo.getFans_count(), Constant.TEN_THOUSAND, "万", "")));
        if (teacherInfo.getIs_follow() == 0) {
            tvFollowButton.setText("+关注");
        } else {
            tvFollowButton.setText("已关注");
        }
        tvFollowButton.setSelected(teacherInfo.getIs_follow() != 0);
        ivIsVip.setVisibility(View.VISIBLE);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case teacher_fg_fetch_detail:
                if (Constant.SUCCEED == event.getCode()) {
                    DataConverter<TeacherInfo> dataConverter = new DataConverter<>();
                    if (event.getData() != null && event.getData().has("data")) {
                        try {
                            JSONObject jsonObject = event.getData().getJSONObject("data");

                            teacherInfo = dataConverter.JsonToObject(jsonObject.getString("info"), TeacherInfo.class);
                            teacherInfoDataBind(teacherInfo);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showToast(this, "老师信息解析失败");
                            finish();
                        }

                    }
                } else {
                    tip(StringUtils.replaceNullToEmpty(event.getMsg(), "获取老师信息失败"));
                }
                break;
            case teacher_zone_fg_del_follow:
                if (Constant.SUCCEED == event.getCode()) {
                    teacherInfo.setIs_follow(0);
                    int size = teacherInfo.getFans_count();
                    size = size - 1;
                    teacherInfo.setIs_follow(0);
                    teacherInfo.setFans_count(size);
                    tvFollowNumber.setText(String.format(Locale.CHINA, "%s人关注", StringUtils.getDecimal(teacherInfo.getFans_count(), Constant.TEN_THOUSAND, "万", "")));
                    tvFollowButton.setText("+ 关注");
                    tvFollowButton.setSelected(false);
                } else {
                    DebugUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "取消关注失败"));
                }
                break;
            case teacher_zone_fg_add_follow:
                if (Constant.SUCCEED == event.getCode()) {
                    teacherInfo.setIs_follow(1);
                    int size = teacherInfo.getFans_count();
                    size = size + 1;
                    teacherInfo.setFans_count(size);
                    teacherInfo.setIs_follow(1);
                    tvFollowNumber.setText(String.format(Locale.CHINA, "%s人关注", StringUtils.getDecimal(teacherInfo.getFans_count(), Constant.TEN_THOUSAND, "万", "")));
                    tvFollowButton.setText("已关注");
                    tvFollowButton.setSelected(true);
                } else {
                    DebugUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "关注失败"));
                }
                break;
            case my_live_list:
                mLiveItemList.clear();
                if (Constant.SUCCEED == event.getCode()) {
                    String json = event.getData().optString("data");
                    LogUtils.e("json", "json:" + json);
                    try {
                        JSONObject object = new JSONObject(json);
                        List<LiveInfo> tempLiveItemList = new DataConverter<LiveInfo>().JsonToListObject(object.optString("live_list"), new TypeToken<List<LiveInfo>>() {
                        }.getType());

                        if (tempLiveItemList != null && tempLiveItemList.size() > 0) {
                            int size = mLiveItemList.size();
                            mLiveItemList.addAll(tempLiveItemList);
                            dataLiveListBind();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    LogUtils.e(TAG, event == null ? "获取正在直播信息失败" : event.getMsg());
                }
                break;
        }

    }

    private void dataLiveListBind() {
        if (mLiveItemList.size() > 0) {
            final LiveInfo liveItem = mLiveItemList.get(0);
            tvLiningTitle.setText(liveItem.getTitle());
            llLiningTitle.setVisibility(View.VISIBLE);
            llLiningTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LiveParameter liveParameter = new LiveParameter();
                    liveParameter.setLiveId(liveItem.getId());
                    liveParameter.setLiveTitle(liveItem.getTitle());
                    liveParameter.setRoomId(liveItem.getTeacher_info().getLive_roomid());
                    liveParameter.setTeacherId(liveItem.getTeacher_id());
                    PlayLiveActivity.intoNewIntent(TeacherZoneActivity.this, liveParameter);
                }
            });
        } else {
            llLiningTitle.setVisibility(View.INVISIBLE);
        }
    }


}
