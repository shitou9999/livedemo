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
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.app.OnItemClickListener;
import tv.kuainiu.app.Theme;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.SupportHttpUtil;
import tv.kuainiu.command.http.TeacherHttpUtil;
import tv.kuainiu.command.http.core.CacheConfig;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.TeacherInfo;
import tv.kuainiu.modle.TeacherZoneDynamics;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.modle.push.CustomVideo;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.comments.CommentListActivity;
import tv.kuainiu.ui.comments.fragmet.PostCommentListFragment;
import tv.kuainiu.ui.friends.model.Message;
import tv.kuainiu.ui.teachers.adapter.TeacherZoneAdapter;
import tv.kuainiu.utils.CustomLinearLayoutManager;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.TitleBarView;

/**
 * 老师专区
 */
public class TeacherZoneActivity extends BaseActivity implements OnItemClickListener {

    private static final String ID = "ID";
    @BindView(R.id.tbv_title)
    TitleBarView mTbvTitle;
    @BindView(R.id.rvReadingTap)
    RecyclerView mRvReadingTap;
    @BindView(R.id.rl_stick)
    RelativeLayout rl_stick;
    @BindView(R.id.btnPublish)
    Button mBtnPublish;
    @BindView(R.id.tab_fragment_major)
    TabLayout mTabFragmentMajor;
    @BindView(R.id.srlRefresh)
    SwipeRefreshLayout mSrlRefresh;
    private TeacherZoneAdapter mTeacherZoneAdapter;
    RecyclerView.OnScrollListener loadmoreListener;
    private CustomLinearLayoutManager mLayoutManager;
    private List<Message> mMessages = new ArrayList<>();
    private List<TabLayout.Tab> listStickTab;
    private String[] tabNames = new String[]{"动态", "观点", "解盘"};
    int selectedIndex = 0;
    private boolean loading;
    private String teacherid = "";
    private String user_id = "";
    private TeacherInfo teacherInfo;
    private int page = 1;
    private int pageJiePan = 1;
    private List<TeacherZoneDynamics> teacherZoneDynamicsList = new ArrayList<>();
    private List<CustomVideo> customVideoList = new ArrayList<>();

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
        user_id = MyApplication.getUser() == null ? "" : MyApplication.getUser().getUser_id();
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
            @Override
            public void onRefresh() {
                if (selectedIndex == 0) {
                    page = 1;
                    getData();
                } else if (selectedIndex == 1) {
                    pageJiePan = 1;
                    getJeiPan("1");
                } else {
                    pageJiePan = 1;
                    getJeiPan("2");
                }
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

                        if (selectedIndex == 0) {
                            page += 1;
                            getData();
                        } else if (selectedIndex == 1) {
                            pageJiePan += 1;
                            getJeiPan("1");
                        } else {
                            pageJiePan += 1;
                            getJeiPan("2");
                        }
                    }
                }
            }
        };
    }

    private TextView tv_follow_button;
    private TextView tv_follow_number;

    private void initData() {
        initTab(0);
        mTeacherZoneAdapter = new TeacherZoneAdapter(this);
        mTeacherZoneAdapter.setOnClickListener(this);
        mTeacherZoneAdapter.setTabNames(tabNames, getOnTabSelectedListener());
        mRvReadingTap.setAdapter(mTeacherZoneAdapter);
//        View view = LayoutInflater.from(this).inflate(R.layout.activity_teacher_zone_top, null);
//        mRvReadingTap.addView(view);
//        FriendsPostAdapter adapter = new FriendsPostAdapter(this, mMessages);
//        mRvReadingTap.setAdapter(adapter);
        getData();
    }
    TextView mTvFriendsPostLike;
    TeacherZoneDynamics teacherZoneDynamics;
    CustomVideo customVideo;
    View ivSupport;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_follow_button:
                TeacherInfo teacherInfo = (TeacherInfo) v.getTag(R.id.tv_follow_button);
                tv_follow_button = (TextView) v;
                tv_follow_number = (TextView) v.getTag(R.id.tv_follow_number);
                addFollow(teacherInfo.getIs_follow(), teacherInfo.getId());
                break;
            case R.id.ivSupport:
                ivSupport=v;
                mTvFriendsPostLike = (TextView) v.getTag(R.id.tv_friends_post_like);
                if(selectedIndex==0) {
                    teacherZoneDynamics = (TeacherZoneDynamics) v.getTag();
                    SupportHttpUtil.supportDynamics(this, String.valueOf(teacherZoneDynamics.getId()),Action.SUPPORT_DYNAMICS2);
                }else{
                    customVideo = (CustomVideo) v.getTag();
                    SupportHttpUtil.supportVideoDynamics(this,customVideo.getCat_id(),customVideo.getId() );
                }

                break;
            case R.id.tv_friends_post_comment:
                if(selectedIndex==0) {
                    teacherZoneDynamics = (TeacherZoneDynamics) v.getTag();
                    SupportHttpUtil.supportDynamics(this, String.valueOf(teacherZoneDynamics.getId()),Action.SUPPORT_DYNAMICS2);
                    CommentListActivity.intoNewIntent(this, PostCommentListFragment.MODE_DYNAMIC, String.valueOf(teacherZoneDynamics.getId()), "");
                }else{
                    customVideo = (CustomVideo) v.getTag();
                    CommentListActivity.intoNewIntent(this, PostCommentListFragment.MODE_DYNAMIC, String.valueOf(customVideo.getId()), "");
                }

                break;
        }
    }

    // Call del follow
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFollow(HttpEvent event) {
        if (Action.teacher_fg_del_follow == event.getAction()) {
            if (Constant.SUCCEED == event.getCode()) {
                teacherInfo.setIs_follow(0);
                int size = teacherInfo.getFans_count();
                size = size - 1;
                teacherInfo.setFans_count(size);
                mTeacherZoneAdapter.setTeacherInfo(teacherInfo);
                tv_follow_number.setText(String.format(Locale.CHINA, "%s人关注", StringUtils.getDecimal(teacherInfo.getFans_count(), Constant.TEN_THOUSAND, "万", "")));
                tv_follow_button.setText("+ 关注");
                tv_follow_button.setSelected(false);
            } else {
                DebugUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "取消关注失败"));
            }
        }
        if (Action.teacher_fg_add_follow == event.getAction()) {
            if (Constant.SUCCEED == event.getCode()) {
                teacherInfo.setIs_follow(1);
                int size = teacherInfo.getFans_count();
                size = size + 1;
                teacherInfo.setFans_count(size);
                mTeacherZoneAdapter.setTeacherInfo(teacherInfo);
                tv_follow_number.setText(String.format(Locale.CHINA, "%s人关注", StringUtils.getDecimal(teacherInfo.getFans_count(), Constant.TEN_THOUSAND, "万", "")));
                tv_follow_button.setText("已关注");
                tv_follow_button.setSelected(true);
            } else {
                DebugUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "关注失败"));
            }
        }
    }

    // 添加 or 取消关注
    private void addFollow(int is_follow, String id) {
        if (Constant.FOLLOWED == is_follow) {
            TeacherHttpUtil.delFollowForTeacherId(this, id, Action.teacher_fg_del_follow);
        } else {
            TeacherHttpUtil.addFollowForTeacherID(this, id, Action.teacher_fg_add_follow);
        }
    }

    private void getData() {
        TeacherHttpUtil.fetchTeacherDetails(this, new TeacherHttpUtil.ParamBuilder(teacherid, user_id), Action.teacher_fg_fetch_detail);
        fetchTeacherDynamicsList();
    }

    private void getJeiPan(String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", String.valueOf(pageJiePan));
        map.put("teacher_id", teacherid);
        map.put("type", type);
        OKHttpUtils.getInstance().syncGet(this, Api.FIND_NEWS_LIST + ParamUtil.getParamForGet(map), Action.find_news_list, CacheConfig.getCacheConfig());
    }

    /**
     * 名师个人专区-动态
     */
    public void fetchTeacherDynamicsList() {
        TeacherHttpUtil.fetchTeacherDynamicsList(this, page, teacherid, Action.find_dynamics_list);
    }

    private void initTab(int selected) {
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
            mTabFragmentMajor.setOnTabSelectedListener(getOnTabSelectedListener());
            mTabFragmentMajor.setTabTextColors(Color.parseColor("#757575"), Color.parseColor(Theme.getCommonColor()));
            mTabFragmentMajor.setSelectedTabIndicatorColor(Color.parseColor(Theme.getCommonColor()));
        } else {
            if (rl_stick.getVisibility() == View.VISIBLE) {
                mTeacherZoneAdapter.setSelectedIndex(selected);
            } else {
                listStickTab.get(selected).select();
            }
            if (selected != selectedIndex) {
                customVideoList.clear();
                teacherZoneDynamicsList.clear();
                selectedIndex = selected;
                if (selectedIndex == 0) {
                    page = 1;
                    fetchTeacherDynamicsList();
                } else if (selectedIndex == 1) {
                    pageJiePan = 1;
                    getJeiPan("1");
                } else {
                    pageJiePan = 1;
                    getJeiPan("2");
                }
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
            mTeacherZoneAdapter.setSelectedIndex(TeacherZoneAdapter.CUSTOM_VIEW_POINT);
            if (page > 1) {
                mTeacherZoneAdapter.notifyItemRangeInserted(oldsize + TeacherZoneAdapter.SIZE, teacherZoneDynamicsList.size());
            }
        }
    }

    private void teacherZoneJeiPanListDataBind(int oldsize) {
        if (customVideoList != null && customVideoList.size() > 0) {
            mTeacherZoneAdapter.setTeacherZoneJiePanList(customVideoList);
            mTeacherZoneAdapter.setSelectedIndex(selectedIndex);
            if (pageJiePan > 1) {
                mTeacherZoneAdapter.notifyItemRangeInserted(oldsize + TeacherZoneAdapter.SIZE, customVideoList.size());
            }
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
            case SUPPORT_DYNAMICS2:
                if (Constant.SUCCEED == event.getCode()) {
                    ivSupport.setVisibility(View.INVISIBLE);
                    mTvFriendsPostLike.setText(String.format(Locale.CHINA, "(%d)", teacherZoneDynamics.getSupport_num() + 1));
                    mTvFriendsPostLike.setSelected(true);
                    ToastUtils.showToast(this, "点赞成功");
                } else if (-2 == event.getCode()) {
                    DebugUtils.showToastResponse(this, "已支持过");
                } else {
                    LogUtils.e("点赞失败", StringUtils.replaceNullToEmpty(event.getMsg()));
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "点赞失败"));
                }
                break;
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
//                            finish();
                        } finally {

                        }

                    }
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "获取老师信息失败"));
//                    finish();
                }
                if (page == 1) {
                    mSrlRefresh.setRefreshing(false);
                    mTeacherZoneAdapter.notifyDataSetChanged();
                }
                break;
            case find_news_list:

                if (Constant.SUCCEED == event.getCode()) {
                    if (event.getData() != null && event.getData().has("data")) {
                        try {
                            if (pageJiePan == 1) {
                                customVideoList.clear();
                            }
                            JSONObject jsonObject = event.getData().getJSONObject("data");
                            List<CustomVideo> tempCustomVideoList = new DataConverter<CustomVideo>().JsonToListObject(jsonObject.getString("list"), new TypeToken<List<CustomVideo>>() {
                            }.getType());
                            if (tempCustomVideoList != null && tempCustomVideoList.size() > 0) {
                                loading = false;
                                int size = customVideoList.size();
                                customVideoList.addAll(tempCustomVideoList);
                                teacherZoneJeiPanListDataBind(size);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showToast(this, "老师信息解析失败");
//                            finish();
                        }

                    }
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "获取老师信息失败"));
//                    finish();
                }
                if (pageJiePan == 1) {
                    mSrlRefresh.setRefreshing(false);
                    mTeacherZoneAdapter.notifyDataSetChanged();
                }
                break;
        }

    }


}
