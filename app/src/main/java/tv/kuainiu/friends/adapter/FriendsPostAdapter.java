package tv.kuainiu.friends.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.R;
import tv.kuainiu.friends.model.Message;
import tv.kuainiu.util.ImageDisplayUtils;
import tv.kuainiu.widget.PostParentLayout;

/**
 * @author nanck on 2016/7/29.
 */
public class FriendsPostAdapter extends RecyclerView.Adapter<FriendsPostAdapter.ViewHolder> {
    private Activity mContext;
    private List<Message> mMessages = new ArrayList<>();

    public FriendsPostAdapter(Activity context) {
        mContext = context;
    }

    public FriendsPostAdapter(Activity context, List<Message> messages) {
        mContext = context;
        mMessages = messages;
    }

    public void setMessages(List<Message> messages) {
        mMessages = messages;
    }

    @Override public int getItemCount() {
        return mMessages == null ? 0 : mMessages.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = mMessages.get(position);

        ImageDisplayUtils.display(mContext, message.getHead_photo(), holder.mCivFriendsPostHead, R.mipmap.ic_launcher);
        holder.mTvFriendsPostNickname.setText(message.getNickname());
        holder.mTvFriendsPostContent.setText(message.getMessage_content());

        String ct = mContext.getString(R.string.value_comment_count, message.getComment_count());
        holder.mTvFriendsPostComment.setText(ct);

        String lt = mContext.getString(R.string.value_comment_like, message.getLike_count());
        holder.mTvFriendsPostLike.setText(lt);


        holder.mPostParentLayout.setPostType(message.getType());
        switch (message.getMessage_type()) {
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


        Log.d("ssfsdfsdfsdfsd", "height : " + holder.itemView.getHeight());
//        int height = holder.itemView.getHeight();
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(2, height);
//        holder.mViewFriendsPostLine.setLayoutParams(lp);
//        holder.mViewFriendsPostLineBottom.setMinimumHeight(height);


        // Hide bottom live
        int v = position == mMessages.size() - 1 ? View.GONE : View.VISIBLE;
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
