package tv.kuainiu.ui.me.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.R;
import tv.kuainiu.app.ISwipeDeleteItemClickListening;
import tv.kuainiu.modle.LiveInfo;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.liveold.PlayLiveActivity;
import tv.kuainiu.ui.liveold.ReplayLiveActivity;
import tv.kuainiu.ui.liveold.model.LiveParameter;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.StringUtils;

import static tv.kuainiu.R.id.ivIamge;
import static tv.kuainiu.R.id.tv_delete;

/**
 * Created by jack on 2016/4/28.
 * 收藏
 */
public class MyLiveFragmentAdapter extends BaseSwipeAdapter {
    private Context mContext;
    private List<LiveInfo> mLiveListList;
    private ISwipeDeleteItemClickListening idelectItem;

    public MyLiveFragmentAdapter(Context context, List<LiveInfo> mLiveListList, ISwipeDeleteItemClickListening idelectItem) {
        this.mContext = context;
        this.mLiveListList = mLiveListList;
        this.idelectItem = idelectItem;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_my_live_item, null);
        return convertView;
    }

    @Override
    public void fillValues(final int position, final View convertView) {

        ViewHolder holder = new ViewHolder(convertView);
        final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        final LiveInfo liveItem = mLiveListList.get(position);
        //绑定直播Item
        ImageDisplayUtil.displayImage(mContext, holder.mIvIamge, liveItem.getThumb());
        ImageDisplayUtil.displayImage(mContext, holder.civ_avatar, liveItem.getTeacher_info().getAvatar(), R.mipmap.default_avatar);
        holder.rlState.setVisibility(View.GONE);
        holder.tvAppointment.setVisibility(View.GONE);
        holder.mTvLiveing.setText(String.format(Locale.CHINA, "%s人预约", StringUtils.getDecimal(liveItem.getAppointment_count(), Constant.TEN_THOUSAND, "万", "")));
        holder.rlState.setVisibility(View.VISIBLE);
        switch (liveItem.getStatus()) {
            case -1://审核未通过
                holder.rlState.setBackgroundColor(Color.parseColor("#66000000"));
                holder.tvState.setText("审核未通过");
                break;

            case 1://等待直播中
                if (liveItem.getLive_status() != 2) {
                    holder.rlState.setBackgroundColor(Color.parseColor("#66F83848"));
                    holder.tvState.setText("等待直播中");
                } else {
                    holder.rlState.setVisibility(View.GONE);
                }

                break;
            default://0 等待审核
                holder.rlState.setBackgroundColor(Color.parseColor("#66F5A623"));
                holder.tvState.setText("等待审核");
                break;
        }
        holder.ivLiveIng.setVisibility(View.GONE);
        holder.mTvLiveingNumber.setText(DateUtil.formatDate(liveItem.getStart_date(), "yyyy-MM-dd HH:mm:ss", "MM-dd HH:mm"));
        holder.tvTitle.setText(StringUtils.replaceNullToEmpty(liveItem.getTitle()));
        holder.mTvTeacherName.setText(StringUtils.replaceNullToEmpty(liveItem.getAnchor()));
        String tags = "";
        if (liveItem.getTeacher_info() != null && liveItem.getTeacher_info().getTag_list() != null) {
            String[] tag_list = liveItem.getTeacher_info().getTag_list();
            if (tag_list.length > 0) {
                for (int i = 0; i < tag_list.length; i++) {
                    tags += tag_list[i] + "　";
                }
            }
        }
        holder.mTvTeacherIntroduce.setText(StringUtils.replaceNullToEmpty(tags));
        holder.mTvTeacherTheme.setText(StringUtils.replaceNullToEmpty(liveItem.getTeacher_info().getSlogan()));
        holder.mIvIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //直播参数传递
                if (TextUtils.isEmpty(liveItem.getPlayback_id())) {
                    clickPlayLive(liveItem);
                } else {
                    clickRePlayLive(liveItem);
                }
            }
        });

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idelectItem != null) {
                    idelectItem.delete(swipeLayout, position, liveItem);
                }
            }
        });
    }

    @Override
    public int getCount() {
        return mLiveListList != null ? mLiveListList.size() : 0;
    }

    @Override
    public LiveInfo getItem(int position) {
        return mLiveListList != null ? mLiveListList.get(position) : null;
    }


    public void clickPlayLive(LiveInfo liveItem) {
        LiveParameter liveParameter = new LiveParameter();
        liveParameter.setLiveId(liveItem.getId());
        liveParameter.setLiveTitle(liveItem.getTitle());
        liveParameter.setRoomId(liveItem.getTeacher_info().getLive_roomid());
        liveParameter.setTeacherId(liveItem.getTeacher_id());
        PlayLiveActivity.intoNewIntent(mContext, liveParameter);
    }

    /**
     * 回放
     *
     * @param liveItem
     */
    public void clickRePlayLive(LiveInfo liveItem) {
        LiveParameter liveParameter = new LiveParameter();
        liveParameter.setLiveId(liveItem.getId());
        liveParameter.setLiveTitle(liveItem.getTitle());
        liveParameter.setRoomId(liveItem.getTeacher_info().getLive_roomid());
        liveParameter.setTeacherId(liveItem.getTeacher_id());
        liveParameter.setCcid(liveItem.getPlayback_id());
        ReplayLiveActivity.intoNewIntent(mContext, liveParameter);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        @BindView(ivIamge)
        ImageView mIvIamge;
        @BindView(R.id.ivIsVip)
        ImageView ivIsVip;
        @BindView(R.id.ivLiveIng)
        ImageView ivLiveIng;
        @BindView(R.id.tvLiveing)
        TextView mTvLiveing;
        @BindView(R.id.civ_avatar)
        CircleImageView civ_avatar;
        @BindView(R.id.tvLiveingNumber)
        TextView mTvLiveingNumber;
        @BindView(R.id.tv_teacher_name)
        TextView mTvTeacherName;
        @BindView(R.id.tv_teacher_introduce)
        TextView mTvTeacherIntroduce;
        @BindView(R.id.tv_teacher_theme)
        TextView mTvTeacherTheme;
        @BindView(R.id.tvAppointment)
        TextView tvAppointment;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.rlState)
        RelativeLayout rlState;
        @BindView(R.id.tvState)
        TextView tvState;
        @BindView(tv_delete)
        public TextView tvDelete;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
