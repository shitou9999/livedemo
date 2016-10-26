package tv.kuainiu.ui.liveold.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bokecc.sdk.mobile.live.pojo.ChatMessage;
import com.bokecc.sdk.mobile.live.pojo.Viewer;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.R;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.utils.FaceStringUtil;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.StringUtils;

/**
 * Created by jack on 2016/7/11.
 */
public class MyChatListViewAdapter extends BaseAdapter {

    private Context context;
    private List<ChatMessage> chatMsgs;
    private String viewerId;
    private String viewerName;
    private final int OTHER = 0;
    private final int MY_SELF = 1;


    public MyChatListViewAdapter(Context context, Viewer viewer, List<ChatMessage> chatMsgs) {
        this.context = context;
        this.chatMsgs = chatMsgs;
        this.viewerId = viewer.getId();
        this.viewerName = viewer.getName();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public int getCount() {
        return chatMsgs.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMsgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (viewerId.equals(chatMsgs.get(position).getUserId())) {
            return MY_SELF;
        } else {
            return OTHER;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == OTHER) {
            ViewHolderOther viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolderOther();
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.lv_chat_left_view, parent, false);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_chat);
                viewHolder.tv_chat_nickname = (TextView) convertView.findViewById(R.id.tv_chat_nickname);
                viewHolder.time = (TextView) convertView.findViewById(R.id.tv_chat_time);
                viewHolder.tv_chat_time2 = (TextView) convertView.findViewById(R.id.tv_chat_time2);
                viewHolder.avatar = (CircleImageView) convertView.findViewById(R.id.civ_avatar);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolderOther) convertView.getTag();
            }

            ChatMessage chatMessage = chatMsgs.get(position);
            String userId = chatMessage.getUserId();
            String text = null;
            if (chatMessage.isPublic()) {
                viewHolder.tv_chat_nickname.setText(chatMessage.getUserName() + ":");
                text = chatMessage.getMessage();
            } else {
                viewHolder.tv_chat_nickname.setText(chatMessage.getUserName() + "对我说：");
                text = chatMessage.getMessage();
            }
            viewHolder.tv_chat_time2.setText(String.format(Locale.CHINA, "(%s)", chatMessage.getTime()));
            viewHolder.tv.setText(FaceStringUtil.parseFaceMsg(context, text));
            ImageDisplayUtil.displayImage(context, viewHolder.avatar, StringUtils.replaceNullToEmpty(chatMessage.getAvatar(), Constant.DEFAULT_AVATAR), R.mipmap.default_avatar);

        } else {
            ViewHolderMySelf viewHolderMySelf = null;
            if (convertView == null) {
                viewHolderMySelf = new ViewHolderMySelf();
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.lv_chat_right_view, parent, false);
                viewHolderMySelf.tv = (TextView) convertView.findViewById(R.id.tv_chat);
                viewHolderMySelf.tv_chat_time = (TextView) convertView.findViewById(R.id.tv_chat_time);
                viewHolderMySelf.avatar = (CircleImageView) convertView.findViewById(R.id.civ_avatar);
                convertView.setTag(viewHolderMySelf);
            } else {
                viewHolderMySelf = (ViewHolderMySelf) convertView.getTag();
            }

            ChatMessage chatMessage = chatMsgs.get(position);
            String userId = chatMessage.getUserId();
            String text = null;
            if (chatMessage.isPublic()) {
                text = chatMessage.getMessage();
            } else {
                text = chatMessage.getMessage();
            }
            viewHolderMySelf.tv.setText(FaceStringUtil.parseFaceMsg(context, text));
            ImageDisplayUtil.displayImage(context, viewHolderMySelf.avatar, StringUtils.replaceNullToEmpty(chatMessage.getAvatar(), Constant.DEFAULT_AVATAR), R.mipmap.default_avatar);
        }
        return convertView;
    }

    static class ViewHolderOther {
        public CircleImageView avatar;
        public TextView tv;
        public TextView tv_chat_nickname;
        public TextView tv_chat_time;
        public TextView tv_chat_time2;
        public TextView time;
    }

    static class ViewHolderMySelf {
        public CircleImageView avatar;
        public TextView tv;
        public TextView tv_chat_time;
        public TextView time;
    }
}
