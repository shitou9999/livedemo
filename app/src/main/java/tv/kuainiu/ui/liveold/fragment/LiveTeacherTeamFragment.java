package tv.kuainiu.ui.liveold.fragment;

public class LiveTeacherTeamFragment{

}
/**
 * @author nanck on 2016/7/7.
 */
/*
public class LiveTeacherTeamFragment extends BaseFragment implements SimpleAdapter.OnClickListener {
    private static final String TAG_LIFE = "LiveTeacherTeamFragment_LIFE";
    @BindView(R.id.ptr_rv_layout) PtrClassicFrameLayout mPtrClassicFrameLayout;
    @BindView(R.id.recyclerView) RecyclerView mRvItems;
    @BindView(R.id.err_layout) NetErrAddLoadView mErrView;


    private SimpleAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private boolean mIsLogin = false;

    List<TeacherItem> mTeacherLIst = new ArrayList<>();

    private boolean mIsFirstIntoLogin = false;

    */
/**
     * Temp view holder
     *//*

    private TextView tempTextViewCount;
    private TeacherItem tempTeacher;
    private TextView tempCheckBox;


    public static LiveTeacherTeamFragment newInstance() {
        Bundle args = new Bundle();
        LiveTeacherTeamFragment fragment = new LiveTeacherTeamFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        View view = inflater.inflate(R.layout.fragment_live_teacher_team, container, false);
        ViewGroup viewgroup = (ViewGroup) view.getParent();
        if (viewgroup != null) {
            viewgroup.removeView(view);
        }
        ButterKnife.bind(this, view);
        initVariate();
        initView();
        initListener();
        initHttp();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        mIsLogin = MyApplication.isLogin();

        if (mIsFirstIntoLogin != MyApplication.isLogin()) {
            initHttp();
            mIsFirstIntoLogin = MyApplication.isLogin();
        }
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }


    */
/**
     * 初始化参数
     *//*

    private void initVariate() {
        mIsFirstIntoLogin = MyApplication.isLogin();
        mAdapter = new SimpleAdapter(getActivity());
        mAdapter.setLiveChild(true);
        mLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLayoutManager.generateDefaultLayoutParams();
        mRvItems.setLayoutParams(lp);
    }

    private void initView() {
        mRvItems.setLayoutManager(mLayoutManager);
        int space = getActivity().getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        mRvItems.addItemDecoration(new TeacherItemDecoration(mAdapter, space));
        mRvItems.setAdapter(mAdapter);
    }

    private void initListener() {
        mAdapter.setOnClickListener(this);
        // 下拉刷新
        mPtrClassicFrameLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                initHttp();
            }
        });
        mErrView.setOnTryCallBack(new NetErrAddLoadView.OnTryCallBack() {
            @Override
            public void callAgain() {
                initHttp();
            }
        });

    }


    */
/**
     * 初始化网络请求
     *//*

    private void initHttp() {
        fetchTeacherList();
    }


    */
/**
     * 获取老师列表
     *//*

    private void fetchTeacherList() {
        TeacherHttpUtil.ParamBuilder builder = new TeacherHttpUtil.ParamBuilder("1", "", "0", "");
        TeacherHttpUtil.fetchTeacherList(getActivity(), builder, Action.teacher_fg_fetch_follow_list);
    }

    // 添加 or 取消关注
    private void addFollow(TeacherItem item) {
        if (Constant.FOLLOWED == item.is_follow) {
            TeacherHttpUtil.delFollowForTeacherId(getActivity(), item.id, Action.del_follow);
        } else {
            TeacherHttpUtil.addFollowForTeacherID(getActivity(), item.id, Action.add_follow);
        }
    }


    // Call fetch teachers
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFetchTeacher(HttpEvent event) {
        if (Action.teacher_fg_fetch_follow_list == event.getAction()) {
            if (SUCCEED == event.getCode()) {
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
            // 停止下拉刷新
            if (null != mPtrClassicFrameLayout) {
                mPtrClassicFrameLayout.onRefreshComplete();
            }
        }

    }

    // Call add follow
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAddFollow(HttpEvent event) {
        if (Action.teacher_fg_add_follow == event.getAction()) {
            if (SUCCEED == event.getCode()) {
                if (tempTeacher != null && mAdapter != null) {
                    tempTeacher.fans_count += 1;
                    tempTeacher.is_follow = Constant.FOLLOWED;
                    mAdapter.resetFollowCheckBox(tempCheckBox, tempTextViewCount, tempTeacher);
                    updateUserFollowCount(ADD);
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
            if (SUCCEED == event.getCode()) {
                if (tempTeacher != null && mAdapter != null) {
                    tempTeacher.fans_count -= 1;
                    tempTeacher.is_follow = Constant.UNFOLLOW;
                    mAdapter.resetFollowCheckBox(tempCheckBox, tempTextViewCount, tempTeacher);
                    updateUserFollowCount(DEL);
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
                if (SUCCEED == event.getCode()) {
                    initHttp();
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventNeedRefresh(EmptyEvent event) {
        if (Action.live_teacher_need_refresh == event.getAction()) {
            initHttp();
        }
    }


    private static final int ADD = 1;
    private static final int DEL = 0;

    private void updateUserFollowCount(int action) {
        if (MyApplication.isLogin()) {
            int count = MyApplication.getUser().getFollow_count();
            if (ADD == action) {
                MyApplication.getUser().setFollow_count(count + 1);
            } else {
                MyApplication.getUser().setFollow_count(count - 1);
            }
            EventBus.getDefault().post(new EmptyEvent(Action.inform_me_fragment_sub_follow_count_refresh));
        }
    }


    @Override
    public void onClick(View v, Object o) {
        switch (v.getId()) {
            case R.id.tv_follow_button:
                tempCheckBox = (TextView) v;
                if (!mIsLogin) {
                    tempCheckBox.setSelected(false);
                    new LoginPromptDialog(getActivity()).show();
                    return;
                } else {
                    tempTeacher = (TeacherItem) v.getTag(R.id.tag_first);
                    tempTextViewCount = (TextView) v.getTag(R.id.tag_second);
                    addFollow(tempTeacher);
                }
                break;

            case R.id.iv_teacher_item_photo:
                TeacherItem teacherItem = (TeacherItem) v.getTag(R.id.image_tag);
                TeacherZoneActivity.intoNewIntent(getActivity(), teacherItem.id);
                break;
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

                if (position % 3 == 0) {
                    ((LinearLayout) view).setGravity(Gravity.START);
                } else if ((position + 1) % 3 == 0) {
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
*/
