package tv.kuainiu.ui.friends.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.R;
import tv.kuainiu.app.OnItemClickListener;
import tv.kuainiu.modle.LiveInfo;
import tv.kuainiu.modle.TeacherZoneDynamics;
import tv.kuainiu.modle.TeacherZoneDynamicsInfo;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.modle.push.CustomVideo;
import tv.kuainiu.ui.adapter.UpLoadImageAdapter;
import tv.kuainiu.ui.teachers.activity.TeacherZoneActivity;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.ImageDisplayUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.widget.ExpandGridView;
import tv.kuainiu.widget.PostParentLayout;

/**
 */
public class FriendsPostAdapter extends RecyclerView.Adapter<FriendsPostAdapter.ViewHolder> {
    public static final int CUSTOM_VIEW_POINT = 0;
    public static final int CUSTOM_VIDEO = 1;
    private Activity mContext;
    private List<TeacherZoneDynamics> teacherZoneDynamicsList;
    private List<CustomVideo> customVideoList;
    private List<LiveInfo> customLiveList;
    private int type = CUSTOM_VIEW_POINT;
    private OnItemClickListener onItemClickListener;
    private boolean isTeacher = false;

    public FriendsPostAdapter(Activity context, boolean isTeacher) {
        mContext = context;
        this.isTeacher = isTeacher;
    }

    public FriendsPostAdapter(Activity context, int type, boolean isTeacher) {
        mContext = context;
        this.type = type;
        this.isTeacher = isTeacher;
    }

    public void setOnClick(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTeacherZoneDynamicsList(List<TeacherZoneDynamics> teacherZoneDynamicsList) {
        this.teacherZoneDynamicsList = teacherZoneDynamicsList;
    }

    public void setCustomVideoList(List<CustomVideo> customVideoList) {
        this.customVideoList = customVideoList;
    }

    public void setCustomLiveList(List<LiveInfo> customLiveList) {
        this.customLiveList = customLiveList;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        switch (type) {
            case CUSTOM_VIEW_POINT:
                count = teacherZoneDynamicsList == null ? 0 : teacherZoneDynamicsList.size();
                break;
            case CUSTOM_VIDEO:
                count = customVideoList == null ? 0 : customVideoList.size();
                break;
        }
        return count;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0) {
            holder.vFriendsTopPadding.setVisibility(View.INVISIBLE);
        } else {
            holder.vFriendsTopPadding.setVisibility(View.GONE);
        }
        switch (type) {
            case CUSTOM_VIEW_POINT:
                dataViewPoint(holder, position);
                break;
            case CUSTOM_VIDEO:
                dataVideo(holder, position);
                break;
        }
    }

