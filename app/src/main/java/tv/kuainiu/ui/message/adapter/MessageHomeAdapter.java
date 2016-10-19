package tv.kuainiu.ui.message.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tv.kuainiu.R;
import tv.kuainiu.modle.MessageHome;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.StringUtils;

/**
 * Created by guxuan on 2016/3/7.
 */
public class MessageHomeAdapter extends BaseAdapter {
    private Context context;
    private List<MessageHome> messageListList;

    public MessageHomeAdapter(Context context, List<MessageHome> messageListList) {
        this.context = context;
        this.messageListList = messageListList;
    }

    @Override
    public int getCount() {
        return messageListList == null ? 0 : messageListList.size();
    }

    @Override
    public MessageHome getItem(int position) {
        return messageListList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_home, parent, false);
            viewHolder.textTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.textContent = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.iv_program_image = (ImageView) convertView.findViewById(R.id.iv_program_image);
            viewHolder.tv_message_count = (TextView) convertView.findViewById(R.id.tv_message_count);
            viewHolder.textDateTime = (TextView) convertView.findViewById(R.id.tv_dateTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MessageHome sysTemMessageList = getItem(position);
        viewHolder.textTitle.setText(StringUtils.replaceNullToEmpty(sysTemMessageList.getTitle()));
        if (TextUtils.isEmpty(StringUtils.replaceNullToEmpty(sysTemMessageList.getDatetime()))) {
            viewHolder.textDateTime.setText(StringUtils.replaceNullToEmpty(sysTemMessageList.getDatetime()));
        } else {
            try {
                long da = Long.parseLong(sysTemMessageList.getDatetime());
                viewHolder.textDateTime.setText(DateUtil.getDurationString(DateUtil.toJava(da)));
            } catch (Exception e) {
                viewHolder.textDateTime.setText(StringUtils.replaceNullToEmpty(sysTemMessageList.getDatetime()));
            }

        }
        viewHolder.textContent.setText(StringUtils.replaceNullToEmpty(sysTemMessageList.getNew_message()));
        ImageDisplayUtil.displayImage(context, viewHolder.iv_program_image, sysTemMessageList.getThumb());
        if (sysTemMessageList.getNew_count() > 0) {
            viewHolder.tv_message_count.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_message_count.setVisibility(View.INVISIBLE);
        }
        viewHolder.tv_message_count.setText(String.valueOf(sysTemMessageList.getNew_count()));
        return convertView;
    }

    public static class ViewHolder {
        private ImageView iv_program_image;
        private TextView textTitle;
        private TextView textContent;
        public TextView tv_message_count;
        private TextView textDateTime;
    }

}
