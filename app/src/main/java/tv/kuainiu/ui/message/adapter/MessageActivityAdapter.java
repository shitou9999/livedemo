package tv.kuainiu.ui.message.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tv.kuainiu.R;
import tv.kuainiu.modle.ActivityMessage;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.TextColorUtil;


/**
 * Created by guxuan on 2016/3/7.
 */
public class MessageActivityAdapter extends BaseAdapter {
    private Context context;
    private List<ActivityMessage> activityMessageList;
    private ICollectClick iCollectClick;

    public MessageActivityAdapter(Context context, List<ActivityMessage> activityMessageList, ICollectClick iCollectClick) {
        this.context = context;
        this.activityMessageList = activityMessageList;
        this.iCollectClick = iCollectClick;
    }

    @Override
    public int getCount() {
        return activityMessageList == null ? 0 : activityMessageList.size();
    }

    @Override
    public ActivityMessage getItem(int position) {
        return activityMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_activity, parent, false);
            viewHolder.textTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.textContent = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.iv_program_image = (ImageView) convertView.findViewById(R.id.iv_program_image);
            viewHolder.iv_collect = (ImageView) convertView.findViewById(R.id.iv_collect);
            viewHolder.textDateTime = (TextView) convertView.findViewById(R.id.tv_dateTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ActivityMessage activityMessage = getItem(position);
        viewHolder.textContent.setText(Html.fromHtml(StringUtils.replaceNullToEmpty(activityMessage.getContent())));
        viewHolder.textTitle.setText(Html.fromHtml(StringUtils.replaceNullToEmpty(activityMessage.getTitle())));
        viewHolder.textDateTime.setText(StringUtils.replaceNullToEmpty(activityMessage.getDatetime()));
        TextColorUtil.setIsRead(context, viewHolder.textTitle, activityMessage.getIs_read() != 0);
        TextColorUtil.setIsRead(context, viewHolder.textDateTime, activityMessage.getIs_read() != 0);
        if (activityMessage.getIs_collect() != 0) {
            viewHolder.iv_collect.setSelected(true);
        } else {
            viewHolder.iv_collect.setSelected(false);
        }
        viewHolder.iv_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityMessage activityMessage = getItem(position);
                if (getItem(position).getIs_collect() != 0) {
                    iCollectClick.deleteCollectEvent(position);
                } else {
                    iCollectClick.collectEvent(position);
                }
            }
        });
        ImageDisplayUtil.displayImage(context, viewHolder.iv_program_image, StringUtils.replaceNullToEmpty(activityMessage.getActivity_thumb()));
        return convertView;
    }

    static class ViewHolder {
        private ImageView iv_program_image;
        private ImageView iv_collect;
        private TextView textTitle;
        private TextView textContent;
        private TextView textDateTime;
    }

    public interface ICollectClick {
        void collectEvent(int position);

        void deleteCollectEvent(int position);
    }
}
