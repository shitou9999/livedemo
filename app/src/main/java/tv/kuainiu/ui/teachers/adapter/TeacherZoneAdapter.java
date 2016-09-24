package tv.kuainiu.ui.teachers.adapter;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;
import tv.kuainiu.modle.TeacherInfo;
import tv.kuainiu.modle.TeacherZoneDynamics;
import tv.kuainiu.modle.TeacherZoneDynamicsInfo;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.modle.push.CustomVideo;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.util.ImageDisplayUtil;
import tv.kuainiu.util.ImageDisplayUtils;
import tv.kuainiu.util.StringUtils;
import tv.kuainiu.widget.PostParentLayout;

/**
 * @author nanck on 2016/7/29.
 */
public class TeacherZoneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private BaseActivity mContext;
    private List<TabLayout.Tab> listTab;
    private List<TeacherZoneDynamics> teacherZoneDynamicsList;
    private List<CustomVideo> customVideoList;
    private TabLayout.OnTabSelectedListener tabSelectedListener;
    private static final int TOP = 0;
    private static final int TAB = 1;
    private static final int BODY = 2;
    public static final int SIZE = 2;
    public static final int CUSTOM_VIEW_POINT = 0;
    public static final int CUSTOM_VIDEO = 1;
    private int selectedIndex = 0;
    private TeacherInfo teacherInfo;


    public TeacherZoneAdapter(BaseActivity context) {
        mContext = context;
    }

    private String[] tabNames;

    public void setTabNames(String[] tabNames, TabLayout.OnTabSelectedListener tabSelectedListener) {
        this.tabNames = tabNames;
        this.tabSelectedListener = tabSelectedListener;
    }

    public void setTeacherInfo(TeacherInfo teacherInfo) {
        this.teacherInfo = teacherInfo;
    }


    public void setTeacherZoneDynamicsList(List<TeacherZoneDynamics> teacherZoneDynamicsList) {
        this.teacherZoneDynamicsList = teacherZoneDynamicsList;
    }
    public void setTeacherZoneJiePanList(List<CustomVideo> customVideoList) {
        this.customVideoList = customVideoList;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        if (listTab != null && listTab.size() > selectedIndex) {
            listTab.get(selectedIndex).select();
        }
    }

    @Override
    public int getItemCount() {
        int count=0;
        switch (selectedIndex){
            case CUSTOM_VIEW_POINT:
                count = teacherZoneDynamicsList == null ? 0 : teacherZoneDynamicsList.size();
                break;
            case CUSTOM_VIDEO:
                count = customVideoList == null ? 0 : customVideoList.size();
                break;
        }
        return SIZE + count;
    }


    @Override public int getItemViewType(int position) {
        int type = 0;
        switch (position) {
            case 0:
                type = TOP;
                break;
            case 1:
                type = TAB;
                break;
            default:
                type = BODY;
                break;
        }

        return type;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TOP:
                //绑定Top
                onBindTopViewHolder((TopViewHolder) holder);

                break;
            case TAB:
                onBindTabViewHolder((TabViewHolder) holder);
                break;
            case BODY:
                //绑定Body
                onBindBodyViewHolder((BodyViewHolder) holder, position);
                break;
        }
    }

    private void onBindTabViewHolder(TabViewHolder holder) {
        if (listTab == null) {
            listTab = new ArrayList<>();
            holder.mTabFragmentMajor.removeAllTabs();
            for (int i = 0; i < tabNames.length; i++) {
                TabLayout.Tab tab = holder.mTabFragmentMajor.newTab();
                tab.setText(tabNames[i]);
                listTab.add(tab);
//            tab.setTag(tabNamesTags[i]);
                holder.mTabFragmentMajor.addTab(tab);
            }
            holder.mTabFragmentMajor.addOnTabSelectedListener(tabSelectedListener);
            holder.mTabFragmentMajor.setTabTextColors(Color.parseColor("#757575"), Color.parseColor(Theme.getCommonColor()));
            holder.mTabFragmentMajor.setSelectedTabIndicatorColor(Color.parseColor(Theme.getCommonColor()));
        }/* else {
            listTab.get(selectedIndex).select();
        }*/
    }


    private void onBindTopViewHolder(TopViewHolder holder) {
        if (teacherInfo == null) {
            return;
        }
        ImageDisplayUtil.displayImage(mContext, holder.mCiAvatar, StringUtils.replaceNullToEmpty(teacherInfo.getAvatar()),R.mipmap.default_avatar);
        ImageDisplayUtil.displayImage(mContext, holder.ivBanner, StringUtils.replaceNullToEmpty(teacherInfo.getBanner()));
        holder.mTvTheme.setText(StringUtils.replaceNullToEmpty(teacherInfo.getSlogan()));
        holder.mTvTeacherName.setText(StringUtils.replaceNullToEmpty(teacherInfo.getNickname()));
        holder.mTvFollowNumber.setText(String.format(Locale.CHINA, "%s人关注", StringUtils.getDecimal(teacherInfo.getFans_count(), Constant.TEN_THOUSAND, "万", "")));
        if (teacherInfo.getIs_follow() == 0) {
            holder.mTvFollowButton.setText("+关注");
            holder.mTvFollowButton.setSelected(teacherInfo.getIs_follow() != 0);
        } else {
            holder.mTvFollowButton.setText("已关注");
        }
    }

    private List<BaseFragment> mBaseFragments = new ArrayList<>();

    private void onBindBodyViewHolder(BodyViewHolder holder, int position) {
        switch (selectedIndex) {
            case CUSTOM_VIEW_POINT:
                dataViewPoint(holder, position);
                break;
            case CUSTOM_VIDEO:
                dataVideo(holder, position);
                break;
        }

    }

    private void dataViewPoint(BodyViewHolder holder, int position) {
        TeacherZoneDynamics teacherZoneDynamics = teacherZoneDynamicsList.get(position - SIZE);
        ImageDisplayUtils.display(mContext, StringUtils.replaceNullToEmpty(teacherZoneDynamics.getAvatar()), holder.mCivFriendsPostHead, R.mipmap.default_avatar);
        holder.mTvFriendsPostNickname.setText(StringUtils.replaceNullToEmpty(teacherZoneDynamics.getNickname()));
        holder.mTvFriendsPostContent.setText(StringUtils.replaceNullToEmpty(teacherZoneDynamics.getDescription()));

        String ct = mContext.getString(R.string.value_comment_count, StringUtils.replaceNullToEmpty(teacherZoneDynamics.getComment_num(), "0"));
        holder.mTvFriendsPostComment.setText(ct);

        String lt = mContext.getString(R.string.value_comment_like, StringUtils.replaceNullToEmpty(teacherZoneDynamics.getSupport_num(), "0"));
        holder.mTvFriendsPostLike.setText(lt);
        holder.mPostParentLayout.setPostType(teacherZoneDynamics.getNews_info());
        switch (teacherZoneDynamics.getType()) {
            case 1:
//                ImageDisplayUtils.display(mContext, R.drawable.temp1, holder.mIvFriendsTemp);
                holder.mViewFriendsPostLine.setBackgroundColor(Color.RED);
                holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time1);
                break;

            case 2:
//                ImageDisplayUtils.display(mContext, R.drawable.temp2, holder.mIvFriendsTemp);
                holder.mViewFriendsPostLine.setBackgroundColor(Color.BLACK);
                holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time2);
                break;

            default:
                holder.mViewFriendsPostLine.setBackgroundColor(Color.BLACK);
                holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time2);
                break;
        }


