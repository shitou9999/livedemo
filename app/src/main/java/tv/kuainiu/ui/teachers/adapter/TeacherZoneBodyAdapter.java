package tv.kuainiu.ui.teachers.adapter;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.R;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.fragment.TabMajorFragment;
import tv.kuainiu.ui.friends.fragment.FriendsTabFragment;

/**
 * @author nanck on 2016/7/29.
 */
public class TeacherZoneBodyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private BaseActivity mContext;

    private static final int TOP = 0;
    private static final int BODY = 1;
    private static final String[] titles ={"观点", "解盘"};
    private List<BaseFragment> mBaseFragments = new ArrayList<>();
    public TeacherZoneBodyAdapter(BaseActivity context) {
        mContext = context;
        mBaseFragments.clear();
        mBaseFragments.add(FriendsTabFragment.newInstance());
        mBaseFragments.add(FriendsTabFragment.newInstance());
    }


    @Override
    public int getItemCount() {
        return 2;
    }


    @Override public int getItemViewType(int position) {
        int type = 0;
        switch (position) {
            case 0:
                type = TOP;
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
            case BODY:
                //绑定Body
                onBindBodyViewHolder((BodyViewHolder) holder);
                break;
        }
    }


    private void onBindTopViewHolder(TopViewHolder holder) {
    }

    private void onBindBodyViewHolder(BodyViewHolder holder) {
        holder.mNvpFragmentMajor.setAdapter(new TabMajorFragment.SimpleViewPager(mContext.getSupportFragmentManager(), mBaseFragments, titles));
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
            case BODY:
                view = LayoutInflater.from(mContext).inflate(R.layout.activity_teacher_zone_body, parent, false);
                vh = new BodyViewHolder(view);
                break;
        }
        return vh;
    }

    //banner ViewPager
    static class TopViewHolder extends RecyclerView.ViewHolder {
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
    }

    //banner ViewPager
    static class BodyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.nvp_fragment_major) ViewPager mNvpFragmentMajor;

        public BodyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