    private void dataViewPoint(ViewHolder holder, int position) {
        final TeacherZoneDynamics info = teacherZoneDynamicsList.get(position);
        ImageDisplayUtils.display(mContext, StringUtils.replaceNullToEmpty(info.getAvatar()), holder.mCivFriendsPostHead, R.mipmap.default_avatar);
        holder.mTvFriendsPostNickname.setText(StringUtils.replaceNullToEmpty(info.getNickname()));
        holder.mTvFriendsPostContent.setText(StringUtils.replaceNullToEmpty(info.getDescription()));

        String ct = mContext.getString(R.string.value_comment_count, StringUtils.replaceNullToEmpty(info.getComment_num(), "0"));
        holder.mTvFriendsPostComment.setText(ct);
        String[] tags = info.getTag_list();
        String tagString = "";
        if (tags != null && tags.length > 0) {
            for (int i = 0; i < tags.length; i++) {
                tagString += tags[i] + "　";
            }

        }
        holder.mTvFriendsPostType.setText(tagString);
        holder.mTvFriendsPostTime.setText(DateUtil.getDurationString("MM-dd HH:mm", info.getCreate_date()));
        String lt = mContext.getString(R.string.value_comment_like, StringUtils.replaceNullToEmpty(info.getSupport_num(), "0"));
        holder.mTvFriendsPostLike.setText(lt);
        TeacherZoneDynamicsInfo teacherZoneDynamicsInfo = info.getNews_info();
        LiveInfo liveInfo = info.getLive_info();
        if (liveInfo != null) {
            holder.mPostParentLayout.setPostType(liveInfo);
        } else {
            holder.mPostParentLayout.setPostType(teacherZoneDynamicsInfo);
        }
        holder.mPostParentLayout.setOnItemClickListener(onItemClickListener);
        String startTime = DateUtil.getCurrentDate() + " 09:30:00";
        long startTimeL = DateUtil.getLongTimeFromStr(startTime, "yyyy-MM-dd HH:mm:ss");
        String endTime = DateUtil.getCurrentDate() + " 15:00:00";
        long endTimeL = DateUtil.getLongTimeFromStr(endTime, "yyyy-MM-dd HH:mm:ss");
        if (DateUtil.toJava(info.getCreate_date()) < startTimeL || endTimeL < DateUtil.toJava(info.getCreate_date())) {
            holder.mViewFriendsPostLine.setBackgroundColor(Color.BLACK);
            holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time_red);
        } else {
            holder.mViewFriendsPostLine.setBackgroundColor(Color.RED);
            holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time_black);
        }

        // Hide bottom line
        int v = position == teacherZoneDynamicsList.size() - 1 ? View.GONE : View.VISIBLE;
        holder.mViewFriendsPostLineBottom.setVisibility(v);
        if (info.getIs_support() == Constant.FAVOURED) {
            holder.ivSupport.setVisibility(View.INVISIBLE);
            holder.mTvFriendsPostLike.setSelected(true);
        } else {
            holder.ivSupport.setVisibility(View.VISIBLE);
            holder.mTvFriendsPostLike.setSelected(false);
        }
        holder.ivSupport.setTag(info);
        holder.ivSupport.setTag(R.id.tv_friends_post_like, holder.mTvFriendsPostLike);
        holder.ivSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(view);
                }
            }
        });
        holder.mTvFriendsPostComment.setTag(info);
        holder.mTvFriendsPostComment.setTag(R.id.tv_friends_post_comment, holder.mTvFriendsPostComment);
        holder.mTvFriendsPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(v);
                }
            }
        });
        holder.mCivFriendsPostHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info.getTeacher_info() != null) {
                    TeacherZoneActivity.intoNewIntent(mContext, info.getTeacher_info().getId());
                }
            }
        });
        String thumb = StringUtils.replaceNullToEmpty(info.getThumb());
        if (!TextUtils.isEmpty(thumb) && !"false".equals(thumb)) {
            String[] array = thumb.split(",");
            if (array != null) {
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < array.length; i++) {
                    if (!TextUtils.isEmpty(array[i])) {
                        list.add(array[i]);
                    }
                }
                if (list.size() > 0) {
                    UpLoadImageAdapter mUpLoadImageAdapter = new UpLoadImageAdapter(list, (tv.kuainiu.ui.activity.BaseActivity) mContext, 1, true);
                    holder.exgv_appraisal_pic.setAdapter(mUpLoadImageAdapter);
                    holder.exgv_appraisal_pic.setVisibility(View.VISIBLE);
                } else {
                    holder.exgv_appraisal_pic.setVisibility(View.GONE);
                }
            } else {
                holder.exgv_appraisal_pic.setVisibility(View.GONE);
            }
        } else {
            holder.exgv_appraisal_pic.setVisibility(View.GONE);
        }
    }

    private void dataVideo(ViewHolder holder, int position) {
       final CustomVideo info = customVideoList.get(position);
        ImageDisplayUtils.display(mContext, StringUtils.replaceNullToEmpty(info.getAvatar()), holder.mCivFriendsPostHead, R.mipmap.default_avatar);
        holder.mTvFriendsPostNickname.setText(StringUtils.replaceNullToEmpty(info.getNickname()));
        holder.mTvFriendsPostContent.setText(StringUtils.replaceNullToEmpty(info.getDescription()));
        holder.mTvFriendsPostType.setText(StringUtils.replaceNullToEmpty(info.getCatname()));
        String ct = mContext.getString(R.string.value_comment_count, StringUtils.replaceNullToEmpty(info.getComment_num(), "0"));
        holder.mTvFriendsPostComment.setText(ct);
        holder.mTvFriendsPostTime.setText(DateUtil.getDurationString("MM-dd HH:mm", info.getInputtime()));
        String lt = mContext.getString(R.string.value_comment_like, StringUtils.replaceNullToEmpty(info.getSupport_num(), "0"));
        holder.mTvFriendsPostLike.setText(lt);
        TeacherZoneDynamicsInfo news_info = new TeacherZoneDynamicsInfo();
        news_info.setType(info.getType());
        news_info.setNews_video_id(info.getVideo_id());
        news_info.setNews_title(info.getTitle());
        news_info.setNews_catid(info.getCat_id());
        news_info.setNews_inputtime(info.getInputtime());
        news_info.setNews_id(info.getId());
        holder.mPostParentLayout.setPostType(news_info);
        holder.mPostParentLayout.setOnItemClickListener(onItemClickListener);
        String startTime = DateUtil.getCurrentDate() + " 09:30:00";
        long startTimeL = DateUtil.getLongTimeFromStr(startTime, "yyyy-MM-dd HH:mm:ss");
        String endTime = DateUtil.getCurrentDate() + " 15:00:00";
        long endTimeL = DateUtil.getLongTimeFromStr(endTime, "yyyy-MM-dd HH:mm:ss");
        if (DateUtil.toJava(info.getInputtime()) < startTimeL || endTimeL < DateUtil.toJava(info.getInputtime())) {
            holder.mViewFriendsPostLine.setBackgroundColor(Color.BLACK);
            holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time_red);
        } else {
            holder.mViewFriendsPostLine.setBackgroundColor(Color.RED);
            holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time_black);
        }


        // Hide bottom live
        int v = position == customVideoList.size() - 1 ? View.GONE : View.VISIBLE;
        holder.mViewFriendsPostLineBottom.setVisibility(v);
        if (info.getIs_support() == Constant.FAVOURED) {
            holder.ivSupport.setVisibility(View.INVISIBLE);
            holder.mTvFriendsPostLike.setSelected(true);
        } else {
            holder.ivSupport.setVisibility(View.VISIBLE);
            holder.mTvFriendsPostLike.setSelected(false);
        }
        holder.ivSupport.setTag(info);
        holder.ivSupport.setTag(R.id.tv_friends_post_like, holder.mTvFriendsPostLike);
        holder.ivSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(view);
                }
            }
        });
        holder.mTvFriendsPostComment.setTag(info);
        holder.mTvFriendsPostComment.setTag(R.id.tv_friends_post_comment, holder.mTvFriendsPostComment);
        holder.mTvFriendsPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(v);
                }
            }
        });
        holder.exgv_appraisal_pic.setVisibility(View.GONE);
    }

    /*private void dataLive(ViewHolder holder, int position) {
        LiveInfo info = customLiveList.get(position);
        ImageDisplayUtils.display(mContext, StringUtils.replaceNullToEmpty(info.getAvatar()), holder.mCivFriendsPostHead, R.mipmap.default_avatar);
        holder.mTvFriendsPostNickname.setText(StringUtils.replaceNullToEmpty(info.getAnchor()));
        holder.mTvFriendsPostContent.setText(StringUtils.replaceNullToEmpty(info.getTitle()));
        holder.mTvFriendsPostType.setText("");
        holder.mTvFriendsPostTime.setText(DateUtil.formatDate(info.getCreate_date()));
        holder.mTvFriendsPostComment.setVisibility(View.GONE);

        String lt = mContext.getString(R.string.value_comment_like, StringUtils.replaceNullToEmpty(info.getSupport(), "0"));
        holder.mTvFriendsPostLike.setText(lt);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        holder.mTvFriendsPostLike.setLayoutParams(lp);
        holder.mPostParentLayout.setPostType(info);

        switch (info.getLive_status()) {
            case Constans.LIVE_END://直播结束
                holder.mViewFriendsPostLine.setBackgroundColor(mContext.getResources().getColor(R.color.colorGrey450));
                holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time_gred);
                break;

            case Constans.LIVE_ING://直播中
                holder.mViewFriendsPostLine.setBackgroundColor(Color.RED);
                holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time_black);
                break;
            case Constans.LiVE_UN_START://直播未开始
                holder.mViewFriendsPostLine.setBackgroundColor(Color.BLACK);
                holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time_red);
                break;
            default:
                holder.mViewFriendsPostLine.setBackgroundColor(Color.BLACK);
                holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time_red);
                break;
        }
        // Hide bottom live
        int v = position == customLiveList.size() - 1 ? View.GONE : View.VISIBLE;
        holder.mViewFriendsPostLineBottom.setVisibility(v);

        if (info.getIs_supported() == Constant.FAVOURED) {
            holder.ivSupport.setVisibility(View.INVISIBLE);
            holder.mTvFriendsPostLike.setSelected(true);
        } else {
            holder.ivSupport.setVisibility(View.VISIBLE);
            holder.mTvFriendsPostLike.setSelected(false);
        }
        holder.ivSupport.setTag(info);
        holder.ivSupport.setTag(R.id.tv_friends_post_like, holder.mTvFriendsPostLike);
        holder.ivSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(view);
                }
            }
        });
        holder.exgv_appraisal_pic.setVisibility(View.GONE);
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_friends_post, parent, false);
        ViewHolder vh = new ViewHolder(view);
//        if (isTeacher) {
//            vh.mCivFriendsPostHead.setVisibility(View.GONE);
//            vh.mTvFriendsPostNickname.setVisibility(View.GONE);
//        }
        return vh;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.vFriendsTopPadding)
        View vFriendsTopPadding;
        @BindView(R.id.tv_friends_post_time)
        TextView mTvFriendsPostTime;
        @BindView(R.id.view_friends_post_line)
        View mViewFriendsPostLine;
        @BindView(R.id.civ_friends_post_head)
        CircleImageView mCivFriendsPostHead;
        //        @BindView(R.id.iv_friends_post_temp) ImageView mIvFriendsTemp;
        @BindView(R.id.pl_friends_post_group)
        PostParentLayout mPostParentLayout;
        @BindView(R.id.tv_friends_post_nickname)
        TextView mTvFriendsPostNickname;
        @BindView(R.id.tv_friends_post_content)
        TextView mTvFriendsPostContent;
        @BindView(R.id.tv_friends_post_type)
        TextView mTvFriendsPostType;
        @BindView(R.id.tv_friends_post_comment)
        TextView mTvFriendsPostComment;
        @BindView(R.id.tv_friends_post_like)
        TextView mTvFriendsPostLike;
        @BindView(R.id.view_friends_post_line_bottom)
        View mViewFriendsPostLineBottom;
        @BindView(R.id.ivSupport)
        ImageView ivSupport;
        @BindView(R.id.exgv_appraisal_pic)
        ExpandGridView exgv_appraisal_pic;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
