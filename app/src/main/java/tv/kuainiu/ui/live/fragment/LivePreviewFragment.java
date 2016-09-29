package tv.kuainiu.ui.live.fragment;

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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;
import tv.kuainiu.command.http.LiveHttpUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.LiveItem;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.live.adapter.ReadingTapeAdapter;
import tv.kuainiu.utils.CustomLinearLayoutManager;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.ToastUtils;

/**
 * 咨询
 */
public class LivePreviewFragment extends BaseFragment {
    private static final String ARG_POSITION = "ARG_POSITION";
    @BindView(R.id.srlRefresh) SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rvReadingTap) RecyclerView rvReadingTap;
    CustomLinearLayoutManager mLayoutManager;
    private int mParentPosition;
    private ReadingTapeAdapter mReadingTapeAdapter;
    private int page = 1;
    private boolean loading = false;
    public List<LiveItem> mLiveItemList = new ArrayList<>();

    public static LivePreviewFragment newInstance(int parentPosition) {
        LivePreviewFragment fragment = new LivePreviewFragment();
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
        return view;
    }

    @Override public void onStart() {
        super.onStart();
        initView();
        initListener();
        initData();
        LogUtils.e("LogUtils", "onStart");
    }

    @Override public void onStop() {
        super.onStop();
        LogUtils.e("LogUtils", "onStop");
    }

    private void initView() {
        srlRefresh.setColorSchemeColors(Theme.getLoadingColor());
        mLayoutManager = new CustomLinearLayoutManager(getActivity());
        rvReadingTap.setLayoutManager(mLayoutManager);

        mReadingTapeAdapter = new ReadingTapeAdapter(getActivity());
        rvReadingTap.setAdapter(mReadingTapeAdapter);

    }

    private void initData() {
        getData();
    }

    private void getData() {
        LiveHttpUtil.liveIndex(getActivity(), "2", page, Action.live_zhi_bo_yu_gao);
    }

    private void initListener() {
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case live_zhi_bo_yu_gao:

                if (Constant.SUCCEED == event.getCode()) {
                    String json = event.getData().optString("data");
                    try {
                        JSONObject object = new JSONObject(json);
                        List<LiveItem> tempLiveItemList = new DataConverter<LiveItem>().JsonToListObject(object.optString("list"), new TypeToken<List<LiveItem>>() {
                        }.getType());
                        if (page == 1) {
                            mLiveItemList.clear();
                        }
                        if (tempLiveItemList != null && tempLiveItemList.size() > 0) {
                            loading = false;
                            int size = mLiveItemList.size();
                            mLiveItemList.addAll(tempLiveItemList);
                            dataLiveListBind(size);
                        }

                    } catch (Exception e) {
                        LogUtils.e("直播预告解析失败", "直播预告解析失败" + "," + event.getData().toString(), e);
                        ToastUtils.showToast(getActivity(), "直播预告解析失败");
                    }
                } else {
                    ToastUtils.showToast(getActivity(), event.getMsg());
                }
                if (page == 1) {
                    srlRefresh.setRefreshing(false);
                }
                break;
        }
    }
}
