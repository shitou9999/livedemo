package tv.kuainiu.ui.me.appointment.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.app.OnItemClickListener;
import tv.kuainiu.app.Theme;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.LiveInfo;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.liveold.ReplayLiveActivity;
import tv.kuainiu.ui.liveold.model.LiveParameter;
import tv.kuainiu.ui.me.adapter.MyLiveHistoryFragmentAdapter;
import tv.kuainiu.utils.CustomLinearLayoutManager;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;


/**
 * 我的直播回放
 */
public class MyLiveHistoryFragment extends BaseFragment {
    private static final String TAG = "MyLiveHistoryFragment";

    @BindView(R.id.srlRefresh)
    SwipeRefreshLayout mSrlRefresh;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private int page = 1;
    private List<LiveInfo> listData = new ArrayList<>();
    Activity context;
    private MyLiveHistoryFragmentAdapter adapter;
    private String teacherId;
    private boolean loading = false;
    private RecyclerView.OnScrollListener loadMoreListener;
    CustomLinearLayoutManager mLayoutManager;

    public static MyLiveHistoryFragment newInstance(String teacherId) {
        Bundle args = new Bundle();
        MyLiveHistoryFragment fragment = new MyLiveHistoryFragment();
        args.putString(TEACHER_ID, teacherId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            teacherId = getArguments().getString(TEACHER_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_live_history, container, false);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this, view);
        context = getActivity();
        initListener();
        mSrlRefresh.setColorSchemeColors(Theme.getLoadingColor());
        mLayoutManager = new CustomLinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(loadMoreListener);
        adapter = new MyLiveHistoryFragmentAdapter(context, listData, new OnItemClickListener() {
            @Override
            public void onClick(View v) {
                LiveInfo liveItem = (LiveInfo) v.getTag();
                LiveParameter liveParameter = new LiveParameter();
                liveParameter.setLiveId(liveItem.getId());
                liveParameter.setLiveTitle(liveItem.getTitle());
                liveParameter.setRoomId(liveItem.getTeacher_info().getLive_roomid());
                liveParameter.setTeacherId(liveItem.getTeacher_info().getId());
                liveParameter.setCcid(liveItem.getPlayback_id());
                ReplayLiveActivity.intoNewIntent(getActivity(), liveParameter);
            }
        });
        mRecyclerView.setAdapter(adapter);
        initData();
        return view;
    }

    private void initData() {
        page = 1;
        fetchList();
    }

    private void initListener() {
        mSrlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
                        page += 1;
                        fetchList();
                    }
                }
            }
        };
    }

    /**
     * 预约
     */
    public void fetchList() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", MyApplication.getUser() == null ? "" : MyApplication.getUser().getUser_id());
        map.put("teacher_id", teacherId);
        map.put("live_type", "2");
        map.put("page", String.valueOf(page));
        OKHttpUtils.getInstance().syncGet(context, Api.my_live_list + ParamUtil.getParamForGet(map), Action.my_live_list_history);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
//            case del_live_appointment_history:
//                if (Constant.SUCCEED == event.getCode()) {
//                    if (mAppointment != null) {
//                        listData.remove(mAppointment);
//                        adapter.notifyDataSetChanged();
//                    }
//                } else {
//                    ToastUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "删除历史预约信息失败"));
//                }
//                break;
            case my_live_list_history:
                if (page == 1) {
                    mSrlRefresh.setRefreshing(false);
                    listData.clear();
                }
                if (Constant.SUCCEED == event.getCode()) {
                    if (event.getData() != null && event.getData().has("data")) {
                        try {
                            JSONObject jsonObject = event.getData().getJSONObject("data");
                            Log.e("jsonObject", jsonObject.toString());
                            List<LiveInfo> tempList = new DataConverter<LiveInfo>().JsonToListObject(jsonObject.getString("live_list"), new TypeToken<List<LiveInfo>>() {
                            }.getType());
                            if (tempList != null && tempList.size() > 0) {
                                loading = false;
                                listData.addAll(tempList);
                                adapter.notifyDataSetChanged();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showToast(getActivity(), "预约信息解析失败");
                        }

                    }
                } else {
                    ToastUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "获取预约信息失败"));
                }
                break;
        }
    }


}
