package tv.kuainiu.ui.teachers.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.teachers.adapter.TeacherZoneAdapter;
import tv.kuainiu.util.CustomLinearLayoutManager;
import tv.kuainiu.widget.TitleBarView;

/**
 * 老师专区
 */
public class TeacherZoneActivity extends BaseActivity {


    @BindView(R.id.tbv_title) TitleBarView mTbvTitle;
    @BindView(R.id.rvReadingTap) RecyclerView mRvReadingTap;
    private CustomLinearLayoutManager mLayoutManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_zone);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
        initData();

    }

    private void initView() {
        mLayoutManager = new CustomLinearLayoutManager(this);
        mRvReadingTap.setLayoutManager(mLayoutManager);
    }

    private void initData() {
        TeacherZoneAdapter mTeacherZoneAdapter = new TeacherZoneAdapter(this);
        mRvReadingTap.setAdapter(mTeacherZoneAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {

    }


}
