package tv.kuainiu.ui.publishing.dynamic;

import android.os.Bundle;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.widget.PostParentLayout;
import tv.kuainiu.widget.TitleBarView;
import tv.kuainiu.widget.tagview.Tag;
import tv.kuainiu.widget.tagview.TagListView;
import tv.kuainiu.widget.tagview.TagView;

public class DynamicActivity extends BaseActivity {


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
    private final List<Tag> mTags = new ArrayList<Tag>();
    private final String[] titles = { "基本面", "K线" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
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
    }


    private void initData() {

        for (int i = 0; i < 2; i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setChecked(true);
            tag.setTitle(titles[i]);
            mTags.add(tag);
        }
        tagListView.setDeleteMode(true);
        tagListView.setTags(mTags);

        tagListView.setOnTagClickListener(new TagListView.OnTagClickListener() {
            @Override
            public void onTagClick(TagView tagView, Tag tag) {
                tagListView.removeTag(tag);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
        }
    }
}
