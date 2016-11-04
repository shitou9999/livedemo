package tv.kuainiu.ui.publishing.article;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.Categroy;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.publishing.pick.PickTagsActivity;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.PermissionManager;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.TakePhotoActivity;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.PermissionDialog;
import tv.kuainiu.widget.TitleBarView;
import tv.kuainiu.widget.dialog.SelectPicPopupWindow;
import tv.kuainiu.widget.tagview.Tag;
import tv.kuainiu.widget.tagview.TagListView;
import tv.kuainiu.widget.tagview.TagView;

import static tv.kuainiu.ui.publishing.pick.PickTagsActivity.NEW_LIST;
import static tv.kuainiu.ui.publishing.pick.PickTagsActivity.PROGRAM;
import static tv.kuainiu.ui.publishing.pick.PickTagsActivity.SELECTED_LIST;

public class PublishArticleActivity extends BaseActivity {

    /**
     * 请求相机权限
     */
    private static final int CAMERA_REQUEST_CODE = 110;
    public static final int REQUSET_TAG_CODE = 0;
    /**
     * 相册选图
     */
    private static final int REQUEST_PICK = 3;
    /**
     * 拍照
     */
    private static final int REQUEST_TAKE = 1;
    /**
     * 图片裁切标记
     */
    private static final int REQUEST_CUTTING = 2;
    @BindView(R.id.tbv_title)
    TitleBarView tbvTitle;
    @BindView(R.id.tvError)
    TextView tvError;
    @BindView(R.id.ivShareSina)
    ImageView ivShareSina;
    @BindView(R.id.ivShareWeChat)
    ImageView ivShareWeChat;
    @BindView(R.id.ivShareQQ)
    ImageView ivShareQQ;
    @BindView(R.id.ivAddCover)
    ImageView ivAddCover;
    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.etDynamics_desc)
    EditText etDynamics_desc;
    @BindView(R.id.tvInputWordLimit)
    TextView tvInputWordLimit;
    @BindView(R.id.btnFlag)
    TextView btnFlag;
    @BindView(R.id.tagListView)
    TagListView tagListView;
    @BindView(R.id.tagCategoryListView)
    TagListView tagCategoryListView;
    @BindView(R.id.sw_dynamic)
    SwitchCompat swDynamic;
    @BindView(R.id.tvInputWordLimit2)
    TextView tvInputWordLimit2;
    @BindView(R.id.llDynamicContent)
    LinearLayout llDynamicContent;
    Bitmap bitmap;
    private List<Tag> mTags = new ArrayList<Tag>();
    private List<Tag> mNewTagList = new ArrayList<Tag>();
    private List<Categroy> mCategroyList = new ArrayList<>();
    private String[] arryCategroy;

    private String type = "1";//     必传     发布类型 1文章 2视频 3声音
    private String synchro_wb = "1";    //可选      是否同步微博     1是 0否

    private String cat_id = "";//    必传     栏目ID
    private String title = "";//     必传     标题
    private String thumb = "";//      必传     缩略图
    private String tag = "";//   可选      标签     选择已有标签(标签ID)     eg:1,2,3,6
    private String tag_new = "";//      可选      新自定义标签（标签字符串）  eg:基本面,涨停,大涨
    private String content = "";    // 必传     内容体
    private String synchro_dynamics = "1";    //  可选     是否同步动态     1是0否
    private String dynamics_desc = "";    //同步动态时必传     动态描述文字

    private boolean isSubmiting = false;

    private SelectPicPopupWindow menuWindow;      // 头像弹出框
    private Tag programTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_article);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();

    }

    @OnClick({R.id.btnFlag, R.id.ivAddCover})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFlag://选择标签
                PickTagsActivity.intoNewActivity(this, "", programTag, mTags, mNewTagList, REQUSET_TAG_CODE);
                break;
            case R.id.ivAddCover://选择缩图
                menuWindow = new SelectPicPopupWindow(this, itemsOnClick);
                menuWindow.showAtLocation(PublishArticleActivity.this.getWindow().getDecorView(),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
        }
    }

    private void initView() {
        etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2000)});
        tbvTitle.setOnClickListening(new TitleBarView.OnClickListening() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                if (!dataVerify()) {
                    return;
                }
                submitData();
            }

            @Override
            public void titleClick() {

            }
        });
        swDynamic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                synchro_dynamics = isChecked ? "1" : "0";
                llDynamicContent.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        tagCategoryListView.setDeleteMode(true);
        tagCategoryListView.setTagViewBackgroundCheckedRes(R.drawable.tag_checked_pressed);
        tagCategoryListView.setOnTagClickListener(new TagListView.OnTagClickListener() {
            @Override
            public void onTagClick(TagView tagView, Tag tag) {
                programTag = null;
                tagCategoryListView.removeTag(tag);
            }
        });
        tagListView.setDeleteMode(true);
        tagListView.setTagViewBackgroundCheckedRes(R.drawable.tag_checked_blue_pressed);
        tagListView.setOnTagClickListener(new TagListView.OnTagClickListener() {
            @Override
            public void onTagClick(TagView tagView, Tag tag) {
                mTags.remove(tag);
                tagListView.removeTag(tag);
            }
        });
    }


    private void dataBind() {
        tagListView.setTags(mTags);
        tagCategoryListView.removeAllViews();
        if (programTag != null) {
            programTag.setChecked(true);
            tagCategoryListView.addTag(programTag);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_PICK:// 直接从相册获取
                if (data != null) {
                    try {
                        startPhotoZoom(data.getData());
                    } catch (NullPointerException e) {
                        e.printStackTrace();// 用户点击取消操作
                    }
                }
                break;
            case REQUEST_TAKE:// 调用相机拍照
                if (data != null) {
                    File temp = new File(data.getStringExtra(TakePhotoActivity.IMAGE_PATH));
                    startPhotoZoom(Uri.fromFile(temp));
                }
                break;
            case REQUEST_CUTTING:// 取得裁剪后的图片
                setPicToView(data);
                break;
            case REQUSET_TAG_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        mTags = (List<Tag>) data.getExtras().getSerializable(SELECTED_LIST);
                        mNewTagList = (List<Tag>) data.getExtras().getSerializable(NEW_LIST);
                        programTag = (Tag) data.getExtras().getSerializable(PROGRAM);
                        dataBind();
                    }
                }
                break;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takeCapture();
            } else {
                new PermissionDialog(this)
                        .show(R.string.permission_tip_dialog_message_camera);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 数据验证
     */
    private boolean dataVerify() {
        boolean flag = true;
        title = "";//     必传     标题
        tag = "";//   可选      标签     选择已有标签(标签ID)     eg:1,2,3,6
        tag_new = "";//      可选      新自定义标签（标签字符串）  eg:基本面,涨停,大涨
        content = "";    // 必传     内容体
        dynamics_desc = "";    //同步动态时必传     动态描述文字
        title = etTitle.getText().toString();
        content = etContent.getText().toString();
        dynamics_desc = etDynamics_desc.getText().toString();

        if (TextUtils.isEmpty(thumb)) {
            flag = false;
            tvError.setText("请选择缩略图");
            ToastUtils.showToast(this, "请选择缩略图");
        } else {
            tvError.setText("");
        }
        if (TextUtils.isEmpty(title)) {
            flag = false;
            etTitle.setError("标题不能为空");
        }
        if (TextUtils.isEmpty(content)) {
            flag = false;
            etContent.setError("文章内容不能为空");
        } else {
            etContent.setError(null);
        }
        if (programTag != null) {
            cat_id = String.valueOf(programTag.getId());
        }else{
            cat_id="";
        }
        if (TextUtils.isEmpty(cat_id)) {
            flag = false;
            ToastUtils.showToast(this, "请选择栏目");
        }
        if (!"0".equals(synchro_dynamics)) {
            if (TextUtils.isEmpty(dynamics_desc)) {
                flag = false;
                etDynamics_desc.setError("动态直播内容不能为空");
            } else {
                etDynamics_desc.setError(null);
            }
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
        Map<String, String> map = new HashMap<>();
        map.put("type", type);//     必传     发布类型 1文章 2视频 3声音
        map.put("synchro_wb", synchro_wb); //可选      是否同步微博     1是 0否
        map.put("cat_id", cat_id);//    必传     栏目ID
        map.put("title", title);//     必传     标题
        map.put("thumb", thumb);//      必传     缩略图
        map.put("tag", tag);//   可选      标签     选择已有标签(标签ID)     eg:1,2,3,6
        map.put("tag_new", tag_new);//      可选      新自定义标签（标签字符串）  eg:基本面,涨停,大涨
        map.put("content", content);// 必传     内容体
        map.put("description", "");// 可选      文章描述
        map.put("synchro_dynamics", synchro_dynamics);  //  可选     是否同步动态     1是0否
        map.put("dynamics_desc", dynamics_desc);//同步动态时必传     动态描述文字
        if (!isSubmiting) {
            isSubmiting = true;
            OKHttpUtils.getInstance().post(this, Api.add_news, ParamUtil.getParam(map), Action.add_news_article);
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case add_news_article:
                isSubmiting = false;
                if (event.getCode() == Constant.SUCCEED) {
                    LogUtils.i(TAG, event.getData().toString());
                    ToastUtils.showToast(this, "发布文章成功");
                   /* {
                        "data" : {
                        "info" : {
                            "tag_new" : "",
                                    "updatetime" : 1476689578,
                                    "voice" : false,
                                    "status" : 99,
                                    "tag" : "",
                                    "permission" : 0,
                                    "voice_time" : false,
                                    "type" : 1,
                                    "url" : "http:\/\/www.kuainiu.tv\/news\/show?id=85",
                                    "inputtime" : 1476689578,
                                    "id" : 85,
                                    "content" : "看看",
                                    "cat_id" : "1",
                                    "title" : "进来了",
                                    "description" : "看看",
                                    "allow_comment" : 0,
                                    "video_id" : false,
                                    "user_id" : "1",
                                    "synchro_dynamics" : 1,
                                    "thumb" : "http:\/\/img.kuainiu.tv\/uploadfile\/thumb\/201610\/1_dynamics_1476689578_34107.jpg",
                                    "synchro_wb" : 1
                        }
                    },
                        "msg" : "成功",
                            "status" : 0
                    }*/
                    try {
                        JsonParser parser = new JsonParser();
                        JsonObject tempJson = (JsonObject) parser.parse(event.getData().toString());
                        JsonObject json = tempJson.getAsJsonObject("data").getAsJsonObject("info");
                        String url = "";
                        String thumb = "";
                        String title = "";
                        String description = "";
                        if (json != null && json.has("url")) {
                            url = json.get("url").getAsString();
                        }
                        if (json != null && json.has("thumb")) {
                            thumb = json.get("thumb").getAsString();
                        }
                        if (json != null && json.has("title")) {
                            title = json.get("title").getAsString();
                        }
                        if (json != null && json.has("description")) {
                            description = json.get("description").getAsString();
                        }
//                        ShareUtils.showShare(ShareUtils.ARTICLE, PublishArticleActivity.this, title, description, thumb, url, null);
                    } catch (Exception e) {
                        LogUtils.e(TAG, "Exception", e);

                    }
                    finish();
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "发布文章失败"));
                }
                break;
        }
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.takePhotoBtn:
                    if (!PermissionManager.checkPermission(PublishArticleActivity.this,
                            Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions(PublishArticleActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                CAMERA_REQUEST_CODE);
                    } else {
                        takeCapture();
                    }
                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    try {
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(pickIntent, REQUEST_PICK);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                        DebugUtils.dd("at 11 从相册选择......给我一个权限");
                    }
                    break;

            }
        }
    };

    private void takeCapture() {
        try {
            Intent takeIntent = new Intent(this, TakePhotoActivity.class);
            // 指定调用相机拍照后的照片存储的路径
//            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                    Uri.fromFile(new File(getExternalCacheDir(), IMAGE_FILE_NAME)));
            startActivityForResult(takeIntent, REQUEST_TAKE);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 16);
        intent.putExtra("aspectY", 9);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 180);
//        if (Build.VERSION.SDK_INT >= 23) {
//            intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                    Uri.fromFile(new File(getExternalCacheDir(), IMAGE_FILE_NAME)));
//            intent.putExtra("return-data", false);
//        } else {
//            intent.putExtra("return-data", true);
//        }
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CUTTING);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        if (picdata == null) return;
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            bitmap = extras.getParcelable("data");
            if (null == bitmap) return;
            Drawable drawable = new BitmapDrawable(null, bitmap);
            ivAddCover.setImageDrawable(drawable);
            thumb = Base64.encodeToString(toImageForBin(bitmap), Base64.DEFAULT);
            if (!TextUtils.isEmpty(thumb)) {
                tvError.setText("");
            }
        }
    }

    private byte[] toImageForBin(Bitmap photo) {

        int size = photo.getWidth() * photo.getHeight() * 4;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
        photo.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] bytes = baos.toByteArray();
//        photo.recycle();
        return bytes;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }
}