//        Log.d("ssfsdfsdfsdfsd", "height : " + holder.itemView.getHeight());
//        int height = holder.itemView.getHeight();
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(2, height);
//        holder.mViewFriendsPostLine.setLayoutParams(lp);
//        holder.mViewFriendsPostLineBottom.setMinimumHeight(height);


        // Hide bottom live
        int v = position == teacherZoneDynamicsList.size() - 1 ? View.GONE : View.VISIBLE;
        holder.mViewFriendsPostLineBottom.setVisibility(v);
    }
    private void dataVideo(BodyViewHolder holder, int position) {
        CustomVideo teacherZoneDynamics = customVideoList.get(position-SIZE);
        ImageDisplayUtils.display(mContext, StringUtils.replaceNullToEmpty(teacherZoneDynamics.getAvatar()), holder.mCivFriendsPostHead, R.mipmap.default_avatar);
        holder.mTvFriendsPostNickname.setText(StringUtils.replaceNullToEmpty(teacherZoneDynamics.getNickname()));
        holder.mTvFriendsPostContent.setText(StringUtils.replaceNullToEmpty(teacherZoneDynamics.getDescription()));

        String ct = mContext.getString(R.string.value_comment_count, StringUtils.replaceNullToEmpty(teacherZoneDynamics.getComment_num(), "0"));
        holder.mTvFriendsPostComment.setText(ct);

        String lt = mContext.getString(R.string.value_comment_like, StringUtils.replaceNullToEmpty(teacherZoneDynamics.getSupport_num(), "0"));
        holder.mTvFriendsPostLike.setText(lt);
        TeacherZoneDynamicsInfo news_info=new TeacherZoneDynamicsInfo();
        news_info.setType(teacherZoneDynamics.getType());
        news_info.setNews_video_id(teacherZoneDynamics.getId());
        news_info.setNews_title(teacherZoneDynamics.getDescription());
        news_info.setNews_catid(teacherZoneDynamics.getCat_id());
        news_info.setNews_inputtime(teacherZoneDynamics.getInputtime());
        news_info.setVideo_id(teacherZoneDynamics.getVideo_id());
        holder.mPostParentLayout.setPostType(news_info);
        switch (Integer.parseInt(teacherZoneDynamics.getType())) {
            case 1:
//                ImageDisplayUtils.display(mContext, R.drawable.temp1, holder.mIvFriendsTemp);
                holder.mViewFriendsPostLine.setBackgroundColor(Color.RED);
                holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time1);
                break;

            case 2:
//                ImageDisplayUtils.display(mContext, R.drawable.temp2, holder.mIvFriendsTemp);
                holder.mViewFriendsPostLine.setBackgroundColor(Color.BLACK);
                holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time2);
                break;

            default:
                holder.mViewFriendsPostLine.setBackgroundColor(Color.BLACK);
                holder.mTvFriendsPostTime.setBackgroundResource(R.drawable.bg_friends_time2);
                break;
        }


