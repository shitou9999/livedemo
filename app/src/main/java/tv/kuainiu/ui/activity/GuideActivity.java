package tv.kuainiu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.synnapps.carouselview.CirclePageIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.CacheConfig;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.TeacherItem;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.ui.MainActivity;
import tv.kuainiu.ui.adapter.GuideAdapter;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.ExitUtil;
import tv.kuainiu.utils.ImageDisplayUtil;

import static tv.kuainiu.modle.cons.Constant.SUCCEED;


/**
 * @version 1.0
 * @ClassName GuideActivity
 * @Description (引导页)
 */
public class GuideActivity extends FragmentActivity implements
        OnClickListener, OnPageChangeListener {
    private GuideAdapter vpAdapter;
    private List<View> views = new ArrayList<View>();
    private ViewPager vp;
    private Button button;

    private CirclePageIndicator cpi_CirclePageIndicator;
    // 底部小点图片
    private ImageView guidencePic;

    List<String> urlPicList = new ArrayList<String>();
    List<TeacherItem> teacherList = new ArrayList<>();
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance);
        ExitUtil.getInstance().addActivity(this);
        cpi_CirclePageIndicator = (CirclePageIndicator) findViewById(R.id.cpi_circle_page_indicator);
        vp = (ViewPager) findViewById(R.id.viewpager);
        // 初始化Adapter
        vpAdapter = new GuideAdapter(views, new String[views.size()]);
        vp.setAdapter(vpAdapter);
        cpi_CirclePageIndicator.setViewPager(vp);
        // 绑定回调
        cpi_CirclePageIndicator.setOnPageChangeListener(this);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        initView();// 本地展示
        cacheData();
    }

    private void cacheData() {
        //缓存首页bannar数据
        OKHttpUtils.getInstance().post(this, Api.TEST_DNS_API_HOST, Api.FIND_BANNAR, ParamUtil.getParam(null), Action.find_bannar, CacheConfig.getCacheConfig());
    }


    public void initView() {
        for (int i = 1; i < 2; i++) {
            LayoutInflater inf = LayoutInflater.from(this);
            view = inf.inflate(R.layout.activity_guidance_item, null);
            guidencePic = (ImageView) view.findViewById(R.id.iv_guidance_pic);
            guidencePic.setScaleType(ScaleType.CENTER_CROP);
            int imageId = getResources().getIdentifier("guidence" + i, "mipmap",
                    getPackageName());
            ImageDisplayUtil.displayImage(this, guidencePic, imageId);
            views.add(view);
        }
        vpAdapter.notifyDataSetChanged();
        if (views.size() <= 1) {
            button.setVisibility(View.VISIBLE);
        }
    }


    @Override protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
//        fetchTeacherList();
    }


    @Override protected void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (views != null) {
            for (int i = 0; i < views.size(); i++) {
                views.get(i).destroyDrawingCache();
            }
        }
        super.onDestroy();
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        if (arg0 == views.size() - 1) {
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                if (!MainActivity.isOpened) {
                    Intent intent = new Intent();
//                    boolean flag = PreferencesUtils.getBoolean(getApplicationContext(), getString(R.string.guide_follow_first), false);
//                    if (teacherList != null && teacherList.size() > 0 && !flag) {
//                        intent.putParcelableArrayListExtra("teacher_list", (ArrayList<TeacherItem>) teacherList);
//                        intent.setClass(GuideActivity.this, FollowFirstActivity.class);
//                    } else {
//                        intent.setClass(GuideActivity.this, MainActivity.class);
//                    }
                    intent.setClass(GuideActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
                break;
        }
    }


    /**
     * 获取老师列表
     */
    private void fetchTeacherList() {
        String param = ParamUtil.getParamForGet("has_news", "0");
        OKHttpUtils.getInstance().syncGet(this, Api.TEST_DNS_API_HOST_V2, Api.FIND_TEACHER_LIST + param, Action.follow_first_follow_list, null);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFetchTeahcer(HttpEvent event) {
        if (Action.follow_first_follow_list == event.getAction()) {
            if (SUCCEED == event.getCode()) {
                JsonParser parser = new JsonParser();
                JsonObject tempJson = (JsonObject) parser.parse(event.getData().toString());
                DebugUtils.dd("guide activity teacher list : " + tempJson.toString());
                JsonArray json = tempJson.getAsJsonObject("data").getAsJsonArray("list");
                teacherList = new DataConverter<TeacherItem>().JsonToListObject(json.toString(), new TypeToken<List<TeacherItem>>() {
                }.getType());
            } else {
                // TODO: 2016/7/4 没有获取到老师信息，跳过这一步
            }
        }

    }
}