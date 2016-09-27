package tv.kuainiu.ui.liveold.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.R;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.liveold.ReplayLiveActivity;
import tv.kuainiu.ui.liveold.fragment.LiveFragment;
import tv.kuainiu.ui.liveold.model.History;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.StringUtils;

/**
 * @author nanck on 2016/7/25.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> implements View.OnClickListener {
    private Activity mContext;
    private List<History> mHistories;

    public HistoryAdapter(Activity context) {
        mContext = context;
    }

    public void setHistories(List<History> histories) {
        mHistories = histories;
    }

    @Override public int getItemCount() {
        return mHistories == null ? 0 : mHistories.size();
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        History history = mHistories.get(position);

        holder.itemView.setTag(history);

        ImageDisplayUtil.displayImage(mContext, holder.imageAlbum, history.getLive_thumb(), R.mipmap.ic_def_error);
        ImageDisplayUtil.displayImage(mContext, holder.civHead, history.getTeacher_thumb(), R.mipmap.ic_def_error);

        holder.textName.setText(history.getAnchor().trim());
        holder.textTitle.setText(history.getTitle().trim());
        // 播放次数
        String viewCount = StringUtils.getDecimal(history.getPlayback_times(), Constant.TEN_THOUSAND, "万次播放", "次播放");
        holder.textViewCount.setText(viewCount);

        // 使用字段不明确。最后返回时间戳
        String tb = DateUtil.getDurationString(DateUtil.toJava(history.getStart_date_time()));
        holder.textTimeBefore.setText(tb);

    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_live_history_news, parent, false);
        ViewHolder vh = new ViewHolder(view);
        vh.itemView.setOnClickListener(this);
        return vh;
    }


    private void startPlayingActivity(History history) {
        if (mContext == null) return;
        if (history == null) {
            DebugUtils.showToast(mContext, R.string.live_toast_not_started);
            return;
        }
        Intent intent = new Intent(mContext, ReplayLiveActivity.class);
//        LivingInfo.LiveingEntity entity = new LivingInfo.LiveingEntity();
//        entity.setTitle(history.getTitle());
//        entity.setAnchor(history.getAnchor());
        intent.putExtra(LiveFragment.ARG_LIVING, history);
        mContext.startActivity(intent);
    }

    @Override public void onClick(View v) {
        History history = (History) v.getTag();
//        LiveHttpUtil.historyCountPlusOne(mContext, history.getId());
        startPlayingActivity(history);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_photo) ImageView imageAlbum;
        @BindView(R.id.civ_head) CircleImageView civHead;
        @BindView(R.id.tv_title) TextView textTitle;
        @BindView(R.id.tv_name) TextView textName;
        @BindView(R.id.tv_view_count) TextView textViewCount;
        @BindView(R.id.tv_time_before) TextView textTimeBefore;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
