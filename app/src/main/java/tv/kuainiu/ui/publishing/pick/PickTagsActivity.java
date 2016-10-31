package tv.kuainiu.ui.publishing.pick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.TitleBarView;
import tv.kuainiu.widget.tagview.Tag;
import tv.kuainiu.widget.tagview.TagListView;
import tv.kuainiu.widget.tagview.TagView;


public class PickTagsActivity extends BaseActivity {


    public final static int MAX_SELECT_NUMBER = 3;
    public final static String SELECTED_LIST = "selectedList";
    public final static String NEW_LIST = "new_List";
    @BindView(R.id.tbv_title)
    TitleBarView tbvTitle;
    @BindView(R.id.et_add_tag)
    EditText etAddTag;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.tagListView)
    TagListView tagListView;
    @BindView(R.id.srlRefresh)
    SwipeRefreshLayout srlRefresh;

    private List<Tag> mTagList = new ArrayList<>();
    private List<Tag> mNewTagList = new ArrayList<>();
    private List<Tag> mSelectedTagList = new ArrayList<>();
    private final String[] titles = {"基本面", "K线"};

    public static void intoNewActivity(BaseActivity mContext, List<Tag> mTagList, List<Tag> mNewTagList, int requestCode) {
        Intent intent = new Intent(mContext, PickTagsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SELECTED_LIST, (Serializable) mTagList);
        bundle.putSerializable(NEW_LIST, (Serializable) mNewTagList);
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
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
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
                            ToastUtils.showToast(PickTagsActivity.this, "最多选择5个自定义标签");
                            return;
                        }
                        mSelectedTagList.add(tag);
                    }
                } else {
                    mSelectedTagList.add(tag);
                }

                dataBind();
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
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }


    private void initData() {
        Map<String, Object> map = new HashMap<>();
        map.put("teacher_id", MyApplication.getUser().getUser_id());
        OKHttpUtils.getInstance().syncGet(this, Api.teacher_news_tags + ParamUtil.getParamForGet(map), Action.teacher_news_tags);
    }

    private void dataBind() {
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
                if (Constant.SUCCEED == event.getCode()) {
                    String json = event.getData().optString("data");
                    try {
                        JSONObject object = new JSONObject(json);
                        List<Tag> tempTagList = new DataConverter<Tag>().JsonToListObject(object.optString("list"), new TypeToken<List<Tag>>() {
                        }.getType());

                        if (tempTagList != null && tempTagList.size() > 0) {
                            mTagList.clear();
                            mTagList.addAll(tempTagList);
                            dataBind();
                        }

                    } catch (Exception e) {
                        LogUtils.e(TAG, "获取标签数据" + "," + event.getData().toString(), e);
                        ToastUtils.showToast(this, "获取标签数据解析失败");
                    }
                } else {
                    ToastUtils.showToast(this, event.getMsg());
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
            dataBind();
            etAddTag.setText("");
        }
    }
}
