package tv.kuainiu.ui.liveold.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.liveold.LiveHttpUtil;
import tv.kuainiu.ui.liveold.RemindUtils;
import tv.kuainiu.ui.liveold.adapter.LivePostAdapter;
import tv.kuainiu.ui.liveold.model.LiveInfoEntry;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.widget.NetErrAddLoadView;

/**
 * @author nanck on 2016/7/7.
 */
public class LiveTodayFragment extends BaseFragment {
    @BindView(R.id.recycler_live) RecyclerView mRecyclerView;
    @BindView(R.id.err_layout) NetErrAddLoadView mErrView;

    private LinearLayoutManager mLayoutManager;
    private LivePostAdapter mLivePostAdapter;
    private OnRetryRequestListener mRetryRequestListener;


    public static LiveTodayFragment newInstance(OnRetryRequestListener retryRequestListener) {
        Bundle args = new Bundle();
        LiveTodayFragment fragment = new LiveTodayFragment();
        fragment.setRetryRequestListener(retryRequestListener);
        fragment.setArguments(args);
        return fragment;
    }


    public void setRetryRequestListener(OnRetryRequestListener retryRequestListener) {
        mRetryRequestListener = retryRequestListener;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        View view = inflater.inflate(R.layout.fragment_live_today, container, false);
        ButterKnife.bind(this, view);

        Activity context = getActivity();

        mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        initListener();

        mLivePostAdapter = new LivePostAdapter(context);
        mRecyclerView.setAdapter(mLivePostAdapter);

        LiveHttpUtil.fetchLiveList(context);
        return view;
    }

    private void initListener() {
        mErrView.setOnTryCallBack(new NetErrAddLoadView.OnTryCallBack() {
            @Override
            public void callAgain() {
                LiveHttpUtil.fetchLiveList(getActivity());
                if (mRetryRequestListener != null) {
                    mRetryRequestListener.request();
                }
            }
        });
    }

    @Override public void onDetach() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDetach();
    }


    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFetchLiveList(HttpEvent event) {
        int code = event.getCode();
        if (Action.live_fetch_live_list == event.getAction()) {
            if (Constant.SUCCEED == event.getCode()) {
                DebugUtils.dd("Live list info : " + event.getData().toString());
                JsonParser parser = new JsonParser();
                JsonObject tempJson = (JsonObject) parser.parse(event.getData().toString());
                JsonObject json = tempJson.getAsJsonObject("data");
                JsonArray jsonArray = json.getAsJsonArray("list");
                if (jsonArray == null || jsonArray.size() < 1) {
                    code = Constant.NULL_FOR_LIST;
                } else {
                    List<LiveInfoEntry> mInfoEntries = new DataConverter<LiveInfoEntry>().JsonToListObject(jsonArray.toString(),
                            new TypeToken<List<LiveInfoEntry>>() {
                            }.getType());
                    // XXX
                    RemindUtils.translate(getActivity(), mInfoEntries);

                    mLivePostAdapter.setInfoEntries(mInfoEntries);
                    mLivePostAdapter.notifyDataSetChanged();
                    mLivePostAdapter.scrollToLiving(mLayoutManager);
                }
            }
            mErrView.StopLoading(code, event.getMsg());
        }
    }


    public interface OnRetryRequestListener {
        void request();
    }
}
