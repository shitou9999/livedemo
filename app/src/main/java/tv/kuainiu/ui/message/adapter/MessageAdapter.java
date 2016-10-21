package tv.kuainiu.ui.message.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.List;

import tv.kuainiu.R;
import tv.kuainiu.app.ISwipeDeleteItemClickListening;
import tv.kuainiu.modle.SystemMessage;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.TextColorUtil;

/**
 * Created by guxuan on 2016/3/7.
 */
public class MessageAdapter extends BaseSwipeAdapter {
    private Context context;
    private List<SystemMessage> messageListList;
    private ISwipeDeleteItemClickListening mISwipeDeleteItemClickListening;

    public MessageAdapter(Context context, List<SystemMessage> messageListList, ISwipeDeleteItemClickListening mISwipeDeleteItemClickListening) {
        this.context = context;
        this.messageListList = messageListList;
        this.mISwipeDeleteItemClickListening = mISwipeDeleteItemClickListening;
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
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_message, null);
        return convertView;
    }

    @Override
    public void fillValues(final int position, View convertView) {

        final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.textTitle = (TextView) convertView.findViewById(R.id.tv_title);
        viewHolder.textDateTime = (TextView) convertView.findViewById(R.id.tv_dateTime);
        viewHolder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
        final SystemMessage sysTemMessageList = getItem(position);
        viewHolder.textTitle.setText(StringUtils.replaceNullToEmpty(sysTemMessageList.getTitle()));
        viewHolder.textDateTime.setText(StringUtils.replaceNullToEmpty(sysTemMessageList.getDatetime()));
        TextColorUtil.setIsRead(context, viewHolder.textTitle, sysTemMessageList.getIs_read() != 0);
        TextColorUtil.setIsRead(context, viewHolder.textDateTime, sysTemMessageList.getIs_read() != 0);
        viewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mISwipeDeleteItemClickListening != null) {
                    mISwipeDeleteItemClickListening.delete(swipeLayout, position, sysTemMessageList);
                }
            }
        });
    }


    static class ViewHolder {
        private TextView textTitle;
        private TextView textDateTime;
        private TextView tv_delete;
    }

}
