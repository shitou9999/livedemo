package tv.kuainiu.ui.me.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import tv.kuainiu.modle.TeacherItem;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.utils.ImageDisplayUtil;


/**
 * @author nanck 2016/3/21.
 */
public class MyFollowAdapter extends RecyclerView.Adapter<MyFollowAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<TeacherItem> mTeacherList = new ArrayList<>();
    private String addFollow;
    private String followed;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private OnFollowClickListener mOnFollowClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnFollowClickListener(OnFollowClickListener onFollowClickListener) {
        this.mOnFollowClickListener = onFollowClickListener;
    }


    public MyFollowAdapter(Context context) {
        this.mContext = context;
        init(context);
    }

    private void init(Context context) {
        addFollow = context.getResources().getString(R.string.follow_new);
        followed = context.getResources().getString(R.string.followed);
    }

    public void setData(List<TeacherItem> list) {
        this.mTeacherList = list;
    }

    @Override public int getItemCount() {
        return mTeacherList.size();
    }

    @Override
    public MyFollowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_follow, parent, false);
        view.setOnClickListener(this);
        ViewHolder vh = new ViewHolder(view);
        vh.mTvFollow.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyFollowAdapter.ViewHolder holder, int position) {
        this.position = position;
        TeacherItem item = mTeacherList.get(position);
        holder.itemView.setTag(item);
        holder.mTvFollow.setTag(item);
//        Glide.with(mContext).load(item.body_thumb).into(holder.mCivPhoto);
        ImageDisplayUtil.displayImage(mContext, holder.mCivPhoto, item.avatar, R.mipmap.head_nor);
        holder.mTvName.setText(item.nickname);
        holder.mTvDescribe.setText(item.slogan);

        holder.mTvFollow.setSelected(Constant.FOLLOWED == item.is_follow);
        String text = (Constant.FOLLOWED == item.is_follow) ? followed : addFollow;
        holder.mTvFollow.setText(text);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.civ_photo) CircleImageView mCivPhoto;
        @BindView(R.id.tv_name) TextView mTvName;
        @BindView(R.id.tv_describe) TextView mTvDescribe;
        @BindView(R.id.tv_follow) TextView mTvFollow;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private int position;

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_follow_root_group:
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, v.getTag());
                }
                break;
            case R.id.tv_follow:
                if (mOnFollowClickListener != null) {
                    mOnFollowClickListener.onClick(v, v.getTag(), position);
                }
                break;
            default:
                break;
        }
    }


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Object data);
    }

    public interface OnFollowClickListener {
        void onClick(View view, Object data, int position);
    }
}
