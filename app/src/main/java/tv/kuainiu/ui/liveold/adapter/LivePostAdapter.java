package tv.kuainiu.ui.liveold.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.R;
import tv.kuainiu.command.dao.LiveSubscribeDao;
import tv.kuainiu.ui.liveold.AlarmReceiver;
import tv.kuainiu.ui.liveold.LIVEConstant;
import tv.kuainiu.ui.liveold.RemindUtils;
import tv.kuainiu.ui.liveold.model.LiveInfoEntry;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.ImageDisplayUtil;

/**
 * @author nanck on 2016/7/7.
 */
public class LivePostAdapter extends RecyclerView.Adapter<LivePostAdapter.ViewHolder>
        implements View.OnClickListener {

    private static final String FORMAT_TIME = "HH:mm";

    public static final long ADV_FIVE_MINUTES = 5 * 60 * 1000;
    public static final long ADV_TEN_MINUTES = 10 * 60 * 1000;
    public static final long ADV_TWENTY_MINUTES = 20 * 60 * 1000;
    public static final long ADV_THIRTY_MINUTES = 30 * 60 * 1000;

    private int advMinutes;

    private Context mContext;
    private List<LiveInfoEntry> mInfoEntries = new ArrayList<>();
    RelativeLayout.LayoutParams lp;
    RelativeLayout.LayoutParams lp2;

    private LiveSubscribeDao mSubscribeDao;

    public LivePostAdapter(Context context) {
        this(context, null);
    }


    public LivePostAdapter(Context context, List<LiveInfoEntry> infoEntries) {
        mContext = context;
        mSubscribeDao = new LiveSubscribeDao(context);

        mInfoEntries = infoEntries;
        int nor = context.getResources().getDimensionPixelSize(R.dimen.live_item_point_size_nor);
        int ing = context.getResources().getDimensionPixelSize(R.dimen.live_item_point_size_ing);
        lp = new RelativeLayout.LayoutParams(nor, nor);
        lp2 = new RelativeLayout.LayoutParams(ing, ing);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
    }


    public void setInfoEntries(List<LiveInfoEntry> infoEntries) {
        mInfoEntries = infoEntries;
    }

    @Override public int getItemCount() {
        return mInfoEntries == null ? 0 : mInfoEntries.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_live_info, parent, false);
        ViewHolder vh = new ViewHolder(view);
        vh.textLiveSubscribe.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LiveInfoEntry entry = mInfoEntries.get(position);

        ImageDisplayUtil.displayImage(mContext, holder.civAnchorHead, entry.getThumb(), R.mipmap.head_nor);

        String tempStart = DateUtil.toTimestampForString(FORMAT_TIME, DateUtil.toJava(entry.getStart_date_time()));
        String tempEnd = DateUtil.toTimestampForString(FORMAT_TIME, DateUtil.toJava(entry.getEnd_date_time()));
        holder.textLiveTime.setText(mContext.getString(R.string.value_live_item_time, tempStart, tempEnd));

        holder.textAnchorName.setText(mContext.getString(R.string.value_live_anchor, entry.getAnchor()));
        holder.textLiveTitle.setText(entry.getTitle());
        holder.textLiveStatus.setText(entry.getLive_status_msg());

        int status = entry.getLive_status();
        holder.rlMid.setSelected(LIVEConstant.PLAYING == entry.getLive_status());
        holder.textLiveTitle.setSelected(LIVEConstant.PLAYING == entry.getLive_status());


        Bundle bundle = new Bundle();
        bundle.putParcelable("1", entry);
        bundle.putInt("p", position);
        holder.textLiveSubscribe.setTag(bundle);

        // XXX 理清逻辑
        if (LIVEConstant.PLAYING == status) {
            holder.textLiveSubscribe.setVisibility(View.INVISIBLE);
            holder.textLiveStatus.setVisibility(View.VISIBLE);

            holder.viewTimePoint.setLayoutParams(lp2);
            holder.viewTimeLine.setBackgroundResource(R.color.colorAmber550);
            holder.viewTimePoint.setBackgroundResource(R.drawable.draw_circle_amber);
        } else {
            holder.viewTimePoint.setLayoutParams(lp);
            holder.viewTimePoint.setBackgroundResource(R.drawable.draw_point_grey);
            holder.viewTimeLine.setBackgroundResource(R.color.colorGrey500);
            if (LIVEConstant.PLAYED == status) {
                holder.textLiveSubscribe.setVisibility(View.INVISIBLE);
                holder.textLiveStatus.setVisibility(View.VISIBLE);
            } else {
                holder.textLiveStatus.setVisibility(View.GONE);
                holder.textLiveSubscribe.setVisibility(View.VISIBLE);

                boolean flag = RemindUtils.REMIND == entry.getRemindFlag();
                holder.textLiveSubscribe.setSelected(flag);
                String text = flag ? "取消预约" : "预约提醒";
                holder.textLiveSubscribe.setText(text);
            }
        }

    }


    // XXX 需要处理各版本 AlarmManager 的兼容性
    @Override public void onClick(View v) {
        if (v.getId() == R.id.tv_live_subscribe) {
            Bundle bundle = (Bundle) v.getTag();
            LiveInfoEntry entry = bundle.getParcelable("1");
            int p = bundle.getInt("p");

            if (entry == null) return;


            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(mContext.getApplicationContext(), AlarmReceiver.class);
            intent.putExtra("id", entry.getId());
            intent.putExtra("title", entry.getTitle());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext.getApplicationContext(), 200, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            boolean isRemind = RemindUtils.REMIND == entry.getRemindFlag();

            if (!isRemind) {
                boolean flag = mSubscribeDao.insert(entry.getId(), 1);
                if (flag) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, (DateUtil.toJava(entry.getStart_date_time()) - ADV_FIVE_MINUTES), pendingIntent);
                    entry.setRemindFlag(RemindUtils.REMIND);
                } else {
                    DebugUtils.showToast(mContext, "添加预约提醒失败，请稍后再试");
                }


            } else {
                boolean flag = mSubscribeDao.delete(entry.getId());
                if (flag) {
                    alarmManager.cancel(pendingIntent);
                    entry.setRemindFlag(RemindUtils.UN_REMIND);
                } else {
                    DebugUtils.showToast(mContext, "取消预约失败，请稍后再试");
                }

            }

            isRemind = RemindUtils.REMIND == entry.getRemindFlag();
            String text = isRemind ? "取消预约" : "预约提醒";
            ((TextView) v).setText(text);

            v.setSelected(RemindUtils.REMIND == entry.getRemindFlag());
