package tv.kuainiu.teachers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import tv.kuainiu.IGXApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.TeacherHttpUtil;
import tv.kuainiu.event.EmptyEvent;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.TeacherItem;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.teachers.adapter.SimpleAdapter;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.util.DataConverter;
import tv.kuainiu.util.DebugUtils;
import tv.kuainiu.util.MeasureUtil;
import tv.kuainiu.util.PreferencesUtils;
import tv.kuainiu.util.StringUtils;
import tv.kuainiu.widget.NetErrAddLoadView;
import tv.kuainiu.widget.TitleBarView;
import tv.kuainiu.widget.dialog.LoginPromptDialog;

/**
 * @author nanck on 2016/7/29.
 */
public class TeachersFragment extends BaseFragment  implements SimpleAdapter.OnClickListener {
    private static final String ARG_COLOR = "ARG_COLOR";
    private static final String ARG_CONTENT = "ARG_CONTENT";

    @BindView(R.id.tbv_title) TitleBarView tbv_title;

    @BindView(R.id.recyclerView) RecyclerView mRvItems;
    @BindView(R.id.err_layout) NetErrAddLoadView mErrView;

    private SimpleAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private boolean mIsLogin = false;
//    private ProgramItem tempPro;
//
//    List<ProgramItem> mProgramList = new ArrayList<>();
    List<TeacherItem> mTeacherLIst = new ArrayList<>();
    IntentFilter intentFilter;

    private boolean mIsFirstIntoLogin = false;
    //    private boolean mIsLiveChild = false;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver mReceiver;

    /**
     * Temp view holder
     */
    private TextView tempTextViewCount;
    private TextView tempTextViewOperation;

    private TeacherItem tempTeacher;
    private TextView tempCheckBox;
    public static TeachersFragment newInstance() {
        TeachersFragment fragment = new TeachersFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLOR, pageColor);
//        args.putString(ARG_CONTENT, pageContent);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teachers, container, false);
        ButterKnife.bind(this, view);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initVariate();
        initView();
        initListener();
        initHttp();
        registerBroadcast();
        return view;
    }
    private void registerBroadcast() {
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Constant.INTENT_ACTION_ACTIVITY_MSG_NUM.equals(intent.getAction())) {
                    int msgNum = PreferencesUtils.getInt(context, Constant.MSG_NUM, 0);
//                    if (IGXApplication.isLogin() && msgNum > 0 && mTvMessage != null) {
//                        mTvMessage.setVisibility(View.VISIBLE);
//                        mTvMessage.setText(String.valueOf(msgNum));
//                    } else if (mTvMessage != null) {
//                        mTvMessage.setVisibility(View.GONE);
//                    }
                }
            }

        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.INTENT_ACTION_ACTIVITY_MSG_NUM);
        localBroadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mIsLogin = IGXApplication.isLogin();

        if (mIsFirstIntoLogin != IGXApplication.isLogin()) {
            initHttp();
            mIsFirstIntoLogin = IGXApplication.isLogin();
        }
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (localBroadcastManager != null && mReceiver != null) {
            localBroadcastManager.unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }


    /**
     * 初始化参数
     */
    private void initVariate() {
        mIsFirstIntoLogin = IGXApplication.isLogin();
        mAdapter = new SimpleAdapter(getActivity());
        mLayoutManager = new GridLayoutManager(getActivity(), 6, GridLayoutManager.VERTICAL, false);
//        mLayoutManager.setSpanSizeLookup(new MySpanSizeLookup(mLayoutManager, mAdapter));

//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        mLayoutManager.generateDefaultLayoutParams();
//        mRvItems.setLayoutParams(lp);
    }

    private void initView() {
        mRvItems.setLayoutManager(mLayoutManager);
//        int spaceProgram = getActivity().getResources().getDimensionPixelSize(R.dimen.small_margin);
//        int space = getActivity().getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
//        mRvItems.addItemDecoration(new SpaceItemDecoration(mAdapter, spaceProgram));
//        mRvItems.addItemDecoration(new TeacherItemDecoration(mAdapter, space));
        mRvItems.setAdapter(mAdapter);
    }

    private void initListener() {
        mAdapter.setOnClickListener(this);
        //TODO 下拉刷新
        mErrView.setOnTryCallBack(new NetErrAddLoadView.OnTryCallBack() {
            @Override
            public void callAgain() {
                initHttp();
            }
        });

//        mFlMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (IGXApplication.isLogin()) {
//                    Intent intent = new Intent(getActivity(), MessageHomeActivity.class);
//                    startActivity(intent);
//                } else {
//                    new LoginPromptDialog(getActivity()).show();
//                }
//            }
//        });
    }


    /**
     * 初始化网络请求
     */
    private void initHttp() {
//        fetchProgramList();
        fetchTeacherList();
    }


    /**
     * 获取节目列表
     */
