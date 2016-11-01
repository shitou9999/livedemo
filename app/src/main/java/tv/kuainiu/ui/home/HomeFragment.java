package tv.kuainiu.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.google.zxing.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.app.OnItemClickListener;
import tv.kuainiu.app.Theme;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.AppointmentRequestUtil;
import tv.kuainiu.command.http.LiveHttpUtil;
import tv.kuainiu.command.http.SupportHttpUtil;
import tv.kuainiu.command.http.TeacherHttpUtil;
import tv.kuainiu.command.http.core.CacheConfig;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.Banner;
import tv.kuainiu.modle.HotPonit;
import tv.kuainiu.modle.LiveInfo;
import tv.kuainiu.modle.NewsItem;
import tv.kuainiu.modle.TeacherZoneDynamicsInfo;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.articles.activity.PostZoneActivity;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.home.adapter.HomeAdapter;
import tv.kuainiu.ui.liveold.PlayLiveActivity;
import tv.kuainiu.ui.liveold.ReplayLiveActivity;
import tv.kuainiu.ui.liveold.model.LiveParameter;
import tv.kuainiu.ui.message.activity.MessageSystemActivity;
import tv.kuainiu.ui.teachers.activity.TeacherZoneActivity;
import tv.kuainiu.ui.video.VideoActivity;
import tv.kuainiu.utils.CustomLinearLayoutManager;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.DividerItemDecoration;
import tv.kuainiu.widget.dialog.LoginPromptDialog;

import static tv.kuainiu.modle.cons.Action.hot_point;

/**
 * 咨询
 */
public class HomeFragment extends BaseFragment {
    private static final String ARG_POSITION = "ARG_POSITION";
    @BindView(R.id.srlRefresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rvReadingTap)
    RecyclerView rvReadingTap;
    @BindView(R.id.ivMessage)
    ImageView ivMessage;

    private HomeAdapter mHomeAdapter;
    private List<Banner> mBannerList = new ArrayList<>();
    public List<LiveInfo> mLiveItemList = new ArrayList<>();
    private List<HotPonit> mHotPonitList = new ArrayList<>();
    private List<NewsItem> mNewsItemList = new ArrayList<>();
    private int HotPointPage = 1;
    private int NewsPage = 1;
    private boolean loading = false;
    RecyclerView.OnScrollListener loadMoreListener;
    CustomLinearLayoutManager mLayoutManager;

