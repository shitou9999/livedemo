package tv.kuainiu.widget;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.kuainiu.R;
import tv.kuainiu.ui.publishing.article.PublishArticleActivity;
import tv.kuainiu.ui.publishing.dynamic.DynamicActivity;

/**
 * 弹幕布局类
 *
 * @author liufh
 */
public class PublishPanelLayout extends RelativeLayout {


    @BindView(R.id.ll_publish_dynamic)
    LinearLayout llPublishDynamic;
    @BindView(R.id.ll_publish_voice)
    LinearLayout llPublishVoice;
    @BindView(R.id.ll_publish_article)
    LinearLayout llPublishArticle;
    @BindView(R.id.ll_publish_video)
    LinearLayout llPublishVideo;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    private Context context;
    private View view;

    public PublishPanelLayout(Context context) {
        super(context);
        init(context);
    }

    public PublishPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PublishPanelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.publish_panel_layout, this);
        ButterKnife.bind(this, view);
    }

    @OnClick({R.id.ll_publish_dynamic, R.id.ll_publish_voice, R.id.ll_publish_article, R.id.ll_publish_video, R.id.ivClose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_publish_dynamic:
                Intent dynamicIntent = new Intent(context, DynamicActivity.class);
                context.startActivity(dynamicIntent);
                break;
            case R.id.ll_publish_voice:
//                Intent dynamicIntent = new Intent(context, DynamicActivity.class);
//                context.startActivity(dynamicIntent);
                break;
            case R.id.ll_publish_article:
                Intent articleIntent = new Intent(context, PublishArticleActivity.class);
                context.startActivity(articleIntent);
                break;
            case R.id.ll_publish_video:
                break;
            case R.id.ivClose:
                BottomSheetBehavior behavior = BottomSheetBehavior.from(this);
                if (behavior != null) {
                    if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    } else {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }
                break;
        }
    }
}
