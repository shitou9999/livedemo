package com.kuainiutv.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author nanck on 2016/5/18.
 */
public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private Context mContext;
    private List<Object> mList;
    private int mLayoutId;
//    private VH ;

    public BaseAdapter(Context context, List<Object> list, int layout) {
        mContext = context;
        mList = list;
        mLayoutId = layout;
    }

    public void setData(List<Object> list) {
        mList = list;
    }

    @Override public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        bindDataToView(holder, position);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        return null;
    }


    protected abstract void bindDataToView(RecyclerView.ViewHolder holder, int position);


}

