package tv.kuainiu.ui.friends.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.CacheConfig;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.modle.push.CustomVideo;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.friends.adapter.FriendsPostAdapter;
import tv.kuainiu.util.CustomLinearLayoutManager;
import tv.kuainiu.util.DataConverter;
import tv.kuainiu.util.StringUtils;
import tv.kuainiu.util.ToastUtils;

/**
 * 定制观点
 */
public class CustomVideoFragment extends BaseFragment {
    private static final String TAG = "CustomVideoFragment";

    @BindView(R.id.rv_fragment_friends_tab) RecyclerView mRecyclerView;
    @BindView(R.id.srlRefresh) SwipeRefreshLayout mSrlRefresh;
    private int page = 1;
    private List<CustomVideo> customVideoList = new ArrayList<>();
    private FriendsPostAdapter adapter;
    private String caid = "";
    Activity context;
    private boolean loading = false;
    private RecyclerView.OnScrollListener loadMoreListener;
    CustomLinearLayoutManager mLayoutManager;

    public static CustomVideoFragment newInstance() {
        Bundle args = new Bundle();
        CustomVideoFragment fragment = new CustomVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_tab, container, false);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this, view);
        context = getActivity();
        initListener();
        mLayoutManager = new CustomLinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSrlRefresh.setColorSchemeColors(Theme.getLoadingColor());
        mRecyclerView.addOnScrollListener(loadMoreListener);
        adapter = new FriendsPostAdapter(context, FriendsPostAdapter.CUSTOM_VIDEO);
        mRecyclerView.setAdapter(adapter);
        getData();
        return view;
    }

    private void initListener() {
        mSrlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                page = 1;
                getData();
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
                        page += 1;
                        getData();
                    }
                }
            }
        };
    }

    /**
     * 动态
     */
    public void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        OKHttpUtils.getInstance().post(context, Api.CUSTOM_VIDEO_LIST, ParamUtil.getParam(map), Action.CUSTOM_VIDEO_LIST, CacheConfig.getCacheConfig());
    }

    private void dataBind(int size) {
        adapter.setCustomVideoList(customVideoList);
        adapter.notifyItemRangeInserted(size, customVideoList.size());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case CUSTOM_VIDEO_LIST:
                if (page == 1) {
                    mSrlRefresh.setRefreshing(false);
                }
                if (Constant.SUCCEED == event.getCode()) {
                    if (event.getData() != null && event.getData().has("data")) {
                        try {
                            JSONObject jsonObject = event.getData().getJSONObject("data");
                            List<CustomVideo> tempCustomVideoList = new DataConverter<CustomVideo>().JsonToListObject(jsonObject.getString("list"), new TypeToken<List<CustomVideo>>() {
                            }.getType());
                            if (page == 1) {
                                customVideoList.clear();
                            }
                            if (tempCustomVideoList != null && tempCustomVideoList.size() > 0) {
                                loading = false;
                                int size = customVideoList.size();
                                customVideoList.addAll(tempCustomVideoList);
                                dataBind(size);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showToast(getActivity(), "老师信息解析失败");
                        }

                    }
                } else {
                    ToastUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "获取老师信息失败"));
                }
                break;
        }
    }

}