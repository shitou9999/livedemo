package tv.kuainiu.ui.publishing.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.kuainiu.R;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.publishing.PickTagsActivity;
import tv.kuainiu.widget.PostParentLayout;
import tv.kuainiu.widget.TitleBarView;
import tv.kuainiu.widget.tagview.Tag;
import tv.kuainiu.widget.tagview.TagListView;
import tv.kuainiu.widget.tagview.TagView;

import static tv.kuainiu.ui.publishing.PickTagsActivity.NEW_LIST;
import static tv.kuainiu.ui.publishing.PickTagsActivity.SELECTED_LIST;

public class DynamicActivity extends BaseActivity {

    public static final int REQUSET_TAG_CODE = 0;
    @BindView(R.id.tbv_title)
    TitleBarView tbvTitle;
    @BindView(R.id.tvInputWordLimit)
    TextView tvInputWordLimit;
    @BindView(R.id.btnFlag)
    TextView btnFlag;
    @BindView(R.id.tagListView)
    TagListView tagListView;
    @BindView(R.id.pl_friends_post_group)
    PostParentLayout plFriendsPostGroup;
    private List<Tag> mTags = new ArrayList<Tag>();
    private List<Tag> mNewTagList = new ArrayList<Tag>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_dynamic);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
        initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
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
    }


    private void initData() {
    }

    private void dataBind() {
        tagListView.setTags(mTags);
    }


    @OnClick(R.id.btnFlag)
    public void onClick() {
        PickTagsActivity.intoNewActivity(this, mTags, mNewTagList, REQUSET_TAG_CODE);
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
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
        }
    }


}
