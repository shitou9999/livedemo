package tv.kuainiu.ui.me.appointment.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
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
import tv.kuainiu.app.ISwipeDeleteItemClickListening;
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
import tv.kuainiu.ui.me.adapter.MyLiveFragmentAdapter;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;

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
    ListView rvReadingTap;
    //    CustomLinearLayoutManager mLayoutManager;
    private String teacherId;
    //    private ReadingTapeAdapter mReadingTapeAdapter;
    private int page = 1;
    private MyLiveFragmentAdapter myLiveFragmentAdapter;
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

    public static MyLiveFragment newInstance(String teacherId) {
        MyLiveFragment fragment = new MyLiveFragment();
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
            view = inflater.inflate(R.layout.fragment_my_live, container, false);
        } else {
            ViewGroup viewgroup = (ViewGroup) view.getParent();
            if (viewgroup != null) {
                viewgroup.removeView(view);
            }
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
        context = getActivity();

        srlRefresh.setColorSchemeColors(Theme.getLoadingColor());
//        mLayoutManager = new CustomLinearLayoutManager(getActivity());
//        rvReadingTap.setLayoutManager(mLayoutManager);

//        mReadingTapeAdapter = new ReadingTapeAdapter(getActivity(), MY_LIVE);
//        rvReadingTap.setAdapter(mReadingTapeAdapter);

    }

    private void initData() {
        getData();
    }


    private void getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", MyApplication.getUser() == null ? "" : MyApplication.getUser().getUser_id());
        map.put("teacher_id", teacherId);
        map.put("live_type", "3");
        map.put("has_living", "1");
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
        rvReadingTap.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!loading && visibleItemCount + firstVisibleItem == totalItemCount - 1) {
                    loading = true;
                    page += 1;
                    getData();
                }
            }
        });
//        rvReadingTap.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0) //向下滚动
//                {
//                    int visibleItemCount = mLayoutManager.getChildCount();
//                    int totalItemCount = mLayoutManager.getItemCount();
//                    int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
//
//                    if (!loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                        loading = true;
//                        page += 1;
//                        getData();
//                    }
//                }
//            }
//        });
    }

    private LiveInfo deleteLiveInfo;

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
        if (myLiveFragmentAdapter == null) {
            myLiveFragmentAdapter = new MyLiveFragmentAdapter(getActivity(), mLiveItemList, new ISwipeDeleteItemClickListening() {
                @Override
                public void delete(SwipeLayout swipeLayout, int position, Object object) {
                    //删除
                    deleteLiveInfo = (LiveInfo) object;
                    deleteLive(swipeLayout, deleteLiveInfo);
                }
            });
            rvReadingTap.setAdapter(myLiveFragmentAdapter);
        } else {
            myLiveFragmentAdapter.notifyDataSetChanged();
        }
    }

    private void deleteLive(SwipeLayout swipeLayout, final LiveInfo liveInfo) {
        swipeLayout.close(true);
        if (liveInfo == null) {
            return;
        }
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("是否删除该直播计划？")
                .setMessage(StringUtils.replaceNullToEmpty(liveInfo.getTitle()))
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, String> map = new HashMap<>();
                        map.put("id", liveInfo.getId());
                        OKHttpUtils.getInstance().post(getActivity(), Api.del_news, ParamUtil.getParam(map), Action.del_live);
                        dialog.dismiss();
                    }
                });
        mBuilder.create().show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case del_live:
                if (deleteLiveInfo != null) {
                    mLiveItemList.remove(deleteLiveInfo);
                    dataLiveListBind(0);
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
