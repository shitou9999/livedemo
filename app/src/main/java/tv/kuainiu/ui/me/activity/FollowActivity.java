package tv.kuainiu.ui.me.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.TeacherItem;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.ui.BaseActivity;
import tv.kuainiu.ui.me.adapter.MyFollowAdapter;
import tv.kuainiu.ui.teachers.activity.TeacherZoneActivity;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.LoadingProgressDialog;
import tv.kuainiu.widget.DividerItemDecoration;

import static tv.kuainiu.modle.cons.Constant.SUCCEED;


/**
 * 关注列表
 */
public class FollowActivity extends BaseActivity {
    @BindView(R.id.ptr_rv_layout) PtrClassicFrameLayout mContentFrameLayout;
    @BindView(R.id.rv_follows) RecyclerViewFinal mRvFollows;

    private List<TeacherItem> mFollowList = new ArrayList<>();
    private MyFollowAdapter mFollowAdapter;
    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGson = new Gson();
        setMyTitle("关注");
        setRightTitle("添加");
        setRightTitleVisibility(View.VISIBLE);

        mContentFrameLayout.setLastUpdateTimeRelateObject(this);
        mContentFrameLayout.disableWhenHorizontalMove(true);

        LoadingProgressDialog.startProgressDialog(this);
        fetchAllFollow();
    }

    @Override protected void initContentView(int layoutId) {
        super.initContentView(R.layout.activity_follow);
        mFollowAdapter = new MyFollowAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvFollows.setLayoutManager(manager);
        mRvFollows.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRvFollows.setAdapter(mFollowAdapter);
    }

    @Override protected void initListener() {
        super.initListener();
        mContentFrameLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override public void onRefreshBegin(PtrFrameLayout frame) {
                mFollowList.clear();
                fetchAllFollow();

            }
        });
        mFollowAdapter.setOnItemClickListener(new MyFollowAdapter.OnRecyclerViewItemClickListener() {
            @Override public void onItemClick(View view, Object data) {
                TeacherItem teacherItem = (TeacherItem) data;
                TeacherZoneActivity.intoNewIntent(FollowActivity.this,teacherItem.id);
            }
        });


        mFollowAdapter.setOnFollowClickListener(new MyFollowAdapter.OnFollowClickListener() {
            @Override
            public void onClick(View view, Object data, int position) {
                try {
                    TeacherItem teacher = (TeacherItem) data;
                    Map<String, String> map = new HashMap<>();
                    map.put("teacher_id", teacher.id);
                    String param = ParamUtil.getParam(map);
                    if (teacher.is_follow == 1) {
                        delFollow(param);
                        teacher.is_follow = 0;
                        mFollowAdapter.setData(mFollowList);
                        mFollowAdapter.notifyDataSetChanged();
                    } else {
                        addFollow(param);
                        teacher.is_follow = 1;
                        mFollowAdapter.setData(mFollowList);
                        mFollowAdapter.notifyDataSetChanged();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    @Override protected void onRightTitleClick() {
        super.onRightTitleClick();
        EventBus.getDefault().post(new HttpEvent(Action.switch_teacher_fragment, SUCCEED));
        finish();
//        Intent intent = new Intent(this, MainTabActivity.class);
//        intent.putExtra("index", 3);
//        startActivity(intent);
//        finish();
    }

    private void fetchAllFollow() {
        Map<String, String> map = new HashMap<>();
        map.put("is_all", "0");
        String param = ParamUtil.getParam(map);
        OKHttpUtils.getInstance().post(this, Api.FOLLOW_LSIT, param, Action.me_follow);
    }

    private void addFollow(String param) {
        OKHttpUtils.getInstance().post(this, Api.ADD_FOLLOW, param, Action.add_follow);
    }

    private void delFollow(String param) {
        OKHttpUtils.getInstance().post(this, Api.DEL_FOLLOW, param, Action.del_follow);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFollowEvent(HttpEvent event) {
        if (Action.me_follow == event.getAction()) {
            if (SUCCEED == event.getCode()) {
                JSONObject jsonObject = event.getData().optJSONObject("data");
                JSONArray jsonArray = jsonObject.optJSONArray("list");
                if (null != jsonArray && jsonArray.length() > 0) {
                    List<TeacherItem> tempList = mGson.fromJson(jsonArray.toString(), new TypeToken<List<TeacherItem>>() {
                    }.getType());
                    mFollowList.addAll(tempList);
                }
                mFollowAdapter.setData(mFollowList);
                mFollowAdapter.notifyDataSetChanged();
            }

            try {
                LoadingProgressDialog.stopProgressDialog();
                mContentFrameLayout.onRefreshComplete();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddFollow(HttpEvent event) {
        if (Action.add_follow == event.getAction()) {
            if (event.getCode() == SUCCEED) {
                // EventBus.getDefault().post(new EmptyEvent(Action.inform_teacher_fragment_refresh));
            } else {
                DebugUtils.showToastResponse(this, event.getMsg());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDelFollow(HttpEvent event) {
        if (Action.del_follow == event.getAction()) {
            if (event.getCode() == SUCCEED) {
                //EventBus.getDefault().post(new EmptyEvent(Action.inform_teacher_fragment_refresh));
            } else {
                DebugUtils.showToastResponse(this, event.getMsg());
            }
        }
    }
}
