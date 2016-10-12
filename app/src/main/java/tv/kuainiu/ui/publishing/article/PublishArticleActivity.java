package tv.kuainiu.ui.publishing.article;

import android.os.Bundle;
import android.widget.ImageView;
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
import tv.kuainiu.widget.TitleBarView;
import tv.kuainiu.widget.tagview.Tag;
import tv.kuainiu.widget.tagview.TagListView;
import tv.kuainiu.widget.tagview.TagView;

public class PublishArticleActivity extends BaseActivity {


    private final List<Tag> mTags = new ArrayList<Tag>();
    private final String[] titles = {"基本面", "K线"};
    @BindView(R.id.tbv_title)
    TitleBarView tbvTitle;
    @BindView(R.id.ivShareSina)
    ImageView ivShareSina;
    @BindView(R.id.ivShareWeChat)
    ImageView ivShareWeChat;
    @BindView(R.id.ivShareQQ)
    ImageView ivShareQQ;
    @BindView(R.id.ivAddCover)
    ImageView ivAddCover;
    @BindView(R.id.tvInputWordLimit)
    TextView tvInputWordLimit;
    @BindView(R.id.btnFlag)
    TextView btnFlag;
    @BindView(R.id.tagListView)
    TagListView tagListView;
    @BindView(R.id.tvLive)
    TextView tvLive;
    @BindView(R.id.tvInputWordLimit2)
    TextView tvInputWordLimit2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_article);
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
            tag.setName(titles[i]);
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
