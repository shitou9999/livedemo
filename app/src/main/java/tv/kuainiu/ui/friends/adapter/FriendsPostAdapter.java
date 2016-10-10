package tv.kuainiu.ui.friends.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.R;
import tv.kuainiu.app.Constans;
import tv.kuainiu.app.OnItemClickListener;
import tv.kuainiu.modle.LiveInfo;
import tv.kuainiu.modle.TeacherZoneDynamics;
import tv.kuainiu.modle.TeacherZoneDynamicsInfo;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.modle.push.CustomVideo;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.ImageDisplayUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.widget.PostParentLayout;

/**
 */
public class FriendsPostAdapter extends RecyclerView.Adapter<FriendsPostAdapter.ViewHolder> {
    public static final int CUSTOM_VIEW_POINT = 0;
    public static final int CUSTOM_VIDEO = 1;
    public static final int CUSTOM_LIVE = 2;
    private Activity mContext;
    private List<TeacherZoneDynamics> teacherZoneDynamicsList;
    private List<CustomVideo> customVideoList;
    private List<LiveInfo> customLiveList;
    private int type = CUSTOM_VIEW_POINT;
    private OnItemClickListener onItemClickListener;

    public FriendsPostAdapter(Activity context) {
        mContext = context;
    }

    public FriendsPostAdapter(Activity context, int type) {
        mContext = context;
        this.type = type;
    }

    public void setOnClick(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTeacherZoneDynamicsList(List<TeacherZoneDynamics> teacherZoneDynamicsList) {
        this.teacherZoneDynamicsList=teacherZoneDynamicsList;
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
            case CUSTOM_LIVE:
                count = customLiveList == null ? 0 : customLiveList.size();
                break;
        }
        return count;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        switch (type) {
            case CUSTOM_VIEW_POINT:
                dataViewPoint(holder, position);
                break;
            case CUSTOM_VIDEO:
                dataVideo(holder, position);
                break;
            case CUSTOM_LIVE:
                dataLive(holder, position);
                break;
        }
    }

    private void dataViewPoint(ViewHolder holder, int position) {
       TeacherZoneDynamics info = teacherZoneDynamicsList.get(position);
        ImageDisplayUtils.display(mContext, StringUtils.replaceNullToEmpty(info.getAvatar()), holder.mCivFriendsPostHead, R.mipmap.default_avatar);
        holder.mTvFriendsPostNickname.setText(StringUtils.replaceNullToEmpty(info.getNickname()));
        holder.mTvFriendsPostContent.setText(StringUtils.replaceNullToEmpty(info.getDescription()));

        String ct = mContext.getString(R.string.value_comment_count, StringUtils.replaceNullToEmpty(info.getComment_num(), "0"));
        holder.mTvFriendsPostComment.setText(ct);
        holder.mTvFriendsPostType.setText(StringUtils.replaceNullToEmpty(info.getNews_info() != null ? info.getNews_info().getNews_catname() : ""));
        holder.mTvFriendsPostTime.setText(DateUtil.getDurationString("HH:ss", info.getCreate_date()));
        String lt = mContext.getString(R.string.value_comment_like, StringUtils.replaceNullToEmpty(info.getSupport_num(), "0"));
        holder.mTvFriendsPostLike.setText(lt);
        TeacherZoneDynamicsInfo teacherZoneDynamicsInfo=info.getNews_info();
        if(teacherZoneDynamicsInfo!=null) {
            teacherZoneDynamicsInfo.setVideo_id(String.valueOf(info.getNews_id()));
        }
        holder.mPostParentLayout.setPostType(teacherZoneDynamicsInfo);
        //不是直播中就是黑色边框
        holder.mViewFriendsPostLine.setBackgroundColor(Color.BLACK);
        holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time_red);
        // Hide bottom line
        int v = position == teacherZoneDynamicsList.size() - 1 ? View.GONE : View.VISIBLE;
        holder.mViewFriendsPostLineBottom.setVisibility(v);
        if(info.getIs_support()== Constant.FAVOURED){
            holder.ivSupport.setVisibility(View.INVISIBLE);
            holder.mTvFriendsPostLike.setSelected(true);
        }else{
            holder.ivSupport.setVisibility(View.VISIBLE);
            holder.mTvFriendsPostLike.setSelected(false);
        }
        holder.ivSupport.setTag(info);
        holder.ivSupport.setTag(R.id.tv_friends_post_like,holder.mTvFriendsPostLike);
        holder.ivSupport.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(view);
                }
            }
        });
    }

    private void dataVideo(ViewHolder holder, int position) {
        CustomVideo info = customVideoList.get(position);
        ImageDisplayUtils.display(mContext, StringUtils.replaceNullToEmpty(info.getAvatar()), holder.mCivFriendsPostHead, R.mipmap.default_avatar);
        holder.mTvFriendsPostNickname.setText(StringUtils.replaceNullToEmpty(info.getNickname()));
        holder.mTvFriendsPostContent.setText(StringUtils.replaceNullToEmpty(info.getDescription()));
        holder.mTvFriendsPostType.setText(StringUtils.replaceNullToEmpty(info.getCatname()));
        String ct = mContext.getString(R.string.value_comment_count, StringUtils.replaceNullToEmpty(info.getComment_num(), "0"));
        holder.mTvFriendsPostComment.setText(ct);
        holder.mTvFriendsPostTime.setText(DateUtil.getDurationString("HH:ss", info.getInputtime()));
        String lt = mContext.getString(R.string.value_comment_like, StringUtils.replaceNullToEmpty(info.getSupport_num(), "0"));
        holder.mTvFriendsPostLike.setText(lt);
        TeacherZoneDynamicsInfo news_info = new TeacherZoneDynamicsInfo();
        news_info.setType(info.getType());
        news_info.setNews_video_id(info.getId());
        news_info.setNews_title(info.getTitle());
        news_info.setNews_catid(info.getCat_id());
        news_info.setNews_inputtime(info.getInputtime());
        news_info.setVideo_id(info.getVideo_id());
        holder.mPostParentLayout.setPostType(news_info);
        //不是直播中就是黑色边框
        holder.mViewFriendsPostLine.setBackgroundColor(Color.BLACK);
        holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time_red);

        // Hide bottom live
        int v = position == customVideoList.size() - 1 ? View.GONE : View.VISIBLE;
        holder.mViewFriendsPostLineBottom.setVisibility(v);
        if(info.getIs_support()== Constant.FAVOURED){
            holder.ivSupport.setVisibility(View.INVISIBLE);
            holder.mTvFriendsPostLike.setSelected(true);
        }else{
            holder.ivSupport.setVisibility(View.VISIBLE);
            holder.mTvFriendsPostLike.setSelected(false);
        }
        holder.ivSupport.setTag(info);
        holder.ivSupport.setTag(R.id.tv_friends_post_like,holder.mTvFriendsPostLike);
        holder.ivSupport.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(view);
                }
            }
        });
    }

    private void dataLive(ViewHolder holder, int position) {
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

        if(info.getIs_supported()== Constant.FAVOURED){
            holder.ivSupport.setVisibility(View.INVISIBLE);
            holder.mTvFriendsPostLike.setSelected(true);
        }else{
            holder.ivSupport.setVisibility(View.VISIBLE);
            holder.mTvFriendsPostLike.setSelected(false);
        }
        holder.ivSupport.setTag(info);
        holder.ivSupport.setTag(R.id.tv_friends_post_like,holder.mTvFriendsPostLike);
        holder.ivSupport.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(view);
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_friends_post, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
