package tv.kuainiu.ui.publishing.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import tv.kuainiu.R;
import tv.kuainiu.app.ISwipeDeleteItemClickListening;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.TeacherZoneDynamicsInfo;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.activity.SelectPictureActivity;
import tv.kuainiu.ui.adapter.UpLoadImageAdapter;
import tv.kuainiu.ui.publishing.pick.PickArticleActivity;
import tv.kuainiu.ui.publishing.pick.PickTagsActivity;
import tv.kuainiu.utils.FileUtils;
import tv.kuainiu.utils.LoadingProgressDialog;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.ExpandGridView;
import tv.kuainiu.widget.ExpandListView;
import tv.kuainiu.widget.TitleBarView;
import tv.kuainiu.widget.tagview.Tag;
import tv.kuainiu.widget.tagview.TagListView;
import tv.kuainiu.widget.tagview.TagView;

import static tv.kuainiu.ui.publishing.pick.PickTagsActivity.NEW_LIST;
import static tv.kuainiu.ui.publishing.pick.PickTagsActivity.SELECTED_LIST;

public class PublishDynamicActivity extends BaseActivity {

    public static final int REQUSET_ARTICLE_CODE = 1;
    public static final int REQUSET_TAG_CODE = 0;
    @BindView(R.id.tbv_title)
    TitleBarView tbvTitle;
    @BindView(R.id.tvInputWordLimit)
    TextView tvInputWordLimit;
    @BindView(R.id.btnFlag)
    TextView btnFlag;
    @BindView(R.id.tagListView)
    TagListView tagListView;
    @BindView(R.id.elv_friends_post_group)
    ExpandListView elv_friends_post_group;
    @BindView(R.id.exgv_appraisal_pic)
    ExpandGridView exgv_appraisal_pic;
    @BindView(R.id.rlRelatedArticles)
    RelativeLayout rlRelatedArticles;
    @BindView(R.id.et_content)
    EditText etContent;
    private List<Tag> mTags = new ArrayList<Tag>();
    private List<Tag> mNewTagList = new ArrayList<Tag>();
    private ArrayList<String> stList;// 用户上传图片的URL集合
    PublicDynamicAdapter mPublicDynamicAdapter;
    List<TeacherZoneDynamicsInfo> listTeacherZoneDynamicsInfo = new ArrayList<>();

