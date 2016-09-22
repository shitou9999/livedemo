package tv.kuainiu.ui.teachers.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.IGXApplication;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;
import tv.kuainiu.command.http.TeacherHttpUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.TeacherInfo;
import tv.kuainiu.modle.TeacherZoneDynamics;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.friends.model.Message;
import tv.kuainiu.ui.teachers.adapter.TeacherZoneAdapter;
import tv.kuainiu.util.CustomLinearLayoutManager;
import tv.kuainiu.util.DataConverter;
import tv.kuainiu.util.StringUtils;
import tv.kuainiu.util.ToastUtils;
import tv.kuainiu.widget.TitleBarView;

/**
 * 老师专区
 */
public class TeacherZoneActivity extends BaseActivity {

    private static final String ID = "ID";
    @BindView(R.id.tbv_title) TitleBarView mTbvTitle;
    @BindView(R.id.rvReadingTap) RecyclerView mRvReadingTap;
    @BindView(R.id.rl_stick) RelativeLayout rl_stick;
    @BindView(R.id.btnPublish) Button mBtnPublish;
    @BindView(R.id.tab_fragment_major) TabLayout mTabFragmentMajor;
    private TeacherZoneAdapter mTeacherZoneAdapter;
    RecyclerView.OnScrollListener loadmoreListener;
    private CustomLinearLayoutManager mLayoutManager;
    private List<Message> mMessages = new ArrayList<>();
    private List<TabLayout.Tab> listStickTab;
    private String[] tabNames = new String[]{"观点", "解盘"};
    int selectedIndex = 0;
    private boolean loading;
    private String teacherid = "";
    private String user_id = "";
    private TeacherInfo teacherInfo;
    private int page = 1;
    private List<TeacherZoneDynamics> teacherZoneDynamicsList = new ArrayList<>();

