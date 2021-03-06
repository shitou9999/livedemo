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

import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import tv.kuainiu.R;
import tv.kuainiu.app.Constans;
import tv.kuainiu.app.OnItemClickListener;
import tv.kuainiu.modle.Banner;
import tv.kuainiu.modle.HotPonit;
import tv.kuainiu.modle.LiveInfo;
import tv.kuainiu.modle.NewsItem;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.WebActivity;
import tv.kuainiu.ui.adapter.ViewPagerAdapter;
import tv.kuainiu.ui.articles.activity.PostZoneActivity;
import tv.kuainiu.ui.liveold.PlayLiveActivity;
import tv.kuainiu.ui.liveold.model.LiveParameter;
import tv.kuainiu.ui.video.VideoActivity;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.DensityUtils;
import tv.kuainiu.utils.GlideImageLoader;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.ScreenUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.widget.DividerItemDecoration;
import tv.kuainiu.widget.LayoutManager.CustomLinearLayoutManager;

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
                onBindBannerViewHolder((BannerHolder) holder);
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
    public void onBindBannerViewHolder(final BannerHolder holder) {
        List<String> mListUrl = new ArrayList<>();
        List<String> mListTitle = new ArrayList<>();
        for (int i=0;i<mBannerList.size();i++){
            mListUrl.add(mBannerList.get(i).thumb);
            mListTitle.add(mBannerList.get(i).title);
        }
        //设置banner样式
        holder.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        holder.banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        holder.banner.setImages(mListUrl);
        //设置标题集合（当banner样式有显示title时）
        holder.banner.setBannerTitles(mListTitle);
        //设置自动轮播，默认为true
        holder. banner.isAutoPlay(true);
        //设置轮播时间
        holder.banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        holder.banner.setIndicatorGravity(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //banner设置方法全部调用完毕时最后调用
        holder.banner.start();
        holder.banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                Banner banner =mBannerList.get(position-1);
                WebActivity.intoNewIntent(mContext, banner.url);
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
            switch (liveInfo.getLive_status()) {
                case Constans.LIVE_END:
                    mTvNextTime.setText("回放");
                    break;
                case Constans.LIVE_ING:
                    mTvNextTime.setText("直播中");
                    break;
                case Constans.LiVE_UN_START:
                    if (liveInfo.getIs_appointment() == 0) {
                        mTvNextTime.setText(Constant.APPOINTMENT_REMINDER);
                    } else {
                        mTvNextTime.setText(Constant.UN_APPOINTMENT_REMINDER);
                    }
                    mTvNextTime.setSelected(liveInfo.getIs_appointment() != 0);
                    mTvNextTime.setTag(liveInfo);
                    mTvNextTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onClick(v);
                            }
                        }
                    });
                    break;
            }


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
            final String videoId = newsItem.getVideo_id();
            //绑定新闻Item
            holder.mTvNewTitle.setText(StringUtils.replaceNullToEmpty(newsItem.getTitle()));
            holder.tvTeacherName.setText(StringUtils.replaceNullToEmpty(newsItem.getNickname()));
            holder.tvDate.setText(DateUtil.getTimeString_MM_dd(newsItem.getInputtime()));
            ImageDisplayUtil.displayImage(mContext, holder.mIvNewIamge, StringUtils.replaceNullToEmpty(newsItem.getThumb()));
            holder.rootview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (newsItem.getType()==Constant.NEWS_TYPE_ARTICLE) {
                        PostZoneActivity.intoNewIntent(mContext, newsItem.getId(), newsItem.getCat_id());
                    } else if (newsItem.getType()==Constant.NEWS_TYPE_VIDEO){
                        VideoActivity.intoNewIntent(mContext, newsItem.getId(), newsItem.getVideo_id(), newsItem.getCat_id(), StringUtils.replaceNullToEmpty(newsItem.getTitle()));
                    }
                }
            });
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
                view = LayoutInflater.from(mContext).inflate(R.layout.fragment_home_bannar, parent, false);
                vh = new BannerHolder(view);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(screenWidth, (int) (screenWidth / 1.67));
                ((BannerHolder) vh).banner.setLayoutParams(lp);
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
                view = LayoutInflater.from(mContext).inflate(R.layout.fragment_home_item_text, parent, false);
                vh = new TextViewHolder(view);
                break;
            case TEXT_HOT_NEW:
                view = LayoutInflater.from(mContext).inflate(R.layout.fragment_home_item_text_white_bg, parent, false);
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
    static class BannerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.banner)
        com.youth.banner.Banner banner;
        public BannerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
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
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvTeacherName)
        TextView tvTeacherName;
        View rootview;

        public ViewNewHolder(View itemView) {
            super(itemView);
            rootview = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    public void clickPlayLive(LiveInfo liveItem) {
        LiveParameter liveParameter = new LiveParameter();
        liveParameter.setLiveId(liveItem.getId());
        liveParameter.setLiveTitle(liveItem.getTitle());
        liveParameter.setRoomId(liveItem.getTeacher_info().getLive_roomid());
        liveParameter.setTeacherId(liveItem.getTeacher_id());

        PlayLiveActivity.intoNewIntent(mContext, liveParameter);
    }
}
