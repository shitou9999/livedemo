package tv.kuainiu.ui.live.adapter;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
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
import tv.kuainiu.modle.Banner;
import tv.kuainiu.modle.LiveItem;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.adapter.ViewPagerAdapter;
import tv.kuainiu.ui.liveold.PlayLiveActivity;
import tv.kuainiu.ui.liveold.model.LiveParameter;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.StringUtils;

/**
 * @author nanck on 2016/7/29.
 */
public class ReadingTapeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Activity mContext;
    private List<Banner> mBannerList;
    private List<Banner> mLiveList;
    private List<LiveItem> mLiveListList;
    int mBannerListSize = 0;
    int mLiveListSize = 0;
    int mLiveListListSize = 0;
    private static final int BANNER = 2;
    private static final int LIVE = 3;
    private static final int LIVE_ITEM = 4;

    public ReadingTapeAdapter(Activity context) {
        mContext = context;
    }


    public void setBannerList(List<Banner> mBannerList) {
        this.mBannerList = mBannerList;
    }

    public void setLiveList(List<Banner> liveList) {
        this.mLiveList = liveList;
    }

    public void setLiveListList(List<LiveItem> liveListList) {
        this.mLiveListList = liveListList;
    }

    @Override
    public int getItemCount() {
        mBannerListSize = (mBannerList == null || mBannerList.size() < 1) ? 0 : 1;
        mLiveListSize = (mLiveList == null || mLiveList.size() < 1) ? 0 : 1;
        mLiveListListSize = (mLiveListList == null || mLiveListList.size() < 1) ? 0 : mLiveListList.size();
        return mBannerListSize + mLiveListSize + mLiveListListSize;
    }

    @Override public int getItemViewType(int position) {
        if (mBannerListSize > 0 && position == 0) {
            return BANNER;
        }
        if (mBannerListSize > 0 && (position == 0 || position == 1)) {
            return LIVE;
        }
        return LIVE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case BANNER:
                onBindBannerViewHolder((BannerViewHolder) holder);
                break;
            case LIVE:
                onBindLiveViewHolder((BannerViewHolder) holder);
                break;
            default:
                onBindLiveItemViewHolder((ViewHolder) holder, position);
                break;
        }
    }

    @BindView(R.id.ivIamge) ImageView mIvIamge;//直播间图片
    @BindView(R.id.tvLiveing) TextView mTvLiveing;//直播状态
    @BindView(R.id.tvLiveingNumber) TextView mTvLiveingNumber;//直播间人数

    //绑定Banner
    public void onBindBannerViewHolder(BannerViewHolder holder) {
        holder.mIndicator.setVisibility(View.VISIBLE);
        List<View> mList = new ArrayList<>();
        for (int i = 0; i < mBannerList.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_live_reading_tap_viewpager_item, null);
            ButterKnife.bind(this, view);

            //TODO 绑定banner
            mList.add(view);
        }
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(mList);
        holder.mVpReadingTap.setAdapter(mViewPagerAdapter);
        holder.mIndicator.setViewPager(holder.mVpReadingTap);
    }

    @BindView(R.id.iv_left) ImageView mIvLeft;//左箭头
    @BindView(R.id.iv_right) ImageView mIvRight;//右箭头
    @BindView(R.id.tv_next_time) TextView mTvNextTime;//
    @BindView(R.id.rl_avatar) RelativeLayout mRlAvatar;//头像
    @BindView(R.id.tv_title) TextView mTvTitle;//直播状态
    @BindView(R.id.tv_state) TextView mTvState;//直播间状态
    @BindView(R.id.tv_time) TextView mTvTime;//时间

    //绑定Live
    public void onBindLiveViewHolder(BannerViewHolder holder) {
        holder.mIndicator.setVisibility(View.GONE);
        List<View> mList = new ArrayList<>();
        for (int i = 0; i < mBannerList.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_live_reading_tap_item_mid, null);
            ButterKnife.bind(this, view);
            //TODO 绑定中间直播信息
            mList.add(view);
        }
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(mList);
        holder.mVpReadingTap.setAdapter(mViewPagerAdapter);
    }

    //绑定Live
    public void onBindLiveItemViewHolder(ViewHolder holder, int position) {
        int currentPosition = position - mBannerListSize - mLiveListSize;
        final LiveItem liveItem = mLiveListList.get(currentPosition);
        //TODO 绑定直播Item
        ImageDisplayUtil.displayImage(mContext, holder.mIvIamge, liveItem.getThumb());
        ImageDisplayUtil.displayImage(mContext, holder.civ_avatar, liveItem.getLive_thumb(), R.mipmap.default_avatar);
        holder.mTvLiveing.setText("直播中");
        holder.mTvLiveingNumber.setText(String.format(Locale.CHINA, "%s", StringUtils.getDecimal(liveItem.getOnline_num(), Constant.TEN_THOUSAND, "万", "")));
        holder.mTvTeacherName.setText(StringUtils.replaceNullToEmpty(liveItem.getNickname()));
        holder.mTvTeacherIntroduce.setText(StringUtils.replaceNullToEmpty(liveItem.getSlogan()));
        holder.mTvTeacherTheme.setText(StringUtils.replaceNullToEmpty(liveItem.getLive_title()));
        holder.mIvIamge.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                //TODO 完善直播参数传递
                LiveParameter liveParameter = new LiveParameter();
                liveParameter.setFansNumber(0);
                liveParameter.setIsFollow(0);
                liveParameter.setIsSupport(0);
                liveParameter.setLiveId(liveItem.getIns_id());
                liveParameter.setLiveTitle(liveItem.getLive_title());
                liveParameter.setOnLineNumber(liveItem.getOnline_num());
                liveParameter.setTeacherAvatar("");
                liveParameter.setRoomId(liveItem.getLive_roomid());
                liveParameter.setSupportNumber(0);
                liveParameter.setTeacherId(liveItem.getTeacher_id());

                PlayLiveActivity.intoNewIntent(mContext, liveParameter);
            }
        });
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
                view = LayoutInflater.from(mContext).inflate(R.layout.fragment_live_reading_tap_viewpager, parent, false);
                vh = new BannerViewHolder(view);
                break;
            case LIVE:
                view = LayoutInflater.from(mContext).inflate(R.layout.fragment_live_reading_tap_viewpager, parent, false);
                vh = new BannerViewHolder(view);
                break;
            default:
                view = LayoutInflater.from(mContext).inflate(R.layout.fragment_live_reading_tap_item, parent, false);
                vh = new ViewHolder(view);
                break;
        }
        return vh;
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.vpReadingTap) ViewPager mVpReadingTap;
        @BindView(R.id.indicator) CircleIndicator mIndicator;

        public BannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivIamge) ImageView mIvIamge;
        @BindView(R.id.ivIsVip) ImageView ivIsVip;
        @BindView(R.id.tvLiveing) TextView mTvLiveing;
        @BindView(R.id.civ_avatar) CircleImageView civ_avatar;
        @BindView(R.id.tvLiveingNumber) TextView mTvLiveingNumber;
        @BindView(R.id.tv_teacher_name) TextView mTvTeacherName;
        @BindView(R.id.tv_teacher_introduce) TextView mTvTeacherIntroduce;
        @BindView(R.id.tv_teacher_theme) TextView mTvTeacherTheme;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
