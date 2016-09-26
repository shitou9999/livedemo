package tv.kuainiu.ui.home;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.IGXApplication;
import tv.kuainiu.R;
import tv.kuainiu.app.OnItemClickListener;
import tv.kuainiu.app.Theme;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.TeacherHttpUtil;
import tv.kuainiu.command.http.core.CacheConfig;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.Banner;
import tv.kuainiu.modle.HotPonit;
import tv.kuainiu.modle.NewsItem;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.home.adapter.HomeAdapter;
import tv.kuainiu.utils.CustomLinearLayoutManager;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.widget.DividerItemDecoration;
import tv.kuainiu.widget.dialog.LoginPromptDialog;

import static tv.kuainiu.R.id.tv_follow_button;

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
        if (getArguments() != null) {
            mParentPosition = getArguments().getInt(ARG_POSITION);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_live_reading_tap, container, false);
        }
        ViewGroup viewgroup = (ViewGroup) view.getParent();
        if (viewgroup != null) {
            viewgroup.removeView(view);
        }
        ButterKnife.bind(this, view);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initListener();
        initView();
        dataBind();
        initData();


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
        OKHttpUtils.getInstance().post(getActivity(), Api.TEST_DNS_API_HOST, Api.FIND_BANNAR, ParamUtil.getParam(null), Action.find_bannar);
    }

    private void getHotPoint() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", String.valueOf(HotPointPage));
        map.put("user_id", IGXApplication.isLogin() ? IGXApplication.getUser().getUser_id() : "");
        OKHttpUtils.getInstance().syncGet(getContext(), Api.HOT_POINT + ParamUtil.getParamForGet(map), Action.hot_point, CacheConfig.getCacheConfig());
    }

    private void getNews() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", String.valueOf(NewsPage));
        OKHttpUtils.getInstance().syncGet(getContext(), Api.FIND_NEWS_LIST + ParamUtil.getParamForGet(map), Action.find_news_list, CacheConfig.getCacheConfig());
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

    class itemClick implements OnItemClickListener {

        @Override public void onClick(View v) {
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
                    HotPonit mHotPoint2 = (HotPonit) v.getTag();
                    mTvHotPointSupport = (TextView) v.getTag(R.id.tv_hot_point_support);
                    if (!IGXApplication.isLogin()) {
                        new LoginPromptDialog(getActivity()).show();
                        return;
                    } else {
                        addFollow(mHotPoint2.getIs_follow(), mHotPoint2.getUser_id());
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
            TeacherHttpUtil.delFollowForTeacherId(getActivity(), teacherId, Action.teacher_fg_del_follow);
        } else {
            TeacherHttpUtil.addFollowForTeacherID(getActivity(), teacherId, Action.teacher_fg_add_follow);
        }
    }

    // 添加 or 取消 赞
    private void addSupport(int is_support, String teacherId) {
        if (Constant.FAVOURED == is_support) {
            TeacherHttpUtil.delFollowForTeacherId(getActivity(), teacherId, Action.teacher_fg_del_follow);
        } else {
            TeacherHttpUtil.addFollowForTeacherID(getActivity(), teacherId, Action.teacher_fg_add_follow);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case find_bannar:
                if (Constant.SUCCEED == event.getCode()) {
                    DebugUtils.dd(event.getData().toString());
                    String json = event.getData().optString("data");
                    try {
                        JSONObject object = new JSONObject(json);
                        mBannerList = new DataConverter<Banner>().JsonToListObject(object.optString("list"), new TypeToken<List<Banner>>() {
                        }.getType());

                        mHomeAdapter.setBannerList(mBannerList);
                        try {
                            mHomeAdapter.notifyItemChanged(0);
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    LogUtils.e("FindFragment", "获取banner数据失败:" + event.getMsg());
                }
                break;
            case hot_point:
                if (Constant.SUCCEED == event.getCode()) {
                    DebugUtils.dd(event.getData().toString());

                    String json = event.getData().optString("data");
                    try {
                        JSONObject object = new JSONObject(json);
                        List<HotPonit> tempHotPonitList = new DataConverter<HotPonit>().JsonToListObject(object.optString("list"), new TypeToken<List<HotPonit>>() {
                        }.getType());
                        if (HotPointPage == 1) {
                            mHotPonitList.clear();
                        }
                        if (tempHotPonitList.size() > 0) {
                            mHotPonitList.addAll(tempHotPonitList);

                            mHomeAdapter.setHotPointList(mHotPonitList);
                            try {
                                mHomeAdapter.notifyItemChanged(4);
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    LogUtils.e("FindFragment", "获取热门观点数据失败:" + event.getMsg());
                }
                break;
            case find_news_list:
                if (NewsPage == 1) {
                    srlRefresh.setRefreshing(false);
                }
                if (Constant.SUCCEED == event.getCode()) {
                    String json = event.getData().optString("data");
                    try {
                        JSONObject object = new JSONObject(json);
                        JSONArray jsonArray = object.optJSONArray("list");
                        List<NewsItem> tempNewsList = new DataConverter<NewsItem>().JsonToListObject(jsonArray.toString(), new TypeToken<List<NewsItem>>() {
                        }.getType());
                        if (NewsPage == 1) {
                            mNewsItemList.clear();
                        }
                        if (tempNewsList.size() > 0) {
                            int startIndex = mNewsItemList.size() + 5;
                            mNewsItemList.addAll(tempNewsList);
                            mHomeAdapter.setNewsList(mNewsItemList);
                            try {
                                loading = false;
                                mHomeAdapter.notifyItemRangeInserted(startIndex, tempNewsList.size());
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    LogUtils.e("FindFragment", "获取实时新闻数据失败:" + event.getMsg());
                }
                break;
            case teacher_fg_del_follow:
                if (Constant.SUCCEED == event.getCode()) {
                    mTvFollowButton.setText("+关注");
                    mTvFollowButton.setSelected(false);
                } else {
                    DebugUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "取消关注失败"));
                }
                break;
            case teacher_fg_add_follow:
                if (Constant.SUCCEED == event.getCode()) {
                    mTvFollowButton.setText("已关注");
                    mTvFollowButton.setSelected(true);
                } else {
                    DebugUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "关注失败"));
                }
                break;
        }
    }
}
