package tv.kuainiu.ui.live.adapter;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import tv.kuainiu.R;
import tv.kuainiu.app.OnItemClickListener;
import tv.kuainiu.modle.LiveInfo;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.adapter.ViewPagerAdapter;
import tv.kuainiu.ui.liveold.PlayLiveActivity;
import tv.kuainiu.ui.liveold.ReplayLiveActivity;
import tv.kuainiu.ui.liveold.model.LiveParameter;
import tv.kuainiu.ui.teachers.activity.TeacherZoneActivity;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.DensityUtils;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.ScreenUtils;
import tv.kuainiu.utils.StringUtils;

import static tv.kuainiu.R.id.ivIamge;

/**
 * @author nanck on 2016/7/29.
 */
public class ReadingTapeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Activity mContext;
    private List<LiveInfo> mBannerList;
    private List<LiveInfo> mLiveList;
    private List<LiveInfo> mLiveListList;
    int mBannerListSize = 0;
    int mLiveListSize = 0;
    int mLiveListListSize = 0;
    private int type = ReadingTapeAdapter.ZHI_BO;
    private static final int BANNER = 2;
    private static final int LIVE = 3;
    private static final int LIVE_ITEM = 4;
    public static final int ZHI_BO = 1;
    public static final int MY_LIVE = 3;
    public static final int YU_YUE = 2;
    List<Integer> types = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public ReadingTapeAdapter(Activity context, int type) {
        mContext = context;
        this.type = type;
    }


    public void setBannerList(List<LiveInfo> mBannerList) {
        this.mBannerList = mBannerList;
    }

    public void setLiveList(List<LiveInfo> liveList) {
        this.mLiveList = liveList;
    }

    public void setLiveListList(List<LiveInfo> liveListList) {
        this.mLiveListList = liveListList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        types.clear();
        if (mBannerList != null && mBannerList.size() > 0) {
            types.add(BANNER);
        }
        if (mLiveList != null && mLiveList.size() > 0) {
            types.add(LIVE);
        }

        mLiveListListSize = (mLiveListList == null || mLiveListList.size() < 1) ? 0 : mLiveListList.size();
        return types.size() + mLiveListListSize;
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if (position < types.size()) {
            type = types.get(position);
        } else {
            type = LIVE_ITEM;
        }
        return type;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case BANNER:
                onBindBannerViewHolder((BannerViewHolder) holder);
                break;
            case LIVE:
                onBindLiveViewHolder((LiveViewHolder) holder);
                break;
            default:
                onBindLiveItemViewHolder((ViewHolder) holder, position);
                break;
        }
    }


    //绑定Banner
    public void onBindBannerViewHolder(BannerViewHolder holder) {
        holder.mIndicator.setVisibility(View.VISIBLE);
        List<View> mList = new ArrayList<>();
        for (int i = 0; i < mBannerList.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_live_reading_tap_viewpager_item2, null);
            ImageView ivIamge = (ImageView) view.findViewById(R.id.ivIamge);
            TextView tvLiveingNumber = (TextView) view.findViewById(R.id.tvLiveingNumber);
            RelativeLayout rl_avatar = (RelativeLayout) view.findViewById(R.id.rl_avatar);//头像
            CircleImageView civ_avatar = (CircleImageView) view.findViewById(R.id.civ_avatar);//头像
            ImageView mIvLeft = (ImageView) view.findViewById(R.id.iv_left);//左箭头
            ImageView mIvRight = (ImageView) view.findViewById(R.id.iv_right);//右箭头
            mIvLeft.setVisibility(View.GONE);
            mIvRight.setVisibility(View.GONE);
            TextView mTvTitle = (TextView) view.findViewById(R.id.tv_title);//直播标题
            mTvTitle.setVisibility(View.VISIBLE);
            TextView mTvState = (TextView) view.findViewById(R.id.tv_state);//直播间状态
            mTvState.setVisibility(View.VISIBLE);
            TextView mTvTime = (TextView) view.findViewById(R.id.tv_time);//时间
            mTvTime.setVisibility(View.VISIBLE);
            TextView mTvNextTime = (TextView) view.findViewById(R.id.tv_next_time);//下一时段
            //绑定中间直播信息
            final LiveInfo liveItem = mBannerList.get(i);
            ImageDisplayUtil.displayImage(mContext, ivIamge, liveItem.getThumb());
            tvLiveingNumber.setText(String.format(Locale.CHINA, "%s", StringUtils.getDecimal(liveItem.getOnline_num(), Constant.TEN_THOUSAND, "万", "")));
            ImageDisplayUtil.displayImage(mContext, civ_avatar, liveItem.getTeacher_info().getAvatar(), R.mipmap.default_avatar);
            mTvTitle.setText(StringUtils.replaceNullToEmpty(liveItem.getTitle()));
            mTvState.setText(StringUtils.replaceNullToEmpty(liveItem.getLive_msg()));
            mTvTime.setText(DateUtil.formatDate(liveItem.getStart_date()) + "-" + DateUtil.formatDate(liveItem.getEnd_date()));//时间
            tvLiveingNumber.setText(String.format(Locale.CHINA, "%s", StringUtils.getDecimal(liveItem.getOnline_num(), Constant.TEN_THOUSAND, "万", "")));
            ivIamge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(liveItem.getPlayback_id())) {
                        clickPlayLive(liveItem);
                    } else {
                        clickRePlayLive(liveItem);
                    }
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(liveItem.getPlayback_id())) {
                        clickPlayLive(liveItem);
                    } else {
                        clickRePlayLive(liveItem);
                    }
                }
            });
            rl_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TeacherZoneActivity.intoNewIntent(mContext, liveItem.getTeacher_info().getId());
                }
            });
            mList.add(view);
        }
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(mList);
        holder.mVpReadingTap.setAdapter(mViewPagerAdapter);
        holder.mIndicator.setViewPager(holder.mVpReadingTap);
    }


    //绑定Live
    public void onBindLiveViewHolder(LiveViewHolder holder) {
        holder.mIndicator.setVisibility(View.GONE);
        List<View> mList = new ArrayList<>();
        for (int i = 0; i < mLiveList.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_live_reading_tap_item_mid, null);
            CircleImageView civ_avatar = (CircleImageView) view.findViewById(R.id.civ_avatar);//头像
            ImageView mIvLeft = (ImageView) view.findViewById(R.id.iv_left);//左箭头
            ImageView mIvRight = (ImageView) view.findViewById(R.id.iv_right);//右箭头
            TextView mTvTitle = (TextView) view.findViewById(R.id.tv_title);//直播标题
            TextView mTvState = (TextView) view.findViewById(R.id.tv_state);//直播间状态
            TextView mTvTime = (TextView) view.findViewById(R.id.tv_time);//时间
            TextView mTvNextTime = (TextView) view.findViewById(R.id.tv_next_time);//下一时段
            //绑定中间直播信息
            LiveInfo liveItem = mLiveList.get(i);
            ImageDisplayUtil.displayImage(mContext, civ_avatar, liveItem.getTeacher_info().getAvatar(), R.mipmap.default_avatar);
            mTvTitle.setText(StringUtils.replaceNullToEmpty(liveItem.getTitle()));
            view.setTag(liveItem);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO 完善直播参数传递
                    LiveInfo liveItem = (LiveInfo) view.getTag();
                    if (TextUtils.isEmpty(liveItem.getPlayback_id())) {
                        clickPlayLive(liveItem);
                    } else {
                        clickRePlayLive(liveItem);
                    }
                }
            });
            mList.add(view);
        }
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(mList);
        holder.mVpReadingTap.setAdapter(mViewPagerAdapter);
    }

    //绑定Live
    public void onBindLiveItemViewHolder(ViewHolder holder, int position) {
        int currentPosition = position - types.size();
        final LiveInfo liveItem = mLiveListList.get(currentPosition);
        //绑定直播Item
        ImageDisplayUtil.displayImage(mContext, holder.mIvIamge, liveItem.getThumb());
        ImageDisplayUtil.displayImage(mContext, holder.civ_avatar, liveItem.getTeacher_info().getAvatar(), R.mipmap.default_avatar);
        if (type == ZHI_BO) {
            holder.ivLiveIng.setVisibility(View.VISIBLE);
            holder.mTvLiveing.setText("直播中");
            holder.mTvLiveingNumber.setText(String.format(Locale.CHINA, "%s", StringUtils.getDecimal(liveItem.getOnline_num(), Constant.TEN_THOUSAND, "万", "")));
            holder.tvAppointment.setVisibility(View.GONE);
        } else {
            if (type == MY_LIVE) {
                holder.tvAppointment.setVisibility(View.GONE);
                holder.mTvLiveing.setText(String.format(Locale.CHINA, "%s人预约", StringUtils.getDecimal(liveItem.getAppointment_count(), Constant.TEN_THOUSAND, "万", "")));
            } else {
                holder.tvAppointment.setVisibility(View.VISIBLE);
                holder.mTvLiveing.setText("开始时间");
            }
            holder.tvAppointment.setTag(liveItem);
            holder.tvAppointment.setTag(R.id.tvAppointment, position);
            holder.tvAppointment.setSelected(liveItem.getIs_appointment() != 0);
            holder.tvAppointment.setText(liveItem.getIs_appointment() == 0 ? "预约" : "预约");
            holder.tvAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(v);
                    }
                }
            });
            holder.ivLiveIng.setVisibility(View.GONE);

            holder.mTvLiveingNumber.setText(DateUtil.formatDate(liveItem.getStart_date(), "yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm"));
        }
        holder.tvTitle.setText(StringUtils.replaceNullToEmpty(liveItem.getTitle()));
        holder.mTvTeacherName.setText(StringUtils.replaceNullToEmpty(liveItem.getAnchor()));
        String tags = "";
        if (liveItem.getTeacher_info() != null && liveItem.getTeacher_info().getTag_list() != null) {
            String[] tag_list = liveItem.getTeacher_info().getTag_list();
            if (tag_list.length > 0) {
                for (int i = 0; i < tag_list.length; i++) {
                    tags += tag_list[i] + "　";
                }
            }
        }
        holder.mTvTeacherIntroduce.setText(StringUtils.replaceNullToEmpty(tags));
        holder.mTvTeacherTheme.setText(StringUtils.replaceNullToEmpty(liveItem.getTeacher_info().getSlogan()));
        holder.mIvIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //直播参数传递
                if (TextUtils.isEmpty(liveItem.getPlayback_id())) {
                    clickPlayLive(liveItem);
                } else {
                    clickRePlayLive(liveItem);
                }
            }
        });

    }

    public void clickPlayLive(LiveInfo liveItem) {
        LiveParameter liveParameter = new LiveParameter();
        liveParameter.setLiveId(liveItem.getId());
        liveParameter.setLiveTitle(liveItem.getTitle());
        liveParameter.setRoomId(liveItem.getTeacher_info().getLive_roomid());
        liveParameter.setTeacherId(liveItem.getTeacher_id());
        PlayLiveActivity.intoNewIntent(mContext, liveParameter);
    }

    /**
     * 回放
     *
     * @param liveItem
     */
    public void clickRePlayLive(LiveInfo liveItem) {
        LiveParameter liveParameter = new LiveParameter();
        liveParameter.setLiveId(liveItem.getId());
        liveParameter.setLiveTitle(liveItem.getTitle());
        liveParameter.setRoomId(liveItem.getTeacher_info().getLive_roomid());
        liveParameter.setTeacherId(liveItem.getTeacher_id());
        liveParameter.setCcid(liveItem.getPlayback_id());
        ReplayLiveActivity.intoNewIntent(mContext, liveParameter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder vh = null;
//        int height = vh.itemView.getHeight();
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(2, height);
//        vh.mViewFriendsPostLine.setLayoutParams(lp);
        switch (viewType) {
            case BANNER:
                view = LayoutInflater.from(mContext).inflate(R.layout.fragment_live_reading_tap_viewpager2, parent, false);
                BannerViewHolder bannerViewHolder = new BannerViewHolder(view);
                int width = ScreenUtils.getScreenWidth(mContext);
                int height = (int) (width / 1.67);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
                bannerViewHolder.mVpReadingTap.setLayoutParams(layoutParams);
                vh = bannerViewHolder;
                break;
            case LIVE:
                view = LayoutInflater.from(mContext).inflate(R.layout.fragment_live_reading_tap_viewpager, parent, false);
                LiveViewHolder liveViewHolder = new LiveViewHolder(view);
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(mContext, 100));
                liveViewHolder.mVpReadingTap.setLayoutParams(layoutParams2);
                vh = liveViewHolder;
                break;
            default:
                view = LayoutInflater.from(mContext).inflate(R.layout.fragment_live_reading_tap_item, parent, false);
                vh = new ViewHolder(view);
                break;
        }
        return vh;
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.vpReadingTap)
        ViewPager mVpReadingTap;
        @BindView(R.id.indicator)
        CircleIndicator mIndicator;

        public BannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class LiveViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.vpReadingTap)
        ViewPager mVpReadingTap;
        @BindView(R.id.indicator)
        CircleIndicator mIndicator;

        public LiveViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(ivIamge)
        ImageView mIvIamge;
        @BindView(R.id.ivIsVip)
        ImageView ivIsVip;
        @BindView(R.id.ivLiveIng)
        ImageView ivLiveIng;
        @BindView(R.id.tvLiveing)
        TextView mTvLiveing;
        @BindView(R.id.civ_avatar)
        CircleImageView civ_avatar;
        @BindView(R.id.tvLiveingNumber)
        TextView mTvLiveingNumber;
        @BindView(R.id.tv_teacher_name)
        TextView mTvTeacherName;
        @BindView(R.id.tv_teacher_introduce)
        TextView mTvTeacherIntroduce;
        @BindView(R.id.tv_teacher_theme)
        TextView mTvTeacherTheme;
        @BindView(R.id.tvAppointment)
        TextView tvAppointment;
        @BindView(R.id.tvTitle)
        TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
