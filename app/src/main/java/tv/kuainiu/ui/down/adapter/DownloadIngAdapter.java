package tv.kuainiu.ui.down.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bokecc.sdk.mobile.download.Downloader;

import java.util.LinkedList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.modle.DownloadInfo;
import tv.kuainiu.utils.ImageDisplayUtil;

/**
 *
 */
public class DownloadIngAdapter extends RecyclerView.Adapter<DownloadIngAdapter.ViewHolder> {
//    private static final int REFRESH_PROGRESS = 1;
//    private static final int SUCCESS = 2;
//    private static final int FAILURE = 3;
    private Context mContext;
    private LinkedList<DownloadInfo> mDownloadInfo2s = new LinkedList<>();
    private LinkedList<DownloadInfo> mSelectedInfos = new LinkedList<>();

    private OnSelectListener mSelectedListener;

    private boolean mIsEditing = false;

    public void setEditing() {
        mIsEditing = !mIsEditing;
        notifyDataSetChanged();
    }


    public void setSelectedInfos(LinkedList<DownloadInfo> selectedInfos) {
        mSelectedInfos = selectedInfos;
    }

    public void setOnSelectedListener(OnSelectListener selectedListener) {
        mSelectedListener = selectedListener;
    }

   /* public static class MyHandler extends WeakHandler<DownloadIngAdapter> {
        public MyHandler(DownloadIngAdapter owner) {
            super(owner);
        }

        public void handleMessage(Message msg) {
            DownloadIngAdapter downloadAdapter = getOwner();
            Button btn = null;
            int position = msg.arg1;
            switch (msg.what) {
                case REFRESH_PROGRESS:
                    int percent = msg.getData().getInt("percent", 0);
                    if (msg.obj != null) {
                        ViewHolder viewHolder = (ViewHolder) msg.obj;
                        setPercentage(percent, viewHolder.textPercentage, viewHolder.progressBar);
                    }
                    break;

                case SUCCESS:
                    DebugUtils.showToast(downloadAdapter.mContext, "下载成功");
                    if (msg.obj != null) {
                        ViewHolder viewHolder = (ViewHolder) msg.obj;
                        setPercentage(100, viewHolder.textPercentage, viewHolder.progressBar);
                        viewHolder.textStatus.setText("下载成功");
                    }
                    break;

                case FAILURE:
                    String error = msg.getData().getString("error");
                    LogUtils.e("DownloadIngAdapter", error);
                    DebugUtils.showToast(downloadAdapter.mContext, "下载失败");
                    if (msg.obj != null) {
                        ViewHolder viewHolder = (ViewHolder) msg.obj;
                        viewHolder.textStatus.setText("下载失败");
                    }
                    break;
            }
        }
    }*/

    private static void setPercentage(int percent, TextView textPercentage, ProgressBar progressBar) {
        int percentTemp = percent > 0 ? percent : 0;
        percentTemp = percentTemp < 100 ? percentTemp : 100;
        textPercentage.setText(String.format(Locale.CHINA, "%d%%", percentTemp));
        progressBar.setProgress(percent);
        progressBar.setMax(100);
    }


//    private MyHandler handler = null;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_major)
        RelativeLayout rlMajor;
        @BindView(R.id.rl_check)
        RelativeLayout rlCheck;
        @BindView(R.id.fl_dl_status)
        FrameLayout flStatus;
        @BindView(R.id.iv_check)
        ImageView imageCheck;
        @BindView(R.id.iv_cover)
        ImageView imageCover;
        @BindView(R.id.tv_title)
        TextView textTitle;
        @BindView(R.id.tv_speed)
        TextView textSpeed;
        @BindView(R.id.tv_dl_status)
        TextView textStatus;
        @BindView(R.id.tv_percentage)
        TextView textPercentage;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public DownloadIngAdapter(Context context, LinkedList<DownloadInfo> data) {
        mContext = context;
        mDownloadInfo2s = data;
//        handler = new MyHandler(this);
    }


    public void setData(LinkedList<DownloadInfo> data) {
        mDownloadInfo2s = data;
    }

    @Override
    public int getItemCount() {
        return mDownloadInfo2s == null ? 0 : mDownloadInfo2s.size();
    }

    @Override
    public DownloadIngAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video_download2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DownloadIngAdapter.ViewHolder holder, final int position) {
        DownloadInfo info = mDownloadInfo2s.get(position);
        ImageDisplayUtil.displayImage(mContext, holder.imageCover, info.getFirstImage());
        holder.textTitle.setText(info.getName());
        holder.textSpeed.setText(info.getProgressText());
        if(info.getStatus()!= Downloader.FINISH) {
            int percent = info.getProgress();
            setPercentage(percent, holder.textPercentage, holder.progressBar);
        }else{
            setPercentage(100, holder.textPercentage, holder.progressBar);
        }
        if (mIsEditing) {
            if (mSelectedInfos != null && mSelectedInfos.contains(info)) {
                holder.imageCheck.setSelected(true);
            } else {
                holder.imageCheck.setSelected(false);
            }
            holder.rlCheck.setVisibility(View.VISIBLE);
        } else {
            holder.rlCheck.setVisibility(View.GONE);
        }


        holder.rlMajor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadInfo info = mDownloadInfo2s.get(position);
                if (mIsEditing) {
                    if (mSelectedInfos != null) {
                        if (!mSelectedInfos.contains(info)) {
                            mSelectedInfos.addLast(info);
                        } else {
                            mSelectedInfos.remove(info);
                        }
                        if (mSelectedListener != null) {
                            mSelectedListener.selected(mSelectedInfos);
                        }
                        notifyItemChanged(position);
                    }
                }
            }
        });


        holder.textStatus.setText(info.getStatusInfo());
        holder.flStatus.setTag(info);
        holder.flStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedListener != null) {
                    mSelectedListener.click(v);
                }
            }
        });
    }


    public interface OnSelectListener {
        void selected(LinkedList<DownloadInfo> selectedInfos);

        void click(View v);
    }
}
