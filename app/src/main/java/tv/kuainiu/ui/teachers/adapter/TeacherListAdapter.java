package tv.kuainiu.ui.teachers.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.modle.ProgramItem;
import tv.kuainiu.modle.TeacherItem;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.ScreenUtils;
import tv.kuainiu.utils.StringUtils;


public class TeacherListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static final String STRING_SUBSCRIPTION_NEW = "+ 订阅";
    private static final String STRING_SUBSCRIPTION_ED = "已订阅";

    public static final int TAG_COUNT = 2;
    public static final int ITEM_TYPE_TAG_PRO = 0x1;
    public static final int ITEM_TYPE_TAG_TEA = 0x2;
    public static final int ITEM_TYPE_PROGRAM = 0x3;
    public static final int ITEM_TYPE_TEACHER = 0x4;

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private List<ProgramItem> mProgramList = new ArrayList<>();
    private List<TeacherItem> mTeacherList = new ArrayList<>();
    private OnClickListener mOnClickListener;

    private boolean isLiveChild = false;


    // Tag view
//    static class TAGViewHolder extends RecyclerView.ViewHolder {
//        public TAGViewHolder(View itemView) {
//            super(itemView);
//        }
//    }


    // Program view item
   /* public static class ProgramViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_program_photo) ImageView imagePhoto;
        @BindView(R.id.tv_coulmn_name) TextView textTitle;
        @BindView(R.id.tv_subscribe_count) TextView textSubscribeCount;
        @BindView(R.id.tv_subscribe) TextView textSubscribe;

        public ProgramViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }*/

    // Teacher view item
    static class TeacherViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_teacher_item_photo)
        ImageView imgPhoto;
        @BindView(R.id.tv_follow_button)
        TextView textFollow;
        @BindView(R.id.tv_name)
        TextView textName;
        @BindView(R.id.tv_fans_count)
        TextView textFansCount;
        @BindView(R.id.tvSubject)
        TextView tvSubject;
        @BindView(R.id.tvAnalysis)
        TextView tvAnalysis;
        @BindView(R.id.ll_root)
        RelativeLayout ll_root;

        public TeacherViewHolder(View itemView) {
            super(itemView);
//            setIsRecyclable(false);
            ButterKnife.bind(this, itemView);
        }
    }


//    private final FrameLayout.LayoutParams layoutParams1;
//    private final LinearLayout.LayoutParams layoutParams2;

    public TeacherListAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);

        int p = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        int scrWidth = ScreenUtils.getScreenWidth(context);

        int teacherImageWidth = (scrWidth - (p * 4)) / 3;
        int bottomViewHeight = (int) (teacherImageWidth / 0.618f - teacherImageWidth);

//        layoutParams1 = new FrameLayout.LayoutParams(teacherImageWidth, teacherImageWidth);
//        layoutParams2 = new LinearLayout.LayoutParams(teacherImageWidth, bottomViewHeight);
    }

