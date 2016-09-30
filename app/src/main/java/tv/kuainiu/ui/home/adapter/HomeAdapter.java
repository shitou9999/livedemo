package tv.kuainiu.ui.home.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import tv.kuainiu.R;
import tv.kuainiu.app.OnItemClickListener;
import tv.kuainiu.modle.Banner;
import tv.kuainiu.modle.HotPonit;
import tv.kuainiu.modle.LiveInfo;
import tv.kuainiu.modle.NewsItem;
import tv.kuainiu.ui.activity.WebActivity;
import tv.kuainiu.ui.adapter.ViewPagerAdapter;
import tv.kuainiu.ui.articles.activity.PostZoneActivity;
import tv.kuainiu.ui.liveold.PlayLiveActivity;
import tv.kuainiu.ui.liveold.model.LiveParameter;
import tv.kuainiu.ui.video.VideoActivity;
import tv.kuainiu.utils.CustomLinearLayoutManager;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.DensityUtils;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.ScreenUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.widget.DividerItemDecoration;

/**
 * @author nanck on 2016/7/29.
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Activity mContext;
    /**
     * 广告List
     */
    private List<Banner> mBannerList = new ArrayList<>();
    /**
     * 指数List
     */
    private List<Banner> mStockIndexList = new ArrayList<>();
    /**
     * 直播List
     */
    private List<LiveInfo> mLiveList = new ArrayList<>();
    /**
     * 热门观点List
     */
    private List<HotPonit> mHotPointList = new ArrayList<>();
    /**
     * 实时要闻List
     */
    private List<NewsItem> mNewList = new ArrayList<>();

    private static final int BANNER = 7;
    private static final int STOCK_INDEX = 1;
    private static final int LIVE = 2;
    private static final int TEXT_HOT_POINT = 3;
    private static final int HOT_POINT = 4;
    private static final int TEXT_HOT_NEW = 5;
    private static final int HOT_NEW = 6;
    private OnItemClickListener mOnItemClickListener;
    List<Integer> types = new ArrayList<>();

    public HomeAdapter(Activity context) {
        mContext = context;
    }

    public List<Integer> getTypes() {
        return types;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setBannerList(List<Banner> mBannerList) {
        this.mBannerList.clear();
//        this.mBannerList.add(mBannerList.get(mBannerList.size() - 1));
        this.mBannerList.addAll(mBannerList);
//        this.mBannerList.add(mBannerList.get(0));
    }

    public void setLiveList(List<LiveInfo> liveList) {
        this.mLiveList.clear();
        this.mLiveList.addAll(liveList);
    }

    public void setLiveListList(List<Banner> liveListList) {
//        this.mNewList.addAll(liveListList);
    }

    public void setHotPointList(List<HotPonit> mHotPointList) {
        this.mHotPointList = mHotPointList;
    }

    public void setNewsList(List<NewsItem> mHotPointList) {
        this.mNewList = mHotPointList;
    }

    @Override
    public int getItemCount() {
        types.clear();
        if (mBannerList.size() > 0) {
            types.add(BANNER);
        }
        if (mStockIndexList.size() > 0) {
            types.add(STOCK_INDEX);
        }
        if (mLiveList.size() > 0) {
            types.add(LIVE);
        }
        if (mHotPointList.size() > 0) {
            types.add(TEXT_HOT_POINT);
            types.add(HOT_POINT);
        }
        if (mNewList.size() > 0) {
            types.add(TEXT_HOT_NEW);
        }
        int mNewListSize = mNewList.size() < 1 ? 0 : mNewList.size();
        return types.size() + mNewListSize;
    }


    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if (position < types.size()) {
            type = types.get(position);
        } else {
            type = HOT_NEW;
        }
        return type;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case BANNER:
                //绑定banner
                onBindBannerViewHolder((BannerViewHolder) holder);
                break;
            case STOCK_INDEX:
                onBindStockIndexViewHolder((BannerViewHolder) holder);
                break;
            case LIVE:
                //绑定中间直播信息
                onBindLiveViewHolder((BannerViewHolder) holder);
                break;
            case TEXT_HOT_POINT:
                onBindTextHotPointViewHolder((TextViewHolder) holder);
                break;
            case HOT_POINT:
                onBindHotPointViewHolder((HotPointHolder) holder);
                break;
            case TEXT_HOT_NEW:
                onBindTextHotNewViewHolder((TextViewHolder) holder);
                break;
            default:
                //绑定新闻Item
                onBindNewsItemViewHolder((ViewNewHolder) holder, position);
                break;
        }
    }

    //TODO 股票指数
    private void onBindStockIndexViewHolder(BannerViewHolder holder) {
    }

    //热门观点
    private void onBindHotPointViewHolder(HotPointHolder holder) {
        HotPointAdapter adp = new HotPointAdapter(mContext, mHotPointList, mOnItemClickListener);
        holder.rc_hot_point.setAdapter(adp);
    }

    private void onBindTextHotNewViewHolder(TextViewHolder holder) {
        holder.tvHomeItemText.setText("实时要闻");
    }

    private void onBindTextHotPointViewHolder(TextViewHolder holder) {
        holder.tvHomeItemText.setText("热门观点");
    }


    //绑定Banner
    public void onBindBannerViewHolder(final BannerViewHolder holder) {
//        holder.mIndicator.setVisibility(View.GONE);
        List<View> mList = new ArrayList<>();
        for (int i = 0; i < mBannerList.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_live_reading_tap_viewpager_item, null);
            final ImageView mIvIamge = (ImageView) view.findViewById(R.id.ivIamge);//直播间图片
            TextView mTvLiveing = (TextView) view.findViewById(R.id.tvLiveing);//直播状态
            TextView tvBannarTitle = (TextView) view.findViewById(R.id.tvBannarTitle);//直播状态
            TextView mTvLiveingNumber = (TextView) view.findViewById(R.id.tvLiveingNumber);//直播间人数
            RelativeLayout rl_state_living = (RelativeLayout) view.findViewById(R.id.rl_state_living);//直播间状态
            rl_state_living.setVisibility(View.GONE);
            Banner banner = mBannerList.get(i);
            view.setTag(banner);
            if (TextUtils.isEmpty(banner.title)) {
                tvBannarTitle.setVisibility(View.INVISIBLE);
            } else {
                tvBannarTitle.setVisibility(View.VISIBLE);
            }
            tvBannarTitle.setText(banner.title);
            ImageDisplayUtil.displayImage(mContext, mIvIamge, banner.thumb);
            //绑定banner click事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Banner banner = (Banner) view.getTag();
                    WebActivity.intoNewIntent(mContext, banner.url);
                }
            });
            mList.add(view);
        }
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(mList);
        holder.mVpReadingTap.setAdapter(mViewPagerAdapter);
        holder.mIndicator.setViewPager(holder.mVpReadingTap);
        holder.mVpReadingTap.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffsetPixels == 0.0) {
                    if (position == mBannerList.size() - 1) {
                        holder.mVpReadingTap.setCurrentItem(1, false);
                    } else if (position == 0) {
                        holder.mVpReadingTap.setCurrentItem(mBannerList.size() - 2, false);
                    } else {
                        holder.mVpReadingTap.setCurrentItem(position);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    //绑定Live
    public void onBindLiveViewHolder(BannerViewHolder holder) {
//        holder.mIndicator.setVisibility(View.GONE);
        List<View> mList = new ArrayList<>();
        for (int i = 0; i < mLiveList.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_live_reading_tap_item_mid, null);
            ImageView mIvLeft = (ImageView) view.findViewById(R.id.iv_left);//左箭头
            ImageView mIvRight = (ImageView) view.findViewById(R.id.iv_right);//右箭头
            CircleImageView civAvatar = (CircleImageView) view.findViewById(R.id.civ_avatar);//右箭头
            TextView mTvNextTime = (TextView) view.findViewById(R.id.tv_next_time);//
            RelativeLayout mRlAvatar = (RelativeLayout) view.findViewById(R.id.rl_avatar);//头像
            TextView mTvTitle = (TextView) view.findViewById(R.id.tv_title);//直播状态
            TextView mTvState = (TextView) view.findViewById(R.id.tv_state);//直播间状态
            TextView mTvTime = (TextView) view.findViewById(R.id.tv_time);

            mIvLeft.setVisibility(View.GONE);
//            mIvRight.setVisibility(View.INVISIBLE);

            final LiveInfo liveInfo = mLiveList.get(i);
            ImageDisplayUtil.displayImage(mContext, civAvatar, liveInfo.getTeacher_info().getAvatar(), R.mipmap.default_avatar);
            mTvTitle.setText(StringUtils.replaceNullToEmpty(liveInfo.getTitle()));
            mTvState.setText(StringUtils.replaceNullToEmpty(liveInfo.getLive_msg()));
            mTvTime.setText(DateUtil.formatDate(liveInfo.getStart_date()) + "-" + DateUtil.formatDate(liveInfo.getEnd_date()));//时间
            //绑定中间直播信息
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickPlayLive(liveInfo);
                }
            });
            mList.add(view);
        }
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(mList);
        holder.mVpReadingTap.setAdapter(mViewPagerAdapter);
    }

    //绑定新闻
    public void onBindNewsItemViewHolder(ViewNewHolder holder, int position) {
        int currentPosition = position - types.size();
        if (currentPosition > -1 && currentPosition < mNewList.size()) {
            final NewsItem newsItem = mNewList.get(currentPosition);
            final String videoId = newsItem.getUpVideoId();
            //绑定新闻Item
            holder.mTvNewTitle.setText(StringUtils.replaceNullToEmpty(newsItem.getTitle()));
            holder.mTvNewContent.setText(StringUtils.replaceNullToEmpty(newsItem.getDescription()));
            ImageDisplayUtil.displayImage(mContext, holder.mIvNewIamge, StringUtils.replaceNullToEmpty(newsItem.getThumb()));
            holder.rootview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(videoId)) {
                        PostZoneActivity.intoNewIntent(mContext, newsItem.getId(), newsItem.getCatId(), newsItem.getCatname());
                    } else {
                        VideoActivity.intoNewIntent(mContext, newsItem.getId(), newsItem.getUpVideoId(), newsItem.getCatId());
                    }
                }
            });
            //TODO 列表直播标签是否显示
            holder.tvType.setVisibility(View.INVISIBLE);
        }
