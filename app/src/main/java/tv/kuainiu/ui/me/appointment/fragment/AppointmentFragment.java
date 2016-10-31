package tv.kuainiu.ui.me.appointment.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daimajia.swipe.SwipeLayout;
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
import tv.kuainiu.command.http.AppointmentRequestUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.Appointment;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.liveold.PlayLiveActivity;
import tv.kuainiu.ui.liveold.model.LiveParameter;
import tv.kuainiu.ui.me.adapter.AppointmentFragmentAdapter;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;

/**
 * 我的预约
 */
public class AppointmentFragment extends BaseFragment {
    private static final String TAG = "AppointmentFragment";

    @BindView(R.id.srlRefresh)
    SwipeRefreshLayout mSrlRefresh;
    @BindView(R.id.lvList)
    ListView lvList;
    private int page = 1;
    private List<Appointment> listData = new ArrayList<>();
    Activity context;
    private boolean loading = false;
    private ListView.OnScrollListener loadMoreListener;
    private AppointmentFragmentAdapter adapter;

    public static AppointmentFragment newInstance() {
        Bundle args = new Bundle();
        AppointmentFragment fragment = new AppointmentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Appointment mAppointment;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_appointment, container, false);
        } else {
            ViewGroup viewgroup = (ViewGroup) view.getParent();
            if (viewgroup != null) {
                viewgroup.removeView(view);
            }
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this, view);
        context = getActivity();
        initListener();
        mSrlRefresh.setColorSchemeColors(Theme.getLoadingColor());
        lvList.setOnScrollListener(loadMoreListener);
        adapter = new AppointmentFragmentAdapter(context, listData, new AppointmentFragmentAdapter.IdeletItem() {
            @Override
            public void delete(SwipeLayout swipeLayout, int position, Appointment mAppointment2) {
                mAppointment = mAppointment2;
                swipeLayout.close(true);
                AppointmentRequestUtil.deleteAppointment(context, mAppointment.getLive_id(), Action.del_live_appointment);
            }
        });
        lvList.setAdapter(adapter);
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
        loadMoreListener = new ListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                page += 1;
                fetchList();
            }
        };
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Appointment liveItem = listData.get(position);
                LiveParameter liveParameter = new LiveParameter();
                liveParameter.setLiveId(liveItem.getId());
                liveParameter.setLiveTitle(liveItem.getTitle());
                liveParameter.setRoomId(liveItem.getTeacher_info().getLive_roomid());
                liveParameter.setTeacherId(liveItem.getTeacher_info().getId());
                liveParameter.setCcid(liveItem.getPlayback_id());
                PlayLiveActivity.intoNewIntent(getActivity(), liveParameter);
            }
        });
    }

    /**
     * 预约
     */
    public void fetchList() {
        AppointmentRequestUtil.fetchList(page, context, "1", Action.live_appointment_list);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case del_live_appointment:
                if (Constant.SUCCEED == event.getCode()) {
                    if (mAppointment != null) {
                        listData.remove(mAppointment);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "删除预约信息失败"));
                }
                break;
            case live_appointment_list:
                if (page == 1) {
                    mSrlRefresh.setRefreshing(false);
                    listData.clear();
                }
                if (Constant.SUCCEED == event.getCode()) {
                    if (event.getData() != null && event.getData().has("data")) {
                        try {
                            JSONObject jsonObject = event.getData().getJSONObject("data");
                            Log.e("jsonObject", jsonObject.toString());
                            List<Appointment> tempList = new DataConverter<Appointment>().JsonToListObject(jsonObject.getString("list"), new TypeToken<List<Appointment>>() {
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