//    private void fetchProgramList() {
//        ProgramHttpUtil.fetchProgramList(getActivity(), true, Action.teacher_fg_fetch_subscibe_list);
//    }

    /**
     * 获取老师列表
     */
    private void fetchTeacherList() {
        TeacherHttpUtil.ParamBuilder builder = new TeacherHttpUtil.ParamBuilder("1", "", "0", "");
        TeacherHttpUtil.fetchTeacherList(getActivity(), builder, Action.teacher_fg_fetch_follow_list);
    }

    // 添加 or 取消关注
    private void addFollow(TeacherItem item) {
        if (Constant.FOLLOWED == item.is_follow) {
            TeacherHttpUtil.delFollowForTeacherId(getActivity(), item.id, Action.teacher_fg_del_follow);
        } else {
            TeacherHttpUtil.addFollowForTeacherID(getActivity(), item.id, Action.teacher_fg_add_follow);
        }
    }


    // 添加 or 取消订阅
    /*private void addSubscribe(ProgramItem item) {
        if (item == null) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put(Constant.KEY_CATID, item.catid);
        String param = ParamUtil.getParam(map);
        if (Constant.SUBSCRIBEED == item.is_subscibe) {
            OKHttpUtils.getInstance().post(getActivity(), Api.DEL_SUBSCRIBER, param, Action.teacher_fg_del_subscribe);
        } else {
            OKHttpUtils.getInstance().post(getActivity(), Api.ADD_SUBSCRIBER, param, Action.teacher_fg_add_subscribe);
        }
    }*/


    // 订阅节目
    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFetchProgram(HttpEvent event) {
        if (Action.teacher_fg_fetch_subscibe_list == event.getAction()) {
            if (Constant.SUCCEED == event.getCode()) {
                JsonParser parser = new JsonParser();
                JsonObject tempJson = (JsonObject) parser.parse(event.getData().toString());
                JsonArray json = tempJson.getAsJsonObject("data").getAsJsonArray("list");
                mProgramList = new DataConverter<ProgramItem>().JsonToListObject(json.toString(), new TypeToken<List<ProgramItem>>() {
                }.getType());

                mAdapter.setProgramList(mProgramList);
                mAdapter.notifyDataSetChanged();
            }
        }
    }*/

    // Call fetch teachers
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFetchTeacher(HttpEvent event) {
        if (Action.teacher_fg_fetch_follow_list == event.getAction()) {
            if (Constant.SUCCEED == event.getCode()) {
                mRvItems.setVisibility(View.VISIBLE);
                DebugUtils.dd(event.getData().toString());
                JsonParser parser = new JsonParser();
                JsonObject tempJson = (JsonObject) parser.parse(event.getData().toString());
                JsonArray json = tempJson.getAsJsonObject("data").getAsJsonArray("list");
                mTeacherLIst = new DataConverter<TeacherItem>().JsonToListObject(json.toString(), new TypeToken<List<TeacherItem>>() {
                }.getType());

                mAdapter.setTeacherList(mTeacherLIst);
                mAdapter.notifyDataSetChanged();
            }
            mErrView.StopLoading(event.getCode(), event.getMsg());
        }

    }

    // Call add follow
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAddFollow(HttpEvent event) {
        if (Action.teacher_fg_add_follow == event.getAction()) {
            if (Constant.SUCCEED == event.getCode()) {
                if (tempTeacher != null && mAdapter != null) {
                    tempTeacher.fans_count += 1;
                    tempTeacher.is_follow = Constant.FOLLOWED;
                    mAdapter.resetFollowCheckBox(tempCheckBox, tempTextViewCount, tempTeacher);
                    updateUserFollowCount(ADD);
                    EventBus.getDefault().post(new EmptyEvent(Action.live_teacher_need_refresh));
                }
            } else {
                DebugUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "关注失败"));
            }
        }

    }

    // Call del follow
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDelFollow(HttpEvent event) {
        if (Action.teacher_fg_del_follow == event.getAction()) {
            if (Constant.SUCCEED == event.getCode()) {
                if (tempTeacher != null && mAdapter != null) {
                    tempTeacher.fans_count -= 1;
                    tempTeacher.is_follow = Constant.UNFOLLOW;
                    mAdapter.resetFollowCheckBox(tempCheckBox, tempTextViewCount, tempTeacher);
                    updateUserFollowCount(DEL);
                    EventBus.getDefault().post(new EmptyEvent(Action.live_teacher_need_refresh));
                }
            } else {
                DebugUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "取消关注失败"));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventInitHttp2(HttpEvent event) {
        Action action = event.getAction();
        switch (action) {
            case add_follow:
            case del_follow:
            case add_subscribe:
            case del_subscribe:
                if (Constant.SUCCEED == event.getCode()) {
                    initHttp();
                }
                break;
        }
    }

   /* // Call add subscribe
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAddSubscribe(HttpEvent event) {
        if (Action.teacher_fg_add_subscribe == event.getAction()) {
            if (Constant.SUCCEED == event.getCode()) {
                tempPro.is_subscibe = Constant.SUBSCRIBEED;
                tempPro.subscibe_count += 1;
                mAdapter.resetSubCheckBox(tempTextViewOperation, tempTextViewCount, tempPro);
                updateUserSubCount(ADD);
            } else {
                DebugUtils.showToastResponse(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "订阅失败"));
            }
        }
    }


    // Call del subscribe
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventDelSubscribe(HttpEvent event) {
        if (Action.teacher_fg_del_subscribe == event.getAction()) {
            if (SUCCEED == event.getCode()) {
                tempPro.is_subscibe = Constant.UNSUBSCRIBE;
                tempPro.subscibe_count -= 1;
                mAdapter.resetSubCheckBox(tempTextViewOperation, tempTextViewCount, tempPro);
                updateUserSubCount(DEL);
            } else {
                DebugUtils.showToastResponse(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "取消订阅失败"));
            }
        }
    }*/


    private static final int ADD = 1;
    private static final int DEL = 0;

    private void updateUserSubCount(int action) {
        if (IGXApplication.isLogin()) {
            int count = IGXApplication.getUser().getSubscibe_count();
            if (ADD == action) {
                IGXApplication.getUser().setSubscibe_count(count + 1);
            } else {
                IGXApplication.getUser().setSubscibe_count(count - 1);
            }
            EventBus.getDefault().post(new EmptyEvent(Action.inform_me_fragment_sub_follow_count_refresh));
        }
    }

    private void updateUserFollowCount(int action) {
        if (IGXApplication.isLogin()) {
            int count = IGXApplication.getUser().getFollow_count();
            if (ADD == action) {
                IGXApplication.getUser().setFollow_count(count + 1);
            } else {
                IGXApplication.getUser().setFollow_count(count - 1);
            }
            EventBus.getDefault().post(new EmptyEvent(Action.inform_me_fragment_sub_follow_count_refresh));
        }
    }


    @Override
    public void onClick(View v, Object o) {
        switch (v.getId()) {
//            case R.id.tv_subscribe:
//                tempPro = (ProgramItem) v.getTag(R.id.tag_first);
//                tempTextViewOperation = (TextView) v;
//                tempTextViewCount = (TextView) v.getTag(R.id.tag_second);
//
//                if (!mIsLogin) {
//                    new LoginPromptDialog(getActivity()).show();
//                } else {
//                    addSubscribe(tempPro);
//                }
//                break;
//
//            case R.id.img_program_photo:
//                ProgramItem pro2 = (ProgramItem) v.getTag(R.id.image_tag);
//                ProgramsZoneActivity.intoNewIntent(getActivity(), pro2.catid, pro2.catname);
//                break;

            case R.id.tv_follow_button:
                tempCheckBox = (TextView) v;
                if (!mIsLogin) {
                    tempCheckBox.setSelected(false);
                    new LoginPromptDialog(getActivity()).show();
                    return;
                } else {
                    tempTeacher = (TeacherItem) v.getTag();
                    tempTextViewCount = (TextView) v.getTag();
                    addFollow(tempTeacher);
                }
                break;

            case R.id.iv_teacher_item_photo:
//                TeacherItem teacherItem = (TeacherItem) v.getTag();
//                TeacherZoneActivity.intoNewIntent(getActivity(), teacherItem.id);
                break;
        }
    }


    static class MySpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
        private final GridLayoutManager manager;
        private final RecyclerView.Adapter adapter;

        public MySpanSizeLookup(GridLayoutManager manager, RecyclerView.Adapter adapter) {
            this.manager = manager;
            this.adapter = adapter;
        }

        @Override
        public int getSpanSize(int position) {
            if (SimpleAdapter.ITEM_TYPE_TAG_PRO == adapter.getItemViewType(position)) {
                return manager.getSpanCount();
            } else if (SimpleAdapter.ITEM_TYPE_TAG_TEA == adapter.getItemViewType(position)) {
                return manager.getSpanCount();
            } else if (SimpleAdapter.ITEM_TYPE_PROGRAM == adapter.getItemViewType(position)) {
                return 3;
            } else {
                return 2;
            }
        }
    }

    /**
     * RecyclerView item 间距
     */
    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private final SimpleAdapter adapter;
        private final int space;

        public SpaceItemDecoration(SimpleAdapter adapter, int space) {
            this.adapter = adapter;
            this.space = space;
        }


        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            int position = parent.getChildLayoutPosition(view);

            if (SimpleAdapter.ITEM_TYPE_PROGRAM == adapter.getItemViewType(position)) {
                if (position == 1) {
                    outRect.left = MeasureUtil.pxToDip(getActivity(), 0);
                    outRect.right = space;
                }
                if (position == 2) {
                    outRect.left = space;
                    outRect.right = MeasureUtil.pxToDip(getActivity(), 0);
                }
            }
        }
    }


    private class TeacherItemDecoration extends RecyclerView.ItemDecoration {
        private final SimpleAdapter adapter;
        private final int space;

        public TeacherItemDecoration(SimpleAdapter adapter, int space) {
            this.adapter = adapter;
            this.space = space;
        }


        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if (SimpleAdapter.ITEM_TYPE_TEACHER == adapter.getItemViewType(position)) {
                outRect.set(0, 0, 0, space);

                if ((position - 1) % 3 == 0) {
                    ((LinearLayout) view).setGravity(Gravity.START);
                } else if (position % 3 == 0) {
                    ((LinearLayout) view).setGravity(Gravity.END);
                } else {
                    ((LinearLayout) view).setGravity(Gravity.CENTER);
                }

                if (position == state.getItemCount() - 1) {
                    outRect.bottom = space * 2;
                }
            }
        }
    }
}
