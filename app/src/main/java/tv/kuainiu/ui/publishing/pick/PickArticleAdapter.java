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
import tv.kuainiu.modle.TeacherZoneDynamicsInfo;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.utils.StringUtils;

/**
 * Created by sirius on 2016/10/13.
 */

public class PickArticleAdapter extends BaseAdapter {
    private Context context;
    private List<TeacherZoneDynamicsInfo> list;
    private View view;

    public PickArticleAdapter(Context context, List<TeacherZoneDynamicsInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public TeacherZoneDynamicsInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = null;
        TeacherZoneDynamicsInfo teacherZoneDynamicsInfo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.pick_article_item, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.tv_article_name = (TextView) convertView.findViewById(R.id.tv_article_name);
            mViewHolder.ivType = (ImageView) convertView.findViewById(R.id.ivType);
            convertView.setTag(convertView);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.tv_article_name.setText(StringUtils.replaceNullToEmpty(teacherZoneDynamicsInfo.getNews_title()));
        switch (teacherZoneDynamicsInfo.getType()) {
            case Constant.NEWS_TYPE_VIDEO:
                mViewHolder.ivType.setImageResource(R.mipmap.play_pre);
                break;
//            case Constant.NEWS_TYPE_VOICE:
//                mViewHolder.ivType.setImageResource(R.mipmap.play_pre);
//                break;
            case Constant.NEWS_TYPE_LIVE:
                mViewHolder.ivType.setImageResource(R.mipmap.video_pre);
                break;
            default:
                mViewHolder.ivType.setImageResource(R.mipmap.text_pre);
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tv_article_name;
        ImageView ivType;
    }
}