    public static HomeFragment newInstance(int parentPosition) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, parentPosition);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, container, false);
            ButterKnife.bind(this, view);
            initListener();
            initView();
            dataBind();
            initData();
        } else {
            ViewGroup viewgroup = (ViewGroup) view.getParent();
            if (viewgroup != null) {
                viewgroup.removeView(view);
            }
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this, view);
        return view;
    }

    private void initView() {
        srlRefresh.setColorSchemeColors(Theme.getLoadingColor());
        mLayoutManager = new CustomLinearLayoutManager(getActivity());
        rvReadingTap.setLayoutManager(mLayoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        rvReadingTap.addItemDecoration(mDividerItemDecoration);
        rvReadingTap.addOnScrollListener(loadMoreListener);
    }

    private void initData() {

        getBannerData();
        getHotPoint();
        getNews();
    }

    private void getBannerData() {
        OKHttpUtils.getInstance().post(getActivity(), Api.TEST_DNS_API_HOST, Api.FIND_BANNAR, ParamUtil.getParam(null), Action.find_bannar, CacheConfig.getCacheConfig());
        LiveHttpUtil.liveHomeIndex(getActivity(), "1", 1, Action.live_zhi_bo_home);
    }

    private void getHotPoint() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", String.valueOf(HotPointPage));
        map.put("user_id", MyApplication.isLogin() ? MyApplication.getUser().getUser_id() : "");
        OKHttpUtils.getInstance().syncGet(getContext(), Api.HOT_POINT + ParamUtil.getParamForGet(map), hot_point, CacheConfig.getCacheConfig());
    }

    private void getNews() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", String.valueOf(NewsPage));
        OKHttpUtils.getInstance().syncGet(getContext(), Api.FIND_NEWS_LIST + ParamUtil.getParamForGet(map), Action.find_home_news_list, CacheConfig.getCacheConfig());
    }

    private void initListener() {
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NewsPage = 1;
                initData();
            }
        });
        loadMoreListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //向下滚动
                {
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (!loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = true;
                        NewsPage += 1;
                        getNews();
                    }
                }
            }
        };
    }

    TextView mTvFollowButton;
    TextView mTvHotPointSupport;
    HotPonit mHotPoint2;
    View vSupport;

    @OnClick({R.id.ivMessage, R.id.ivQRCode})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivMessage:
                Intent messageIntent = new Intent();
                messageIntent.setClass(getActivity(), MessageSystemActivity.class);
                startActivity(messageIntent);
                break;
            case R.id.ivQRCode:
                Intent qrIntent = new Intent();
                qrIntent.setClass(getActivity(), CaptureActivity.class);
                startActivity(qrIntent);
                break;
        }

    }

    LiveInfo appointmentLiveInfo;
    TextView tvAppointment;

    class itemClick implements OnItemClickListener {

        @Override
        public void onClick(View v) {
            Object obj = v.getTag();
            HotPonit mHotPoint = null;
            if (obj instanceof HotPonit) {
                mHotPoint = (HotPonit) obj;
            }
            if (obj instanceof LiveInfo) {
                appointmentLiveInfo = (LiveInfo) v.getTag();
            }
            LiveInfo liveItem = mHotPoint == null ? null : mHotPoint.getLive_info();
            TeacherZoneDynamicsInfo newsInfo = mHotPoint == null ? null : mHotPoint.getNews_info();
            switch (v.getId()) {
                case R.id.tv_next_time://预约
                    if (!MyApplication.isLogin()) {
                        showLoginTip();
                        return;
                    }
                    tvAppointment = (TextView) v;
                    appointment(appointmentLiveInfo);
                    break;
                case R.id.tv_follow_button://点关注
                    if (!MyApplication.isLogin()) {
                        showLoginTip();
                        return;
                    }
                    mTvFollowButton = (TextView) v;
                    //关注
                    if (!MyApplication.isLogin()) {
                        new LoginPromptDialog(getActivity()).show();
                        return;
                    } else {
                        addFollow(mHotPoint.getIs_follow(), mHotPoint.getUser_id());
                    }
                    break;
                case R.id.ll_hot_point_support://点赞
                    vSupport = v;
                    mHotPoint2 = (HotPonit) v.getTag();
                    mTvHotPointSupport = (TextView) v.getTag(R.id.tv_hot_point_support);
                    if (mHotPoint2 != null) {
                        if (mHotPoint2.getIs_support() == Constant.FAVOURED) {
                            vSupport.setSelected(true);
                            ToastUtils.showToast(getActivity(), "已经点过赞了");
                        } else {
                            SupportHttpUtil.supportDynamics(getActivity(), mHotPoint2.getId(), Action.home_support_dynamics);
                        }
                    }
                    break;
                case R.id.tv_teacher_name:
                case R.id.civ_avatar://点击头像
                    if (mHotPoint != null && mHotPoint.getTeacher_info() != null) {
                        TeacherZoneActivity.intoNewIntent(getActivity(), mHotPoint.getTeacher_info().getId());
                    }
                    break;
                case R.id.tv_hot_point_content://带你内容
                    if (mHotPoint != null) {
                        switch (mHotPoint.getQuote_type()) {
                            case Constant.NEWS_TYPE_ARTICLE:

                                if (newsInfo != null) {
                                    PostZoneActivity.intoNewIntent(getActivity(), newsInfo.getNews_id(), newsInfo.getNews_catid());
                                }

                                break;
                            case Constant.NEWS_TYPE_VIDEO:
                                if (newsInfo != null) {
                                    VideoActivity.intoNewIntent(getActivity(), newsInfo.getNews_id(), newsInfo.getNews_video_id(), newsInfo.getNews_catid(), StringUtils.replaceNullToEmpty(newsInfo.getNews_title()));
                                }
                                break;
                            case Constant.NEWS_TYPE_LIVE:
                                if (liveItem != null) {
                                    LiveParameter liveParameter = new LiveParameter();
                                    liveParameter.setLiveId(liveItem.getId());
                                    liveParameter.setLiveTitle(liveItem.getTitle());
                                    liveParameter.setRoomId(liveItem.getLive_roomid());
                                    liveParameter.setTeacherId(liveItem.getTeacher_id());
                                    if (TextUtils.isEmpty(liveItem.getPlayback_id())) {
                                        PlayLiveActivity.intoNewIntent(getActivity(), liveParameter);
                                    } else {
                                        ReplayLiveActivity.intoNewIntent(getActivity(), liveParameter);
                                    }
                                }
                                break;
                        }
                    }
                    break;
            }

        }
    }

    private void appointment(LiveInfo liveInfo) {
        if (liveInfo == null) {
            return;
        }
        if (liveInfo.getIs_appointment() == 0) {
            AppointmentRequestUtil.addAppointment(getActivity(), liveInfo.getTeacher_id(), liveInfo.getId(), liveInfo.getTeacher_info() == null ? "" : liveInfo.getTeacher_info().getLive_roomid(), Action.add_live_appointment);
        } else {
            AppointmentRequestUtil.deleteAppointment(getActivity(), liveInfo.getId(), Action.del_live_appointment);
        }
    }

    private void dataBind() {
        if (mHomeAdapter == null) {
            mHomeAdapter = new HomeAdapter(getActivity());
            mHomeAdapter.setOnItemClickListener(new itemClick());
            rvReadingTap.setAdapter(mHomeAdapter);
        } else {
            mHomeAdapter.notifyDataSetChanged();
        }
    }

    // 添加 or 取消关注
    private void addFollow(int is_follow, String teacherId) {
        if (Constant.FOLLOWED == is_follow) {
            TeacherHttpUtil.delFollowForTeacherId(getActivity(), teacherId, Action.home_teacher_fg_del_follow);
        } else {
            TeacherHttpUtil.addFollowForTeacherID(getActivity(), teacherId, Action.home_teacher_fg_add_follow);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case off_line:
            case login:
                getHotPoint();
                break;
            case add_live_appointment:
                if (Constant.SUCCEED == event.getCode() || Constant.HAS_SUCCEED == event.getCode()) {
                    if (appointmentLiveInfo != null) {
                        tvAppointment.setSelected(true);
                        tvAppointment.setText(Constant.UN_APPOINTMENT_REMINDER);
                        appointmentLiveInfo.setIs_appointment(1);
                        tvAppointment.setTag(appointmentLiveInfo);
                    }
                } else {
                    ToastUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "预约失败"));
                }
                appointmentLiveInfo = null;
                break;
            case del_live_appointment:
                if (Constant.SUCCEED == event.getCode() || Constant.HAS_SUCCEED == event.getCode()) {
                    if (appointmentLiveInfo != null) {
                        tvAppointment.setSelected(false);
                        tvAppointment.setText(Constant.APPOINTMENT_REMINDER);
                        appointmentLiveInfo.setIs_appointment(0);
                        tvAppointment.setTag(appointmentLiveInfo);

                    }
                } else {
                    ToastUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "取消预约失败"));
                }
                appointmentLiveInfo = null;
                break;
            case home_support_dynamics:
                if (Constant.SUCCEED == event.getCode()) {
                    mTvHotPointSupport.setText(String.format(Locale.CHINA, "(%d)", mHotPoint2.getSupport_num() + 1));
                    vSupport.setSelected(true);
                    ToastUtils.showToast(getActivity(), "点赞成功");
                } else if (-2 == event.getCode()) {
                    DebugUtils.showToastResponse(getActivity(), "已支持过");
                } else {
                    LogUtils.e("点赞失败", StringUtils.replaceNullToEmpty(event.getMsg()));
                    ToastUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "点赞失败"));
                }
                break;
            case find_bannar:
                if (Constant.SUCCEED == event.getCode()) {
                    DebugUtils.dd(event.getData().toString());
                    String json = event.getData().optString("data");
                    try {
                        JSONObject object = new JSONObject(json);
                        mBannerList = new DataConverter<Banner>().JsonToListObject(object.optString("list"), new TypeToken<List<Banner>>() {
                        }.getType());

                        mHomeAdapter.setBannerList(mBannerList);
//                            mHomeAdapter.notifyItemChanged(0);
                        mHomeAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.e("FindFragment", "解析banner数据失败:" + event.getMsg());
                    }
                } else {
                    LogUtils.e("FindFragment", "获取banner数据失败:" + event.getMsg());
                }
                break;
            case hot_point:
                if (HotPointPage == 1) {
                    mHotPonitList.clear();
                }
                if (Constant.SUCCEED == event.getCode()) {
                    DebugUtils.dd(event.getData().toString());
                    String json = event.getData().optString("data");
                    try {
                        JSONObject object = new JSONObject(json);
                        List<HotPonit> tempHotPonitList = new DataConverter<HotPonit>().JsonToListObject(object.optString("list"), new TypeToken<List<HotPonit>>() {
                        }.getType());

                        if (tempHotPonitList.size() > 0) {
                            mHotPonitList.addAll(tempHotPonitList);

                            mHomeAdapter.setHotPointList(mHotPonitList);
                            try {
//                                mHomeAdapter.notifyItemChanged(4);
                                mHomeAdapter.notifyDataSetChanged();
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.e("FindFragment", "解析热门观点数据失败:" + event.getMsg());
                    }

                } else {
                    LogUtils.e("FindFragment", "获取热门观点数据失败:" + event.getMsg());
                }
                break;
            case find_home_news_list:
                if (NewsPage == 1) {
                    srlRefresh.setRefreshing(false);
                    mNewsItemList.clear();
                }
                if (Constant.SUCCEED == event.getCode()) {
                    String json = event.getData().optString("data");
                    try {
                        JSONObject object = new JSONObject(json);
                        JSONArray jsonArray = object.optJSONArray("list");
                        List<NewsItem> tempNewsList = new DataConverter<NewsItem>().JsonToListObject(jsonArray.toString(), new TypeToken<List<NewsItem>>() {
                        }.getType());
                        if (tempNewsList.size() > 0) {
                            int startIndex = mNewsItemList.size() + 5;
                            mNewsItemList.addAll(tempNewsList);
                            mHomeAdapter.setNewsList(mNewsItemList);
                            try {
                                loading = false;
//                                mHomeAdapter.notifyItemRangeInserted(startIndex, tempNewsList.size());
                                mHomeAdapter.notifyDataSetChanged();
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.e("FindFragment", "解析实时新闻数据失败:" + event.getMsg());
                    }
                } else {
                    LogUtils.e("FindFragment", "获取实时新闻数据失败:" + event.getMsg());
                }
                break;
            case home_teacher_fg_del_follow:
                if (Constant.SUCCEED == event.getCode()) {
                    mTvFollowButton.setText("+ 关注");
                    mTvFollowButton.setSelected(false);
                    HotPonit mHotPoint = (HotPonit) mTvFollowButton.getTag();
                    mHotPoint.setIs_follow(0);
                    mTvFollowButton.setTag(mHotPoint);
                } else {
                    DebugUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "取消关注失败"));
                }
                break;
            case home_teacher_fg_add_follow:
                if (Constant.SUCCEED == event.getCode()) {
                    mTvFollowButton.setText("已关注");
                    mTvFollowButton.setSelected(true);
                    HotPonit mHotPoint = (HotPonit) mTvFollowButton.getTag();
                    mHotPoint.setIs_follow(1);
                    mTvFollowButton.setTag(mHotPoint);
                } else {
                    DebugUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "关注失败"));
                }
                break;
            case live_zhi_bo_home:
                if (Constant.SUCCEED == event.getCode()) {
                    String json = event.getData().optString("data");
                    try {
                        JSONObject object = new JSONObject(json);
                        LiveInfo tempLiveItem = new DataConverter<LiveInfo>().JsonToObject(object.optString("info"), LiveInfo.class);

                        if (tempLiveItem != null) {
                            mLiveItemList.clear();
                            loading = false;
                            int size = mLiveItemList.size();
                            mLiveItemList.add(tempLiveItem);
                            mHomeAdapter.setLiveList(mLiveItemList);
                            mHomeAdapter.notifyDataSetChanged();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.showToast(getActivity(), "直播信息解析失败");
                    }
                } else {
                    ToastUtils.showToast(getActivity(), event.getMsg());
                }
                break;
        }
    }
}
