package tv.kuainiu.ui.me.appointment.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;
import tv.kuainiu.command.dao.LiveSubscribeDao;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.LiveInfo;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.live.adapter.ReadingTapeAdapter;
import tv.kuainiu.ui.liveold.AlarmReceiver;
import tv.kuainiu.utils.CustomLinearLayoutManager;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.ToastUtils;

import static tv.kuainiu.ui.live.adapter.ReadingTapeAdapter.MY_LIVE;

/**
 * 我的直播
 */
public class MyLiveFragment extends BaseFragment {
    private static final String ARG_POSITION = "ARG_POSITION";
    @BindView(R.id.rlCountdown)
    RelativeLayout rlCountdown;
    @BindView(R.id.tvCountdown)
    TextView tvCountdown;
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
    private Context context;
    private LiveSubscribeDao mSubscribeDao;
    /**
     * 倒计时事件秒
     */
    private int recLen = 120;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (recLen > 0) {
                recLen--;
                int hour = recLen / 3600;
                int Minute = recLen % 3600;
                int minute = Minute / 60;
                int second = Minute % 60;
                tvCountdown.setText(String.format(Locale.CHINA, "%02d:%02d:%02d", hour, minute, second));
                handler.postDelayed(this, 1000);
            } else {
                tvCountdown.setText("直播时间到");
            }
        }
    };

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
//        if (view == null) {
        view = inflater.inflate(R.layout.fragment_live_reading_tap, container, false);
//        }
//        ViewGroup viewgroup = (ViewGroup) view.getParent();
//        if (viewgroup != null) {
//            viewgroup.removeView(view);
//        }
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
        context = getActivity();

        srlRefresh.setColorSchemeColors(Theme.getLoadingColor());
        mLayoutManager = new CustomLinearLayoutManager(getActivity());
        rvReadingTap.setLayoutManager(mLayoutManager);

        mReadingTapeAdapter = new ReadingTapeAdapter(getActivity(), MY_LIVE);
        rvReadingTap.setAdapter(mReadingTapeAdapter);

    }

    private void initData() {
        getData();
    }


    private void getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", MyApplication.getUser().getUser_id());
        map.put("teacher_id", MyApplication.getUser().getUser_id());
        map.put("live_type", "1");
        map.put("page", String.valueOf(page));
        OKHttpUtils.getInstance().syncGet(context, Api.my_live_list + ParamUtil.getParamForGet(map), Action.my_live_list);
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
        if (mLiveItemList.size() > 0) {
            LiveInfo entry= mLiveItemList.get(0);
            recLen = DateUtil.secondBetweenTwoDate(DateUtil.getCurrentDate(),entry.getStart_date());
            mSubscribeDao = new LiveSubscribeDao(context);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context.getApplicationContext(), AlarmReceiver.class);
            intent.putExtra("id", entry.getId());
            intent.putExtra("title", entry.getTitle());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 200, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            boolean isRemind=mSubscribeDao.has(entry.getId());
            if (!isRemind) {
                boolean flag = mSubscribeDao.insert(entry.getId(), 1);
                if (flag) {
                    alarmManager.set(AlarmManager.RTC, recLen*1000, pendingIntent);
                } else {
                    DebugUtils.showToast(context, "添加直播提醒失败，请稍后再试");
                }


            }
            handler.removeCallbacks(runnable);
//            if (recLen <= 600) {
            handler.postDelayed(runnable, 0);
            rlCountdown.setVisibility(View.VISIBLE);
//            }
        } else {
            rlCountdown.setVisibility(View.GONE);
        }
        mReadingTapeAdapter.setLiveListList(mLiveItemList);
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
        }
    }
}
