package tv.kuainiu.ui.teachers.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.IGXApplication;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;
import tv.kuainiu.command.http.TeacherHttpUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.TeacherInfo;
import tv.kuainiu.modle.TeacherZoneDynamics;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.friends.model.Message;
import tv.kuainiu.ui.teachers.adapter.TeacherZoneAdapter;
import tv.kuainiu.util.CustomLinearLayoutManager;
import tv.kuainiu.util.DataConverter;
import tv.kuainiu.util.StringUtils;
import tv.kuainiu.util.ToastUtils;
import tv.kuainiu.widget.TitleBarView;

/**
 * 老师专区
 */
public class TeacherZoneActivity extends BaseActivity {

    private static final String ID = "ID";
    @BindView(R.id.tbv_title) TitleBarView mTbvTitle;
    @BindView(R.id.rvReadingTap) RecyclerView mRvReadingTap;
    @BindView(R.id.rl_stick) RelativeLayout rl_stick;
    @BindView(R.id.btnPublish) Button mBtnPublish;
    @BindView(R.id.tab_fragment_major) TabLayout mTabFragmentMajor;
    @BindView(R.id.srlRefresh) SwipeRefreshLayout mSrlRefresh;
    private TeacherZoneAdapter mTeacherZoneAdapter;
    RecyclerView.OnScrollListener loadmoreListener;
    private CustomLinearLayoutManager mLayoutManager;
    private List<Message> mMessages = new ArrayList<>();
    private List<TabLayout.Tab> listStickTab;
    private String[] tabNames = new String[]{"观点", "解盘"};
    int selectedIndex = 0;
    private boolean loading;
    private String teacherid = "";
    private String user_id = "";
    private TeacherInfo teacherInfo;
    private int page = 1;
    private List<TeacherZoneDynamics> teacherZoneDynamicsList = new ArrayList<>();

