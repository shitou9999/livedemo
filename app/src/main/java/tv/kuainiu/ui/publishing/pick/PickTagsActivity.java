package tv.kuainiu.ui.publishing.pick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.Categroy;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.TitleBarView;
import tv.kuainiu.widget.tagview.Tag;
import tv.kuainiu.widget.tagview.TagListView;
import tv.kuainiu.widget.tagview.TagView;

import static tv.kuainiu.modle.cons.Constant.SUCCEED;


public class PickTagsActivity extends BaseActivity {


    public final static int MAX_SELECT_NUMBER = 3;
    public final static String SELECTED_LIST = "selectedList";
    public final static String NEW_LIST = "new_List";
    public final static String PROGRAM = "program";
    public final static String TYPE = "Type";
    public final static String CLASS = "class";
    public final static int PROGRAM_TAG = 1;
    @BindView(R.id.tbv_title)
    TitleBarView tbvTitle;
    @BindView(R.id.et_add_tag)
    EditText etAddTag;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.tagListView)
    TagListView tagListView;
    @BindView(R.id.tagCategroyListView)
    TagListView tagCategroyListView;
    @BindView(R.id.srlRefresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.tvCategory)
    TextView tvCategory;
    private List<Categroy> mCategroyList = new ArrayList<>();
    private List<Tag> mTagList = new ArrayList<>();
    private List<Tag> mNewTagList = new ArrayList<>();
    private List<Tag> mSelectedTagList = new ArrayList<>();
    private List<Tag> mCategroyTagList = new ArrayList<>();
    private final String[] titles = {"基本面", "K线"};
    private Tag programTag = null;
    private boolean isHaveProgramTag = false;
    private String cls = "";

    public static void intoNewActivity(BaseActivity mContext, List<Tag> mTagList, List<Tag> mNewTagList, int requestCode) {
        Intent intent = new Intent(mContext, PickTagsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SELECTED_LIST, (Serializable) mTagList);
        bundle.putSerializable(NEW_LIST, (Serializable) mNewTagList);
        bundle.putInt(TYPE, 0);
        intent.putExtras(bundle);
        mContext.startActivityForResult(intent, requestCode);
    }

    public static void intoNewActivity(BaseActivity mContext, String cls, Tag programTag, List<Tag> mTagList, List<Tag> mNewTagList, int requestCode) {
        Intent intent = new Intent(mContext, PickTagsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SELECTED_LIST, (Serializable) mTagList);
        bundle.putSerializable(NEW_LIST, (Serializable) mNewTagList);
        bundle.putSerializable(PROGRAM, programTag);
        bundle.putInt(TYPE, PROGRAM_TAG);
        bundle.putString(CLASS, cls);
        intent.putExtras(bundle);
        mContext.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_pick_tags);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mSelectedTagList = (List<Tag>) getIntent().getExtras().getSerializable(SELECTED_LIST);
        mNewTagList = (List<Tag>) getIntent().getExtras().getSerializable(NEW_LIST);
        programTag = (Tag) getIntent().getExtras().getSerializable(PROGRAM);
        isHaveProgramTag = getIntent().getExtras().getInt(TYPE, 0) == PROGRAM_TAG;
        cls = getIntent().getExtras().getString(CLASS);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        srlRefresh.setColorSchemeColors(Theme.getLoadingColor());
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        tvCategory.setVisibility(isHaveProgramTag ? View.VISIBLE : View.GONE);
        tagCategroyListView.setVisibility(isHaveProgramTag ? View.VISIBLE : View.GONE);

        tagCategroyListView.setTagViewBackgroundCheckedRes(R.drawable.tag_checked_pressed);
        tagListView.setTagViewBackgroundCheckedRes(R.drawable.tag_checked_blue_pressed);
        tagListView.setOnTagClickListener(new TagListView.OnTagClickListener() {
            @Override
            public void onTagClick(TagView tagView, Tag tag) {

                if (mSelectedTagList.size() > 0) {

                    if (tag.isChecked()) {
                        for (int i = 0; i < mSelectedTagList.size(); i++) {
                            if (mSelectedTagList.get(i).getName().equals(tag.getName())) {
                                mSelectedTagList.remove(i);
                                break;
                            }
                        }
                    } else {
                        if (mSelectedTagList.size() >= MAX_SELECT_NUMBER) {
                            ToastUtils.showToast(PickTagsActivity.this, String.format(Locale.CHINA, "最多选择%d个自定义标签", MAX_SELECT_NUMBER));
                            return;
                        }
                        mSelectedTagList.add(tag);
                    }
                } else {
                    mSelectedTagList.add(tag);
                }

                dataTagsBind();
            }
        });
        tagCategroyListView.setOnTagClickListener(new TagListView.OnTagClickListener() {
            @Override
            public void onTagClick(TagView tagView, Tag tag) {
                programTag = tag;
                dataBindCategroyView();
            }
        });
        tbvTitle.setOnClickListening(new TitleBarView.OnClickListening() {
            @Override
            public void leftClick() {
                setResult();
            }

            @Override
            public void rightClick() {

            }

            @Override
            public void titleClick() {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            setResult();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setResult() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SELECTED_LIST, (Serializable) mSelectedTagList);
        bundle.putSerializable(NEW_LIST, (Serializable) mNewTagList);
        bundle.putSerializable(PROGRAM, (Serializable) programTag);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }


    private void initData() {
        if (isHaveProgramTag) {
            if (TextUtils.isEmpty(cls)) {
                OKHttpUtils.getInstance().syncGet(this, Api.get_cats, Action.get_cats);
            } else {
                OKHttpUtils.getInstance().syncGet(this, Api.get_cats + ParamUtil.getParamForGet("class", cls), Action.get_cats);
            }

        }
        Map<String, Object> map = new HashMap<>();
        map.put("teacher_id", MyApplication.getUser().getUser_id());
        OKHttpUtils.getInstance().syncGet(this, Api.teacher_news_tags + ParamUtil.getParamForGet(map), Action.teacher_news_tags);
    }

    @OnClick(R.id.tvSubmit)
    public void onClick() {
        String tagName = etAddTag.getText().toString();
        tagName = tagName.trim().toLowerCase();
        if (TextUtils.isEmpty(tagName)) {
            etAddTag.setError("请输入自定义标签");
            return;
        } else if (tagName.length() > 10) {
            etAddTag.setError("自定义标签不能超过10个字符");
            return;
        } else {
            int size = mTagList.size();
            for (int i = 0; i < mTagList.size(); i++) {
                if (tagName.equals(mTagList.get(i).getName())) {
                    etAddTag.setError("自定义标签已存在");
                    return;
                }
                if (size - 1 == i) {
                    etAddTag.setError(null);
                }
            }
            for (int i = 0; i < mNewTagList.size(); i++) {
                if (tagName.equals(mNewTagList.get(i).getName())) {
                    etAddTag.setError("自定义标签已存在");
                    return;
                }
                if (size - 1 == i) {
                    etAddTag.setError(null);
                }
            }
            Tag mTag = new Tag();
            mTag.setName(tagName);
            mTagList.add(mTag);
            if (mSelectedTagList.size() < MAX_SELECT_NUMBER) {
                mSelectedTagList.add(mTag);
            }
            mNewTagList.add(mTag);
            dataTagsBind();
            etAddTag.setText("");
        }
    }

    private void dataBindCategroyView() {
        if (mCategroyTagList.size() < 1 && mCategroyList.size() > 0) {
            for (int i = 0; i < mCategroyList.size(); i++) {
                if (!TextUtils.isEmpty(mCategroyList.get(i).getId())) {
                    try {
                        Tag tag = new Tag();
                        tag.setId(Integer.parseInt(mCategroyList.get(i).getId()));
                        tag.setName(mCategroyList.get(i).getCatname());
                        tag.setChecked(false);
                        mCategroyTagList.add(tag);
                    } catch (Exception e) {
                        LogUtils.e(TAG, e.getLocalizedMessage(), e);
                    }
                }
            }
        }
        if (programTag == null) {
            for (int i = 0; i < mCategroyTagList.size(); i++) {
                mCategroyTagList.get(i).setChecked(false);
            }
        } else {
            for (int i = 0; i < mCategroyTagList.size(); i++) {
                Tag mTag = mCategroyTagList.get(i);
                if (mTag.getName().equals(programTag.getName())) {
                    mCategroyTagList.get(i).setChecked(true);
                } else {
                    mCategroyTagList.get(i).setChecked(false);
                }
            }
        }

        tagCategroyListView.setTags(mCategroyTagList);
    }

    private void dataTagsBind() {
        if (mSelectedTagList == null || mSelectedTagList.size() < 1) {
            for (int i = 0; i < mTagList.size(); i++) {
                mTagList.get(i).setChecked(false);
            }
        } else {
            for (int i = 0; i < mTagList.size(); i++) {
                Tag mTag = mTagList.get(i);
                for (int j = 0; j < mSelectedTagList.size(); j++) {
                    if (mTag.getName().equals(mSelectedTagList.get(j).getName())) {
                        mTagList.get(i).setChecked(true);
                        break;
                    } else {
                        mTagList.get(i).setChecked(false);
                    }
                }
            }
            if (mNewTagList != null && mNewTagList.size() > 0) {
                for (int i = 0; i < mNewTagList.size(); i++) {
                    if (!mTagList.contains(mNewTagList.get(i))) {
                        mTagList.add(mNewTagList.get(i));
                    }
                }
            }
        }

        tagListView.setTags(mTagList);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case teacher_news_tags:
                srlRefresh.setRefreshing(false);
                if (Constant.SUCCEED == event.getCode()) {
                    String json = event.getData().optString("data");
                    try {
                        JSONObject object = new JSONObject(json);
                        List<Tag> tempTagList = new DataConverter<Tag>().JsonToListObject(object.optString("list"), new TypeToken<List<Tag>>() {
                        }.getType());

                        if (tempTagList != null && tempTagList.size() > 0) {
                            mTagList.clear();
                            mTagList.addAll(tempTagList);
                            dataTagsBind();
                        }

                    } catch (Exception e) {
                        LogUtils.e(TAG, "获取标签数据" + "," + event.getData().toString(), e);
                        ToastUtils.showToast(this, "获取标签数据解析失败");
                    }
                } else {
                    ToastUtils.showToast(this, event.getMsg());
                }
                break;
            case get_cats:
                srlRefresh.setRefreshing(false);
                if (SUCCEED == event.getCode()) {
                    try {
                        JsonParser parser = new JsonParser();
                        JsonObject tempJson = (JsonObject) parser.parse(event.getData().toString());
                        JsonArray json = tempJson.getAsJsonObject("data").getAsJsonArray("list");
                        List<Categroy> listTemp = new Gson().fromJson(json, new TypeToken<List<Categroy>>() {
                        }.getType());

                        if (listTemp != null && listTemp.size() > 0) {
                            mCategroyList.clear();
                            Categroy categroy = new Categroy();
                            categroy.setCatname("请选择");
                            categroy.setId("");
                            mCategroyList.add(categroy);
                            mCategroyList.addAll(listTemp);
                            dataBindCategroyView();
                        } else {
                            ToastUtils.showToast(this, "未获取到文章栏目信息");
                        }
                    } catch (Exception e) {
                        LogUtils.e(TAG, "获取到文章信息解析异常", e);
                        ToastUtils.showToast(this, "获取到文章栏目信息解析异常");
                    }
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "获取到文章栏目信息失败"));
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
