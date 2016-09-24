package tv.kuainiu.ui.friends.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.R;
import tv.kuainiu.modle.TeacherZoneDynamics;
import tv.kuainiu.modle.TeacherZoneDynamicsInfo;
import tv.kuainiu.modle.push.CustomVideo;
import tv.kuainiu.util.ImageDisplayUtils;
import tv.kuainiu.util.StringUtils;
import tv.kuainiu.widget.PostParentLayout;

/**
 */
public class FriendsPostAdapter extends RecyclerView.Adapter<FriendsPostAdapter.ViewHolder> {
    public static final int CUSTOM_VIEW_POINT = 0;
    public static final int CUSTOM_VIDEO = 1;
    private Activity mContext;
    private List<TeacherZoneDynamics> teacherZoneDynamicsList;
    private List<CustomVideo> customVideoList;
    private int type = CUSTOM_VIEW_POINT;

    public FriendsPostAdapter(Activity context) {
        mContext = context;
    }

    public FriendsPostAdapter(Activity context, int type) {
        mContext = context;
        this.type=type;
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

    @Override public int getItemCount() {
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
        TeacherZoneDynamics teacherZoneDynamics = teacherZoneDynamicsList.get(position);
        ImageDisplayUtils.display(mContext, StringUtils.replaceNullToEmpty(teacherZoneDynamics.getAvatar()), holder.mCivFriendsPostHead, R.mipmap.ic_launcher);
        holder.mTvFriendsPostNickname.setText(StringUtils.replaceNullToEmpty(teacherZoneDynamics.getNickname()));
        holder.mTvFriendsPostContent.setText(StringUtils.replaceNullToEmpty(teacherZoneDynamics.getDescription()));

        String ct = mContext.getString(R.string.value_comment_count, StringUtils.replaceNullToEmpty(teacherZoneDynamics.getComment_num(), "0"));
        holder.mTvFriendsPostComment.setText(ct);

        String lt = mContext.getString(R.string.value_comment_like, StringUtils.replaceNullToEmpty(teacherZoneDynamics.getSupport_num(), "0"));
        holder.mTvFriendsPostLike.setText(lt);
        holder.mPostParentLayout.setPostType(teacherZoneDynamics.getNews_info());
        switch (teacherZoneDynamics.getType()) {
            case 1:
//                ImageDisplayUtils.display(mContext, R.drawable.temp1, holder.mIvFriendsTemp);
                holder.mViewFriendsPostLine.setBackgroundColor(Color.RED);
                holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time1);
                break;

            case 2:
//                ImageDisplayUtils.display(mContext, R.drawable.temp2, holder.mIvFriendsTemp);
                holder.mViewFriendsPostLine.setBackgroundColor(Color.BLACK);
                holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time2);
                break;

            default:
                holder.mViewFriendsPostLine.setBackgroundColor(Color.BLACK);
                holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time2);
                break;
        }


//        Log.d("ssfsdfsdfsdfsd", "height : " + holder.itemView.getHeight());
//        int height = holder.itemView.getHeight();
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(2, height);
//        holder.mViewFriendsPostLine.setLayoutParams(lp);
//        holder.mViewFriendsPostLineBottom.setMinimumHeight(height);


        // Hide bottom live
        int v = position == teacherZoneDynamicsList.size() - 1 ? View.GONE : View.VISIBLE;
        holder.mViewFriendsPostLineBottom.setVisibility(v);
    }
    private void dataVideo(ViewHolder holder, int position) {
        CustomVideo teacherZoneDynamics = customVideoList.get(position);
        ImageDisplayUtils.display(mContext, StringUtils.replaceNullToEmpty(teacherZoneDynamics.getAvatar()), holder.mCivFriendsPostHead, R.mipmap.ic_launcher);
        holder.mTvFriendsPostNickname.setText(StringUtils.replaceNullToEmpty(teacherZoneDynamics.getNickname()));
        holder.mTvFriendsPostContent.setText(StringUtils.replaceNullToEmpty(teacherZoneDynamics.getDescription()));

        String ct = mContext.getString(R.string.value_comment_count, StringUtils.replaceNullToEmpty(teacherZoneDynamics.getComment_num(), "0"));
        holder.mTvFriendsPostComment.setText(ct);

        String lt = mContext.getString(R.string.value_comment_like, StringUtils.replaceNullToEmpty(teacherZoneDynamics.getSupport_num(), "0"));
        holder.mTvFriendsPostLike.setText(lt);
        TeacherZoneDynamicsInfo news_info=new TeacherZoneDynamicsInfo();
        news_info.setType(teacherZoneDynamics.getType());
        news_info.setNews_video_id(teacherZoneDynamics.getId());
        news_info.setNews_title(teacherZoneDynamics.getDescription());
        news_info.setNews_catid(teacherZoneDynamics.getCat_id());
        news_info.setNews_inputtime(teacherZoneDynamics.getInputtime());
        news_info.setVideo_id(teacherZoneDynamics.getVideo_id());
        holder.mPostParentLayout.setPostType(news_info);
        switch (Integer.parseInt(teacherZoneDynamics.getType())) {
            case 1:
//                ImageDisplayUtils.display(mContext, R.drawable.temp1, holder.mIvFriendsTemp);
                holder.mViewFriendsPostLine.setBackgroundColor(Color.RED);
                holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time1);
                break;

            case 2:
//                ImageDisplayUtils.display(mContext, R.drawable.temp2, holder.mIvFriendsTemp);
                holder.mViewFriendsPostLine.setBackgroundColor(Color.BLACK);
                holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time2);
                break;

            default:
                holder.mViewFriendsPostLine.setBackgroundColor(Color.BLACK);
                holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time2);
                break;
        }


//        Log.d("ssfsdfsdfsdfsd", "height : " + holder.itemView.getHeight());
//        int height = holder.itemView.getHeight();
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(2, height);
//        holder.mViewFriendsPostLine.setLayoutParams(lp);
//        holder.mViewFriendsPostLineBottom.setMinimumHeight(height);


        // Hide bottom live
        int v = position == customVideoList.size() - 1 ? View.GONE : View.VISIBLE;
        holder.mViewFriendsPostLineBottom.setVisibility(v);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_friends_post, parent, false);
        ViewHolder vh = new ViewHolder(view);
//        int height = vh.itemView.getHeight();
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(2, height);
//        vh.mViewFriendsPostLine.setLayoutParams(lp);
        return vh;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_friends_post_time) TextView mTvFriendsPostTime;
        @BindView(R.id.view_friends_post_line) View mViewFriendsPostLine;
        @BindView(R.id.civ_friends_post_head) CircleImageView mCivFriendsPostHead;
        //        @BindView(R.id.iv_friends_post_temp) ImageView mIvFriendsTemp;
        @BindView(R.id.pl_friends_post_group) PostParentLayout mPostParentLayout;
        @BindView(R.id.tv_friends_post_nickname) TextView mTvFriendsPostNickname;
        @BindView(R.id.tv_friends_post_content) TextView mTvFriendsPostContent;
        @BindView(R.id.tv_friends_post_type) TextView mTvFriendsPostType;
        @BindView(R.id.tv_friends_post_comment) TextView mTvFriendsPostComment;
        @BindView(R.id.tv_friends_post_like) TextView mTvFriendsPostLike;
        @BindView(R.id.view_friends_post_line_bottom) View mViewFriendsPostLineBottom;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
