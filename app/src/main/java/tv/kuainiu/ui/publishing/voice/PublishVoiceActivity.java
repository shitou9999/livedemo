package tv.kuainiu.ui.publishing.voice;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
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
import tv.kuainiu.R;
import tv.kuainiu.app.ConfigUtil;
import tv.kuainiu.app.Constans;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.Categroy;
import tv.kuainiu.modle.TeacherZoneDynamicsInfo;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.publishing.dynamic.PublicDynamicAdapter;
import tv.kuainiu.ui.publishing.pick.PickTagsActivity;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.MediaPlayUtil;
import tv.kuainiu.utils.PermissionManager;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.TakePhotoActivity;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.ExpandListView;
import tv.kuainiu.widget.PermissionDialog;
import tv.kuainiu.widget.TitleBarView;
import tv.kuainiu.widget.dialog.SelectPicPopupWindow;
import tv.kuainiu.widget.tagview.Tag;
import tv.kuainiu.widget.tagview.TagListView;
import tv.kuainiu.widget.tagview.TagView;

import static tv.kuainiu.R.id.elv_friends_post_group;
import static tv.kuainiu.modle.cons.Constant.SUCCEED;
import static tv.kuainiu.ui.publishing.pick.PickTagsActivity.NEW_LIST;
import static tv.kuainiu.ui.publishing.pick.PickTagsActivity.SELECTED_LIST;

public class PublishVoiceActivity extends BaseActivity {

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
    //    @BindView(R.id.et_content)
//    EditText etContent;
//    @BindView(R.id.tvInputWordLimit)
//    TextView tvInputWordLimit;
    @BindView(R.id.btnFlag)
    TextView btnFlag;
    @BindView(R.id.tagListView)
    TagListView tagListView;
    @BindView(R.id.spCategory)
    Spinner spCategory;

    //话筒的图片
    @BindView(R.id.ivVoiceBtn)
    ImageView ivVoiceBtn;

    Bitmap bitmap;
    @BindView(R.id.recording_hint)
    TextView recordingHint;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(elv_friends_post_group)
    ExpandListView elvFriendsPostGroup;
    private List<Tag> mTags = new ArrayList<Tag>();
    private List<Tag> mNewTagList = new ArrayList<Tag>();
    private List<Categroy> mCategroyList = new ArrayList<>();
    private String[] arryCategroy;

    private String type = "3";//     必传     发布类型 1文章 2视频 3声音
    private String synchro_wb = "1";    //可选      是否同步微博     1是 0否

    private String cat_id = "";//    必传     栏目ID
    private String title = "";//     必传     标题
    private String thumb = "";//      必传     缩略图
    private String tag = "";//   可选      标签     选择已有标签(标签ID)     eg:1,2,3,6
    private String tag_new = "";//      可选      新自定义标签（标签字符串）  eg:基本面,涨停,大涨
    private String content = "";    // 必传     内容体
    private String voice = "";    // 声音必传     声音文件
    private String synchro_dynamics = "1"; //   可选     是否同步动态     1是0否
    private String dynamics_desc = "";//     同步动态时必传     动态描述文字

    private boolean isSubmiting = false;

    private SelectPicPopupWindow menuWindow;      // 头像弹出框

