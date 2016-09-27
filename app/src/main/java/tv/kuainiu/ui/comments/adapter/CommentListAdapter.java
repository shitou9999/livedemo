package tv.kuainiu.ui.comments.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.R;
import tv.kuainiu.modle.CommentItem;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.Utils;
import tv.kuainiu.widget.LinkTextView;

/**
 * @author nanck on 2016/4/5.
 */
public class CommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnClickListener {
    private Context mContext;
    /**
     * 热门评论集合
     */
    private List<CommentItem> mHotList = new ArrayList<>();
    /**
     * 所有评论集合
     */
    private LinkedList<CommentItem> mAllList = new LinkedList<>();
    /**
     * Item点击事件监听对象
     */
    private OnRecyclerItemClickListener mOnRecyclerItemClickListener;
    /**
     * 赞按钮点击事件监听对象
     */
    private OnStart mOnStart;


    enum ITEM_TYPE {
        TAG_HOT,
        LIST_HOT,
        TAG_ALL,
        LIST_ALL
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        mOnRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    public void setOnStart(OnStart onStart) {
        mOnStart = onStart;
    }

    public CommentListAdapter(Context context) {
        mContext = context;
    }

    public void setAllData(LinkedList<CommentItem> list) {
        mAllList = list;
    }

    public void setHotData(List<CommentItem> list) {
        mHotList = list;
    }

