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
import tv.kuainiu.modle.TeacherZoneDynamics;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.friends.adapter.FriendsPostAdapter;
import tv.kuainiu.util.CustomLinearLayoutManager;
import tv.kuainiu.util.DataConverter;
import tv.kuainiu.util.StringUtils;
import tv.kuainiu.util.ToastUtils;

/**
 * 定制观点
 */
public class CustomViewPointFragment extends BaseFragment {
    private static final String TAG = "CustomViewPointFragment";

    @BindView(R.id.rv_fragment_friends_tab) RecyclerView mRecyclerView;
    @BindView(R.id.srlRefresh) SwipeRefreshLayout mSrlRefresh;
    private int page = 1;
    private List<TeacherZoneDynamics> teacherZoneDynamicsList = new ArrayList<>();
    private FriendsPostAdapter adapter;
    private String caid = "";
    Activity context;
    private boolean loading = false;
    private RecyclerView.OnScrollListener loadMoreListener;
    CustomLinearLayoutManager mLayoutManager;

    public static CustomViewPointFragment newInstance() {
        Bundle args = new Bundle();
        CustomViewPointFragment fragment = new CustomViewPointFragment();
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
        adapter = new FriendsPostAdapter(context);
        mRecyclerView.setAdapter(adapter);
        fetchTeacherDynamicsList();
        return view;
    }

    private void initListener() {
        mSrlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                page = 1;
                fetchTeacherDynamicsList();
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
                        fetchTeacherDynamicsList();
                    }
                }
            }
        };
    }

    /**
     * 动态
     */
    public void fetchTeacherDynamicsList() {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        OKHttpUtils.getInstance().post(context, Api.CUSTOM_LIST, ParamUtil.getParam(map), Action.CUSTOM_LIST, CacheConfig.getCacheConfig());
    }

    private void dataBind(int size) {
        adapter.setTeacherZoneDynamicsList(teacherZoneDynamicsList);
        adapter.notifyItemRangeInserted(size, teacherZoneDynamicsList.size());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case CUSTOM_LIST:
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