    private String news_id = "";//可选     引用的文章ID
    private String description = "";//必传     文字内容
    private String thumb = "";//缩略图     多个以英文逗号隔开
    private String synchro_wb = "1";//是否同步微博     1是 0否
    private String tag = "";//
    private String tag_new = "";//
    private boolean isSubmiting = false;
    private UpLoadImageAdapter mUpLoadImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_dynamic);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();

        getImageList(null);// 占位符
        mUpLoadImageAdapter = new UpLoadImageAdapter(stList, this, 1);
        exgv_appraisal_pic.setAdapter(mUpLoadImageAdapter);
    }

    int j = 0;
    int i = 0;

    private void initView() {
        tagListView.setDeleteMode(true);
        tagListView.setOnTagClickListener(new TagListView.OnTagClickListener() {
            @Override
            public void onTagClick(TagView tagView, Tag tag) {
                mTags.remove(tag);
                tagListView.removeTag(tag);
            }
        });

        tbvTitle.setOnClickListening(new TitleBarView.OnClickListening() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                LoadingProgressDialog.startProgressDialog("正在发布", PublishDynamicActivity.this);
                if (!dataVerify()) {
                    LoadingProgressDialog.stopProgressDialog();
                    return;
                }

                j = stList == null ? 0 : stList.size();
                LogUtils.e(TAG, "j0=" + j);
                if (j > 1) {

                    if (stList.contains(Constant.UPLOADIMAGE)) {
                        j--;
                        LogUtils.e(TAG, "j1=" + j);
                    }
                    i = 0;
                    press();
                } else {
                    submitData();
                }
            }

            @Override
            public void titleClick() {

            }
        });
    }

    private void press() {
        String path = stList.get(i).replace("file://", "");
        LogUtils.e(TAG, "path=" + path);
        Luban.get(PublishDynamicActivity.this)
                .load(new File(path))                     //传人要压缩的图片
                .putGear(1)      //设定压缩档次，默认三挡
                .setCompressListener(new OnCompressListener() { //设置回调

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        LogUtils.e(TAG, "file=" + file.getPath());
                        byte[] mByte = FileUtils.fileToBytes(file);
                        if (mByte != null) {
                            thumb += Base64.encodeToString(mByte, Base64.DEFAULT) + "####";
                        } else {
                            LogUtils.e(TAG, "图片压转码异常");
                        }
                        i++;
                        if (i == j) {
                            submitData();
                        } else {
                            press();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 当压缩过去出现问题时调用
                        LogUtils.e(TAG, "图片压缩错误", e);
                        i++;
                        if (i == j) {
                            submitData();
                        } else {
                            press();
                        }
                    }
                }).launch();    //启动压缩
    }

    /**
     * 数据验证
     */
    private boolean dataVerify() {
        boolean flag = true;
        news_id = "";//可选     引用的文章ID
        description = "";//必传     文字内容
        thumb = "";//缩略图     多个以英文逗号隔开
        synchro_wb = "1";//是否同步微博     1是 0否
        tag = "";
        tag_new = "";
        if (listTeacherZoneDynamicsInfo.size() > 0) {
            news_id = listTeacherZoneDynamicsInfo.get(0).getNews_id();
        }
        description = etContent.getText().toString();
        if (TextUtils.isEmpty(description)) {
            flag = false;
            etContent.setError("动态直播内容不能为空");
        } else {
            etContent.setError(null);
        }
        if (mTags.size() > 0) {
            for (int i = 0; i < mTags.size(); i++) {
                Tag mTag = mTags.get(i);
                if (mTag.getId() == 0) {
                    tag_new += mTag.getName();
                    tag_new += ",";
                } else {
                    tag += mTag.getId();
                    tag += ",";
                }
            }
            if (tag_new.length() > 0) {
                tag_new = tag_new.substring(0, tag_new.length() - 1);
            }
            if (tag.length() > 0) {
                tag = tag.substring(0, tag.length() - 1);
            }
        }
        return flag;
    }

    private void submitData() {
        if (!TextUtils.isEmpty(thumb)) {
            thumb = thumb.substring(0, thumb.length() - 4);
            LogUtils.e(TAG, "thumb=" + thumb);
            LogUtils.e(TAG, "thumb.length=" + thumb.length());
        }
        Map<String, String> map = new HashMap<>();
        map.put("news_id", news_id);
        map.put("description", description);
        map.put("thumb", thumb);
        map.put("synchro_wb", synchro_wb);
        map.put("tag", tag);
        map.put("tag_new", tag_new);
        if (!isSubmiting) {
            isSubmiting = true;
            OKHttpUtils.getInstance().post(this, Api.add_dynamics, ParamUtil.getParam(map), Action.add_dynamics);
        }

    }

    private void dataBind() {
        tagListView.setTags(mTags);
    }


    @OnClick({R.id.btnFlag, R.id.rlRelatedArticles})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlRelatedArticles:
                PickArticleActivity.intoNewActivity(this, REQUSET_ARTICLE_CODE);
                break;
            case R.id.btnFlag:
                PickTagsActivity.intoNewActivity(this, mTags, mNewTagList, REQUSET_TAG_CODE);
                break;
        }

    }

    /**
     * 获取上传图片数据
     *
     * @param list
     * @return
     */
    private ArrayList<String> getImageList(List<String> list) {
        stList = new ArrayList<String>();
        if (list != null && list.size() > 0) {
            stList.addAll(list);
        }
        // 占位符
        if (Constant.UPLOAD_IMAGE_MAX_NUMBER > stList.size()) {
            stList.add(Constant.UPLOADIMAGE);
        }

        return stList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUSET_TAG_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        mTags = (List<Tag>) data.getExtras().getSerializable(SELECTED_LIST);
                        mNewTagList = (List<Tag>) data.getExtras().getSerializable(NEW_LIST);
                        dataBind();
                    }
                }
                break;
            case REQUSET_ARTICLE_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        TeacherZoneDynamicsInfo newsItem = (TeacherZoneDynamicsInfo) data.getSerializableExtra(PickArticleActivity.NEWS_ITEM);
                        listTeacherZoneDynamicsInfo.clear();
                        listTeacherZoneDynamicsInfo.add(newsItem);
                        dataArticleBind();
                    }
                }
                break;
            case Constant.SELECT_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    if (data.getExtras().getStringArrayList(SelectPictureActivity.INTENT_SELECTED_PICTURE) != null) {
                        stList = getImageList(data.getExtras().getStringArrayList(SelectPictureActivity.INTENT_SELECTED_PICTURE));
                        mUpLoadImageAdapter = new UpLoadImageAdapter(stList, PublishDynamicActivity.this, 1);
                        exgv_appraisal_pic.setAdapter(mUpLoadImageAdapter);
                    }
                }
                break;
            case Constant.PICTURE_PREVIEW:// 图片预览
                if (resultCode == RESULT_OK && data != null) {
                    if (data.getExtras().getStringArrayList(Constant.PICTURE_PREVIEW_INDEX_KEY) != null) {
                        stList = getImageList(data.getExtras().getStringArrayList(Constant.PICTURE_PREVIEW_INDEX_KEY));
                        mUpLoadImageAdapter = new UpLoadImageAdapter(stList, PublishDynamicActivity.this, 1);
                        exgv_appraisal_pic.setAdapter(mUpLoadImageAdapter);
                    }
                }
                break;
        }
    }

    private void dataArticleBind() {
        if (mPublicDynamicAdapter == null) {
            mPublicDynamicAdapter = new PublicDynamicAdapter(this, listTeacherZoneDynamicsInfo);
            elv_friends_post_group.setAdapter(mPublicDynamicAdapter);
            mPublicDynamicAdapter.setIDeleteItemClickListener(new ISwipeDeleteItemClickListening() {
                @Override
                public void delete(SwipeLayout swipeLayout, int position, Object newsItem) {
                    listTeacherZoneDynamicsInfo.remove(newsItem);
                    mPublicDynamicAdapter.notifyDataSetChanged();
                }
            });
        } else {
            mPublicDynamicAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case add_dynamics:
                LoadingProgressDialog.stopProgressDialog();
                isSubmiting = false;
                if (event.getCode() == Constant.SUCCEED) {
                    ToastUtils.showToast(this, "发布动态成功");
                    finish();
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "发布动态直播失败"));
                }
                break;
        }
    }

}