    public static void intoNewIntent(Activity context, String id) {
        Intent intent = new Intent(context, TeacherZoneActivity.class);
        intent.putExtra(ID, id);
        context.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_zone);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        teacherid = getIntent().getStringExtra(ID);
        user_id = IGXApplication.getUser() == null ? "" : IGXApplication.getUser().getUser_id();
        if (TextUtils.isEmpty(teacherid)) {
            ToastUtils.showToast(this, "未获取到老师");
            finish();
        }
        initListener();
        initView();
        initData();

    }

    private void initView() {
        rl_stick.setVisibility(View.INVISIBLE);
        mLayoutManager = new CustomLinearLayoutManager(this);
        mRvReadingTap.setLayoutManager(mLayoutManager);
        mRvReadingTap.addOnScrollListener(loadmoreListener);
    }

    private void initListener() {
        loadmoreListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                if (pastVisiblesItems >= 1) {
                    rl_stick.setVisibility(View.VISIBLE);
                } else {
                    rl_stick.setVisibility(View.INVISIBLE);
                }
                if (dy > 0) //向下滚动
                {
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();


                    if (!loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = true;
//                        NewsPage += 1;
//                        getNews();
                    }
                }
            }
        };
    }

    private void initData() {
        initTab(0);
        mTeacherZoneAdapter = new TeacherZoneAdapter(this);
        mTeacherZoneAdapter.setTabNames(tabNames, getOnTabSelectedListener());
        mRvReadingTap.setAdapter(mTeacherZoneAdapter);
//        View view = LayoutInflater.from(this).inflate(R.layout.activity_teacher_zone_top, null);
//        mRvReadingTap.addView(view);
//        FriendsPostAdapter adapter = new FriendsPostAdapter(this, mMessages);
//        mRvReadingTap.setAdapter(adapter);
        TeacherHttpUtil.fetchTeacherDetails(this, new TeacherHttpUtil.ParamBuilder(teacherid, user_id), Action.teacher_fg_fetch_detail);
        fetchTeacherDynamicsList();
    }

    /**
     * 名师个人专区-动态
     */
    public void fetchTeacherDynamicsList() {
        TeacherHttpUtil.fetchTeacherDynamicsList(this, page, teacherid, Action.find_dynamics_list);
    }

    private void initTab(int selectedIndex) {
        if (listStickTab == null) {
            listStickTab = new ArrayList<>();
            mTabFragmentMajor.removeAllTabs();
            for (int i = 0; i < tabNames.length; i++) {

                TabLayout.Tab tabStick = mTabFragmentMajor.newTab();
                tabStick.setText(tabNames[i]);
//                tabStick.setTag(tabNamesTags[i]);
                listStickTab.add(tabStick);
                mTabFragmentMajor.addTab(tabStick);
            }
            mTabFragmentMajor.addOnTabSelectedListener(getOnTabSelectedListener());
            mTabFragmentMajor.setTabTextColors(Color.parseColor("#757575"), Color.parseColor(Theme.getCommonColor()));
            mTabFragmentMajor.setSelectedTabIndicatorColor(Color.parseColor(Theme.getCommonColor()));
        } else {
            if (rl_stick.getVisibility() == View.VISIBLE) {
                mTeacherZoneAdapter.setSelectedIndex(selectedIndex);
            } else {
                listStickTab.get(selectedIndex).select();
            }
        }
    }

    private void teacherInfoDataBind(TeacherInfo teacherInfo) {
        mTeacherZoneAdapter.setTeacherInfo(teacherInfo);
        mTeacherZoneAdapter.notifyItemChanged(0);
    }

    private void teacherZoneDynamicsListDataBind() {
        if(teacherZoneDynamicsList!=null && teacherZoneDynamicsList.size()>0) {
            mTeacherZoneAdapter.setTeacherZoneDynamicsList(teacherZoneDynamicsList);
            mTeacherZoneAdapter.notifyItemRangeInserted(TeacherZoneAdapter.SIZE, teacherZoneDynamicsList.size());
        }
    }

    @NonNull
    private TabLayout.OnTabSelectedListener getOnTabSelectedListener() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                if (tab.getTag() instanceof Integer) {
//                    catid = tab.getTag().toString();
//                    type = "cat";
//                } else {
//                    catid = "";
//                    type = tab.getTag().toString();
//                }
//                page = 1;
//                fetchContentList();
                initTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case teacher_fg_fetch_detail:
                if (Constant.SUCCEED == event.getCode()) {
                    DataConverter<TeacherInfo> dataConverter = new DataConverter<>();
                    if (event.getData() != null && event.getData().has("data")) {
                        try {
                            JSONObject jsonObject = event.getData().getJSONObject("data");

                            teacherInfo = dataConverter.JsonToObject(jsonObject.getString("info"), TeacherInfo.class);
                            teacherInfoDataBind(teacherInfo);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showToast(this, "老师信息解析失败");
                            finish();
                        }

                    }
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "获取老师信息失败"));
                    finish();
                }
                break;
            case find_dynamics_list:
                if (Constant.SUCCEED == event.getCode()) {
                    DataConverter<TeacherInfo> dataConverter = new DataConverter<>();
                    if (event.getData() != null && event.getData().has("data")) {
                        try {
                            JSONObject jsonObject = event.getData().getJSONObject("data");
                            List<TeacherZoneDynamics> tempTeacherZoneDynamicsList = new DataConverter<TeacherZoneDynamics>().JsonToListObject(jsonObject.getString("list"), new TypeToken<List<TeacherZoneDynamics>>() {
                            }.getType());
                            if (page == 1) {
                                teacherZoneDynamicsList.clear();
                            }
                            if (tempTeacherZoneDynamicsList != null && tempTeacherZoneDynamicsList.size() > 0) {
                                teacherZoneDynamicsList.addAll(tempTeacherZoneDynamicsList);
                                teacherZoneDynamicsListDataBind();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showToast(this, "老师信息解析失败");
                            finish();
                        }

                    }
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "获取老师信息失败"));
                    finish();
                }
                break;
        }

    }


    private static final String JSON = "[\n" +
            "  {\n" +
            "    \"publish_timestamp\": 2342342342,\n" +
            "    \"head_photo\": \"http://img4.imgtn.bdimg.com/it/u=687186100,1032324030&fm=21&gp=0.jpg\",\n" +
            "    \"nickname\": \"王丽\",\n" +
            "    \"message_type\": 1,\n" +
            "    \"message_content\": \"写完此文, 偶然机会在InfoQ上看到Uber的技术主管Raffi Krikorian在 O’Reilly Software Architecture conference上谈及的关于架构重构的12条规则, 共勉之。\",\n" +
            "    \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "    \"comment_count\": 233,\n" +
            "    \"like_count\": 134,\n" +
            "    \"like_before\": 0,\n" +
            "    \"type\": 2\n" +
            "  },\n" +
            "  {\n" +
            "    \"publish_timestamp\": 2342342342,\n" +
            "    \"head_photo\": \"http://img1.imgtn.bdimg.com/it/u=380201267,4002174318&fm=23&gp=0.jpg\",\n" +
            "    \"nickname\": \"angel\",\n" +
            "    \"message_type\": 2,\n" +
            "    \"message_content\": \"写完此文, 偶然机会在InfoQ上看到Uber的技术主管Raffi Krikorian在 O’Reilly Software Architecture conference上谈及的关于架构重构的12条规则, 共勉之。\",\n" +
            "    \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "    \"comment_count\": 456,\n" +
            "    \"like_count\": 764,\n" +
            "    \"like_before\": 0,\n" +
            "    \"type\": 1\n" +
            "  },\n" +
            "  {\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"miss\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"鉴于20个设计的核心要素和原则（上）取得了不错的反响，所以我决定把下篇也出了，希望能帮到大家！\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\": 4\n" +
            "    },\n" +
            "   {\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"周小川\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"写完此文, 偶然机会在InfoQ上看到Uber的技术主管Raffi Krikorian在 O’Reilly Software Architecture conference上谈及的关于架构重构的12条规则, 共勉之。\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\": 4\n" +
            "    },\n" +
            "  {\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"papi酱\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"鉴于20个设计的核心要素和原则（上）取得了不错的反响，所以我决定把下篇也出了，希望能帮到大家！\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\": 1\n" +
            "    },\n" +
            "   {\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"周小川\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"写完此文, 偶然机会在InfoQ上看到Uber的技术主管Raffi Krikorian在 O’Reilly Software Architecture conference上谈及的关于架构重构的12条规则, 共勉之。\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\": 2\n" +
            "    },\n" +
            "\t {\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"刘念\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"鉴于20个设计的核心要素和原则（上）取得了不错的反响，所以我决定把下篇也出了，希望能帮到大家！\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\": 4\n" +
            "    },\n" +
            "\t{\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"李青\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"鉴于20个设计的核心要素和原则（上）取得了不错的反响，所以我决定把下篇也出了，希望能帮到大家！\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\":2\n" +
            "    },\n" +
            "\t{\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"大冰\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"鉴于20个设计的核心要素和原则（上）取得了不错的反响，所以我决定把下篇也出了，希望能帮到大家！\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\": 0\n" +
            "    },\n" +
            "\t{\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"王丽\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"鉴于20个设计的核心要素和原则（上）取得了不错的反响，所以我决定把下篇也出了，希望能帮到大家！\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\": 3\n" +
            "    },\n" +
            "\t{\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"李青\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"鉴于20个设计的核心要素和原则（上）取得了不错的反响，所以我决定把下篇也出了，希望能帮到大家！\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\": 3\n" +
            "    },\n" +
            "\t{\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"miss\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"鉴于20个设计的核心要素和原则（上）取得了不错的反响，所以我决定把下篇也出了，希望能帮到大家！\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\": 3\n" +
            "    }\n" +
            "]";

    private void parseJson() {
//        try {
//            InputStream in = getActivity().getAssets().open("friends_message.txt");
//            InputStreamReader ir = new InputStreamReader(in, "UTF-8");
//            BufferedReader br = new BufferedReader(ir);
//            String info;
        Gson gson = new Gson();
        Type type = new TypeToken<List<Message>>() {
        }.getType();
//            while ((info = br.readLine()) != null) {
        mMessages = gson.fromJson(JSON, type);
//            }
//            br.close();
//            ir.close();
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


}