    @Override
    public int getItemCount() {
        return mAllList.size() + 2 + mHotList.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.TAG_HOT.ordinal()) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment_tag_hot, parent, false);
            return new TagHotViewHolder(view);
        } else if (viewType == ITEM_TYPE.TAG_ALL.ordinal()) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment_tag_all, parent, false);
            return new TagAllViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment_post, parent, false);
            ListViewHolder vh = new ListViewHolder(view);
            vh.itemView.setOnClickListener(this);
            vh.textFavour.setOnClickListener(this);
            vh.textContent.setOnClickListener(this);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ListViewHolder) {
            if (getItemViewType(position) == ITEM_TYPE.LIST_HOT.ordinal()) {
                bindViewForList((ListViewHolder) holder, mHotList, (position - 1));
            }
            if (getItemViewType(position) == ITEM_TYPE.LIST_ALL.ordinal()) {
                bindViewForList((ListViewHolder) holder, mAllList, (position - 2 - mHotList.size()));
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (position == 0) {
            type = ITEM_TYPE.TAG_HOT.ordinal();
        } else if (position >= 1 && position < (1 + mHotList.size())) {
            type = ITEM_TYPE.LIST_HOT.ordinal();
        } else if (position == (1 + mHotList.size())) {
            type = ITEM_TYPE.TAG_ALL.ordinal();
        } else {
            type = ITEM_TYPE.LIST_ALL.ordinal();
        }
        return type;
    }


    static class TagHotViewHolder extends RecyclerView.ViewHolder {
        public TagHotViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class TagAllViewHolder extends RecyclerView.ViewHolder {
        public TagAllViewHolder(View itemView) {
            super(itemView);
        }
    }


    static class ListViewHolder extends RecyclerView.ViewHolder {
        // @Bind(R.id.tv_reply_tag) TextView textReplyTag;
        @BindView(R.id.civ_head)
        CircleImageView civHead;
        @BindView(R.id.tv_nickname) TextView textNickname;
        @BindView(R.id.tv_content)
        LinkTextView textContent;
        @BindView(R.id.tv_content_reply) TextView textContentReply;
        @BindView(R.id.tv_date) TextView textDate;
        @BindView(R.id.tv_favour) TextView textFavour;
        @BindView(R.id.tv_device_source) TextView textDeviceSource;

        @BindView(R.id.tv_nickname2) TextView textNickname2;
        @BindView(R.id.tv_teacher_flag) TextView textTeacherFlag;
        @BindView(R.id.iv_v) ImageView imageLableV;
        @BindView(R.id.iv_v2) ImageView imageLableV2;
        @BindView(R.id.ll_reply_area) LinearLayout llReplyArea;

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    /**
     * 点赞 TextView
     *
     * @param textView
     * @param item
     */
    public void resetFavourView(TextView textView, CommentItem item) {
        if (null == textView || null == item) {
            return;
        }
        // 设置赞总数
        textView.setText(String.valueOf(item.getSupport()));
        String text = (item.getSupport() > 999) ? mContext.getString(R.string.more_than_999) : String.valueOf(item.getSupport());
        textView.setText(text);
        textView.setSelected(Constant.FAVOURED == item.getIs_support());

    }

    /**
     * 绑定评论列表视图
     *
     * @param holder
     * @param list
     * @param position
     */
    private void bindViewForList(ListViewHolder holder, List<CommentItem> list, int position) {
        CommentItem commentItem = list.get(position);

        String content = commentItem.getContent();
        String parentNickname = commentItem.getParent_user_name();
        String parentContent = commentItem.getParent_comment_content();

        holder.itemView.setTag(commentItem);
        holder.textFavour.setTag(commentItem);
        holder.textContent.setTag(commentItem);

        // 昵称
        holder.textNickname.setText(commentItem.getNickname().trim());
        if (Constant.BASE_TRUE == commentItem.getIs_teacher()) {
            holder.imageLableV.setVisibility(View.VISIBLE);
            holder.textTeacherFlag.setVisibility(View.VISIBLE);
        } else {
            holder.imageLableV.setVisibility(View.GONE);
            holder.textTeacherFlag.setVisibility(View.GONE);
        }

        // 来源
        if (TextUtils.isEmpty(commentItem.getSource_show())) {
            holder.textDeviceSource.setVisibility(View.GONE);
        } else {
//            holder.textDeviceSource.setText(mContext.getString(R.string.value_comment_source, commentItem.getSource()));
            holder.textDeviceSource.setText(commentItem.getSource_show());
            holder.textDeviceSource.setVisibility(View.VISIBLE);
        }

        // 内容
//        holder.textContent.setText(Html.fromHtml(content));
        Utils.showComment(holder.textContent, content);

        if (TextUtils.isEmpty(commentItem.getParent_comment_id()) || TextUtils.isEmpty(parentContent)) {
            holder.llReplyArea.setVisibility(View.GONE);
            holder.textContentReply.setVisibility(View.GONE);
        } else {
            holder.llReplyArea.setVisibility(View.VISIBLE);
            holder.textContentReply.setVisibility(View.VISIBLE);

            if (Constant.BASE_TRUE == commentItem.getParent_is_teacher()) {
                holder.imageLableV2.setVisibility(View.VISIBLE);
            } else {
                holder.imageLableV2.setVisibility(View.GONE);
            }
            holder.textNickname2.setText(parentNickname);
            holder.textContentReply.setText(Html.fromHtml(parentContent));

        }

        // 头像
        ImageDisplayUtil.displayImage(mContext, holder.civHead, commentItem.getAvatar(), R.mipmap.head_nor);
        // 赞
        resetFavourView(holder.textFavour, commentItem);
        // 评论时间
        long time =Long.parseLong(commentItem.getCreate_date());
        String text = DateUtil.getDurationString(DateUtil.toJava(time));
        holder.textDate.setText(text);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_favour:
                if (mOnStart != null) {
                    mOnStart.onStart(v);
                }
                break;
//            case R.id.tv_content:
//                if (mOnRecyclerItemClickListener != null) {
//                    mOnRecyclerItemClickListener.onItemClick(v);
//                }
//                break;
            default:
                if (mOnRecyclerItemClickListener != null) {
                    mOnRecyclerItemClickListener.onItemClick(v);
                }
                break;
        }
    }


    /**
     * Item点击事件
     */
    public interface OnRecyclerItemClickListener {
        void onItemClick(View v);
    }

    /**
     * 赞按钮点击事件
     */
    public interface OnStart {
        void onStart(View v);
    }
}