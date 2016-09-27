package tv.kuainiu.ui.liveold.adapter;

public class SimpleAdapter{

}
/**
 * @author nanck on 2016/4/13.
 *//*

public class SimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
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
    static class TAGViewHolder extends RecyclerView.ViewHolder {
        public TAGViewHolder(View itemView) {
            super(itemView);
        }
    }


    // Program view item
    public static class ProgramViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_program_photo) ImageView imagePhoto;
        @BindView(R.id.tv_coulmn_name) TextView textTitle;
        @BindView(R.id.tv_subscribe_count) TextView textSubscribeCount;
        @BindView(R.id.tv_subscribe) TextView textSubscribe;

        public ProgramViewHolder(View itemView) {
            super(itemView);
            ButterKnife.BindView(this, itemView);
        }
    }

    // Teacher view item
    static class TeacherViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_teacher_item_photo) ImageView imgPhoto;
        @BindView(R.id.tv_follow_button) TextView textFollow;
        @BindView(R.id.tv_name) TextView textName;
        @BindView(R.id.tv_fans_count) TextView textFansCount;
        @BindView(R.id.rl_bottom) RelativeLayout rlBottom;

        public TeacherViewHolder(View itemView) {
            super(itemView);
            setIsRecyclable(false);
            ButterKnife.BindView(this, itemView);
        }
    }


    private final FrameLayout.LayoutParams layoutParams1;
    private final LinearLayout.LayoutParams layoutParams2;

    public SimpleAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);

        int p = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        int scrWidth = ScreenUtils.getScreenWidth(context);

        int teacherImageWidth = (scrWidth - (p * 4)) / 3;
        int bottomViewHeight = (int) (teacherImageWidth / 0.618f - teacherImageWidth);

        layoutParams1 = new FrameLayout.LayoutParams(teacherImageWidth, teacherImageWidth);
        layoutParams2 = new LinearLayout.LayoutParams(teacherImageWidth, bottomViewHeight);
    }

    public void setLiveChild(boolean liveChild) {
        isLiveChild = liveChild;
    }

    public boolean isLiveChild() {
        return isLiveChild;
    }

    public void setProgramList(List<ProgramItem> programList) {
        mProgramList = programList;
    }

    public void setTeacherList(List<TeacherItem> teacherList) {
        mTeacherList = teacherList;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override public int getItemCount() {
        if (isLiveChild) {
            return mTeacherList == null ? 0 : mTeacherList.size();
        } else {
            return TAG_COUNT + mProgramList.size() + mTeacherList.size();
        }
    }

    @Override
    public void onBindViewViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (ITEM_TYPE_PROGRAM == getItemViewType(position) && holder instanceof ProgramViewHolder) {
            BindViewPrograms(((ProgramViewHolder) holder), position - 1);
        }
        if (ITEM_TYPE_TEACHER == getItemViewType(position) && holder instanceof TeacherViewHolder) {
            if (isLiveChild) {
                BindViewTeachers(((TeacherViewHolder) holder), position);
            } else {
                BindViewTeachers(((TeacherViewHolder) holder), position - (TAG_COUNT + mProgramList.size()));
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create ViewHolder tag program
        if (ITEM_TYPE_TAG_PRO == viewType) {
            return new TAGViewHolder(mLayoutInflater.inflate(R.layout.teacher_item_tag_p, parent, false));
        }
        // Create ViewHolder tag teacher
        else if (ITEM_TYPE_TAG_TEA == viewType) {
            return new TAGViewHolder(mLayoutInflater.inflate(R.layout.teacher_item_tag_t, parent, false));
        }
        // Create ViewHolder program
        else if (ITEM_TYPE_PROGRAM == viewType) {
            View view = mLayoutInflater.inflate(R.layout.item_simple_column_group, parent, false);
            ProgramViewHolder vh = new ProgramViewHolder(view);
            vh.imagePhoto.setOnClickListener(this);
            vh.textSubscribe.setOnClickListener(this);
            return vh;
        }
        // Create ViewHolder Teacher
        else {
            View view = mLayoutInflater.inflate(R.layout.teacher_item_t, parent, false);
            TeacherViewHolder vh = new TeacherViewHolder(view);
            vh.imgPhoto.setOnClickListener(this);
            vh.textFollow.setOnClickListener(this);

            vh.imgPhoto.setLayoutParams(layoutParams1);
            vh.rlBottom.setLayoutParams(layoutParams2);
            return vh;
        }
    }


    @Override public int getItemViewType(int position) {
        if (isLiveChild) {
            return ITEM_TYPE_TEACHER;
        } else {
            if (position == 0) {
                return ITEM_TYPE_TAG_PRO;
            } else if (position >= 1 && position <= mProgramList.size()) {
                return ITEM_TYPE_PROGRAM;
            } else if (position == 1 + mProgramList.size()) {
                return ITEM_TYPE_TAG_TEA;
            } else {
                return ITEM_TYPE_TEACHER;
            }
        }
    }


    private void BindViewPrograms(ProgramViewHolder holder, int position) {
        if (null == mProgramList || mTeacherList.size() < 1) {
            return;
        }
        ProgramItem item = mProgramList.get(position);
        holder.imagePhoto.setTag(R.id.image_tag, item);
        holder.textSubscribe.setTag(R.id.tag_first, item);
        holder.textSubscribe.setTag(R.id.tag_second, holder.textSubscribeCount);
        ImageDisplayUtil.displayImage(mContext, holder.imagePhoto, item.img);
        holder.textTitle.setText(item.catname);
        resetSubCheckBox(holder.textSubscribe, holder.textSubscribeCount, item);
    }


    public void resetSubCheckBox(TextView textSubscribe, TextView textSubscribeCount, ProgramItem item) {
        if (textSubscribe == null || textSubscribeCount == null || item == null) {
            return;
        }
        textSubscribeCount.setText(StringUtils.getDecimal(item.subscibe_count, Constant.TEN_THOUSAND, "万人订阅", "人订阅"));
        if (Constant.SUBSCRIBEED == item.is_subscibe && IGXApplication.isLogin()) {
            textSubscribe.setText(STRING_SUBSCRIPTION_ED);
            textSubscribe.setTextColor(Color.parseColor("#9E9E9E"));
            textSubscribe.setBackgroundResource(R.drawable.bg_button_subscribeed);
        } else {
            textSubscribe.setText(STRING_SUBSCRIPTION_NEW);
            textSubscribe.setTextColor(Color.WHITE);
            textSubscribe.setBackgroundResource(R.drawable.bg_button_unsubscribe);
        }
    }


    private void BindViewTeachers(TeacherViewHolder holder, int position) {
        if (null == mTeacherList || mTeacherList.size() < 1) {
            return;
        }
        TeacherItem item = mTeacherList.get(position);
        // 传递数据
        holder.imgPhoto.setTag(R.id.image_tag, item);
        holder.textFollow.setTag(R.id.tag_first, item);
        holder.textFollow.setTag(R.id.tag_second, holder.textFansCount);

        holder.textName.setText(item.title);
        ImageDisplayUtil.displayImage(mContext, holder.imgPhoto, item.body_thumb, R.mipmap.ic_def_error);

        resetFollowCheckBox(holder.textFollow, holder.textFansCount, item);
    }


    */
/**
     * 关注或取消关注后刷新 view
     *
     * @param v             关注 Button
     * @param textFansCount 粉丝数量 TextView
     * @param teacher       TeacherItem
     *//*

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


    @Override public void onClick(View v) {
        if (null != mOnClickListener) {
            mOnClickListener.onClick(v, v.getTag());
        }
    }


    public interface OnClickListener {
        void onClick(View v, Object o);
    }

}
*/
