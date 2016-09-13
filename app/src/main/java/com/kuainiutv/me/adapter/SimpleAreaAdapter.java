package com.kuainiutv.me.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kuainiutv.modle.Area;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author nanck on 2016/7/4.
 */
public class SimpleAreaAdapter extends BaseAdapter {

    private Context mContext;
    private List<Area> mDatas;

    public SimpleAreaAdapter(Context context) {
        mContext = context;
    }

    public SimpleAreaAdapter(Context context, List<Area> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setData(List<Area> data) {
        mDatas = data;
    }

    @Override public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override public Area getItem(int position) {
        return mDatas.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Area area = getItem(position);
        if (!TextUtils.isEmpty(area.name)) {
            holder.textView.setText(area.name);
            holder.textView.setVisibility(View.VISIBLE);
        } else {
            holder.textView.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(android.R.id.text1) TextView textView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
