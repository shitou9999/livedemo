package tv.kuainiu.ui.publishing.dynamic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.List;

import tv.kuainiu.R;
import tv.kuainiu.modle.TeacherZoneDynamicsInfo;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.widget.PostParentLayout;

/**
 * Created by jack on 2016/4/28.
 * 收藏
 */
public class PublicDynamicAdapter extends BaseSwipeAdapter {
    private Context context;
    private List<TeacherZoneDynamicsInfo> listTeacherZoneDynamicsInfo;
    private IDeleteItemClickListener iDeleteItemClickListener;
    private boolean isEdit = false;

    public PublicDynamicAdapter(Context context, List<TeacherZoneDynamicsInfo> listTeacherZoneDynamicsInfo) {
        this.context = context;
        this.listTeacherZoneDynamicsInfo = listTeacherZoneDynamicsInfo;
    }

    public void setIDeleteItemClickListener(IDeleteItemClickListener iDeleteItemClickListener) {
        this.iDeleteItemClickListener = iDeleteItemClickListener;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }


    @Override
    public View generateView(int position, ViewGroup parent) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_publish_dynamic, null);
        return convertView;
    }

    @Override
    public void fillValues(final int position, final View convertView) {
        PostParentLayout pplPublishDynamic = (PostParentLayout) convertView.findViewById(R.id.pplPublishDynamic);
        TextView tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
        final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        final TeacherZoneDynamicsInfo newsItem = getItem(position);
        pplPublishDynamic.setPostType(newsItem);
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iDeleteItemClickListener != null) {
                    iDeleteItemClickListener.delete(swipeLayout, position, newsItem);
                } else {
                    LogUtils.e("EnshrineAdapter", "删除接口不能为null");
                }
            }
        });
    }

    @Override
    public int getCount() {
        return listTeacherZoneDynamicsInfo != null ? listTeacherZoneDynamicsInfo.size() : 0;
    }

    @Override
    public TeacherZoneDynamicsInfo getItem(int position) {
        return listTeacherZoneDynamicsInfo != null ? listTeacherZoneDynamicsInfo.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface IDeleteItemClickListener {
        public void delete(SwipeLayout swipeLayout, int position, TeacherZoneDynamicsInfo newsItem);

    }

}
