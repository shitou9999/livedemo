package tv.kuainiu.ui.me.appointment.fragment;

import android.content.Context;
import android.os.Bundle;
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
import tv.kuainiu.IGXApplication;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.LiveInfo;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.live.adapter.ReadingTapeAdapter;
import tv.kuainiu.utils.CustomLinearLayoutManager;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.ToastUtils;

import static tv.kuainiu.ui.live.adapter.ReadingTapeAdapter.ZHI_BO;

/**
 * 我的直播
 */
public class MyLiveFragment extends BaseFragment {
    private static final String ARG_POSITION = "ARG_POSITION";

    @BindView(R.id.srlRefresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rvReadingTap)
    RecyclerView rvReadingTap;
    CustomLinearLayoutManager mLayoutManager;
    private int mParentPosition;
    private ReadingTapeAdapter mReadingTapeAdapter;
    private int page = 1;
    private boolean loading = false;
    public List<LiveInfo> mLiveItemList = new ArrayList<>();
    public List<LiveInfo> mLiveHuiFangItemList = new ArrayList<>();
    private Context context;

    public static MyLiveFragment newInstance(int parentPosition) {
        MyLiveFragment fragment = new MyLiveFragment();
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

    View view;

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

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        initListener();
        initData();
    }

    private void initView() {
        context=getActivity();
        srlRefresh.setColorSchemeColors(Theme.getLoadingColor());
        mLayoutManager = new CustomLinearLayoutManager(getActivity());
        rvReadingTap.setLayoutManager(mLayoutManager);

        mReadingTapeAdapter = new ReadingTapeAdapter(getActivity(), ZHI_BO);
        rvReadingTap.setAdapter(mReadingTapeAdapter);

    }

    private void initData() {
        getData();
    }


    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("teacher_id", IGXApplication.getUser().getUser_id());
        map.put("live_type", "1");
        map.put("page", String.valueOf(page));
        OKHttpUtils.getInstance().post(context, Api.my_live_list, ParamUtil.getParam(map), Action.my_live_list);
    }

    private void initListener() {
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getData();
            }
        });
        rvReadingTap.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        });
    }

    private void dataLiveListBind(int size) {
        mReadingTapeAdapter.setLiveListList(mLiveItemList);
//        mReadingTapeAdapter.notifyItemRangeInserted(size, mLiveItemList.size());
        mReadingTapeAdapter.notifyDataSetChanged();
    }

    private void dataLiveBind() {
        mReadingTapeAdapter.setLiveList(mLiveHuiFangItemList);
//        mReadingTapeAdapter.notifyItemRangeInserted(size, mLiveItemList.size());
        mReadingTapeAdapter.notifyDataSetChanged();
    }

    private void dataBannerBind() {
        mReadingTapeAdapter.setBannerList(mLiveItemList);
//        mReadingTapeAdapter.notifyItemRangeInserted(size, mLiveItemList.size());
        mReadingTapeAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case my_live_list:
                if (page == 1) {
                    mLiveItemList.clear();
                    srlRefresh.setRefreshing(false);
                }
                if (Constant.SUCCEED == event.getCode()) {
                    String json = event.getData().optString("data");
                    try {
                        JSONObject object = new JSONObject(json);
                        List<LiveInfo> tempLiveItemList = new DataConverter<LiveInfo>().JsonToListObject(object.optString("live_list"), new TypeToken<List<LiveInfo>>() {
                        }.getType());

                        if (tempLiveItemList != null && tempLiveItemList.size() > 0) {
                            loading = false;
                            int size = mLiveItemList.size();
                            mLiveItemList.addAll(tempLiveItemList);
//                            dataBannerBind();
                            dataLiveListBind(size);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.showToast(getActivity(), "直播列表解析失败");
                    }
                } else {
                    ToastUtils.showToast(getActivity(), event.getMsg());
                }
                break;
            case live_hui_fang_kan_pan:
                if (page == 1) {
                    mLiveHuiFangItemList.clear();
                    srlRefresh.setRefreshing(false);
                }
                if (Constant.SUCCEED == event.getCode()) {
                    String json = event.getData().optString("data");
                    try {
                        JSONObject object = new JSONObject(json);
                        List<LiveInfo> tempLiveItemList = new DataConverter<LiveInfo>().JsonToListObject(object.optString("list"), new TypeToken<List<LiveInfo>>() {
                        }.getType());

                        if (tempLiveItemList != null && tempLiveItemList.size() > 0) {
                            int size = mLiveHuiFangItemList.size();
                            mLiveHuiFangItemList.addAll(tempLiveItemList);
                            dataLiveBind();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.showToast(getActivity(), "直播回放解析失败");
                    }
                } else {
                    ToastUtils.showToast(getActivity(), event.getMsg());
                }
                break;
        }
    }
}
