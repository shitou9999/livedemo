package tv.kuainiu.ui.publishing.pick;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tv.kuainiu.R;
import tv.kuainiu.modle.TeacherZoneDynamicsInfo;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item, parent, false);
            mViewHolder=new ViewHolder();
            mViewHolder.tv_head_top = (TextView) convertView.findViewById(R.id.tv_head_top);
            mViewHolder.tv_city = (TextView) convertView.findViewById(R.id.tv_city);
            mViewHolder.tv_head_top.setVisibility(View.GONE);
            convertView.setTag(convertView);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.tv_city.setText(StringUtils.replaceNullToEmpty(getItem(position).getNews_title()));
        return convertView;
    }

    static class ViewHolder {
        TextView tv_head_top;
        TextView tv_city;
    }
}
