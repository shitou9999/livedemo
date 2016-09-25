package tv.kuainiu.ui.video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.IGXApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.CommentHttpUtil;
import tv.kuainiu.command.http.core.CacheConfig;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.CommentItem;
import tv.kuainiu.modle.NewsItem;
import tv.kuainiu.modle.TeacherInfo;
import tv.kuainiu.modle.VideoDetail;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.teachers.activity.TeacherZoneActivity;
import tv.kuainiu.ui.video.adapter.VideoCommentAdapter;
import tv.kuainiu.util.CustomLinearLayoutManager;
import tv.kuainiu.util.DataConverter;
import tv.kuainiu.util.DateTimeUtils;
import tv.kuainiu.util.DateUtil;
import tv.kuainiu.util.ImageDisplayUtil;
import tv.kuainiu.util.LogUtils;
import tv.kuainiu.util.StringUtils;
import tv.kuainiu.util.ToastUtils;
import tv.kuainiu.widget.DividerItemDecoration;
import tv.kuainiu.widget.ExpandListView;

/**
 * Created by sirius on 2016/9/24.
 * 点播
 */

public class VideoActivity extends BaseActivity {
    public static final String VIDEO_ID = "video_id";
    public static final String CAT_ID = "cat_id";
    @BindView(R.id.rlVideo)
    RelativeLayout rlVideo;
    @BindView(R.id.tvTiltle)
    TextView tvTiltle;
    @BindView(R.id.tvDescripion)
    TextView tvDescripion;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvViewNumber)
    TextView tvViewNumber;
    @BindView(R.id.tvSupport)
    TextView tvSupport;
    @BindView(R.id.tvCollection)
    TextView tvCollection;
    @BindView(R.id.tvShare)
    TextView tvShare;
    @BindView(R.id.tvDown)
    TextView tvDown;
    @BindView(R.id.ci_avatar)
    CircleImageView ciAvatar;
    @BindView(R.id.rl_avatar)
    RelativeLayout rlAvatar;
    @BindView(R.id.tvTheme)
    TextView tvTheme;
    @BindView(R.id.tvTeacherName)
    TextView tvTeacherName;
    @BindView(R.id.tv_follow_button)
    TextView tvFollowButton;
    @BindView(R.id.tv_follow_number)
    TextView tvFollowNumber;
    @BindView(R.id.tvIntroduce)
    TextView tvIntroduce;
    @BindView(R.id.elvComments)
    RecyclerView elvComments;
    @BindView(R.id.ciCommentAavatar)
    CircleImageView ciCommentAavatar;
    @BindView(R.id.rlCommentAvatar)
    RelativeLayout rlCommentAvatar;
    @BindView(R.id.ivCommentBtn)
    ImageView ivCommentBtn;
    @BindView(R.id.tv_comment_number)
    TextView tvCommentNumber;
    @BindView(R.id.rl_comment_btn)
    RelativeLayout rlCommentBtn;

    private String video_id = "";
    private String cat_id = "";
    private VideoDetail mVideoDetail = null;
    private List<CommentItem> listCommentItem=new ArrayList<>();

    /**
     * @param context
     * @param video_id 视频文章id
     * @param cat_id   栏目id
     */
    public static void intoNewIntent(Context context, String video_id, String cat_id) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(VIDEO_ID, video_id);
        intent.putExtra(CAT_ID, cat_id);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        video_id = getIntent().getStringExtra(VIDEO_ID);
        cat_id = getIntent().getStringExtra(CAT_ID);
        if (TextUtils.isEmpty(video_id)) {
            ToastUtils.showToast(this, "未获取到视频信息");
            finish();
            return;
        }
        initView();
        initData();
    }

    private void initView() {
        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(this);
        elvComments.setLayoutManager(mLayoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);

    }

    private void initData() {
        getNews();
        getHotCommentList();
    }

    private void dataBindComment() {
        VideoCommentAdapter videoCommentAdapter=new VideoCommentAdapter(this);
        videoCommentAdapter.setData(listCommentItem);
        elvComments.setAdapter(videoCommentAdapter);
    }

    private void dataBind() {
        tvTiltle.setText(StringUtils.replaceNullToEmpty(mVideoDetail.getTitle()));
        tvDescripion.setText(StringUtils.replaceNullToEmpty(mVideoDetail.getCatname()));//栏目

        tvDate.setText(DateUtil.getDurationString(StringUtils.replaceNullToEmpty(mVideoDetail.getInputtime())));//日期
        //播放次数
        tvViewNumber.setText(String.format(Locale.CHINA, "%s次", StringUtils.getDecimal(Integer.parseInt(StringUtils.replaceNullToEmpty(mVideoDetail.getView_num(), "0")), Constant.TEN_THOUSAND, "万", "")));
        //点赞次数
        tvSupport.setText(String.format(Locale.CHINA, "%s", StringUtils.getDecimal(Integer.parseInt(StringUtils.replaceNullToEmpty(mVideoDetail.getSupport_num(), "0")), Constant.TEN_THOUSAND, "万", "")));
        //评论次数
        tvCommentNumber.setText(String.format(Locale.CHINA, "%s", StringUtils.getDecimal(Integer.parseInt(StringUtils.replaceNullToEmpty(mVideoDetail.getComment_num(), "0")), Constant.TEN_THOUSAND, "万", "")));
        teacherDataBind(mVideoDetail.getTeacher_info());
        ImageDisplayUtil.displayImage(this, ciCommentAavatar, StringUtils.replaceNullToEmpty(IGXApplication.isLogin() ? IGXApplication.getUser().getAvatar() : ""), R.mipmap.default_avatar);

    }

    private void teacherDataBind(TeacherInfo teacherInfo) {
        if (teacherInfo == null) {
            return;
        }
        ImageDisplayUtil.displayImage(this, ciAvatar, StringUtils.replaceNullToEmpty(teacherInfo.getAvatar()), R.mipmap.default_avatar);
        tvTheme.setText(StringUtils.replaceNullToEmpty(teacherInfo.getSlogan()));
        tvTeacherName.setText(StringUtils.replaceNullToEmpty(teacherInfo.getNickname()));
        tvFollowNumber.setText(String.format(Locale.CHINA, "%s人关注", StringUtils.getDecimal(teacherInfo.getFans_count(), Constant.TEN_THOUSAND, "万", "")));
        if (teacherInfo.getIs_follow() == 0) {
            tvFollowButton.setText("+关注");
            tvFollowButton.setSelected(teacherInfo.getIs_follow() != 0);
        } else {
            tvFollowButton.setText("已关注");
        }
        tvIntroduce.setText(StringUtils.replaceNullToEmpty(teacherInfo.getIntroduce()));//介绍
    }

    private void getNews() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", video_id);
        map.put("user_id", IGXApplication.isLogin() ? IGXApplication.getUser().getUser_id() : "");
        OKHttpUtils.getInstance().syncGet(this, Api.VIDEO_OR_POST_DETAILS + ParamUtil.getParamForGet(map), Action.video_details, CacheConfig.getCacheConfig());
    }

    private void getHotCommentList() {
        CommentHttpUtil.hotComment(this, "1", cat_id, video_id, "", 1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case video_details:
                if (Constant.SUCCEED == event.getCode()) {
                    String json = event.getData().optString("data");
                    try {
                        JSONObject object = new JSONObject(json);
                        String info = object.optString("info");
                        mVideoDetail = new DataConverter<VideoDetail>().JsonToObject(info, VideoDetail.class);
                        dataBind();
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.e("VideoActivity", "获取视频详情数据失败:" + event.getMsg());
                        ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "视频详情数据解析失败"));
                        finish();
                    }
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "获取视频详情数据失败"));
                    LogUtils.e("VideoActivity", "获取视频详情数据失败:" + event.getMsg());
                    finish();
                }
                break;
            case comment_list_hot:
                if (Constant.SUCCEED == event.getCode()) {
                    String json = event.getData().optString("data");
                    LogUtils.e("VideoActivity", "json=" + json);
                    try {
                        JSONObject object = new JSONObject(json);

                        JSONArray jsonArray = object.optJSONArray("list");
                        List<CommentItem> tempNewsList = new DataConverter<CommentItem>().JsonToListObject(jsonArray.toString(), new TypeToken<List<CommentItem>>() {
                        }.getType());
                        if (tempNewsList != null && tempNewsList.size() > 0) {
                            listCommentItem.addAll(tempNewsList);
                            dataBindComment();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.e("VideoActivity", "解析视频热门评论数据失败:" + event.getMsg());
                        ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "解析视频热门评论数据失败"));
                    }
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "获取视频热门评论失败"));
                    LogUtils.e("VideoActivity", "获取视频热门评论失败：" + event.getMsg());
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
