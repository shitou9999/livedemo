package tv.kuainiu.ui.publishing.pick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.TeacherZoneDynamicsInfo;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.TitleBarView;

import static tv.kuainiu.modle.cons.Constant.SUCCEED;

/**
 * 选择引用文章
 * Created by sirius on 2016/10/13.
 */

public class PickArticleActivity extends BaseActivity {
    public static final String NEWS_ITEM = "NEWSITEM";
    @BindView(R.id.tbv_title)
    TitleBarView tbvTitle;
    @BindView(R.id.lvArticleList)
    ListView lvArticleList;
    List<TeacherZoneDynamicsInfo> listArticleList = new ArrayList<>();
    PickArticleAdapter mPickArticleAdapter;

    public static void intoNewActivity(BaseActivity context,int requestCode) {
        Intent intent = new Intent(context, PickArticleActivity.class);
        context.startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_article);
        ButterKnife.bind(this);
        initView();
        initData();
    }
    private void initView() {
        lvArticleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(NEWS_ITEM, listArticleList.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    private void initData() {
        Map<String, Object> map = new HashMap<>();
        map.put("teacher_id", MyApplication.getUser().getUser_id());
        OKHttpUtils.getInstance().syncGet(this, Api.quote_list + ParamUtil.getParamForGet(map), Action.quote_list);
    }

    private void dataBindView() {
        if (mPickArticleAdapter == null) {
            mPickArticleAdapter = new PickArticleAdapter(this, listArticleList);
            lvArticleList.setAdapter(mPickArticleAdapter);
        } else {
            mPickArticleAdapter.notifyDataSetChanged();
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case quote_list:
                if (SUCCEED == event.getCode()) {
                    try {
                        JsonParser parser = new JsonParser();
                        JsonObject tempJson = (JsonObject) parser.parse(event.getData().toString());
                        JsonArray json = tempJson.getAsJsonObject("data").getAsJsonArray("list");
                        List<TeacherZoneDynamicsInfo> listTeacherTemp = new Gson().fromJson(json, new TypeToken<List<TeacherZoneDynamicsInfo>>() {
                        }.getType());

                        if (listTeacherTemp != null && listTeacherTemp.size() > 0) {
                            listArticleList.addAll(listTeacherTemp);
                            dataBindView();
                        } else {
                            ToastUtils.showToast(PickArticleActivity.this, "未获取到文章信息");
                        }
                    } catch (Exception e) {
                        LogUtils.e(TAG, "获取到文章信息解析异常", e);
                        ToastUtils.showToast(PickArticleActivity.this, "获取到文章信息解析异常");
                    }
                } else {
                    ToastUtils.showToast(PickArticleActivity.this, StringUtils.replaceNullToEmpty(event.getMsg(), "获取到文章信息失败"));
                }
                break;
        }
    }

}
