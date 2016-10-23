package tv.kuainiu.ui.me.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.app.Constans;
import tv.kuainiu.app.OnItemClickListener;
import tv.kuainiu.modle.LiveInfo;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.utils.StringUtils;

/**
 * Created by jack on 2016/4/28.
 * 收藏
 */
public class MyLiveHistoryFragmentAdapter extends RecyclerView.Adapter<MyLiveHistoryFragmentAdapter.ViewHolder> {
    private Context mContext;
    private List<LiveInfo> listData;
    private OnItemClickListener mOnItemClickListener;

    public MyLiveHistoryFragmentAdapter(Context context, List<LiveInfo> listData, OnItemClickListener mOnItemClickListener) {
        this.mContext = context;
        this.listData = listData;
        this.mOnItemClickListener = mOnItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_my_live_history_item, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final LiveInfo itemData = listData.get(position);
        holder.tvLiveState.setText(StringUtils.replaceNullToEmpty(itemData.getLive_msg()));
        switch (itemData.getLive_status()) {
            case Constans.LIVE_END://直播结束
                holder.tvLiveState.setBackgroundColor(mContext.getResources().getColor(R.color.colorGrey900));
                break;

            case Constans.LIVE_ING://直播中
                holder.tvLiveState.setBackgroundColor(mContext.getResources().getColor(R.color.colorRed500));
                break;
            case Constans.LiVE_UN_START://直播未开始
                holder.tvLiveState.setBackgroundColor(mContext.getResources().getColor(R.color.colorGrey450));
                break;
            default:
                holder.tvLiveState.setBackgroundColor(mContext.getResources().getColor(R.color.colorGrey900));
                break;
        }
        holder.tvLiveDescription.setText(StringUtils.replaceNullToEmpty(itemData.getTitle()));
        holder.tvNumber.setText(String.format(Locale.CHINA, "%s人", StringUtils.getDecimal(itemData.getAppointment_count(), Constant.TEN_THOUSAND, "万", "")));
        holder.tvState.setText("观看过");
        holder.rlItem.setTag(itemData);
        holder.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(v);
                }
            }
        });
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvLiveState)
        TextView tvLiveState;
        @BindView(R.id.tvLiveDescription)
        TextView tvLiveDescription;
        @BindView(R.id.vLine)
        View vLine;
        @BindView(R.id.tvNumber)
        TextView tvNumber;
        @BindView(R.id.tvState)
        TextView tvState;
        @BindView(R.id.rl_item)
        RelativeLayout rlItem;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