    public static void intoNewIntent(Activity context, String id) {
        Intent intent = new Intent(context, TeacherZoneActivity.class);
        intent.putExtra(ID, id);
        context.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_zone);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        teacherid = getIntent().getStringExtra(ID);
        user_id = IGXApplication.getUser() == null ? "" : IGXApplication.getUser().getUser_id();
        if (TextUtils.isEmpty(teacherid)) {
            ToastUtils.showToast(this, "未获取到老师");
            finish();
        }
        initListener();
        initView();
        initData();

    }

    private void initView() {
        mSrlRefresh.setColorSchemeColors(Theme.getLoadingColor());
        rl_stick.setVisibility(View.INVISIBLE);
        mLayoutManager = new CustomLinearLayoutManager(this);
        mRvReadingTap.setLayoutManager(mLayoutManager);
        mRvReadingTap.addOnScrollListener(loadmoreListener);
    }

    private void initListener() {
        mSrlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                page = 1;
                getData();
            }
        });
        loadmoreListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                if (pastVisiblesItems >= 1) {
                    rl_stick.setVisibility(View.VISIBLE);
                } else {
                    rl_stick.setVisibility(View.INVISIBLE);
                }
                if (dy > 0) //向下滚动
                {
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();


                    if (!loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = true;
                        page += 1;
                        getData();
                    }
                }
            }
        };
    }

    private void initData() {
        initTab(0);
        mTeacherZoneAdapter = new TeacherZoneAdapter(this);
        mTeacherZoneAdapter.setTabNames(tabNames, getOnTabSelectedListener());
        mRvReadingTap.setAdapter(mTeacherZoneAdapter);
//        View view = LayoutInflater.from(this).inflate(R.layout.activity_teacher_zone_top, null);
//        mRvReadingTap.addView(view);
//        FriendsPostAdapter adapter = new FriendsPostAdapter(this, mMessages);
//        mRvReadingTap.setAdapter(adapter);
        getData();
    }

    private void getData() {
        TeacherHttpUtil.fetchTeacherDetails(this, new TeacherHttpUtil.ParamBuilder(teacherid, user_id), Action.teacher_fg_fetch_detail);
        fetchTeacherDynamicsList();
    }

    /**
     * 名师个人专区-动态
     */
    public void fetchTeacherDynamicsList() {
        TeacherHttpUtil.fetchTeacherDynamicsList(this, page, teacherid, Action.find_dynamics_list);
    }

    private void initTab(int selectedIndex) {
        if (listStickTab == null) {
            listStickTab = new ArrayList<>();
            mTabFragmentMajor.removeAllTabs();
            for (int i = 0; i < tabNames.length; i++) {

                TabLayout.Tab tabStick = mTabFragmentMajor.newTab();
                tabStick.setText(tabNames[i]);
//                tabStick.setTag(tabNamesTags[i]);
                listStickTab.add(tabStick);
                mTabFragmentMajor.addTab(tabStick);
            }
            mTabFragmentMajor.addOnTabSelectedListener(getOnTabSelectedListener());
            mTabFragmentMajor.setTabTextColors(Color.parseColor("#757575"), Color.parseColor(Theme.getCommonColor()));
            mTabFragmentMajor.setSelectedTabIndicatorColor(Color.parseColor(Theme.getCommonColor()));
        } else {
            if (rl_stick.getVisibility() == View.VISIBLE) {
                mTeacherZoneAdapter.setSelectedIndex(selectedIndex);
            } else {
                listStickTab.get(selectedIndex).select();
            }
        }
    }

    private void teacherInfoDataBind(TeacherInfo teacherInfo) {
        mTbvTitle.setText(StringUtils.replaceNullToEmpty(teacherInfo.getNickname()));
        mTeacherZoneAdapter.setTeacherInfo(teacherInfo);
        mTeacherZoneAdapter.notifyItemChanged(0);
    }

    private void teacherZoneDynamicsListDataBind(int oldsize) {
        if (teacherZoneDynamicsList != null && teacherZoneDynamicsList.size() > 0) {
            mTeacherZoneAdapter.setTeacherZoneDynamicsList(teacherZoneDynamicsList);
            mTeacherZoneAdapter.notifyItemRangeInserted(oldsize + TeacherZoneAdapter.SIZE, teacherZoneDynamicsList.size());
        }
    }

    @NonNull
    private TabLayout.OnTabSelectedListener getOnTabSelectedListener() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                if (tab.getTag() instanceof Integer) {
//                    catid = tab.getTag().toString();
//                    type = "cat";
//                } else {
//                    catid = "";
//                    type = tab.getTag().toString();
//                }
//                page = 1;
//                fetchContentList();
                initTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
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
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "获取老师信息失败"));
                    finish();
                }
                break;
            case find_dynamics_list:
                if (page == 1) {
                    mSrlRefresh.setRefreshing(false);
                }
                if (Constant.SUCCEED == event.getCode()) {
                    if (event.getData() != null && event.getData().has("data")) {
                        try {
                            JSONObject jsonObject = event.getData().getJSONObject("data");
                            List<TeacherZoneDynamics> tempTeacherZoneDynamicsList = new DataConverter<TeacherZoneDynamics>().JsonToListObject(jsonObject.getString("list"), new TypeToken<List<TeacherZoneDynamics>>() {
                            }.getType());
                            if (page == 1) {
                                teacherZoneDynamicsList.clear();
                            }
                            if (tempTeacherZoneDynamicsList != null && tempTeacherZoneDynamicsList.size() > 0) {
                                loading = false;
                                int size = teacherZoneDynamicsList.size();
                                teacherZoneDynamicsList.addAll(tempTeacherZoneDynamicsList);
                                teacherZoneDynamicsListDataBind(size);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showToast(this, "老师信息解析失败");
                            finish();
                        }

                    }
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "获取老师信息失败"));
                    finish();
                }
                break;
        }

    }
}
