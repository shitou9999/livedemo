package tv.kuainiu.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.kuainiu.IGXApplication;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;
import tv.kuainiu.command.http.UserHttpRequest;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.InitInfo;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.friends.fragment.FriendsMainFragment;
import tv.kuainiu.ui.home.HomeFragment;
import tv.kuainiu.ui.live.LiveMainFragment;
import tv.kuainiu.ui.me.MeFragment;
import tv.kuainiu.ui.me.activity.SettingActivity;
import tv.kuainiu.ui.teachers.TeachersFragment;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.NetUtils;
import tv.kuainiu.utils.PreferencesUtils;
import tv.kuainiu.widget.NoSlideViewPager;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.vp_main) NoSlideViewPager mVpMain;
    @BindView(R.id.tv_main_home) TextView mMainHome;
    @BindView(R.id.tv_main_live) TextView mMainLive;
    @BindView(R.id.tv_main_friends) TextView mMainFriends;
    @BindView(R.id.tv_main_teacher) TextView mMainTeacher;
    @BindView(R.id.tv_main_me) TextView mMainMe;

    @BindView(R.id.ll_main_home) LinearLayout ll_main_home;
    @BindView(R.id.ll_main_live) LinearLayout ll_main_live;
    @BindView(R.id.ll_main_friends) LinearLayout ll_main_friends;
    @BindView(R.id.ll_main_teacher) LinearLayout ll_main_teacher;
    @BindView(R.id.ll_main_me) LinearLayout ll_main_me;

    private List<BaseFragment> mBaseFragments = new ArrayList<>();
    public static boolean isOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        isOpened = true;
        initView();
        initData();

    }

    @Override protected void onStart() {
        super.onStart();
        //百度统计
        StatService.setSendLogStrategy(this, SendStrategyEnum.APP_START, 1, false);
    }

    @Override protected void onResume() {
        super.onResume();
        requestInit();
        MobclickAgent.onResume(this);
        if (Constant.IS_FIRIST) {
            SettingActivity.CheckAppUpdate(this);
            Constant.IS_FIRIST = false;
        }
    }

    private void initView() {
        mMainHome.setTextColor(Theme.getColorSelectedStateList());
        mMainLive.setTextColor(Theme.getColorSelectedStateList());
        mMainFriends.setTextColor(Theme.getColorSelectedStateList());
        mMainTeacher.setTextColor(Theme.getColorSelectedStateList());
        mMainMe.setTextColor(Theme.getColorSelectedStateList());
        ll_main_home.setSelected(true);
    }


    private void initData() {
        mBaseFragments.clear();
        mBaseFragments.add(HomeFragment.newInstance(0));
        mBaseFragments.add(LiveMainFragment.newInstance());
        mBaseFragments.add(FriendsMainFragment.newInstance());
        mBaseFragments.add(TeachersFragment.newInstance());
        mBaseFragments.add(MeFragment.newInstance());

        mVpMain.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager(), mBaseFragments));
        mVpMain.setOffscreenPageLimit(mBaseFragments.size() - 1);
        switchFragment(0);
    }

    private void switchFragment(int position) {
        ll_main_home.setSelected(position == 0);
        ll_main_live.setSelected(position == 1);
        ll_main_friends.setSelected(position == 2);
        ll_main_teacher.setSelected(position == 3);
        ll_main_me.setSelected(position == 4);
        mVpMain.setCurrentItem(position, false);
    }

    @OnClick({R.id.ll_main_home, R.id.ll_main_live, R.id.ll_main_friends, R.id.ll_main_teacher, R.id.ll_main_me})
    @Override public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_main_home:
                switchFragment(0);
                break;
            case R.id.ll_main_live:
                switchFragment(1);
                break;
            case R.id.ll_main_friends:
                switchFragment(2);
                break;

            case R.id.ll_main_teacher:
                switchFragment(3);
                break;

            case R.id.ll_main_me:
                switchFragment(4);
                break;
        }
    }


    // ==================================================================================
    // nil  ||
    // ==================================================================================
    public static class TabFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<BaseFragment> mFragments = new ArrayList<>();

        public TabFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override public int getCount() {
            return mFragments == null ? 0 : mFragments.size();
        }
    }

    /**
     * 请求后台初始化
     */
    private void requestInit() {
        if (!NetUtils.isOnline(this)) {
            DebugUtils.showToast(this, R.string.toast_not_network);
            return;
        }
        UserHttpRequest.initApp(this, Action.client_init);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClientInitEvent(HttpEvent event) {
        initClientPost(event.getCode(), this);
        switch (event.getAction()) {
            case client_init:
                if (Constant.SUCCEED == event.getCode()) {
//                    BaseActivity.initClientNumbers = 0;//初始化成功后清空初始化计数
                    LogUtils.i(TAG, "json data : " + event.getData().toString());
                    String tempString = event.getData().optString("data");
                    InitInfo initInfo = new Gson().fromJson(tempString, InitInfo.class);
//                    PreferencesUtils.putString(this, Constant.COURSE_URL, initInfo.getKe_url());
                    String privateKey = initInfo.getPrivate_key();
                    LogUtils.i(TAG, "privateKey : " + privateKey);
                    if (!TextUtils.isEmpty(privateKey)) {
                        IGXApplication.setKey(privateKey);
                    }
//                    showCustomRedPoint(initInfo.getCustom_num() > 0);//显示红点
                    PreferencesUtils.putInt(this, Constant.MSG_NUM, initInfo.getMsg_num());
//                    localBroadcastManager.sendBroadcast(new Intent(Constant.INTENT_ACTION_ACTIVITY_MSG_NUM));
                }
                break;
            case switch_teacher_fragment:
                switchFragment(3);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOpened = false;
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