    //存储很多张话筒图片的数组
    private Drawable[] micImages;
    private int BASE = 600;
    private int SPACE = 200;// 间隔取样时间
    private boolean isStop;  // 录音是否结束的标志 超过1分钟停止
    private String mSoundDataFilePath = "";//语音文件
    private String dataPath;
    private boolean isCanceled = false; // 是否取消录音
    private float downY;
    private MediaRecorder mRecorder;
    private MediaPlayUtil mMediaPlayUtil;
    private long mStartTime;
    private long mEndTime;
    private int mTime;
    private PowerManager.WakeLock wakeLock;
    private PublicDynamicAdapter mPublicVoiceAdapter;
    List<TeacherZoneDynamicsInfo> listTeacherZoneDynamicsInfo = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_voice);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
        initData();
        initSoundData();
    }

    @OnClick({R.id.btnFlag, R.id.ivAddCover})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFlag://选择标签
                PickTagsActivity.intoNewActivity(this, mTags, mNewTagList, REQUSET_TAG_CODE);
                break;
            case R.id.ivAddCover://选择缩图
                menuWindow = new SelectPicPopupWindow(this, itemsOnClick);
                menuWindow.showAtLocation(PublishVoiceActivity.this.getWindow().getDecorView(),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
        }
    }

    private void initView() {
        tvTime.setText("0" + '"');
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
        tagListView.setDeleteMode(true);
        tagListView.setOnTagClickListener(new TagListView.OnTagClickListener() {
            @Override
            public void onTagClick(TagView tagView, Tag tag) {
                mTags.remove(tag);
                tagListView.removeTag(tag);
            }
        });
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                cat_id = mCategroyList.get(pos).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cat_id = mCategroyList.get(0).getId();
            }
        });
        // 动画资源文件,用于录制语音时
        micImages = new Drawable[]{
                getResources().getDrawable(R.mipmap.record1),
                getResources().getDrawable(R.mipmap.record2),
                getResources().getDrawable(R.mipmap.record3),
                getResources().getDrawable(R.mipmap.record4),
                getResources().getDrawable(R.mipmap.record5)
        };
        ivVoiceBtn.setOnTouchListener(new VoiceTouch());
    }


    private void initData() {
        OKHttpUtils.getInstance().syncGet(this, Api.get_cats, Action.get_cats);
    }

    /**
     * 录音存放路径
     */
    public void initSoundData() {
        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "time");
        dataPath = Environment.getExternalStorageDirectory() + "/" + ConfigUtil.DOWNLOAD_DIR + "/Sounds/";
        File folder = new File(dataPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File nomedia = new File(dataPath + ".nomedia");
        if (!nomedia.exists()) {
            try {
                nomedia.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mMediaPlayUtil = MediaPlayUtil.getInstance();
    }

    private void dataBind() {
        tagListView.setTags(mTags);
    }

    private void dataBindView() {
        if (mCategroyList.size() > 0) {
            cat_id = mCategroyList.get(0).getId();
            arryCategroy = new String[mCategroyList.size()];
        }
        for (int i = 0; i < mCategroyList.size(); i++) {
            arryCategroy[i] = mCategroyList.get(i).getCatname();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arryCategroy);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spCategory.setAdapter(adapter);
    }
    private void dataBindVoice() {
        if (mPublicVoiceAdapter == null) {
            mPublicVoiceAdapter = new PublicDynamicAdapter(this, listTeacherZoneDynamicsInfo);
            elvFriendsPostGroup.setAdapter(mPublicVoiceAdapter);
            mPublicVoiceAdapter.setIDeleteItemClickListener(new PublicDynamicAdapter.IDeleteItemClickListener() {
                @Override
                public void delete(SwipeLayout swipeLayout, int position, TeacherZoneDynamicsInfo newsItem) {
                    listTeacherZoneDynamicsInfo.remove(newsItem);
                    deleteSoundFileUnSend();
                    voice="";
                    swipeLayout.close(true);
                    mPublicVoiceAdapter.notifyDataSetChanged();
                }
            });
        } else {
            mPublicVoiceAdapter.notifyDataSetChanged();
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
        title = etTitle.getText().toString();
//        content = etContent.getText().toString();

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
        if (TextUtils.isEmpty(voice)) {
            flag = false;
            ToastUtils.showToast(this, "请先录音");
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
        map.put("voice", voice);  // 声音必传     声音文件
        map.put("synchro_dynamics", synchro_dynamics);  // 可选     是否同步动态     1是0否
        map.put("dynamics_desc", dynamics_desc);  // 同步动态时必传     动态描述文字
        if (!isSubmiting) {
            isSubmiting = true;
            OKHttpUtils.getInstance().post(this, Api.add_news, ParamUtil.getParam(map), Action.add_news_vioce);
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case get_cats:
                if (SUCCEED == event.getCode()) {
                    try {
                        JsonParser parser = new JsonParser();
                        JsonObject tempJson = (JsonObject) parser.parse(event.getData().toString());
                        JsonArray json = tempJson.getAsJsonObject("data").getAsJsonArray("list");
                        List<Categroy> listTemp = new Gson().fromJson(json, new TypeToken<List<Categroy>>() {
                        }.getType());

                        if (listTemp != null && listTemp.size() > 0) {
                            mCategroyList.addAll(listTemp);
                            dataBindView();
                        } else {
                            ToastUtils.showToast(PublishVoiceActivity.this, "未获取到文章栏目信息");
                        }
                    } catch (Exception e) {
                        LogUtils.e(TAG, "获取到文章信息解析异常", e);
                        ToastUtils.showToast(PublishVoiceActivity.this, "获取到文章栏目信息解析异常");
                    }
                } else {
                    ToastUtils.showToast(PublishVoiceActivity.this, StringUtils.replaceNullToEmpty(event.getMsg(), "获取到文章栏目信息失败"));
                }
                break;
            case add_news_vioce:
                isSubmiting = false;
                if (event.getCode() == Constant.SUCCEED) {
                    ToastUtils.showToast(this, "发布语音直播成功");
                    deleteSoundFileUnSend();
                    finish();
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "发布语音直播失败"));
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
                    if (!PermissionManager.checkPermission(PublishVoiceActivity.this,
                            Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions(PublishVoiceActivity.this,
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private final Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            //根据mHandler发送what的大小决定话筒的图片是哪一张
            //说话声音越大,发送过来what值越大
            if (what > 4) {
                what = 4;
            }
            ivVoiceBtn.setImageDrawable(micImages[what]);
        }
    };
    private Runnable mupdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };

    /**
     * 录音的触摸监听
     */
    class VoiceTouch implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downY = motionEvent.getY();
                    recordingHint.setText("录制中...");
                    deleteSoundFileUnSend();//删除上一次录制（如果有上次录音文件）没有上传的录音
                    mSoundDataFilePath = dataPath + StringUtils.getRandomFileName();

                    // TODO 防止开权限后崩溃
                    if (mRecorder != null) {
                        mRecorder.reset();
                    } else {
                        mRecorder = new MediaRecorder();
                    }
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                    mRecorder.setOutputFile(mSoundDataFilePath);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    try {
                        mRecorder.prepare();
                        mRecorder.start();
                        wakeLock.acquire();
                        isCanceled = false;
                        mStartTime = System.currentTimeMillis();
                        tvTime.setText("0" + '"');
                        updateMicStatus();
                        // TODO 开启定时器
                        mHandler.postDelayed(runnable, 1000);
                    } catch (Exception e) {
                        if (wakeLock.isHeld()) {
                            wakeLock.release();
                        }
                        Log.i("recoder", "prepare() failed-Exception-" + e.toString());
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    if (wakeLock.isHeld()) {
                        wakeLock.release();
                    }
                    if (!isStop) {
                        mEndTime = System.currentTimeMillis();
                        mTime = (int) ((mEndTime - mStartTime) / 1000);
                        if (isCanceled) {
                            deleteSoundFileUnSend();
                            ToastUtils.showToast(PublishVoiceActivity.this, "录音取消");
                            initVoice();
                        } else {
                            ivVoiceBtn.setImageDrawable(getResources().getDrawable(R.drawable.selector_publish_voice_icon));
                            tvTime.setText(mTime + "" + '"');
                            stopRecord();
                            try {
                                voice = StringUtils.encodeBase64File(mSoundDataFilePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        initVoice();
                    }
                    break;
                case MotionEvent.ACTION_CANCEL: // 首次开权限时会走这里，录音取消
                    Log.i("record_test", "权限影响录音录音");
                    if (wakeLock.isHeld()) {
                        wakeLock.release();
                    }
                    // TODO 这里一定注意，先release，还要置为null，否则录音会发生错误，还有可能崩溃
                    if (mRecorder != null) {
                        mRecorder.stop();
                        mRecorder.release();
                        mRecorder = null;
                        System.gc();
                    }
                    initVoice();

                    isCanceled = true;

                    break;

                case MotionEvent.ACTION_MOVE: // 滑动手指
                    float moveY = motionEvent.getY();
                    if (downY - moveY > 100) {
                        isCanceled = true;
                        recordingHint.setText("松开手指可取消录音");
                    }
                    if (downY - moveY < 20) {
                        isCanceled = false;
                        recordingHint.setText("向上滑动取消发送");
                    }
                    break;

            }
            return true;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
        if (mRecorder != null) {
            mHandler2.removeCallbacks(mupdateMicStatusTimer);
            mHandler.removeCallbacks(runnable);
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            System.gc();
        }
    }

    private void initVoice() {
        mHandler2.removeCallbacks(mupdateMicStatusTimer);
        mHandler.removeCallbacks(runnable);
        voice = "";
        tvTime.setText("0" + '"');
        ivVoiceBtn.setImageDrawable(getResources().getDrawable(R.drawable.selector_publish_voice_icon));
        recordingHint.setText("按住说话");
    }

    /**
     * 结束录音
     */
    public void stopRecord() {
        if (mTime < 1) {
            deleteSoundFileUnSend();
            isCanceled = true;
            initVoice();
            ToastUtils.showToast(PublishVoiceActivity.this, "录音时间太短，长按开始录音");
        } else {
            isCanceled = false;
            recordingHint.setText("录音成功");
            //成功就显示
            tvTime.setText(String.valueOf(mTime) + '"');
            Log.i("record_test", "录音成功");
            TeacherZoneDynamicsInfo mTeacherZoneDynamicsInfo=new TeacherZoneDynamicsInfo();
            mTeacherZoneDynamicsInfo.setType(String.valueOf(Constans.TYPE_AUDIO));
            mTeacherZoneDynamicsInfo.setNews_voice_url(mSoundDataFilePath);
            mTeacherZoneDynamicsInfo.setNews_title(String.valueOf(mTime) + '"');
            listTeacherZoneDynamicsInfo.clear();
            listTeacherZoneDynamicsInfo.add(mTeacherZoneDynamicsInfo);
            dataBindVoice();
        }
        //mRecorder.setOnErrorListener(null);
        try {
//            mRecorder.reset();
            mRecorder.stop();
            mRecorder.release();
        } catch (Exception e) {
            tvTime.setText("0" + '"');
            ToastUtils.showToast(PublishVoiceActivity.this, "录音发生错误,请重新录音");
            LogUtils.e("record_test", "录音发生错误", e);
        } finally {
            try {
                mHandler2.removeCallbacks(mupdateMicStatusTimer);
                mHandler.removeCallbacks(runnable);
                if (mRecorder != null) {
                    mRecorder = null;
                    System.gc();
                }
            } catch (Exception e) {
                LogUtils.e("record_test", "finally", e);
            }
        }


    }

    // 定时器
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                long endTime = System.currentTimeMillis();
                int time = (int) ((endTime - mStartTime) / 1000);
                //mRlSoundLengthLayout.setVisibility(View.VISIBLE);
                tvTime.setText(time + "" + '"');
                // 限制录音时间不长于两分钟
                if (time > 59) {
                    isStop = true;
                    mTime = time;
                    stopRecord();
                    Toast.makeText(PublishVoiceActivity.this, "录制时长最多60s", Toast.LENGTH_SHORT).show();
                } else {
                    mHandler.postDelayed(this, 1000);
                    isStop = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 录音完毕后，若不发送，则删除文件
     */
    public void deleteSoundFileUnSend() {
        mTime = 0;
        if (!"".equals(mSoundDataFilePath)) {
            try {
                File file = new File(mSoundDataFilePath);
                if (file.exists()) {
                    file.delete();
                }
                mSoundDataFilePath = "";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateMicStatus() {
        if (mRecorder != null) {
            int ratio = mRecorder.getMaxAmplitude() / BASE;
            int db = 0;// 分贝
            if (ratio > 1)
                db = (int) (20 * Math.log10(ratio));
            LogUtils.e(TAG, "分贝值：" + db + "     " + Math.log10(ratio));
            //我对着手机说话声音最大的时候，db达到了35左右，
            mHandler2.postDelayed(mupdateMicStatusTimer, SPACE);
            //所以除了7，为的就是对应5张图片
            mHandler2.sendEmptyMessage(db / 7);
        }
    }
}
