package tv.kuainiu.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

import tv.kuainiu.R;
import tv.kuainiu.app.Constans;
import tv.kuainiu.modle.LiveInfo;
import tv.kuainiu.modle.TeacherZoneDynamicsInfo;
import tv.kuainiu.ui.friends.model.BasePost;
import tv.kuainiu.ui.video.VideoActivity;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;

/**
 * @author nanck on 2016/8/2.
 */
public class PostParentLayout extends RelativeLayout {
    private Context mContext;
    private int mPostType = 0;
    private BasePost mBasePost;
    private TeacherZoneDynamicsInfo teacherZoneDynamicsInfo;
    private LiveInfo liveInfo;

    public PostParentLayout(Context context) {
        super(context);
        init(context);
    }

    public PostParentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public PostParentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setPostType(TeacherZoneDynamicsInfo teacherZoneDynamicsInfo) {
        this.teacherZoneDynamicsInfo = teacherZoneDynamicsInfo;
        if (this.teacherZoneDynamicsInfo != null) {
            mPostType = Integer.parseInt(this.teacherZoneDynamicsInfo.getType());
            loadPostView();
        } else {
            setVisibility(View.GONE);
        }
    }

    public void setPostType(LiveInfo liveInfo) {
        this.liveInfo = liveInfo;
        if (this.liveInfo != null) {
            if (liveInfo.getLive_status() == 1)
                mPostType = Constans.TYPE_LIVE_END;
            loadPostView();
        } else {
            setVisibility(View.GONE);
        }
    }

    private void init(Context context) {
        mContext = context;

    }

    private void loadPostView() {
        View view;
        switch (mPostType) {
//            case Constans.TYPE_POST:
//                view = LayoutInflater.from(mContext).inflate(R.layout.include_post_normal, this, false);
//                if (getChildAt(0) != null) {
//                    removeAllViews();
//                }
//                ImageView  ivThumb= (ImageView) view.findViewById(R.id.ivThumb);
//                TextView  tvTitle= (TextView) view.findViewById(R.id.tvTitle);
//                ImageDisplayUtil.displayImage(mContext,ivThumb,teacherZoneDynamicsInfo.getNews_thumb());
//                tvTitle.setText(StringUtils.replaceNullToEmpty(teacherZoneDynamicsInfo.getNews_title()));
//                addView(view);
//                break;

            case Constans.TYPE_LIVING:
                view = LayoutInflater.from(mContext).inflate(R.layout.include_post_living, this, false);
                if (getChildAt(0) != null) {
                    removeAllViews();
                }
                TextView tvState = (TextView) view.findViewById(R.id.tvLiveState);
                TextView tvLiveDescription = (TextView) view.findViewById(R.id.tvLiveDescription);
                tvState.setText(liveInfo.getLive_msg());
                tvLiveDescription.setText(liveInfo.getDescription());
                addView(view);
                break;
//
            case Constans.TYPE_LIVE:
            case Constans.TYPE_LIVE_END:
                view = LayoutInflater.from(mContext).inflate(R.layout.include_post_live, this, false);
                if (getChildAt(0) != null) {
                    removeAllViews();
                }
                TextView tvState2 = (TextView) view.findViewById(R.id.tvLiveState);
                TextView tvLiveDescription2 = (TextView) view.findViewById(R.id.tvLiveDescription);
                tvState2.setText(liveInfo.getLive_msg() + "/n" + liveInfo.getStart_date());
                tvLiveDescription2.setText(liveInfo.getDescription());
                addView(view);
                break;
            //视频
            case Constans.TYPE_VIDEO:
                view = LayoutInflater.from(mContext).inflate(R.layout.include_post_video, this, false);
                if (getChildAt(0) != null) {
                    removeAllViews();
                }
                ImageView ivivPlay = (ImageView) view.findViewById(R.id.ivPlay);
                TextView tvTitle2 = (TextView) view.findViewById(R.id.tvTitle);
                tvTitle2.setText(StringUtils.replaceNullToEmpty(teacherZoneDynamicsInfo.getNews_title()));
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VideoActivity.intoNewIntent(mContext, teacherZoneDynamicsInfo.getNews_video_id(), teacherZoneDynamicsInfo.getVideo_id(), teacherZoneDynamicsInfo.getNews_catid());
                    }
                });
                addView(view);
                break;
            //音频
            case Constans.TYPE_AUDIO:
                view = LayoutInflater.from(mContext).inflate(R.layout.include_post_audio, this, false);
                if (getChildAt(0) != null) {
                    removeAllViews();
                }
                ImageView ivPlayVoice = (ImageView) view.findViewById(R.id.ivPlay);

                ivPlayVoice.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!TextUtils.isEmpty(teacherZoneDynamicsInfo.getNews_voice_url())) {
                            MediaPlayer mp = new MediaPlayer();
                            try {
                                mp.setDataSource(teacherZoneDynamicsInfo.getNews_voice_url());
                                mp.prepare();
                                mp.start();
                            } catch (IOException e) {
                                ToastUtils.showToast(mContext, "该音频无法播放");
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtils.showToast(mContext, "无效的播放地址");
                        }

                    }
                });
                addView(view);
                break;
            // 文章
            default:
                view = LayoutInflater.from(mContext).inflate(R.layout.include_post_normal, this, false);
                if (getChildAt(0) != null) {
                    removeAllViews();
                }
                ImageView ivThumb = (ImageView) view.findViewById(R.id.ivThumb);
                TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                ImageDisplayUtil.displayImage(mContext, ivThumb, teacherZoneDynamicsInfo.getNews_thumb());
                tvTitle.setText(StringUtils.replaceNullToEmpty(teacherZoneDynamicsInfo.getNews_title()));
                addView(view);
                break;
        }
    }
}
