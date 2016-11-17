package tv.kuainiu.ui.down.adapter;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.util.Attributes;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.app.ConfigUtil;
import tv.kuainiu.app.DataSet;
import tv.kuainiu.command.dao.VideoDownloadDao;
import tv.kuainiu.modle.DownloadInfo;
import tv.kuainiu.ui.video.VideoActivity;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.StringUtils;

/**
 * @author nanck on 2016/5/6.
 */
public class DownloadEdAdapter extends RecyclerSwipeAdapter<DownloadEdAdapter.ViewHolder> implements View.OnClickListener {
    /* Item正常状态 */
    public static final int NORMAL = 0x1;

    /* Item 左滑状态 */
    public static final int LEFT_SLIDE = 0x2;

    /* Item 可编辑(多选状态) */
    public static final int EDITING = 0x3;

    private int mItemStatus = NORMAL;
    private LinkedList<DownloadInfo> mDownloadInfos = new LinkedList<>();
    private LinkedList<DownloadInfo> mSelectedInfos = new LinkedList<>();

    private Context mContext;
    private VideoDownloadDao mDownloadDao;
    private OnSelectListener mSelectedListener;


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.download_ed_swipe)
        SwipeLayout swipeLayout;
        @BindView(R.id.rl_major)
        RelativeLayout rlMajor;
        @BindView(R.id.ll_delete)
        LinearLayout llDelete;
        @BindView(R.id.iv_cover)
        ImageView imageCover;
        @BindView(R.id.rl_check)
        RelativeLayout rlCheck;
        @BindView(R.id.iv_check)
        ImageView imageCheck;
        @BindView(R.id.tv_title)
        TextView textTitle;
        @BindView(R.id.tv_teacherName)
        TextView tvTeacherName;
        @BindView(R.id.tv_video_size)
        TextView textVideoSize;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public void setDownloadInfos(LinkedList<DownloadInfo> downloadInfos) {
        mDownloadInfos = downloadInfos;
    }

    public void setSelectedInfos(LinkedList<DownloadInfo> selectedInfos) {
        mSelectedInfos = selectedInfos;
    }

    public DownloadEdAdapter(Context context, LinkedList<DownloadInfo> downloadInfos) {
        mContext = context;
        mDownloadInfos = downloadInfos;
        mDownloadDao = new VideoDownloadDao(context);
    }


    public void setItemStatusEditing() {
        mItemStatus = (EDITING != mItemStatus) ? EDITING : NORMAL;
        if (mSelectedInfos != null) {
            mSelectedInfos.clear();
        }
        notifyDataSetChanged();
    }

    private void setItemStatus(int itemStatus) {
        mItemStatus = itemStatus;
    }

    public void setOnSelectedListener(OnSelectListener selectedListener) {
        mSelectedListener = selectedListener;
    }

    @Override
    public int getItemCount() {
        return mDownloadInfos == null ? 0 : mDownloadInfos.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video_download_ed, null);
        ViewHolder vh = new ViewHolder(view);
        vh.rlMajor.setOnClickListener(this);
        return vh;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mItemManger.setMode(Attributes.Mode.Single);
        final DownloadInfo info = mDownloadInfos.get(position);
        holder.rlMajor.setTag(R.id.rl_check, position);
        holder.rlMajor.setTag(R.id.iv_check, info);

        if (EDITING == mItemStatus) {
            if (mSelectedInfos != null && mSelectedInfos.contains(info)) {
                holder.imageCheck.setSelected(true);
            } else {
                holder.imageCheck.setSelected(false);
            }
            holder.rlCheck.setVisibility(View.VISIBLE);
            holder.swipeLayout.setSwipeEnabled(false);
        } else {
            holder.swipeLayout.setSwipeEnabled(true);
            holder.rlCheck.setVisibility(View.GONE);
        }

        ImageDisplayUtil.displayImage(mContext, holder.imageCover, info.getFirstImage(), R.mipmap.ic_def_error);
        holder.textTitle.setText(StringUtils.replaceNullToEmpty(info.getName()));
        holder.tvTeacherName.setText(StringUtils.replaceNullToEmpty(info.getTeacherName()));
        holder.textVideoSize.setText(info.getProgressText());


        holder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
                List<Integer> openItems = mItemManger.getOpenItems();
                if (openItems != null && openItems.size() > 0) {
                    mItemManger.closeItem(openItems.get(0));
                }
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                setItemStatus(LEFT_SLIDE);
            }

            @Override
            public void onClose(SwipeLayout layout) {
                setItemStatus(NORMAL);
            }
        });

        holder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataSet.removeDownloadInfo(info.getTitle());
                String path=Environment.getExternalStorageDirectory()
                        + "/".concat(ConfigUtil.DOWNLOAD_DIR).concat("/")
                        .concat(info.getTitle()).concat(".mp4");
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
                mItemManger.removeShownLayouts(holder.swipeLayout);
                mDownloadInfos.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mDownloadInfos.size());
                mItemManger.closeAllItems();
            }
        });


    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.download_ed_swipe;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(R.id.rl_check);
        DownloadInfo info = (DownloadInfo) v.getTag(R.id.iv_check);
        if (info == null) {
            return;
        }
        DebugUtils.dd("Current Item Status click : " + mItemStatus);
        switch (mItemStatus) {
            case NORMAL:
                VideoActivity.intoNewIntent(mContext, info.getNewId(), info.getVideoId(), info.getCatId(), info.getName(),true, info.getTitle());
                break;

            case LEFT_SLIDE:
                List<Integer> openItems = mItemManger.getOpenItems();
                if (openItems != null && openItems.size() > 0) {
                    mItemManger.closeItem(openItems.get(0));
                    setItemStatus(NORMAL);
                }
                break;

            case EDITING:
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
                break;

            default:
                break;
        }
    }

    public interface OnSelectListener {
        void selected(LinkedList<DownloadInfo> selectedInfos);
    }

}