//            notifyItemChanged(p);
        }
    }

    private int findLiving() {
        if (mInfoEntries == null || mInfoEntries.isEmpty()) return -1;
        int position = 0;
        for (int i = 0; i < mInfoEntries.size(); i++) {
            if (LIVEConstant.PLAYING == mInfoEntries.get(i).getLive_status()) {
                position = i;
                break;
            }
        }
        return position;
    }

    // 滚动到正在直播的条目
    public void scrollToLiving(LinearLayoutManager layoutManager) {
        int position = findLiving();
        if (position > -1) {
            layoutManager.scrollToPositionWithOffset(position, -8);
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.view_live_time_point) View viewTimePoint;
        @BindView(R.id.view_live_time_line) View viewTimeLine;
        @BindView(R.id.rl_live_entry_mid) RelativeLayout rlMid;
        @BindView(R.id.civ_anchor_head) CircleImageView civAnchorHead;
        @BindView(R.id.tv_live_anchor) TextView textAnchorName;
        @BindView(R.id.tv_live_time) TextView textLiveTime;
        @BindView(R.id.tv_live_status) TextView textLiveStatus;
        @BindView(R.id.tv_live_subscribe) TextView textLiveSubscribe;
        @BindView(R.id.tv_live_title) TextView textLiveTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
