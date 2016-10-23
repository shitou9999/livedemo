package tv.kuainiu.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tv.kuainiu.R;
import tv.kuainiu.app.Constans;
import tv.kuainiu.modle.LiveInfo;
import tv.kuainiu.modle.TeacherZoneDynamicsInfo;
import tv.kuainiu.ui.articles.activity.PostZoneActivity;
import tv.kuainiu.ui.friends.model.BasePost;
import tv.kuainiu.ui.liveold.PlayLiveActivity;
import tv.kuainiu.ui.liveold.model.LiveParameter;
import tv.kuainiu.ui.video.VideoActivity;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.MediaPlayUtil;
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
            setVisibility(View.VISIBLE);
            loadPostView();
        } else {
            setVisibility(View.GONE);
        }
    }

    public void setPostType(LiveInfo liveInfo) {
        this.liveInfo = liveInfo;
        if (this.liveInfo != null) {
            mPostType = Constans.TYPE_LIVE;
            setVisibility(View.VISIBLE);
            loadPostView();
        } else {
            setVisibility(View.GONE);
        }
    }

    private void init(Context context) {
        mContext = context;

    }

    public void clickPlayLive(LiveInfo liveItem) {
//        if (liveItem.getLive_status() == Constans.LIVE_ING) {
//            LiveParameter liveParameter = new LiveParameter();
//            liveParameter.setLiveId(liveItem.getId());
//            liveParameter.setLiveTitle(liveItem.getTitle());
//            liveParameter.setRoomId(liveItem.getTeacher_info().getLive_roomid());
//            liveParameter.setTeacherId(liveItem.getTeacher_id());
//            PlayLiveActivity.intoNewIntent(mContext, liveParameter);
//        } else {
//            ToastUtils.showToast(mContext, liveItem.getLive_msg());
//        }
        LiveParameter liveParameter = new LiveParameter();
        liveParameter.setLiveId(liveItem.getId());
        liveParameter.setLiveTitle(liveItem.getTitle());
        liveParameter.setRoomId(liveItem.getTeacher_info().getLive_roomid());
        liveParameter.setTeacherId(liveItem.getTeacher_id());
        PlayLiveActivity.intoNewIntent(mContext, liveParameter);
    }

    private void loadPostView() {
        View view;
        switch (mPostType) {
            case Constans.TYPE_LIVE:
                view = LayoutInflater.from(mContext).inflate(R.layout.include_post_live, this, false);
//                if (getChildAt(0) != null) {
                    removeAllViews();
//                }
                TextView tvState2 = (TextView) view.findViewById(R.id.tvLiveState);
                View vLine = view.findViewById(R.id.vLine);
                TextView tvLiveDescription2 = (TextView) view.findViewById(R.id.tvLiveDescription);
                switch (liveInfo.getLive_status()) {
                    case Constans.LIVE_ING:
                        tvState2.setText(StringUtils.replaceNullToEmpty(liveInfo.getLive_msg(), "直播中"));
                        tvState2.setBackgroundColor(Color.RED);
                        vLine.setBackgroundColor(Color.RED);
                        break;
                    case Constans.LiVE_UN_START:
                        tvState2.setText(liveInfo.getLive_msg() + "/n" + DateUtil.formatDate(liveInfo.getStart_date()));
                        tvState2.setBackgroundColor(Color.RED);
                        vLine.setBackgroundColor(Color.RED);
                        break;
                    case Constans.LIVE_END:
                        tvState2.setBackgroundColor(mContext.getResources().getColor(R.color.colorGrey450));
                        vLine.setBackgroundColor(mContext.getResources().getColor(R.color.colorGrey450));
                        tvState2.setText(StringUtils.replaceNullToEmpty(liveInfo.getLive_msg(), "已结束"));
                        break;

                }
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickPlayLive(liveInfo);
                    }
                });
                tvLiveDescription2.setText(liveInfo.getTitle());
                addView(view);
                break;
            //视频
            case Constans.TYPE_VIDEO:
                view = LayoutInflater.from(mContext).inflate(R.layout.include_post_video, this, false);
//                if (getChildAt(0) != null) {
                removeAllViews();
//                }
//                ImageView ivivPlay = (ImageView) view.findViewById(R.id.ivPlay);
                TextView tvTitle2 = (TextView) view.findViewById(R.id.tvTitle);
                tvTitle2.setText(StringUtils.replaceNullToEmpty(teacherZoneDynamicsInfo.getNews_title()));
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VideoActivity.intoNewIntent(mContext, teacherZoneDynamicsInfo.getNews_id(), teacherZoneDynamicsInfo.getNews_video_id(), teacherZoneDynamicsInfo.getNews_catid(), StringUtils.replaceNullToEmpty(teacherZoneDynamicsInfo.getNews_title()));
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
                TextView tvVideoLength = (TextView) view.findViewById(R.id.tvVideoLength);
                tvVideoLength.setText(teacherZoneDynamicsInfo.getNews_title());
                ivPlayVoice.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!TextUtils.isEmpty(teacherZoneDynamicsInfo.getNews_voice_url())) {
                            try {
                                MediaPlayUtil mMediaPlayUtil = MediaPlayUtil.getInstance();
                                if (mMediaPlayUtil.isPlaying()) {
                                    mMediaPlayUtil.stop();
                                    //停止动画
                                } else {
                                    mMediaPlayUtil.play(teacherZoneDynamicsInfo.getNews_voice_url());
                                    //开始动画
                                }

                            } catch (Exception e) {
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
//                if (getChildAt(0) != null) {
                removeAllViews();
//                }
                ImageView ivThumb = (ImageView) view.findViewById(R.id.ivThumb);
                TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                if (TextUtils.isEmpty(teacherZoneDynamicsInfo.getNews_thumb())) {
                    ivThumb.setVisibility(View.GONE);
                } else {
                    ivThumb.setVisibility(View.VISIBLE);
                    ImageDisplayUtil.displayImage(mContext, ivThumb, teacherZoneDynamicsInfo.getNews_thumb(), R.mipmap.word_ic);
                }

                tvTitle.setText(StringUtils.replaceNullToEmpty(teacherZoneDynamicsInfo.getNews_title()));
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PostZoneActivity.intoNewIntent(mContext, teacherZoneDynamicsInfo.getNews_id(), teacherZoneDynamicsInfo.getNews_catid());
                    }
                });
                addView(view);
                break;
        }
    }
}