//
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder vh = null;
        int screenWidth = ScreenUtils.getScreenWidth(mContext);
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(2, height);
//        vh.mViewFriendsPostLine.setLayoutParams(lp);
        switch (viewType) {
            case BANNER:

                view = LayoutInflater.from(mContext).inflate(R.layout.fragment_live_reading_tap_viewpager, parent, false);
                vh = new BannerViewHolder(view);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(screenWidth, (int) (screenWidth / 1.67));
                ((BannerViewHolder) vh).mVpReadingTap.setLayoutParams(lp);
                break;
            case STOCK_INDEX:
            case LIVE:
                view = LayoutInflater.from(mContext).inflate(R.layout.fragment_live_reading_tap_viewpager, parent, false);
                BannerViewHolder bannerViewHolder = new BannerViewHolder(view);
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(mContext, 80));
                bannerViewHolder.mVpReadingTap.setLayoutParams(layoutParams2);
                vh = bannerViewHolder;
                break;
            case HOT_POINT:
                view = LayoutInflater.from(mContext).inflate(R.layout.fragment_home_item_hot_point, parent, false);
                vh = new HotPointHolder(view);
                CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(mContext);
                mLayoutManager.setOrientation(CustomLinearLayoutManager.HORIZONTAL);
                ((HotPointHolder) vh).rc_hot_point.setLayoutManager(mLayoutManager);
                DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mContext, LinearLayoutManager.HORIZONTAL);
                mDividerItemDecoration.setColor(Color.parseColor("#00000000"));
                mDividerItemDecoration.setItemSize(mContext.getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin));
                ((HotPointHolder) vh).rc_hot_point.addItemDecoration(mDividerItemDecoration);
                break;
            case TEXT_HOT_POINT:
            case TEXT_HOT_NEW:
                view = LayoutInflater.from(mContext).inflate(R.layout.fragment_home_item_text, parent, false);
                vh = new TextViewHolder(view);
                break;
            default:
                view = LayoutInflater.from(mContext).inflate(R.layout.fragment_home_item_new, parent, false);
                vh = new ViewNewHolder(view);
                break;
        }
        return vh;
    }

    //banner ViewPager
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

    //热点指数
    static class HotPointHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rc_hot_point)
        RecyclerView rc_hot_point;

        public HotPointHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //文字Item
    static class TextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_home_item_text)
        TextView tvHomeItemText;

        public TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //新闻Item
    static class ViewNewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivNewIamge)
        ImageView mIvNewIamge;
        @BindView(R.id.tvNewTitle)
        TextView mTvNewTitle;
        @BindView(R.id.tvNewContent)
        TextView mTvNewContent;
        @BindView(R.id.tvType)
        TextView tvType;
        View rootview;

        public ViewNewHolder(View itemView) {
            super(itemView);
            rootview = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    public void clickPlayLive(LiveInfo liveItem) {
        LiveParameter liveParameter = new LiveParameter();
        liveParameter.setFansNumber(liveItem.getTeacher_info().getFans_count());
        liveParameter.setIsFollow(liveItem.getIs_follow());
        liveParameter.setIsSupport(liveItem.getIs_supported());
        liveParameter.setLiveId(liveItem.getId());
        liveParameter.setLiveTitle(liveItem.getTitle());
        liveParameter.setOnLineNumber(liveItem.getOnline_num());
        liveParameter.setTeacherAvatar(liveItem.getTeacher_info().getAvatar());
        liveParameter.setRoomId(liveItem.getTeacher_info().getLive_roomid());
        liveParameter.setSupportNumber(liveItem.getSupport());
        liveParameter.setTeacherId(liveItem.getTeacher_id());

        PlayLiveActivity.intoNewIntent(mContext, liveParameter);
    }
}
