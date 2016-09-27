package tv.kuainiu.ui.liveold.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.loadingviewfinal.LoadMoreMode;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;
import tv.kuainiu.R;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.liveold.LiveHttpUtil;
import tv.kuainiu.ui.liveold.adapter.HistoryAdapter;
import tv.kuainiu.ui.liveold.model.History;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.widget.DividerItemDecoration;
import tv.kuainiu.widget.NetErrAddLoadView;

/**
 * @author nanck on 2016/7/7.
 */
public class LiveHistoryFragment extends BaseFragment {
    private static final String TAG_LIFE = "LiveHistoryFragment";
    @BindView(R.id.ptr_rv_layout) PtrClassicFrameLayout mPtrClassicFrameLayout;
    @BindView(R.id.rv_items) RecyclerViewFinal mRvItems;
    @BindView(R.id.err_layout) NetErrAddLoadView mErrView;

    private int mCurPageNumber = Constant.DEFAULT_PAGE_NUMBER;
    private List<History> mHistories = new ArrayList<>();
    private HistoryAdapter mAdapter;


    public static LiveHistoryFragment newInstance() {
        Bundle args = new Bundle();
        LiveHistoryFragment fragment = new LiveHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        View view = inflater.inflate(R.layout.fragment_live_history, container, false);
        ViewGroup viewgroup = (ViewGroup) view.getParent();
        if (viewgroup != null) {
            viewgroup.removeView(view);
        }
        ButterKnife.bind(this, view);
        initView();
        initListener();
        initHttp();


        return view;
    }


    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }


    private void initView() {
        mPtrClassicFrameLayout.setLastUpdateTimeRelateObject(this);
        mPtrClassicFrameLayout.disableWhenHorizontalMove(true);

        mRvItems.setLoadMoreMode(LoadMoreMode.SCROLL);
        mRvItems.setHasLoadMore(false);
        mRvItems.setNoLoadMoreHideView(true);

        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRvItems.setLayoutManager(lm);


        mAdapter = new HistoryAdapter(getActivity());
        mRvItems.setAdapter(mAdapter);

        mRvItems.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }


    private void initHttp() {
        mCurPageNumber = Constant.DEFAULT_PAGE_NUMBER;
        LiveHttpUtil.fetchLiveHistory(getActivity(), mCurPageNumber);
    }

    private void initListener() {
//        mAdapter.setOnClickListener(this);
        // 下拉刷新
        mPtrClassicFrameLayout.setOnRefreshListener(new OnDefaultRefreshListener() {

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                initHttp();
            }

        });


        mRvItems.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override public void loadMore() {
                mCurPageNumber = mCurPageNumber + 1;
                LiveHttpUtil.fetchLiveHistory(getActivity(), mCurPageNumber);
            }
        });

        mErrView.setOnTryCallBack(new NetErrAddLoadView.OnTryCallBack() {
            @Override
            public void callAgain() {
                initHttp();
            }
        });

    }

    /**
     * 刷新完成
     *
     * @throws NullPointerException
     */
    private void stopRefresh() throws NullPointerException {
        if (mCurPageNumber == 1) {
            mPtrClassicFrameLayout.onRefreshComplete();
        } else {
            mRvItems.onLoadMoreComplete();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMain(HttpEvent event) {
        if (Action.live_history == event.getAction()) {
            stopRefresh();
            if (Constant.SUCCEED == event.getCode()) {
                DebugUtils.dd("hhhhhhhhh : " + event.getData());
                JsonParser parser = new JsonParser();
                JsonObject tempJson = (JsonObject) parser.parse(event.getData().toString());
                JsonObject json = tempJson.getAsJsonObject("data");
                JsonArray jsonArray = json.getAsJsonArray("list");
                if (jsonArray.size() > 0) {
                    mHistories = new DataConverter<History>().JsonToListObject(jsonArray.toString(),
                            new TypeToken<List<History>>() {
                            }.getType());
                }
                if (mHistories.size() >= 15) {
                    mRvItems.setHasLoadMore(true);
                } else {
                    mRvItems.setNoLoadMoreHideView(false);
                }
                mAdapter.setHistories(mHistories);
                mAdapter.notifyDataSetChanged();
            }
            mErrView.StopLoading(event.getCode(), event.getMsg());
        }
    }
}
