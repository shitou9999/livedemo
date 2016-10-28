package tv.kuainiu.ui.me.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.command.dao.AreaDao;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.Area;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.me.adapter.SimpleAreaAdapter;
import tv.kuainiu.utils.DebugUtils;

/**
 * 选择地区
 */
public class ChooseRegionActivity extends BaseActivity {
    public static final String ARG_AREA = "param_area";
    @BindView(R.id.lv_region) ListView mListView;
    @BindView(R.id.tv_title) TextView mTitle;

    private SimpleAreaAdapter mAdapter;
    private static String mResult = "";
    private Area mArea;


    private List<Area> mCurList = new ArrayList<>();


    private AreaDao mAreaDao;

    public static void intoNewIntent(Activity context, Area area) {
        Intent intent = new Intent(context, ChooseRegionActivity.class);
        Bundle args = new Bundle();
        args.putParcelable(ARG_AREA, area);
        intent.putExtras(args);
        context.startActivity(intent);
    }

    private void initData(Intent intent) {
        if (intent == null) return;
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mArea = bundle.getParcelable(ARG_AREA);
            if (mArea != null) {
                mResult = mResult.concat(mArea.name).concat("-");
                DebugUtils.dd("xxxxxxxxxx result : " + mResult);
            }
        }
    }

    @Override protected void onResume() {
        super.onResume();
        if (mArea == null) {
            mResult = "";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_region);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData(getIntent());
        if (mArea == null) {
            mCurList = (new AreaDao(this)).selectProvince();
        } else {
            mCurList = (new AreaDao(this)).selectArea(mArea.getId());
        }
        mAdapter = new SimpleAreaAdapter(ChooseRegionActivity.this, mCurList);
        mListView.setAdapter(mAdapter);
        initListener();
    }


    @Override protected void onDestroy() {

        updateResultString();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (mCurList != null && mCurList.size()>0) {
            mCurList.clear();
        }
        super.onDestroy();
    }

    private void updateResultString() {
        if (TextUtils.isEmpty(mResult)) return;

        if (mArea != null) {
            int level = Integer.parseInt(mArea.level);
            if (level == 2) {
                int endIndex = mResult.length() - mArea.name.length() - 1;
                if (mResult.length() > endIndex) {
                    mResult = mResult.substring(0, endIndex);
                }
            }
        }
    }

    private void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Area area = mCurList == null ? null : mCurList.get(position);
                if (area == null) {
                    return;
                }
                int mLevel = Integer.parseInt(area.getLevel());
                if (mLevel > 2) {
                    mResult += area.name;
                    EventBus.getDefault().post(new HttpEvent(Action.choose_city, Constant.SUCCEED, mResult));
                    mResult = "";
                } else {
                    intoNewIntent(ChooseRegionActivity.this, area);
                }
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HttpEvent event) {
        if (event.getAction() == Action.choose_city) {
            finish();
        }

    }
}
