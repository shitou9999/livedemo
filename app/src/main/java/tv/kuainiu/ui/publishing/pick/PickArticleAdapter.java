package tv.kuainiu.ui.publishing.pick;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tv.kuainiu.R;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.modle.push.CustomVideo;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.StringUtils;

/**
 * Created by sirius on 2016/10/13.
 */

public class PickArticleAdapter extends BaseAdapter {
    private Context context;
    private List<CustomVideo> customVideoList;
    private View view;

    public PickArticleAdapter(Context context, List<CustomVideo> list) {
        this.context = context;
        this.customVideoList = list;
    }

    @Override
    public int getCount() {
        return customVideoList == null ? 0 : customVideoList.size();
    }

    @Override
    public CustomVideo getItem(int position) {
        return customVideoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = null;
        CustomVideo teacherZoneDynamicsInfo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.pick_article_item, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.tv_article_name = (TextView) convertView.findViewById(R.id.tv_article_name);
            mViewHolder.tv_article_date = (TextView) convertView.findViewById(R.id.tv_article_date);
            mViewHolder.ivType = (ImageView) convertView.findViewById(R.id.ivType);
            convertView.setTag(convertView);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.tv_article_date.setText(DateUtil.getTimeString_MM_dd(teacherZoneDynamicsInfo.getInputtime()));
        mViewHolder.tv_article_name.setText(StringUtils.replaceNullToEmpty(teacherZoneDynamicsInfo.getTitle()));
        switch (teacherZoneDynamicsInfo.getType()) {
            case Constant.NEWS_TYPE_VIDEO:
                mViewHolder.ivType.setImageResource(R.drawable.movie_btn);
                break;
//            case Constant.NEWS_TYPE_VOICE:
//                mViewHolder.ivType.setImageResource(R.mipmap.play_pre);
//                break;
            case Constant.NEWS_TYPE_LIVE:
                mViewHolder.ivType.setImageResource(R.drawable.movie_btn);
                break;
            default:
                mViewHolder.ivType.setImageResource(R.drawable.word_btn);
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tv_article_name;
        TextView tv_article_date;
        ImageView ivType;
    }
}