//        Log.d("ssfsdfsdfsdfsd", "height : " + holder.itemView.getHeight());
//        int height = holder.itemView.getHeight();
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(2, height);
//        holder.mViewFriendsPostLine.setLayoutParams(lp);
//        holder.mViewFriendsPostLineBottom.setMinimumHeight(height);


        // Hide bottom live
        int v = position == customVideoList.size() - 1 ? View.GONE : View.VISIBLE;
        holder.mViewFriendsPostLineBottom.setVisibility(v);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder vh = null;
        switch (viewType) {
            case TOP:
                view = LayoutInflater.from(mContext).inflate(R.layout.activity_teacher_zone_top, parent, false);
                vh = new TopViewHolder(view);
                break;
            case TAB:
                view = LayoutInflater.from(mContext).inflate(R.layout.activity_teacher_zone_tab, parent, false);
                vh = new TabViewHolder(view);
                break;
            case BODY:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_friends_post, parent, false);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                int margin = mContext.getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
                layoutParams.setMargins(margin, 0, margin, 0);
                view.setLayoutParams(layoutParams);
                vh = new BodyViewHolder(view);
                break;
        }
        return vh;
    }

    //banner ViewPager
    static class TopViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivBanner) ImageView ivBanner;
        @BindView(R.id.ci_avatar) CircleImageView mCiAvatar;
        @BindView(R.id.rl_avatar) RelativeLayout mRlAvatar;
        @BindView(R.id.tvTheme) TextView mTvTheme;
        @BindView(R.id.tvTeacherName) TextView mTvTeacherName;
        @BindView(R.id.tv_follow_button) TextView mTvFollowButton;
        @BindView(R.id.tv_follow_number) TextView mTvFollowNumber;
        @BindView(R.id.ll_fragment_friends_main_news_info) LinearLayout mLlFragmentFriendsMainNewsInfo;

        public TopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    } //banner ViewPager

    static class TabViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btnPublish) Button mBtnPublish;
        @BindView(R.id.tab_fragment_major) TabLayout mTabFragmentMajor;

        public TabViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //banner ViewPager
    static class BodyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_friends_post_time) TextView mTvFriendsPostTime;
        @BindView(R.id.view_friends_post_line) View mViewFriendsPostLine;
        @BindView(R.id.civ_friends_post_head) CircleImageView mCivFriendsPostHead;
        //        @BindView(R.id.iv_friends_post_temp) ImageView mIvFriendsTemp;
        @BindView(R.id.pl_friends_post_group) PostParentLayout mPostParentLayout;
        @BindView(R.id.tv_friends_post_nickname) TextView mTvFriendsPostNickname;
        @BindView(R.id.tv_friends_post_content) TextView mTvFriendsPostContent;
        @BindView(R.id.tv_friends_post_type) TextView mTvFriendsPostType;
        @BindView(R.id.tv_friends_post_comment) TextView mTvFriendsPostComment;
        @BindView(R.id.tv_friends_post_like) TextView mTvFriendsPostLike;
        @BindView(R.id.view_friends_post_line_bottom) View mViewFriendsPostLineBottom;

        public BodyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
