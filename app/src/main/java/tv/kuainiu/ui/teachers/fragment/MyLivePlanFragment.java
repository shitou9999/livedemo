package tv.kuainiu.ui.teachers.fragment;

import android.content.Context;
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
import tv.kuainiu.app.OnItemClickListener;
import tv.kuainiu.app.Theme;
import tv.kuainiu.command.dao.LiveSubscribeDao;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.AppointmentRequestUtil;
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
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;

import static tv.kuainiu.ui.live.adapter.ReadingTapeAdapter.MY_PLAN;

/**
 * 我的直播
 */
public class MyLivePlanFragment extends BaseFragment {
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
    private String teacherId;
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

    public static MyLivePlanFragment newInstance(String teacherId) {
        MyLivePlanFragment fragment = new MyLivePlanFragment();
        Bundle args = new Bundle();
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

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_live_reading_tap, container, false);
            ButterKnife.bind(this, view);
            initView();
            initListener();
            initData();
        } else {
            ViewGroup viewgroup = (ViewGroup) view.getParent();
            if (viewgroup != null) {
                viewgroup.removeView(view);
            }
        }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    TextView tvAppointment;
    int position = -1;
    LiveInfo liveInfo;

    private void initView() {
        context = getActivity();

        srlRefresh.setColorSchemeColors(Theme.getLoadingColor());
        mLayoutManager = new CustomLinearLayoutManager(getActivity());
        rvReadingTap.setLayoutManager(mLayoutManager);

        mReadingTapeAdapter = new ReadingTapeAdapter(getActivity(), MY_PLAN);
        mReadingTapeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tvAppointment:
                        if (!MyApplication.isLogin()) {
                            showLoginTip();
                            return;
                        }
                        tvAppointment = (TextView) v;
                        liveInfo = (LiveInfo) v.getTag();
                        position = (int) v.getTag(R.id.tvAppointment);
                        if (liveInfo != null) {
                            if (liveInfo.getIs_appointment() == 0) {
                                AppointmentRequestUtil.addAppointment(getActivity(), liveInfo.getTeacher_id(), liveInfo.getId(), liveInfo.getTeacher_info().getLive_roomid(), Action.add_live_appointment);
                            } else {
                                AppointmentRequestUtil.deleteAppointment(getActivity(), liveInfo.getId(), Action.del_live_appointment);
                            }
                        }
                        break;
                }

            }
        });
        rvReadingTap.setAdapter(mReadingTapeAdapter);

    }

    private void initData() {
        getData();
    }


    private void getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", MyApplication.getUser() == null ? "" : MyApplication.getUser().getUser_id());
        map.put("teacher_id", teacherId);
        map.put("live_type", "3");
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
            LiveInfo entry = mLiveItemList.get(0);
            recLen = DateUtil.secondBetweenTwoDate(DateUtil.getCurrentDate(), entry.getStart_date());
          /*
           本地提醒
           mSubscribeDao = new LiveSubscribeDao(context);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(ALARMRECEIVER_ACTION);
            intent.putExtra(AlarmReceiver.LIVE_ID, entry.getId());
            intent.putExtra(AlarmReceiver.TEACHER_ID, entry.getTeacher_info().getId());
            intent.putExtra(AlarmReceiver.LIVE_TITLE, entry.getTitle());
            intent.putExtra(AlarmReceiver.ROOM_ID, entry.getTeacher_info().getLive_roomid());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 200, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            boolean isRemind = mSubscribeDao.has(entry.getId());
            if (!isRemind) {
                boolean flag = mSubscribeDao.insert(entry.getId(), 1);
                if (flag && page == 1) {
                    LogUtils.e("AlarmReceiver", "添加直播提醒成功");
                    //提前5分钟提醒
                    alarmManager.set(AlarmManager.RTC, DateUtil.toTimestampFormStingDate(entry.getStart_date()) - ADV_TEN_MINUTES, pendingIntent);
                } else {
                    LogUtils.e("AlarmReceiver", "添加直播提醒失败");
                }


            }*/
            handler.removeCallbacks(runnable);
//            if (recLen <= 600) {
            handler.postDelayed(runnable, 0);
            rlCountdown.setVisibility(View.VISIBLE);
//            }
        } else {
            rlCountdown.setVisibility(View.GONE);
        }
        mReadingTapeAdapter.setLiveListList(mLiveItemList);
        mReadingTapeAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case add_live_appointment:
                if (Constant.SUCCEED == event.getCode() || Constant.HAS_SUCCEED == event.getCode()) {
                    if (tvAppointment != null) {
                        tvAppointment.setSelected(true);
                        tvAppointment.setText(Constant.UN_APPOINTMENT_REMINDER);
                        liveInfo.setIs_appointment(1);
                        if (position > -1 && position < mLiveItemList.size()) {
                            mLiveItemList.get(position).setIs_appointment(1);
                        }

                        tvAppointment.setTag(liveInfo);
                    }
                } else {
                    ToastUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "预约失败"));
                }
                break;
            case del_live_appointment:
                if (Constant.SUCCEED == event.getCode() || Constant.HAS_SUCCEED == event.getCode()) {
                    if (tvAppointment != null) {
                        tvAppointment.setSelected(false);
                        tvAppointment.setText(Constant.APPOINTMENT_REMINDER);
                        liveInfo.setIs_appointment(0);
                        if (position > -1 && position < mLiveItemList.size()) {
                            mLiveItemList.get(position).setIs_appointment(0);
                        }
                        tvAppointment.setTag(liveInfo);
                    }
                } else {
                    ToastUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "取消预约失败"));
                }
                break;
            case my_live_list:
                if (page == 1) {
                    mLiveItemList.clear();
                    srlRefresh.setRefreshing(false);
                }
                if (Constant.SUCCEED == event.getCode()) {
                    String json = event.getData().optString("data");
                    LogUtils.e("json", "json:" + json);
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
