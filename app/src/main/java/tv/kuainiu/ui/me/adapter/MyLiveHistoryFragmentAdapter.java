package tv.kuainiu.ui.me.adapter;

import android.content.Context;
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

import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.R;
import tv.kuainiu.app.Constans;
import tv.kuainiu.modle.LiveInfo;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.StringUtils;

/**
 * Created by jack on 2016/4/28.
 * 收藏
 */
public class MyLiveHistoryFragmentAdapter extends BaseSwipeAdapter {
    private Context context;
    private List<LiveInfo> listCollect;
    private IdeletItem idelectItem;

    public MyLiveHistoryFragmentAdapter(Context context, List<LiveInfo> listCollect, IdeletItem idelectItem) {
        this.context = context;
        this.listCollect = listCollect;
        this.idelectItem = idelectItem;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.activity_appointment_item, null);
        return convertView;
    }

    @Override
    public void fillValues(final int position, final View convertView) {
        RelativeLayout rl_avatar = (RelativeLayout) convertView.findViewById(R.id.rl_avatar);
        rl_avatar.setVisibility(View.GONE);
        CircleImageView ci_avatar = (CircleImageView) convertView.findViewById(R.id.ci_avatar);
        ci_avatar.setVisibility(View.GONE);
        ImageView ivIsVip = (ImageView) convertView.findViewById(R.id.ivIsVip);
        ivIsVip.setVisibility(View.GONE);
        TextView tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
        TextView tvTeacherName = (TextView) convertView.findViewById(R.id.tvTeacherName);
        tvTeacherName.setVisibility(View.GONE);
        TextView tvTheme = (TextView) convertView.findViewById(R.id.tvTheme);
        tvTheme.setVisibility(View.GONE);
        TextView tv_live_time = (TextView) convertView.findViewById(R.id.tv_live_time);
        tv_live_time.setVisibility(View.GONE);
        TextView tv_live_flag = (TextView) convertView.findViewById(R.id.tv_live_flag);
        tv_live_flag.setVisibility(View.GONE);
        TextView tvLiveState = (TextView) convertView.findViewById(R.id.tvLiveState);
        TextView tvLiveDescription = (TextView) convertView.findViewById(R.id.tvLiveDescription);
        TextView tvNumber = (TextView) convertView.findViewById(R.id.tvNumber);
        TextView tvState = (TextView) convertView.findViewById(R.id.tvState);
        final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        swipeLayout.setSwipeEnabled(false);
        final LiveInfo itemData = getItem(position);
        ImageDisplayUtil.displayImage(context, ci_avatar, StringUtils.replaceNullToEmpty(itemData.getTeacher_info().getAvatar()), R.mipmap.default_avatar);
        tvTeacherName.setText(StringUtils.replaceNullToEmpty(itemData.getTeacher_info().getNickname()));
        tvTheme.setText(StringUtils.replaceNullToEmpty(itemData.getTeacher_info().getSlogan()));
//        tv_live_time.setText(StringUtils.replaceNullToEmpty(itemData.getStart_date()));
        tvLiveState.setText(StringUtils.replaceNullToEmpty(itemData.getLive_msg()));
        switch (itemData.getLive_status()) {
            case Constans.LIVE_END://直播结束
                tvLiveState.setBackgroundColor(context.getResources().getColor(R.color.colorGrey900));
                break;

            case Constans.LIVE_ING://直播中
                tvLiveState.setBackgroundColor(context.getResources().getColor(R.color.colorRed500));
                break;
            case Constans.LiVE_UN_START://直播未开始
                tvLiveState.setBackgroundColor(context.getResources().getColor(R.color.colorGrey450));
                break;
            default:
                tvLiveState.setBackgroundColor(context.getResources().getColor(R.color.colorGrey900));
                break;
        }
        tvLiveDescription.setText(StringUtils.replaceNullToEmpty(itemData.getTitle()));
        tvNumber.setText(String.format(Locale.CHINA, "%s人", StringUtils.getDecimal(itemData.getAppointment_count(), Constant.TEN_THOUSAND, "万", "")));
        tvState.setText("观看过");

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idelectItem != null) {
                    idelectItem.delete(swipeLayout, position, itemData);
                } else {
                    LogUtils.e("EnshrineAdapter", "删除接口不能为null");
                }
            }
        });
    }

    @Override
    public int getCount() {
        return listCollect != null ? listCollect.size() : 0;
    }

    @Override
    public LiveInfo getItem(int position) {
        return listCollect != null ? listCollect.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface IdeletItem {
        public void delete(SwipeLayout swipeLayout, int position, LiveInfo newsItem);
    }
}
