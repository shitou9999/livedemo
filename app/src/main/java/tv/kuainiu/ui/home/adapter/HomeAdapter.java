package tv.kuainiu.ui.home.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import me.relex.circleindicator.CircleIndicator;
import tv.kuainiu.R;
import tv.kuainiu.modle.Banner;
import tv.kuainiu.modle.HotPonit;
import tv.kuainiu.modle.NewsItem;
import tv.kuainiu.ui.adapter.ViewPagerAdapter;
import tv.kuainiu.utils.CustomLinearLayoutManager;
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
    private List<Banner> mLiveList = new ArrayList<>();
    /**
     * 热门观点List
     */
    private List<HotPonit> mHotPointList = new ArrayList<>();
    /**
     * 实时要闻List
     */
    private List<NewsItem> mNewList = new ArrayList<>();

    int mBannerListSize = 0;
    int mStockIndexListSize = 0;
    int mLiveListSize = 0;
    int mHotPointListSize = 0;
    int mNewListSize = 0;
    private static final int BANNER = 7;
    private static final int STOCK_INDEX = 1;
    private static final int LIVE = 2;
    private static final int TEXT_HOT_POINT = 3;
    private static final int HOT_POINT = 4;
    private static final int TEXT_HOT_NEW = 5;
    private static final int HOT_NEW = 6;

    public HomeAdapter(Activity context) {
        mContext = context;
    }


    public void setBannerList(List<Banner> mBannerList) {
        this.mBannerList.clear();
//        this.mBannerList.add(mBannerList.get(mBannerList.size() - 1));
        this.mBannerList.addAll(mBannerList);
//        this.mBannerList.add(mBannerList.get(0));
    }

    public void setLiveList(List<Banner> liveList) {
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
        mBannerListSize = 1;
        mStockIndexListSize = 1;
        mLiveListSize = 1;
        mHotPointListSize = 1;
        mNewListSize = mNewList.size() < 1 ? 0 : mNewList.size();
        return mBannerListSize + mStockIndexListSize + mLiveListSize + mHotPointListSize + mNewListSize + 2;
    }


    @Override public int getItemViewType(int position) {
//        if (mBannerListSize > 0 && position == 0) {
//            return BANNER;
//        } else if (mStockIndexListSize > 0 && position <= 1) {
//            return STOCK_INDEX;
//        } else if (mLiveListSize > 0 && position <= 2) {
//            return LIVE;
//        } else if (mHotPointListSize > 0 && position <= 3) {
//            return TEXT_HOT_POINT;
//        } else if (mHotPointListSize > 0 && position <= 4) {
//            return HOT_POINT;
//        } else if (position <= 5) {
//            return TEXT_HOT_NEW;
//        }
        int type = 0;
        switch (position) {
            case 0:
//                if (mBannerListSize > 0) {
//                    type = BANNER;
//                }else{
//                    type = STOCK_INDEX;
//                }
                type = BANNER;
                break;
            case 1:
//                if(mBannerListSize>0) {
//                    type = STOCK_INDEX;
//                }else{
//                    type = LIVE;
//                }
                type = STOCK_INDEX;
                break;
            case 2:
                type = LIVE;
                break;
            case 3:
                type = TEXT_HOT_POINT;
                break;
            case 4:
                type = HOT_POINT;
                break;
            case 5:
                type = TEXT_HOT_NEW;
                break;
            default:
                type = HOT_NEW;
                break;
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
        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(mContext);
        mLayoutManager.setOrientation(CustomLinearLayoutManager.HORIZONTAL);
        holder.rc_hot_point.setLayoutManager(mLayoutManager);
        DividerItemDecoration mDividerItemDecoration=new DividerItemDecoration(mContext, LinearLayoutManager.HORIZONTAL);
        mDividerItemDecoration.setColor(Color.parseColor("#00000000"));
        mDividerItemDecoration.setItemSize(mContext.getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin));
        holder.rc_hot_point.addItemDecoration(mDividerItemDecoration);
        HotPointAdapter adp = new HotPointAdapter(mContext, mHotPointList);
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
            ImageView mIvIamge = (ImageView) view.findViewById(R.id.ivIamge);//直播间图片
            TextView mTvLiveing = (TextView) view.findViewById(R.id.tvLiveing);//直播状态
            TextView mTvLiveingNumber = (TextView) view.findViewById(R.id.tvLiveingNumber);//直播间人数
            RelativeLayout rl_state_living = (RelativeLayout) view.findViewById(R.id.rl_state_living);//直播间状态
            rl_state_living.setVisibility(View.GONE);
            ImageDisplayUtil.displayImage(mContext, mIvIamge, mBannerList.get(i).thumb);
            //TODO 绑定banner click事件
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
        for (int i = 0; i < mBannerList.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_live_reading_tap_item_mid, null);
            ImageView mIvLeft = (ImageView) view.findViewById(R.id.iv_left);//左箭头
            ImageView mIvRight = (ImageView) view.findViewById(R.id.iv_right);//右箭头
            TextView mTvNextTime = (TextView) view.findViewById(R.id.tv_next_time);//
            RelativeLayout mRlAvatar = (RelativeLayout) view.findViewById(R.id.rl_avatar);//头像
            TextView mTvTitle = (TextView) view.findViewById(R.id.tv_title);//直播状态
            TextView mTvState = (TextView) view.findViewById(R.id.tv_state);//直播间状态
            TextView mTvTime = (TextView) view.findViewById(R.id.tv_time);
            ;//时间
            //TODO 绑定中间直播信息
            mList.add(view);
        }
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(mList);
        holder.mVpReadingTap.setAdapter(mViewPagerAdapter);
    }

    //绑定新闻
    public void onBindNewsItemViewHolder(ViewNewHolder holder, int position) {
        int currentPosition = position - mBannerListSize - mStockIndexListSize - mLiveListSize - mHotPointListSize - mNewListSize;
        if (currentPosition > -1 && currentPosition < mNewList.size()) {
            //绑定新闻Item
            holder.mTvNewContent.setText(StringUtils.replaceNullToEmpty(mNewList.get(currentPosition).getTitle()));
            holder.mTvNewContent.setText(StringUtils.replaceNullToEmpty(mNewList.get(currentPosition).getDescription()));
            ImageDisplayUtil.displayImage(mContext, holder.mIvNewIamge, StringUtils.replaceNullToEmpty(mNewList.get(currentPosition).getThumb()));
        }
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
                vh = new BannerViewHolder(view);
                break;
            case HOT_POINT:
                view = LayoutInflater.from(mContext).inflate(R.layout.fragment_home_item_hot_point, parent, false);
                vh = new HotPointHolder(view);
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
        @BindView(R.id.vpReadingTap) ViewPager mVpReadingTap;
        @BindView(R.id.indicator) CircleIndicator mIndicator;

        public BannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //热点指数
    static class HotPointHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rc_hot_point) RecyclerView rc_hot_point;

        public HotPointHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //文字Item
    static class TextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_home_item_text) TextView tvHomeItemText;

        public TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //新闻Item
    static class ViewNewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivNewIamge) ImageView mIvNewIamge;
        @BindView(R.id.tvNewTitle) TextView mTvNewTitle;
        @BindView(R.id.tvNewContent) TextView mTvNewContent;

        public ViewNewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