//    public void setLiveChild(boolean liveChild) {
//        isLiveChild = liveChild;
//    }
//
//    public boolean isLiveChild() {
//        return isLiveChild;
//    }
//
//    public void setProgramList(List<ProgramItem> programList) {
//        mProgramList = programList;
//    }

    public void setTeacherList(List<TeacherItem> teacherList) {
        mTeacherList = teacherList;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
//        if (isLiveChild) {
//            return mTeacherList == null ? 0 : mTeacherList.size();
//        } else {
//            return TAG_COUNT + mProgramList.size() + mTeacherList.size();
//        }
        return mTeacherList == null ? 0 : mTeacherList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (ITEM_TYPE_PROGRAM == getItemViewType(position) && holder instanceof ProgramViewHolder) {
//            bindPrograms(((ProgramViewHolder) holder), position - 1);
//        }
//        if (ITEM_TYPE_TEACHER == getItemViewType(position) && holder instanceof TeacherViewHolder) {
//            if (isLiveChild) {
//                bindTeachers(((TeacherViewHolder) holder), position);
//            } else {
//                bindTeachers(((TeacherViewHolder) holder), position - (TAG_COUNT + mProgramList.size()));
//            }
//        }
        bindTeachers(((TeacherViewHolder) holder), position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create ViewHolder tag program
//        if (ITEM_TYPE_TAG_PRO == viewType) {
//            return new TAGViewHolder(mLayoutInflater.inflate(R.layout.teacher_item_tag_p, parent, false));
//        }
//        // Create ViewHolder tag teacher
//        else if (ITEM_TYPE_TAG_TEA == viewType) {
//            return new TAGViewHolder(mLayoutInflater.inflate(R.layout.teacher_item_tag_t, parent, false));
//        }
//        // Create ViewHolder program
//        else if (ITEM_TYPE_PROGRAM == viewType) {
//            View view = mLayoutInflater.inflate(R.layout.item_simple_column_group, parent, false);
//            ProgramViewHolder vh = new ProgramViewHolder(view);
//            vh.imagePhoto.setOnClickListener(this);
//            vh.textSubscribe.setOnClickListener(this);
//            return vh;
//        }
//        // Create ViewHolder Teacher
//        else {
//            View view = mLayoutInflater.inflate(R.layout.teacher_item_t, parent, false);
//            TeacherViewHolder vh = new TeacherViewHolder(view);
//            vh.imgPhoto.setOnClickListener(this);
//            vh.textFollow.setOnClickListener(this);
//
//            vh.imgPhoto.setLayoutParams(layoutParams1);
//            vh.rlBottom.setLayoutParams(layoutParams2);
//            return vh;
//        }
        View view = mLayoutInflater.inflate(R.layout.teacher_item_t, parent, false);
        TeacherViewHolder vh = new TeacherViewHolder(view);

//            vh.imgPhoto.setLayoutParams(layoutParams1);
//            vh.rlBottom.setLayoutParams(layoutParams2);
        return vh;
    }


    @Override
    public int getItemViewType(int position) {
//        if (isLiveChild) {
//            return ITEM_TYPE_TEACHER;
//        } else {
//            if (position == 0) {
//                return ITEM_TYPE_TAG_PRO;
//            } else if (position >= 1 && position <= mProgramList.size()) {
//                return ITEM_TYPE_PROGRAM;
//            } else if (position == 1 + mProgramList.size()) {
//                return ITEM_TYPE_TAG_TEA;
//            } else {
//                return ITEM_TYPE_TEACHER;
//            }
//        }
        return ITEM_TYPE_TEACHER;
    }


//    private void bindPrograms(ProgramViewHolder holder, int position) {
//        if (null == mProgramList || mTeacherList.size() < 1) {
//            return;
//        }
//        ProgramItem item = mProgramList.get(position);
//        holder.imagePhoto.setTag(R.id.image_tag, item);
//        holder.textSubscribe.setTag(R.id.tag_first, item);
//        holder.textSubscribe.setTag(R.id.tag_second, holder.textSubscribeCount);
//        ImageDisplayUtil.displayImage(mContext, holder.imagePhoto, item.img);
//        holder.textTitle.setText(item.catname);
//        resetSubCheckBox(holder.textSubscribe, holder.textSubscribeCount, item);
//    }


  /*  public void resetSubCheckBox(TextView textSubscribe, TextView textSubscribeCount, ProgramItem item) {
        if (textSubscribe == null || textSubscribeCount == null || item == null) {
            return;
        }
        textSubscribeCount.setText(StringUtils.getDecimal(item.subscibe_count, Constant.TEN_THOUSAND, "万人订阅", "人订阅"));
        if (Constant.SUBSCRIBEED == item.is_subscibe && MyApplication.isLogin()) {
            textSubscribe.setText(STRING_SUBSCRIPTION_ED);
            textSubscribe.setTextColor(Color.parseColor("#9E9E9E"));
            textSubscribe.setBackgroundResource(R.drawable.bg_button_subscribeed);
        } else {
            textSubscribe.setText(STRING_SUBSCRIPTION_NEW);
            textSubscribe.setTextColor(Color.WHITE);
            textSubscribe.setBackgroundResource(R.drawable.bg_button_unsubscribe);
        }
    }*/


    private void bindTeachers(TeacherViewHolder holder, int position) {
        if (null == mTeacherList || mTeacherList.size() < 1 && mTeacherList.size() <= position) {
            return;
        }
        TeacherItem item = mTeacherList.get(position);
        // 传递数据
        holder.imgPhoto.setTag(R.id.iv_teacher_item_photo, item);
        holder.textFollow.setTag(R.id.iv_teacher_item_photo, item);
        holder.textFollow.setTag(R.id.tv_fans_count, holder.textFansCount);
        holder.ll_root.setTag(R.id.ll_root, item);

        holder.textName.setText(item.nickname);
        ImageDisplayUtil.displayImage(mContext, holder.imgPhoto, item.avatar, R.mipmap.default_avatar);
        holder.tvAnalysis.setText(StringUtils.replaceNullToEmpty(item.listorder));
        holder.tvSubject.setText(StringUtils.replaceNullToEmpty(item.slogan));
        resetFollowCheckBox(holder.textFollow, holder.textFansCount, item);
        if (item.nickname != null && !item.nickname.contains("助手")) {
            holder.textFollow.setOnClickListener(this);
            holder.ll_root.setOnClickListener(this);
            holder.textFollow.setVisibility(View.VISIBLE);
        }else{
            holder.textFollow.setVisibility(View.INVISIBLE);
        }

    }


    /**
     * 关注或取消关注后刷新 view
     *
     * @param v             关注 Button
     * @param textFansCount 粉丝数量 TextView
     * @param teacher       TeacherItem
     */
    public void resetFollowCheckBox(TextView v, TextView textFansCount, TeacherItem teacher) {
        if (v == null || textFansCount == null || teacher == null) {
            return;
        }
        textFansCount.setText(StringUtils.getDecimal(teacher.fans_count, Constant.TEN_THOUSAND, "万个粉丝", "个粉丝"));

        boolean bool = (Constant.FOLLOWED == teacher.is_follow);
        v.setSelected(bool);

        Resources res = mContext.getResources();
        String text = bool ? res.getString(R.string.followed) : res.getString(R.string.follow_new);
        v.setText(text);
    }


    @Override
    public void onClick(View v) {
        if (null != mOnClickListener) {
            mOnClickListener.onClick(v, v.getTag());
        }
    }


    public interface OnClickListener {
        void onClick(View v, Object o);
    }

}
