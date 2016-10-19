package tv.kuainiu.ui.message.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.R;
import tv.kuainiu.modle.CommentMessage;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.StringUtils;

/**
 * Created by guxuan on 2016/3/7.
 */
public class MessageCommentAdapter extends BaseAdapter {
    private Context context;
    private List<CommentMessage> listCommentMessage;
    private IItemClick iItemClick;

    public MessageCommentAdapter(Context context, List<CommentMessage> messageListList, IItemClick iItemClick) {
        this.context = context;
        this.listCommentMessage = messageListList;
        this.iItemClick = iItemClick;
    }

    @Override
    public int getCount() {
        return listCommentMessage == null ? 0 : listCommentMessage.size();
    }

    @Override
    public CommentMessage getItem(int position) {
        return listCommentMessage.get(position);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_comment, parent, false);
            viewHolder.iv_avatar_bg = (ImageView) convertView.findViewById(R.id.iv_avatar_bg);
            viewHolder.iv_avatar = (CircleImageView) convertView.findViewById(R.id.iv_avatar);
            viewHolder.tv_is_new = (TextView) convertView.findViewById(R.id.tv_is_new);
            viewHolder.tv_comment_title = (TextView) convertView.findViewById(R.id.tv_comment_title);
            viewHolder.tv_comment_title2 = (TextView) convertView.findViewById(R.id.tv_comment_title2);
            viewHolder.tv_has_answer = (TextView) convertView.findViewById(R.id.tv_has_answer);
            viewHolder.tv_comment_datetime = (TextView) convertView.findViewById(R.id.tv_comment_datetime);
            viewHolder.tv_comment_content = (TextView) convertView.findViewById(R.id.tv_comment_content);
            viewHolder.tv_comment_content2 = (TextView) convertView.findViewById(R.id.tv_comment_content2);
            viewHolder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_device_source = (TextView) convertView.findViewById(R.id.tv_device_source);
            viewHolder.ll_comment_right = (LinearLayout) convertView.findViewById(R.id.ll_comment_right);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CommentMessage sysTemMessageList = getItem(position);
        ImageDisplayUtil.displayImage(context, viewHolder.iv_avatar, sysTemMessageList.getFrom_user_avatar());
        if (!"0".equals(sysTemMessageList.getReplyed())) {
            viewHolder.tv_has_answer.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_has_answer.setVisibility(View.GONE);
        }
        if (0 == sysTemMessageList.getFrom_user_is_teacher()) {
            viewHolder.iv_avatar_bg.setVisibility(View.GONE);
            viewHolder.tv_comment_title.setCompoundDrawables(null, null, null, null);
        } else {
            viewHolder.iv_avatar_bg.setVisibility(View.VISIBLE);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.ic_v);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
            viewHolder.tv_comment_title.setCompoundDrawables(null, null, drawable, null);
        }
        if (sysTemMessageList.getIs_new() == 1) {
            viewHolder.tv_is_new.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_is_new.setVisibility(View.GONE);
        }
        String title = sysTemMessageList.getTitle();
        title = title.replace(sysTemMessageList.getFrom_user_nickname(), "");
        viewHolder.tv_comment_title.setText(sysTemMessageList.getFrom_user_nickname());
        viewHolder.tv_comment_title2.setText(Html.fromHtml(title));
        viewHolder.tv_comment_datetime.setText(DateUtil.getDurationString(DateUtil.toJava(sysTemMessageList.getDatetime())));
        String content = StringUtils.replaceNullToEmpty(sysTemMessageList.getContent_1());
        if (content.startsWith("赞") && content.endsWith("赞")) {
            content = "❤";
            viewHolder.tv_comment_content.setText(content);
            viewHolder.tv_comment_content.setTextColor(context.getResources().getColor(R.color.colorRed500));
        } else {
            viewHolder.tv_comment_content.setText(StringUtils.replaceNullToEmpty(sysTemMessageList.getContent_1()));
            viewHolder.tv_comment_content.setTextColor(context.getResources().getColor(R.color.colorGrey900));
        }
        viewHolder.tv_comment_content2.setText(Html.fromHtml(StringUtils.replaceNullToEmpty(sysTemMessageList.getContent_2())));

        viewHolder.tv_type.setText(sysTemMessageList.getCatname());
        viewHolder.tv_title.setText(sysTemMessageList.getNews_title());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentMessage sysTemMessageList = getItem(position);
                String content = StringUtils.replaceNullToEmpty(sysTemMessageList.getContent_1());
                if (!content.startsWith("赞") && !content.endsWith("赞")) {
                    iItemClick.itemClick(position);
                }
            }
        });
        viewHolder.ll_comment_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iItemClick.itemButtomClick(position);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        private ImageView iv_avatar_bg;
        private CircleImageView iv_avatar;
        private TextView tv_comment_title;
        private TextView tv_comment_title2;
        private TextView tv_is_new;
        private TextView tv_has_answer;
        private TextView tv_comment_datetime;
        private TextView tv_comment_content;
        private TextView tv_comment_content2;
        private TextView tv_type;
        private TextView tv_device_source;
        private TextView tv_title;
        private LinearLayout ll_comment_right;
    }

    public interface IItemClick {
        void itemClick(int position);

        void itemButtomClick(int position);

    }
}
