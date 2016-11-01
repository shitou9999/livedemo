package tv.kuainiu.ui.home.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.R;
import tv.kuainiu.app.OnItemClickListener;
import tv.kuainiu.modle.HotPonit;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.ScreenUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.widget.NoScrollGridView;

/**
 * @author nanck on 2016/7/29.
 */
public class HotPointAdapter extends RecyclerView.Adapter<HotPointAdapter.HotPointHolder> {


    private Activity mContext;
    private OnItemClickListener mOnItemClickListener;
    /**
     * 热门观点List
     */
    private List<HotPonit> mHotPointList = new ArrayList<>();

    public HotPointAdapter(Activity context, List<HotPonit> hotPointList, OnItemClickListener mOnItemClickListener) {
        mContext = context;
        mHotPointList = hotPointList;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mHotPointList.size();
    }


    @Override
    public void onBindViewHolder(final HotPointHolder holder, int position) {
        final HotPonit mHotPoint = mHotPointList.get(position);
        holder.mTvTeacherName.setText(StringUtils.replaceNullToEmpty(mHotPoint.getTeacher_info()==null?"":mHotPoint.getTeacher_info().getNickname(),mHotPoint.getNickname()));
        holder.mTvHotPointContent.setText(StringUtils.replaceNullToEmpty(mHotPoint.getDescription()));
        ImageDisplayUtil.displayImage(mContext, holder.mCivAvatar, StringUtils.replaceNullToEmpty(mHotPoint.getTeacher_info()==null?"":mHotPoint.getTeacher_info().getAvatar(),mHotPoint.getAvatar()), R.mipmap.default_avatar);
        holder.mTvHotPointSupport.setText(String.format(Locale.CHINA, "(%d)", mHotPoint.getSupport_num()));
        if (mHotPoint.getTag_list() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.fragment_home_item_hot_point_flag_item,//只能有一个定义了id的TextView
                    mHotPoint.getTag_list());//data既可以是数组，也可以是List集合
            holder.mLlFlag.setAdapter(adapter);
        }
        if (mHotPoint.getIs_follow() == 0) {
            holder.mTvFollowButton.setText(" +关注");
        } else {
            holder.mTvFollowButton.setText("已关注");
        }
        switch (mHotPoint.getQuote_type()) {
            case Constant.NEWS_TYPE_ARTICLE:
                holder.mTvType.setText("文章");
                break;
            case Constant.NEWS_TYPE_VIDEO:
                holder.mTvType.setText("视频");
                break;
            case Constant.NEWS_TYPE_VOICE:
                holder.mTvType.setText("声音");
                break;
            case Constant.NEWS_TYPE_LIVE:
                holder.mTvType.setText("直播");
                break;
        }
        holder.mTvHotPointContent.setTag(mHotPoint);
        holder.mTvTeacherName.setTag(mHotPoint);
        holder.mCivAvatar.setTag(mHotPoint);
        holder.mCivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(v);
                }
            }
        });
        holder.mTvHotPointContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(v);
                }
            }
        });
        holder.mTvTeacherName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(v);
                }
            }
        });
        holder.mTvFollowButton.setSelected(mHotPoint.getIs_follow() != 0);
        holder.mTvFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    view.setTag(mHotPoint);
                    mOnItemClickListener.onClick(view);
                }
            }
        });
        if (mHotPoint.getIs_support() == 0) {
            holder.mLlHotPointSupport.setSelected(false);
        } else {
            holder.mLlHotPointSupport.setSelected(true);
        }
        holder.tvDate.setText(DateUtil.getTimeString_MM_dd(mHotPoint.getCreate_date()));
        holder.mLlHotPointSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点赞
                if (mOnItemClickListener != null) {
                    view.setTag(mHotPoint);
                    view.setTag(R.id.tv_hot_point_support, holder.mTvHotPointSupport);
                    mOnItemClickListener.onClick(view);
                }
//
            }
        });
    }


    @Override
    public HotPointHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int screenWidth = ScreenUtils.getScreenWidth(mContext);
//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams((int) (screenWidth * 0.75), (int) (screenWidth * 0.75 / 1.5));
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams((int) (screenWidth * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_home_item_hot_point_item, parent, false);
        view.setLayoutParams(lp);
        return new HotPointHolder(view);
    }

    //热点指数
    static class HotPointHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.civ_avatar)
        CircleImageView mCivAvatar;
        @BindView(R.id.tv_teacher_name)
        TextView mTvTeacherName;
        @BindView(R.id.tv_follow_button)
        TextView mTvFollowButton;
        @BindView(R.id.ll_flag)
        NoScrollGridView mLlFlag;
        @BindView(R.id.tvType)
        TextView mTvType;
        @BindView(R.id.tv_hot_point_content)
        TextView mTvHotPointContent;
        @BindView(R.id.tv_hot_point_support)
        TextView mTvHotPointSupport;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.ll_hot_point_support)
        LinearLayout mLlHotPointSupport;

        public HotPointHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
