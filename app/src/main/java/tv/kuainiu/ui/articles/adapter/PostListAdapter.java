package tv.kuainiu.ui.articles.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.command.dao.VideoDownloadDao;
import tv.kuainiu.modle.NewsItem;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.articles.activity.PostZoneActivity;
import tv.kuainiu.ui.video.VideoActivity;
import tv.kuainiu.utils.ImageDisplayUtil;

/**
 * @author nanck on 2016/3/31.
 */
public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> implements OnClickListener {
    private Context mContext;
    private List<NewsItem> mNewsList;
    private VideoDownloadDao mDownloadDao;

    public void setNewsItemList(List<NewsItem> newsItemList) {
        mNewsList = newsItemList;
    }

    public PostListAdapter(Context context) {
        mContext = context;
        mDownloadDao = new VideoDownloadDao(context);
    }

    @Override
    public int getItemCount() {
        return mNewsList != null ? mNewsList.size() : 0;
    }

    @Override
    public PostListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_simple_news, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostListAdapter.ViewHolder holder, int position) {
        NewsItem newsItem = mNewsList.get(position);
        holder.itemView.setTag(newsItem);
        ImageDisplayUtil.displayImage(mContext, holder.ivPhoto, newsItem.getThumb(), R.mipmap.ic_def_error);
        holder.textName.setText(newsItem.getNickname());
        holder.textDescribe.setText(newsItem.getTitle());

        // 设置解盘类型
//        String flag = newsItem.getType();
//        if (TextUtils.isEmpty(flag) || "请选择".equals(flag)) {
//            holder.textFlag.setVisibility(View.INVISIBLE);
//        } else {
//            holder.textFlag.setVisibility(View.VISIBLE);
//            holder.textFlag.setText(newsItem.getType());
//        }

//        if (!TextUtils.isEmpty(newsItem.getVideo_id())) {
//            String text = StringUtils.getDecimal(newsItem.getViews(), Constant.TEN_THOUSAND, "万次播放", "次播放");
//            holder.textViewCount.setText(text);
//        } else {
//            holder.textViewCount.setVisibility(View.INVISIBLE);
//        }

//        int color = TextColorUtil.getBackgroundColorForJpType(flag);
//        holder.textFlag.setBackgroundColor(color);
//        // 设置时间
//        long updateTime = newsItem.getInputtime();
//        holder.textTimeBefore.setText(DateUtil.getDurationString(DateUtil.toJava(updateTime)));
    }


    @Override public void onClick(View v) {
        final NewsItem newsItem = (NewsItem) v.getTag();
        final int type = newsItem.getType();
        if (type== Constant.NEWS_TYPE_ARTICLE) {
            PostZoneActivity.intoNewIntent(mContext, newsItem.getId(), newsItem.getCat_id());
        } else if (type== Constant.NEWS_TYPE_VIDEO){
            VideoActivity.intoNewIntent(mContext, newsItem.getId(), newsItem.getVideo_id(),newsItem.getVideo_id(),newsItem.getTitle());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_photo) ImageView ivPhoto;
        @BindView(R.id.tv_flag) TextView textFlag;
        @BindView(R.id.tv_describe) TextView textDescribe;
        @BindView(R.id.tv_name) TextView textName;
        @BindView(R.id.tv_timeBefore) TextView textTimeBefore;
        @BindView(R.id.tv_view_count) TextView textViewCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
