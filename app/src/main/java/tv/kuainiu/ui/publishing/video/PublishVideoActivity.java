package tv.kuainiu.ui.publishing.video;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

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
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.Categroy;
import tv.kuainiu.modle.Permissions;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.publishing.pick.PickTagsActivity;
import tv.kuainiu.utils.DateUtil;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.PermissionManager;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.TakePhotoActivity;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.PermissionDialog;
import tv.kuainiu.widget.PublishPickTimeLayout;
import tv.kuainiu.widget.TitleBarView;
import tv.kuainiu.widget.dialog.SelectPicPopupWindow;
import tv.kuainiu.widget.tagview.Tag;
import tv.kuainiu.widget.tagview.TagListView;
import tv.kuainiu.widget.tagview.TagView;

import static tv.kuainiu.R.id.etDynamics_desc;
import static tv.kuainiu.modle.cons.Constant.SUCCEED;
import static tv.kuainiu.ui.publishing.pick.PickTagsActivity.NEW_LIST;
import static tv.kuainiu.ui.publishing.pick.PickTagsActivity.PROGRAM;
import static tv.kuainiu.ui.publishing.pick.PickTagsActivity.SELECTED_LIST;

public class PublishVideoActivity extends BaseActivity {

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

    Bitmap bitmap;
    @BindView(R.id.tbv_title)
    TitleBarView tbvTitle;
    @BindView(R.id.ivShareSina)
    ImageView ivShareSina;
    @BindView(R.id.ivShareWeChat)
    ImageView ivShareWeChat;
    @BindView(R.id.ivShareQQ)
    ImageView ivShareQQ;
    @BindView(R.id.ivAddCover)
    ImageView ivAddCover;
    @BindView(R.id.tvError)
    TextView tvError;
    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.spPermissions)
    Spinner spPermissions;

    @BindView(R.id.tvLiveStartTime)
    TextView tvLiveStartTime;
    @BindView(R.id.tvLiveEndTime)
    TextView tvLiveEndTime;
    @BindView(R.id.et_content)
    EditText etContent;
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
    @BindView(etDynamics_desc)
    EditText etDynamicsDesc;
    @BindView(R.id.tvInputWordLimit2)
    TextView tvInputWordLimit2;
    @BindView(R.id.llDynamicContent)
    LinearLayout llDynamicContent;
    @BindView(R.id.pptTime)
    PublishPickTimeLayout pptTime;
    private List<Tag> mTags = new ArrayList<Tag>();
    private List<Tag> mNewTagList = new ArrayList<Tag>();
    Tag programTag;
    private List<Categroy> mCategroyList = new ArrayList<>();
    private List<Permissions> mPermissionsList = new ArrayList<>();
    private String[] arryCategroy;
    private String[] arryPermissions;

    private String type = "2";//     必传     发布类型 1文章 2视频 3声音
    private String synchro_wb = "1";    //可选      是否同步微博     1是 0否

    private String cat_id = "";//    必传     栏目ID
    private String title = "";//     必传     标题
    private String thumb = "";//      必传     缩略图
    private String tag = "";//   可选      标签     选择已有标签(标签ID)     eg:1,2,3,6
    private String tag_new = "";//      可选      新自定义标签（标签字符串）  eg:基本面,涨停,大涨
    private String content = "";    // 必传     内容体
    private String video_id = "";    //视频必传     视频ID
    private String permission = "1";    //可选      权限 1,公开开放   2,粉丝可见

    private boolean isSubmiting = false;

    private SelectPicPopupWindow menuWindow;      // 头像弹出框
    private String teacher_id;
    private String anchor;
    private String date = "2016-10-18";
    private String start_date = "";
    private String end_date = "";
    private String synchro_dynamics = "1";
    private String dynamics_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_video);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
        initData();

    }

    @OnClick({R.id.btnFlag, R.id.ivAddCover, R.id.rlPick, R.id.llStartTime, R.id.llEndTime})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFlag://选择标签
                PickTagsActivity.intoNewActivity(this, "live", programTag, mTags, mNewTagList, REQUSET_TAG_CODE);
                break;
            case R.id.ivAddCover://选择缩图
                menuWindow = new SelectPicPopupWindow(this, itemsOnClick);
                menuWindow.showAtLocation(PublishVideoActivity.this.getWindow().getDecorView(),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.llStartTime:
                pptTime.setTextView(tvLiveStartTime);
                intro();
                break;
            case R.id.llEndTime:
                pptTime.setTextView(tvLiveEndTime);
                intro();

                break;
        }
    }

    /**
     * 控制发布面板的显示与隐藏
     */
    public void intro() {
        BottomSheetBehavior behavior = BottomSheetBehavior.from(findViewById(R.id.pptTime));
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    private void initView() {
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
                llDynamicContent.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
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
//        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int pos, long id) {
//                cat_id = mCategroyList.get(pos).getId();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                cat_id = mCategroyList.get(0).getId();
//            }
//        });
        spPermissions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                permission = String.valueOf(mPermissionsList.get(pos).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                permission = String.valueOf(mPermissionsList.get(0).getId());
            }
        });
    }


    private void initData() {
        anchor = MyApplication.getUser().getNickname();
        teacher_id = MyApplication.getUser().getUser_id();

//        OKHttpUtils.getInstance().syncGet(this, Api.get_cats + ParamUtil.getParamForGet("class", "live"), Action.get_cats);
        OKHttpUtils.getInstance().syncGet(this, Api.live_permissions, Action.live_permissions);
    }

    private void dataBind() {
        tagListView.setTags(mTags);
        tagCategoryListView.removeAllViews();
        if (programTag != null) {
            programTag.setChecked(true);
            tagCategoryListView.addTag(programTag);
        }
    }

    private void dataBindView() {
////        if (mCategroyList.size() > 0) {
////            cat_id = mCategroyList.get(0).getId();
////            arryCategroy = new String[mCategroyList.size()];
////        }
//        for (int i = 0; i < mCategroyList.size(); i++) {
//            arryCategroy[i] = mCategroyList.get(i).getCatname();
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arryCategroy);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //绑定 Adapter到控件
//        spCategory.setAdapter(adapter);
    }

    private void dataBindPermission() {
        if (mPermissionsList.size() > 0) {
            permission = String.valueOf(mPermissionsList.get(0).getId());
            arryPermissions = new String[mPermissionsList.size()];
        }
        for (int i = 0; i < mPermissionsList.size(); i++) {
            arryPermissions[i] = mPermissionsList.get(i).getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arryPermissions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spPermissions.setAdapter(adapter);
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
        dynamics_desc = "";    // 必传     内容体
        date = "";
        start_date = "";
        end_date = "";
        title = etTitle.getText().toString();
        content = etContent.getText().toString();
        dynamics_desc = etDynamicsDesc.getText().toString();
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
            etTitle.findFocus();
        }
        if (TextUtils.isEmpty(content)) {
            etContent.setError("文章内容不能为空");
            if (flag) {
                etContent.findFocus();
            }
            flag = false;

        } else {
            etContent.setError(null);
        }
        start_date = tvLiveStartTime.getText().toString();
        date = tvLiveStartTime.getTag() == null ? "" : tvLiveStartTime.getTag().toString();
        end_date = tvLiveEndTime.getText().toString();
        if (programTag != null) {
            cat_id = String.valueOf(programTag.getId());
        } else {
            cat_id = "";
        }
        if (TextUtils.isEmpty(cat_id)) {
            flag = false;
            ToastUtils.showToast(this, "请选择栏目");
        }
        if (TextUtils.isEmpty(end_date) && TextUtils.isEmpty(start_date)) {
            flag = false;
            ToastUtils.showToast(this, "请选择开始和结束时间");
        } else if (TextUtils.isEmpty(start_date)) {
            flag = false;
            ToastUtils.showToast(this, "请选择开始时间");
        } else if (TextUtils.isEmpty(end_date)) {
            flag = false;
            ToastUtils.showToast(this, "请选择结束时间");
        }
        if (DateUtil.minutesBetweenTwoDate(start_date, end_date) <= 0) {
            flag = false;
            ToastUtils.showToast(this, "结束时间要大于开始时间");
        }

        if (!"0".equals(synchro_dynamics)) {
            if (TextUtils.isEmpty(dynamics_desc)) {
                etDynamicsDesc.setError("动态内容不能为空");
                if (flag) {
                    etDynamicsDesc.findFocus();
                }
                flag = false;

            } else {
                etDynamicsDesc.setError(null);
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
        map.put("permission", permission);  //  可选      权限 1,公开开放   2,粉丝可见
        map.put("ins_id", "0");  // 机构老师可传     机构ID
        map.put("allow_comment", "1");  //可选      是否允许评论     1允许 0否
        map.put("teacher_id", teacher_id);  //  必传    主播老师ID
        map.put("anchor", anchor);  //必传     主播名称
        map.put("date", date);  // 必传     直播日期  YYYY-mm-dd
        map.put("start_date", start_date);  //必传     开始直播时间     YYYY-mm-dd H:i:s
        map.put("end_date", end_date);  //必传     结束直播时间     YYYY-mm-dd H:i:s
        map.put("synchro_dynamics", String.valueOf(synchro_dynamics));  // 可选     是否同步动态     1是0否
        map.put("dynamics_desc", dynamics_desc);  // 同步动态时必传     动态描述文字
        if (!isSubmiting) {
            isSubmiting = true;
            OKHttpUtils.getInstance().post(this, Api.add_live, ParamUtil.getParam(map), Action.add_news_live);
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case live_permissions:
                if (SUCCEED == event.getCode()) {
                    try {
                        JsonParser parser = new JsonParser();
                        JsonObject tempJson = (JsonObject) parser.parse(event.getData().toString());
                        JsonArray json = tempJson.getAsJsonObject("data").getAsJsonArray("list");
                        List<Permissions> listTemp = new Gson().fromJson(json, new TypeToken<List<Permissions>>() {
                        }.getType());

                        if (listTemp != null && listTemp.size() > 0) {
                            mPermissionsList.clear();
                            mPermissionsList.addAll(listTemp);
                            dataBindPermission();
                        } else {
                            ToastUtils.showToast(PublishVideoActivity.this, "未获取到权限信息");
                        }
                    } catch (Exception e) {
                        LogUtils.e(TAG, "获取到权限信息解析异常", e);
                        ToastUtils.showToast(PublishVideoActivity.this, "获取到权限信息解析异常");
                    }
                } else {
                    ToastUtils.showToast(PublishVideoActivity.this, StringUtils.replaceNullToEmpty(event.getMsg(), "获取到权限信息失败"));
                }
                break;
//            case get_cats:
//                if (SUCCEED == event.getCode()) {
//                    try {
//                        JsonParser parser = new JsonParser();
//                        JsonObject tempJson = (JsonObject) parser.parse(event.getData().toString());
//                        JsonArray json = tempJson.getAsJsonObject("data").getAsJsonArray("list");
//                        List<Categroy> listTemp = new Gson().fromJson(json, new TypeToken<List<Categroy>>() {
//                        }.getType());
//
//                        if (listTemp != null && listTemp.size() > 0) {
//                            mCategroyList.clear();
//                            Categroy categroy = new Categroy();
//                            categroy.setCatname("请选择");
//                            categroy.setId("");
//                            mCategroyList.add(categroy);
//                            mCategroyList.addAll(listTemp);
//                            dataBindView();
//                        } else {
//                            ToastUtils.showToast(PublishVideoActivity.this, "未获取到文章栏目信息");
//                        }
//                    } catch (Exception e) {
//                        LogUtils.e(TAG, "获取到文章信息解析异常", e);
//                        ToastUtils.showToast(PublishVideoActivity.this, "获取到文章栏目信息解析异常");
//                    }
//                } else {
//                    ToastUtils.showToast(PublishVideoActivity.this, StringUtils.replaceNullToEmpty(event.getMsg(), "获取到文章栏目信息失败"));
//                }
//                break;
            case add_news_live:
                isSubmiting = false;
                if (event.getCode() == Constant.SUCCEED) {
                    LogUtils.i(TAG, event.getData().toString());
                    ToastUtils.showToast(this, "发布视频直播成功");
                /*    {
                        "data" : {
                        "info" : {
                            "tag_new" : "",
                                    "status" : 1,
                                    "tag" : "41",
                                    "ins_id" : "0",
                                    "permission" : 1,
                                    "date" : "2016-10-17",
                                    "url" : "http:\/\/www.kuainiu.tv\/user\/live?teacher_id=1",
                                    "anchor" : "快牛官方",
                                    "id" : 20,
                                    "end_date" : "09:00:00",
                                    "title" : "我们",
                                    "create_date" : "2016-10-17 16:31:35",
                                    "description" : false,
                                    "allow_comment" : 1,
                                    "user_id" : "1",
                                    "teacher_id" : "1",
                                    "synchro_dynamics" : 1,
                                    "start_date" : "08:00:00",
                                    "thumb" : "http:\/\/img.kuainiu.tv\/uploadfile\/thumb\/201610\/1_dynamics_1476693095_89353.jpg",
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
//                        ShareUtils.showShare(ShareUtils.ARTICLE, PublishVideoActivity.this, title, description, thumb, url, null);
                    } catch (Exception e) {
                        LogUtils.e(TAG, "Exception", e);

                    }
                    finish();
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "发布视频直播失败"));
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
                    if (!PermissionManager.checkPermission(PublishVideoActivity.this,
                            Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions(PublishVideoActivity.this,
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
