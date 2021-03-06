package tv.kuainiu.ui.friends.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import tv.kuainiu.R;
import tv.kuainiu.app.OnItemClickListener;
import tv.kuainiu.app.Theme;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.AppointmentRequestUtil;
import tv.kuainiu.command.http.SupportHttpUtil;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.EmptyEvent;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.LiveInfo;
import tv.kuainiu.modle.TeacherZoneDynamics;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.comments.CommentListActivity;
import tv.kuainiu.ui.comments.fragmet.PostCommentListFragment;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.friends.adapter.FriendsPostAdapter;
import tv.kuainiu.widget.LayoutManager.CustomLinearLayoutManager;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.MediaPlayUtil;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;

/**
 * 定制老师
 */
public class CustomTeacherFragment extends BaseFragment implements OnItemClickListener {
    private static final String TAG = "CustomTeacherFragment";

    @BindView(R.id.rv_fragment_friends_tab)
    RecyclerView mRecyclerView;
    @BindView(R.id.srlRefresh)
    SwipeRefreshLayout mSrlRefresh;
    private int page = 1;
    private List<TeacherZoneDynamics> teacherZoneDynamicsList = new ArrayList<>();
    private FriendsPostAdapter adapter;
    private String caid = "";
    Activity context;
    private boolean loading = false;
    private RecyclerView.OnScrollListener loadMoreListener;
    CustomLinearLayoutManager mLayoutManager;

    public static CustomTeacherFragment newInstance() {
        Bundle args = new Bundle();
        CustomTeacherFragment fragment = new CustomTeacherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_friends_tab, container, false);
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
        mLayoutManager = new CustomLinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSrlRefresh.setColorSchemeColors(Theme.getLoadingColor());
        mRecyclerView.addOnScrollListener(loadMoreListener);
        adapter = new FriendsPostAdapter(context, false,"");
        adapter.setOnClick(this);
        mRecyclerView.setAdapter(adapter);
        initData();
        return view;
    }

    private void initData() {
        page = 1;
        fetchTeacherDynamicsList();
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
                        fetchTeacherDynamicsList();
                    }
                }
            }
        };
    }

    TextView mTvFriendsPostLike;
    TeacherZoneDynamics teacherZoneDynamics;
    View ivSupport;
    TextView tvAppointment;
    LiveInfo liveInfo;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSupport:
                ivSupport = v;
                teacherZoneDynamics = (TeacherZoneDynamics) v.getTag();
                mTvFriendsPostLike = (TextView) v.getTag(R.id.tv_friends_post_like);
                SupportHttpUtil.supportDynamics(getActivity(), String.valueOf(teacherZoneDynamics.getId()), Action.SUPPORT_DYNAMICS_teacher);
                break;
            case R.id.tv_friends_post_comment:
                teacherZoneDynamics = (TeacherZoneDynamics) v.getTag();
//                mTvFriendsPostComment = (TextView) v.getTag(R.id.tv_friends_post_comment);
                CommentListActivity.intoNewIntent(getActivity(), PostCommentListFragment.MODE_DYNAMIC, String.valueOf(teacherZoneDynamics.getId()), "");
                break;
            case R.id.tvAppointment:
                liveInfo = (LiveInfo) v.getTag();
                tvAppointment = (TextView) v;
                appointment(liveInfo);
                break;
        }
    }

    private void appointment(LiveInfo liveInfo) {
        if (liveInfo == null) {
            return;
        }
        if (liveInfo.getIs_appointment() == 0) {
            AppointmentRequestUtil.addAppointment(getActivity(), liveInfo.getTeacher_id(), liveInfo.getId(), liveInfo.getLive_roomid(), Action.add_live_appointment);
        } else {
            AppointmentRequestUtil.deleteAppointment(getActivity(), liveInfo.getId(), Action.del_live_appointment);
        }
    }

    /**
     * 动态
     */
    public void fetchTeacherDynamicsList() {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        OKHttpUtils.getInstance().post(context, Api.custom_teacher_list, ParamUtil.getParam(map), Action.custom_teacher_list);
    }

    private void dataBind(int size) {
        adapter.setTeacherZoneDynamicsList(teacherZoneDynamicsList);
//        adapter.notifyItemRangeInserted(size, teacherZoneDynamicsList.size());
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(EmptyEvent event) {
        switch (event.getAction()) {
            case live_teacher_need_refresh:
                initData();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case off_line:
            case login:
                initData();
                break;
            case add_live_appointment:
                if (Constant.SUCCEED == event.getCode() || Constant.HAS_SUCCEED == event.getCode()) {
                    if (liveInfo != null) {
                        tvAppointment.setSelected(true);
                        tvAppointment.setText("预约");
                        liveInfo.setIs_appointment(1);
                        tvAppointment.setTag(liveInfo);
                    }
                } else {
                    ToastUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "预约失败"));
                }
                liveInfo = null;
                break;
            case del_live_appointment:
                if (Constant.SUCCEED == event.getCode() || Constant.HAS_SUCCEED == event.getCode()) {
                    if (liveInfo != null) {
                        tvAppointment.setSelected(false);
                        tvAppointment.setText("预约");
                        liveInfo.setIs_appointment(0);
                        tvAppointment.setTag(liveInfo);

                    }
                } else {
                    ToastUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "取消预约失败"));
                }
                liveInfo = null;
                break;
            case SUPPORT_DYNAMICS_teacher:
                if (Constant.SUCCEED == event.getCode()) {
                    ivSupport.setVisibility(View.INVISIBLE);
                    mTvFriendsPostLike.setText(String.format(Locale.CHINA, "(%d)", teacherZoneDynamics.getSupport_num() + 1));
                    mTvFriendsPostLike.setSelected(true);
                    ToastUtils.showToast(getActivity(), "点赞成功");
                } else if (-2 == event.getCode()) {
                    DebugUtils.showToastResponse(getActivity(), "已支持过");
                } else {
                    LogUtils.e("点赞失败", StringUtils.replaceNullToEmpty(event.getMsg()));
                    ToastUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "点赞失败"));
                }
                break;
            case custom_teacher_list:
                if (page == 1) {
                    mSrlRefresh.setRefreshing(false);
                    teacherZoneDynamicsList.clear();
                }
                if (Constant.SUCCEED == event.getCode()) {
                    if (event.getData() != null && event.getData().has("data")) {
                        try {
                            JSONObject jsonObject = event.getData().getJSONObject("data");
                            Log.e("jsonObject", jsonObject.toString());
                            List<TeacherZoneDynamics> tempTeacherZoneDynamicsList = new DataConverter<TeacherZoneDynamics>().JsonToListObject(jsonObject.getString("list"), new TypeToken<List<TeacherZoneDynamics>>() {
                            }.getType());
                            if (tempTeacherZoneDynamicsList != null && tempTeacherZoneDynamicsList.size() > 0) {
                                loading = false;
                                int size = teacherZoneDynamicsList.size();
                                teacherZoneDynamicsList.addAll(tempTeacherZoneDynamicsList);
                                dataBind(size);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showToast(getActivity(), "观点信息解析失败");
                        }

                    }
                } else {
                    ToastUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "获取观点信息失败"));
                }
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MediaPlayUtil mMediaPlayUtil = MediaPlayUtil.getInstance();
        if (mMediaPlayUtil.isPlaying()) {
            mMediaPlayUtil.stop();
        }
    }
}
