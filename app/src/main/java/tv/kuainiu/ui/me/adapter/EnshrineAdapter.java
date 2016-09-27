package tv.kuainiu.ui.me.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;
import java.util.List;

import tv.kuainiu.R;
import tv.kuainiu.modle.NewsItem;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.articles.activity.PostZoneActivity;
import tv.kuainiu.ui.me.activity.CollectActivity;
import tv.kuainiu.ui.video.VideoActivity;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.StringUtils;

/**
 * Created by jack on 2016/4/28.
 * 收藏
 */
public class EnshrineAdapter extends BaseSwipeAdapter {
    private Context context;
    private List<NewsItem> listCollect;
    private IdeletItem idelectItem;
    private boolean isEdit = false;
    private List<NewsItem> selectedListCollect = new ArrayList<NewsItem>();

    public EnshrineAdapter(Context context, List<NewsItem> listCollect) {
        this.context = context;
        this.listCollect = listCollect;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public void setOnItemDelete(IdeletItem idelectItem) {
        this.idelectItem = idelectItem;
    }

    public boolean getEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public void setSelectedListCollect(List<NewsItem> selectedListCollect) {
        this.selectedListCollect = selectedListCollect;
    }

    public List<NewsItem> getSelectedListCollect() {
        return selectedListCollect;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_enshrine, null);
        return convertView;
    }

    @Override
    public void fillValues(final int position, final View convertView) {
        ImageView iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
        TextView tv_describe = (TextView) convertView.findViewById(R.id.tv_describe);
        TextView tv_flag = (TextView) convertView.findViewById(R.id.tv_flag);
        TextView tv_timeBefore = (TextView) convertView.findViewById(R.id.tv_timeBefore);
        TextView tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
        RelativeLayout rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
        RelativeLayout rl_check = (RelativeLayout) convertView.findViewById(R.id.rl_check);
        final ImageView iv_check = (ImageView) convertView.findViewById(R.id.iv_check);
        final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        final NewsItem newsItem = getItem(position);
        if (!(context instanceof CollectActivity)) {
            tv_flag.setVisibility(View.VISIBLE);
            if ("10".equals(newsItem.getCatId())) {
                tv_flag.setText(StringUtils.replaceNullToEmpty(newsItem.getJpType()));
            } else {
                tv_flag.setText(StringUtils.replaceNullToEmpty(newsItem.getCatname()));
            }
        } else {
            tv_flag.setVisibility(View.GONE);
        }
        tv_timeBefore.setText(StringUtils.replaceNullToEmpty(DateUtil.getDurationString(DateUtil.toJava(newsItem.getUpdatetime()))));
        tv_describe.setText(StringUtils.replaceNullToEmpty(newsItem.getTitle()));
        ImageDisplayUtil.displayImage(context, iv_photo, newsItem.getThumb());
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idelectItem != null) {
                    idelectItem.delete(swipeLayout, position, newsItem);
                } else {
                    LogUtils.e("EnshrineAdapter", "删除接口不能为null");
                }
            }
        });
        rl_check.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        if (selectedListCollect != null && selectedListCollect.contains(newsItem)) {
            iv_check.setSelected(true);
        } else {
            iv_check.setSelected(false);
        }
        rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    if (selectedListCollect == null) {
                        selectedListCollect = new ArrayList<NewsItem>();
                    }
                    if (selectedListCollect != null && selectedListCollect.contains(newsItem)) {
                        idelectItem.unelected(newsItem);
                    } else {
                        idelectItem.selected(newsItem);
                    }
                } else {
                    jump(context, newsItem);
                }
            }
        });
    }

    @Override
    public int getCount() {
        return listCollect != null ? listCollect.size() : 0;
    }

    @Override
    public NewsItem getItem(int position) {
        return listCollect != null ? listCollect.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface IdeletItem {
        public void delete(SwipeLayout swipeLayout, int position, NewsItem newsItem);

        public void selected(NewsItem newsItem);

        public void unelected(NewsItem newsItem);
    }

    /**
     * @param newsItem
     */
    private void jump(Context context, NewsItem newsItem) {
        if (newsItem == null) {
            LogUtils.e(CollectActivity.class.getSimpleName(), "item on click:param [newsItem] is null ,can't jump");
            return;
        }
        //跳转
        Intent i = new Intent();
        if (TextUtils.isEmpty(newsItem.getVideodm()) && TextUtils.isEmpty(newsItem.getUpVideoId())) {
            i.setClass(context, PostZoneActivity.class);
            i.putExtra(Constant.KEY_ID, newsItem.getId());
            i.putExtra(Constant.KEY_CATID, newsItem.getCatId());
            i.putExtra(Constant.KEY_CATNAME, newsItem.getCatname());
        } else {
            VideoActivity.intoNewIntent(context,newsItem.getId(),newsItem.getUpVideoId(),newsItem.getCatId());
//            i.setClass(context, VideoActivity.class);
//            i.putExtra(Constant.KEY_ID, newsItem.getId());
//            i.putExtra(Constant.KEY_CATID, newsItem.getCatId());
//            i.putExtra(Constant.KEY_DAOSHI, newsItem.getDaoshi());
//            if (!TextUtils.isEmpty(newsItem.getVideodm())) {
//                i.putExtra(Constant.KEY_VIDEO_ID, newsItem.getVideodm());
//            } else {
//                i.putExtra(Constant.KEY_VIDEO_ID, newsItem.getUpVideoId());
//            }
        }
        context.startActivity(i);
    }
}
