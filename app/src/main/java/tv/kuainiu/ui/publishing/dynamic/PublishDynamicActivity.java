package tv.kuainiu.ui.publishing.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.TeacherZoneDynamicsInfo;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.publishing.pick.PickArticleActivity;
import tv.kuainiu.ui.publishing.pick.PickTagsActivity;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.ExpandListView;
import tv.kuainiu.widget.TitleBarView;
import tv.kuainiu.widget.tagview.Tag;
import tv.kuainiu.widget.tagview.TagListView;
import tv.kuainiu.widget.tagview.TagView;

import static tv.kuainiu.ui.publishing.pick.PickTagsActivity.NEW_LIST;
import static tv.kuainiu.ui.publishing.pick.PickTagsActivity.SELECTED_LIST;

public class PublishDynamicActivity extends BaseActivity {

    public static final int REQUSET_ARTICLE_CODE = 1;
    public static final int REQUSET_TAG_CODE = 0;
    @BindView(R.id.tbv_title)
    TitleBarView tbvTitle;
    @BindView(R.id.tvInputWordLimit)
    TextView tvInputWordLimit;
    @BindView(R.id.btnFlag)
    TextView btnFlag;
    @BindView(R.id.tagListView)
    TagListView tagListView;
    @BindView(R.id.elv_friends_post_group)
    ExpandListView elv_friends_post_group;
    @BindView(R.id.rlRelatedArticles)
    RelativeLayout rlRelatedArticles;
    @BindView(R.id.et_content)
    EditText etContent;
    private List<Tag> mTags = new ArrayList<Tag>();
    private List<Tag> mNewTagList = new ArrayList<Tag>();

    PublicDynamicAdapter mPublicDynamicAdapter;
    List<TeacherZoneDynamicsInfo> listTeacherZoneDynamicsInfo = new ArrayList<>();

    private String news_id = "";//可选     引用的文章ID
    private String description = "";//必传     文字内容
    private String thumb = "";//缩略图     多个以英文逗号隔开
    private String synchro_wb = "1";//是否同步微博     1是 0否
    private String tag = "";//是否同步微博     1是 0否
    private boolean isSubmiting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_dynamic);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
    }

    private void initView() {
        tagListView.setDeleteMode(true);
        tagListView.setOnTagClickListener(new TagListView.OnTagClickListener() {
            @Override
            public void onTagClick(TagView tagView, Tag tag) {
                mTags.remove(tag);
                tagListView.removeTag(tag);
            }
        });

        tbvTitle.setOnClickListening(new TitleBarView.OnClickListening() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                if (!dataVerify()) {
                    return;
                }
                submitData();
            }

            @Override
            public void titleClick() {

            }
        });
    }

    /**
     * 数据验证
     */
    private boolean dataVerify() {
        boolean flag = true;
        news_id = "";//可选     引用的文章ID
        description = "";//必传     文字内容
        thumb = "";//缩略图     多个以英文逗号隔开
        synchro_wb = "1";//是否同步微博     1是 0否
        tag = "";
        if (listTeacherZoneDynamicsInfo.size() > 0) {
            news_id = listTeacherZoneDynamicsInfo.get(0).getNews_id();
        }
        description = etContent.getText().toString();
        if (TextUtils.isEmpty(description)) {
            flag = false;
            etContent.setError("动态直播内容不能为空");
        } else {
            etContent.setError(null);
        }
        if (mTags.size() > 0) {
            for (int i = 0; i < mTags.size(); i++) {
                Tag mTag = mTags.get(i);
                if (mTag.getId() == 0) {
                    tag += mTag.getName();
                } else {
                    tag += mTag.getId();
                }
                if (i <= mTags.size() - 2) {
                    tag += ",";
                }

            }
        }
        return flag;
    }

    //    private String news_id = "";//可选     引用的文章ID
//    private String description = "";//必传     文字内容
//    private String thumb = "";//缩略图     多个以英文逗号隔开
//    private String synchro_wb = "1";//是否同步微博     1是 0否
//    private String tag = "";//是否同步微博     1是 0否
    private void submitData() {

        Map<String, String> map = new HashMap<>();
        map.put("news_id", news_id);
        map.put("description", description);
        map.put("thumb", thumb);
        map.put("synchro_wb", synchro_wb);
        map.put("tag", tag);
        if (!isSubmiting) {
            isSubmiting = true;
            OKHttpUtils.getInstance().post(this, Api.add_dynamics, ParamUtil.getParam(map), Action.teacher_news_tags.add_dynamics);
        }

    }

    private void dataBind() {
        tagListView.setTags(mTags);
    }


    @OnClick({R.id.btnFlag, R.id.rlRelatedArticles})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlRelatedArticles:
                PickArticleActivity.intoNewActivity(this, REQUSET_ARTICLE_CODE);
                break;
            case R.id.btnFlag:
                PickTagsActivity.intoNewActivity(this, mTags, mNewTagList, REQUSET_TAG_CODE);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUSET_TAG_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                mTags = (List<Tag>) data.getExtras().getSerializable(SELECTED_LIST);
                mNewTagList = (List<Tag>) data.getExtras().getSerializable(NEW_LIST);
                dataBind();
            }
        } else if (requestCode == REQUSET_ARTICLE_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                TeacherZoneDynamicsInfo newsItem = (TeacherZoneDynamicsInfo) data.getSerializableExtra(PickArticleActivity.NEWS_ITEM);
                listTeacherZoneDynamicsInfo.clear();
                listTeacherZoneDynamicsInfo.add(newsItem);
                dataArticleBind();
            }
        }
    }

    private void dataArticleBind() {
        if (mPublicDynamicAdapter == null) {
            mPublicDynamicAdapter = new PublicDynamicAdapter(this, listTeacherZoneDynamicsInfo);
            elv_friends_post_group.setAdapter(mPublicDynamicAdapter);
            mPublicDynamicAdapter.setIDeleteItemClickListener(new PublicDynamicAdapter.IDeleteItemClickListener() {
                @Override
                public void delete(SwipeLayout swipeLayout, int position, TeacherZoneDynamicsInfo newsItem) {
                    listTeacherZoneDynamicsInfo.remove(newsItem);
                    mPublicDynamicAdapter.notifyDataSetChanged();
                }
            });
        } else {
            mPublicDynamicAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case add_dynamics:
                isSubmiting = false;
                if (event.getCode() == Constant.SUCCEED) {
                    ToastUtils.showToast(this, "发布动态成功");
                    finish();
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "发布动态直播失败"));
                }
                break;
        }
    }

}
