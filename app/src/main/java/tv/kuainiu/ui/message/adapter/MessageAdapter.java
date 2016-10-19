package tv.kuainiu.ui.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tv.kuainiu.R;
import tv.kuainiu.modle.SystemMessage;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.TextColorUtil;

/**
 * Created by guxuan on 2016/3/7.
 */
public class MessageAdapter extends BaseAdapter {
    private Context context;
    private List<SystemMessage> messageListList;

    public MessageAdapter(Context context, List<SystemMessage> messageListList) {
        this.context = context;
        this.messageListList = messageListList;
    }

    @Override
    public int getCount() {
        return messageListList == null ? 0 : messageListList.size();
    }

    @Override
    public SystemMessage getItem(int position) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
            viewHolder.textTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.textDateTime = (TextView) convertView.findViewById(R.id.tv_dateTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SystemMessage sysTemMessageList = getItem(position);
        viewHolder.textTitle.setText(StringUtils.replaceNullToEmpty(sysTemMessageList.getTitle()));
        viewHolder.textDateTime.setText(StringUtils.replaceNullToEmpty(sysTemMessageList.getDatetime()));
        TextColorUtil.setIsRead(context, viewHolder.textTitle, sysTemMessageList.getIs_read() != 0);
        TextColorUtil.setIsRead(context, viewHolder.textDateTime, sysTemMessageList.getIs_read() != 0);
        return convertView;
    }


//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
//        ViewHolder vh = new ViewHolder(view);
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.itemView.setTag(position);
//    }
//
//    @Override public int getItemCount() {
//        return 20;
//    }

    static class ViewHolder {
        private TextView textTitle;
        private TextView textDateTime;
    }

}
