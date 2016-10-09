package tv.kuainiu.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

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
import tv.kuainiu.IGXApplication;
import tv.kuainiu.R;
import tv.kuainiu.app.OnItemClickListener;
import tv.kuainiu.app.Theme;
import tv.kuainiu.command.http.Api;
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
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.home.adapter.HomeAdapter;
import tv.kuainiu.ui.me.activity.LoginActivity;
import tv.kuainiu.utils.CustomLinearLayoutManager;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.DividerItemDecoration;
import tv.kuainiu.widget.dialog.LoginPromptDialog;

import static tv.kuainiu.R.id.tv_follow_button;
import static tv.kuainiu.modle.cons.Action.hot_point;

/**
 * 咨询
 */
public class HomeFragment extends BaseFragment {
    private static final String ARG_POSITION = "ARG_POSITION";
    @BindView(R.id.srlRefresh) SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rvReadingTap) RecyclerView rvReadingTap;

    private int mParentPosition;
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
    private boolean isShowLoginTip;

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
        if (getArguments() != null) {
            mParentPosition = getArguments().getInt(ARG_POSITION);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_live_reading_tap, container, false);
            ButterKnife.bind(this, view);
            initListener();
            initView();
            dataBind();
            initData();
        }
        ViewGroup viewgroup = (ViewGroup) view.getParent();
        if (viewgroup != null) {
            viewgroup.removeView(view);
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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
        LiveHttpUtil.liveIndex(getActivity(), "1", 1,1, Action.live_zhi_bo_home);
    }

    private void getHotPoint() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", String.valueOf(HotPointPage));
        map.put("user_id", IGXApplication.isLogin() ? IGXApplication.getUser().getUser_id() : "");
        OKHttpUtils.getInstance().syncGet(getContext(), Api.HOT_POINT + ParamUtil.getParamForGet(map), hot_point, CacheConfig.getCacheConfig());
    }

    private void getNews() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", String.valueOf(NewsPage));
        OKHttpUtils.getInstance().syncGet(getContext(), Api.FIND_NEWS_LIST + ParamUtil.getParamForGet(map), Action.find_home_news_list, CacheConfig.getCacheConfig());
    }

    private void initListener() {
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
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
    class itemClick implements OnItemClickListener {

        @Override public void onClick(View v) {
            if(!IGXApplication.isLogin()){
                showLoginTip();
                return;
            }
            switch (v.getId()) {
                case tv_follow_button:
                    HotPonit mHotPoint = (HotPonit) v.getTag();
                    mTvFollowButton = (TextView) v;
                    //关注
                    if (!IGXApplication.isLogin()) {
                        new LoginPromptDialog(getActivity()).show();
                        return;
                    } else {
                        addFollow(mHotPoint.getIs_follow(), mHotPoint.getUser_id());
                    }
                    break;
                case R.id.ll_hot_point_support:
                    vSupport=v;
                    mHotPoint2 = (HotPonit) v.getTag();
                    mTvHotPointSupport = (TextView) v.getTag(R.id.tv_hot_point_support);
                    if (!IGXApplication.isLogin()) {
                        new LoginPromptDialog(getActivity()).show();
                        return;
                    } else {
                        if(mHotPoint2.getIs_support()== Constant.FAVOURED){
                            vSupport.setSelected(true);
                            ToastUtils.showToast(getActivity(),"已经点过赞了");
                        }else{
                            SupportHttpUtil.supportDynamics(getActivity(), mHotPoint2.getId(), Action.home_support_dynamics);
                        }
                    }
                    break;
            }

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
            case home_teacher_fg_del_follow :
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
                        List<LiveInfo> tempLiveItemList = new DataConverter<LiveInfo>().JsonToListObject(object.optString("list"), new TypeToken<List<LiveInfo>>() {
                        }.getType());

                        if (tempLiveItemList != null && tempLiveItemList.size() > 0) {
                            mLiveItemList.clear();
                            loading = false;
                            int size = mLiveItemList.size();
                            mLiveItemList.addAll(tempLiveItemList);
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
    private void showLoginTip() {
        if (isShowLoginTip) {
            return;
        }
        LoginPromptDialog loginPromptDialog = new LoginPromptDialog(getActivity());
        loginPromptDialog.setCallBack(new LoginPromptDialog.CallBack() {
            @Override
            public void onCancel(DialogInterface dialog, int which) {

            }

            @Override
            public void onLogin(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                isShowLoginTip = true;
            }

            @Override
            public void onDismiss(DialogInterface dialog) {
                isShowLoginTip = false;
            }
        });
        loginPromptDialog.show();
    }
}
