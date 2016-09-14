package tv.kuainiu.ui.home.adapter;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;
import tv.kuainiu.R;
import tv.kuainiu.modle.Banner;
import tv.kuainiu.ui.adapter.ViewPagerAdapter;
import tv.kuainiu.widget.NoScrollGridView;

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
    private List<Banner> mHotPointList = new ArrayList<>();
    /**
     * 实时要闻List
     */
    private List<Banner> mNewList = new ArrayList<>();
    int mBannerListSize = 0;
    int mStockIndexListSize = 0;
    int mLiveListSize = 0;
    int mHotPointListSize = 0;
    int mNewListSize = 0;
    private static final int BANNER = 0;
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
        this.mBannerList.addAll(mBannerList);
    }

    public void setLiveList(List<Banner> liveList) {
        this.mLiveList.addAll(liveList);
    }

    public void setLiveListList(List<Banner> liveListList) {
        this.mNewList.addAll(liveListList);
    }

    @Override
    public int getItemCount() {
        mBannerListSize = mBannerList.size() < 1 ? 0 : 1;
        mStockIndexListSize = mStockIndexList.size() < 1 ? 0 : 1;
        mLiveListSize = mLiveList.size() < 1 ? 0 : 1;
        mHotPointListSize = mHotPointList.size() < 1 ? 0 : 1;
        mNewListSize = mNewList.size() < 1 ? 0 : mNewList.size();
        return mBannerListSize + mStockIndexListSize + mLiveListSize + mHotPointListSize + mNewListSize;
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
                onBindBannerViewHolder((BannerViewHolder) holder);
                break;
            case STOCK_INDEX:

                break;
            case LIVE:
                onBindLiveViewHolder((BannerViewHolder) holder);
                break;
            case TEXT_HOT_POINT:
                break;
            case HOT_POINT:
                break;
            case TEXT_HOT_NEW:
                break;
            default:
                onBindNewsItemViewHolder((ViewNewHolder) holder, position);
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
    public void onBindNewsItemViewHolder(ViewNewHolder holder, int position) {
        int currentPosition = position - mBannerListSize - mStockIndexListSize-mLiveListSize-mHotPointListSize-mNewListSize;
        //TODO 绑定新闻Item
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
            case STOCK_INDEX:
            case LIVE:
                view = LayoutInflater.from(mContext).inflate(R.layout.fragment_live_reading_tap_viewpager, parent, false);
                vh = new BannerViewHolder(view);
                break;
            case HOT_POINT:
                view = LayoutInflater.from(mContext).inflate(R.layout.fragment_home_item_index_grid_item, parent, false);
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

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.vpReadingTap) ViewPager mVpReadingTap;
        @BindView(R.id.indicator) CircleIndicator mIndicator;

        public BannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class HotPointHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.gv_hot_point_grid) NoScrollGridView gv_hot_point_grid;

        public HotPointHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class TextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_home_item_text) TextView tvHomeItemText;

        public TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

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
