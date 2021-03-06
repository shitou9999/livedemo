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
import tv.kuainiu.command.http.SupportHttpUtil;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.EmptyEvent;
import tv.kuainiu.event.HttpEvent;
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
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;

/**
 * 定制解盘视频
 */
public class CustomVideoFragment extends BaseFragment implements OnItemClickListener {
    private static final String TAG = "CustomVideoFragment";
    private static final String JIE_PAN_TYPE = "jie_pan_type";

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
    private boolean isTeacher = false;

    public static CustomVideoFragment newInstance() {
        Bundle args = new Bundle();
        CustomVideoFragment fragment = new CustomVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_friends_tab, container, false);
        }else{
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
        adapter = new FriendsPostAdapter(context,isTeacher,"");
        adapter.setOnClick(this);
        mRecyclerView.setAdapter(adapter);
        initData();
        return view;
    }

    private void initData() {
        page = 1;
        getData();
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
                        getData();
                    }
                }
            }
        };
    }

    TextView mTvFriendsPostLike;
    //    TextView mTvFriendsPostComment;
    TeacherZoneDynamics teacherZoneDynamics;
    View ivSupport;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSupport:
                ivSupport = v;
                teacherZoneDynamics = (TeacherZoneDynamics) v.getTag();
                mTvFriendsPostLike = (TextView) v.getTag(R.id.tv_friends_post_like);
                SupportHttpUtil.supportDynamics(getActivity(), String.valueOf(teacherZoneDynamics.getId()), Action.SUPPORT_DYNAMICS);
                break;
            case R.id.tv_friends_post_comment:
                teacherZoneDynamics = (TeacherZoneDynamics) v.getTag();
//                mTvFriendsPostComment = (TextView) v.getTag(R.id.tv_friends_post_comment);
                CommentListActivity.intoNewIntent(getActivity(), PostCommentListFragment.MODE_DYNAMIC, String.valueOf(teacherZoneDynamics.getId()), "");
                break;
        }
    }

    /**
     * 动态
     */
    public void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        OKHttpUtils.getInstance().post(context, Api.CUSTOM_VIDEO_LIST, ParamUtil.getParam(map), Action.CUSTOM_VIDEO_LIST);
    }

    private void dataBind(int size) {
        adapter.setTeacherZoneDynamicsList(teacherZoneDynamicsList);
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
            case SUPPORT_DYNAMICS:
                if (teacherZoneDynamics != null) {
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
                    teacherZoneDynamics = null;
                }
                break;
            case CUSTOM_VIDEO_LIST:
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
                            ToastUtils.showToast(getActivity(), "点播信息解析失败");
                        }

                    }
                } else {
                    ToastUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "获取点播信息失败"));
                }

                break;
        }
    }
}
