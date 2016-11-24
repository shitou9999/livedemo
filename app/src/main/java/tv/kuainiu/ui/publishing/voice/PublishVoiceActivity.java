package tv.kuainiu.ui.publishing.voice;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import tv.kuainiu.app.ConfigUtil;
import tv.kuainiu.app.Constans;
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
import tv.kuainiu.ui.publishing.pick.PickTagsActivity;
import tv.kuainiu.utils.FileUtils;
import tv.kuainiu.utils.LoadingProgressDialog;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.MediaPlayUtil;
import tv.kuainiu.utils.PermissionManager;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.ExpandGridView;
import tv.kuainiu.widget.ExpandListView;
import tv.kuainiu.widget.PermissionDialog;
import tv.kuainiu.widget.TitleBarView;
import tv.kuainiu.widget.dialog.SelectPicPopupWindow;
import tv.kuainiu.widget.tagview.Tag;
import tv.kuainiu.widget.tagview.TagListView;
import tv.kuainiu.widget.tagview.TagView;

import static tv.kuainiu.R.id.elv_friends_post_group;
import static tv.kuainiu.ui.publishing.pick.PickTagsActivity.NEW_LIST;
import static tv.kuainiu.ui.publishing.pick.PickTagsActivity.PROGRAM;
import static tv.kuainiu.ui.publishing.pick.PickTagsActivity.SELECTED_LIST;

public class PublishVoiceActivity extends BaseActivity {

    /**
     * 请求相机权限
     */
    private static final int CAMERA_REQUEST_CODE = 110;
    private static final int RECORD_AUDIO_REQUEST_CODE = 111;
    public static final int REQUSET_TAG_CODE = 0;

    @BindView(R.id.tbv_title)
    TitleBarView tbvTitle;
    @BindView(R.id.ivShareSina)
    ImageView ivShareSina;
    @BindView(R.id.ivShareWeChat)
    ImageView ivShareWeChat;
    @BindView(R.id.ivShareQQ)
    ImageView ivShareQQ;
    @BindView(R.id.exgv_appraisal_pic)
    ExpandGridView exgv_appraisal_pic;
    @BindView(R.id.etTitle)
    EditText etTitle;
    //    @BindView(R.id.et_content)
//    EditText etContent;
//    @BindView(R.id.tvInputWordLimit)
//    TextView tvInputWordLimit;
    @BindView(R.id.btnFlag)
    TextView btnFlag;
    @BindView(R.id.tagCategoryListView)
    TagListView tagCategoryListView;
    @BindView(R.id.tagListView)
    TagListView tagListView;
//    @BindView(R.id.spCategory)
//    Spinner spCategory;

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
    @BindView(R.id.rlVoicePanel)
    RelativeLayout rlVoicePanel;
    private List<Tag> mTags = new ArrayList<Tag>();
    private List<Tag> mNewTagList = new ArrayList<Tag>();
    private ArrayList<String> stList;// 用户上传图片的URL集合
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
    private PublicVoiceAdapter mPublicVoiceAdapter;
    List<TeacherZoneDynamicsInfo> listTeacherZoneDynamicsInfo = new ArrayList<>();
    private boolean isHaveRecordingPermissions = false;
    private Tag programTag;
    private UpLoadImageAdapter mUpLoadImageAdapter;
    int j = 0;
    int i = 0;
    private int showNumberInline = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_voice);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
        mMediaPlayUtil = MediaPlayUtil.getInstance();
        initSoundData();
        getImageList(null);// 占位符
        showNumberInline=getResources().getInteger(R.integer.show_line_image_number);
        mUpLoadImageAdapter = new UpLoadImageAdapter(stList, this, 1,showNumberInline);
        exgv_appraisal_pic.setAdapter(mUpLoadImageAdapter);
    }

    @OnClick({R.id.btnFlag})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFlag://选择标签
                PickTagsActivity.intoNewActivity(this, "", programTag, mTags, mNewTagList, REQUSET_TAG_CODE);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

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
                if (!isSubmiting) {
                    isSubmiting = true;

                    LoadingProgressDialog.startProgressDialog("正在发布", PublishVoiceActivity.this);
                    if (!dataVerify()) {
                        isSubmiting = false;
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
            }

            @Override
            public void titleClick() {

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

    private void press() {
        String path = stList.get(i).replace("file://", "");
        File file = new File(path);
        if (file != null && file.exists() && file.length() < 102400) {//100k以内的图片不做压缩处理
            thumb += Base64.encodeToString(FileUtils.fileToBytes(file), Base64.DEFAULT) + "####";
            i++;
            if (i == j) {
                submitData();
            } else {
                press();
            }
        }else {
            Luban.get(PublishVoiceActivity.this)
                    .load(new File(path))                     //传人要压缩的图片
                    .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
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
    }


    /**
     * 录音存放路径
     */
    public void initSoundData() {
        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.FULL_WAKE_LOCK, "time");
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

    private void dataBind() {
        tagListView.setTags(mTags);

        tagCategoryListView.removeAllViews();
        if (programTag != null) {
            programTag.setChecked(true);
            tagCategoryListView.addTag(programTag);
        }
    }


    private void dataBindVoice() {
        mPublicVoiceAdapter = new PublicVoiceAdapter(this, listTeacherZoneDynamicsInfo);
        elvFriendsPostGroup.setAdapter(mPublicVoiceAdapter);
        mPublicVoiceAdapter.setIDeleteItemClickListener(new ISwipeDeleteItemClickListening() {
            @Override
            public void delete(SwipeLayout swipeLayout, int position, Object object) {
                listTeacherZoneDynamicsInfo.remove(object);
                deleteSoundFileUnSend();
                initVoice();
                voice = "";
                swipeLayout.close(true);
                mPublicVoiceAdapter.notifyDataSetChanged();
            }
        });
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
                        programTag = (Tag) data.getExtras().getSerializable(PROGRAM);
                        dataBind();
                    }
                }
                break;
            case Constant.SELECT_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    if (data.getExtras().getStringArrayList(SelectPictureActivity.INTENT_SELECTED_PICTURE) != null) {
                        stList = getImageList(data.getExtras().getStringArrayList(SelectPictureActivity.INTENT_SELECTED_PICTURE));
                        mUpLoadImageAdapter = new UpLoadImageAdapter(stList, PublishVoiceActivity.this, 1,showNumberInline);
                        exgv_appraisal_pic.setAdapter(mUpLoadImageAdapter);
                    }
                }
                break;
            case Constant.PICTURE_PREVIEW:// 图片预览
                if (resultCode == RESULT_OK && data != null) {
                    if (data.getExtras().getStringArrayList(Constant.PICTURE_PREVIEW_INDEX_KEY) != null) {
                        stList = getImageList(data.getExtras().getStringArrayList(Constant.PICTURE_PREVIEW_INDEX_KEY));
                        mUpLoadImageAdapter = new UpLoadImageAdapter(stList, PublishVoiceActivity.this, 1,showNumberInline);
                        exgv_appraisal_pic.setAdapter(mUpLoadImageAdapter);
                    }
                }
                break;
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

//        if (TextUtils.isEmpty(thumb)) {
//            flag = false;
//            tvError.setText("请选择缩略图");
//            ToastUtils.showToast(this, "请选择缩略图");
//        } else {
//            tvError.setText("");
//        }
        if (TextUtils.isEmpty(title)) {
            flag = false;
            etTitle.setError("标题不能为空");
        }
        if (programTag != null) {
            cat_id = String.valueOf(programTag.getId());
        }
        if (TextUtils.isEmpty(cat_id)) {
            flag = false;
            ToastUtils.showToast(this, "请选择栏目");
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
        map.put("voice_time", String.valueOf(mTime));  // 录音时间
        OKHttpUtils.getInstance().post(this, Api.add_news, ParamUtil.getParam(map), Action.add_news_vioce);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case add_news_vioce:
                LoadingProgressDialog.stopProgressDialog();
                isSubmiting = false;
                if (event.getCode() == Constant.SUCCEED) {
                    ToastUtils.showToast(this, "发布语音直播成功");
                    deleteSoundFileUnSend();
                    initVoice();
                    finish();
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "发布语音直播失败"));
                }
                break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mMediaPlayUtil.isPlaying()) {
            mMediaPlayUtil.stop();
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                takeCapture();
            } else {
                new PermissionDialog(this)
                        .show(R.string.permission_tip_dialog_message_camera);
            }
        } else if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                new PermissionDialog(this)
                        .show(R.string.permission_tip_dialog_message_record_audio);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 录音的触摸监听
     */
    class VoiceTouch implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!PermissionManager.checkPermission(PublishVoiceActivity.this,
                            Manifest.permission.RECORD_AUDIO)) {
                        isHaveRecordingPermissions = false;
                        ActivityCompat.requestPermissions(PublishVoiceActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);

                    } else {
                        isHaveRecordingPermissions = true;
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
                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        mRecorder.setOutputFile(mSoundDataFilePath);
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
                            rlVoicePanel.setBackgroundColor(getResources().getColor(R.color.colorRedVoice));
                        } catch (Exception e) {
                            if (wakeLock.isHeld()) {
                                wakeLock.release();
                            }
                            Log.i("recoder", "prepare() failed-Exception-" + e.toString());
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (!isHaveRecordingPermissions) {
                        return true;
                    }
                    rlVoicePanel.setBackgroundColor(getResources().getColor(R.color.colorBlue50));
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
                    if (!isHaveRecordingPermissions) {
                        return true;
                    }
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
                    if (!isHaveRecordingPermissions) {
                        return true;
                    }
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
        if (isHaveRecordingPermissions && wakeLock.isHeld()) {
            wakeLock.release();
        }
        if (isHaveRecordingPermissions && mRecorder != null) {
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
        rlVoicePanel.setBackgroundColor(getResources().getColor(R.color.colorBlue50));
        listTeacherZoneDynamicsInfo.clear();
        dataBindVoice();
    }

    /**
     * 结束录音
     */
    public void stopRecord() {
        if (!isHaveRecordingPermissions) {
            return;
        }
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
            TeacherZoneDynamicsInfo mTeacherZoneDynamicsInfo = new TeacherZoneDynamicsInfo();
            mTeacherZoneDynamicsInfo.setType(Constans.TYPE_AUDIO);
            mTeacherZoneDynamicsInfo.setNews_voice_url(mSoundDataFilePath);
            mTeacherZoneDynamicsInfo.setNews_voice_time(String.valueOf(mTime));
            listTeacherZoneDynamicsInfo.clear();
            listTeacherZoneDynamicsInfo.add(mTeacherZoneDynamicsInfo);
            dataBindVoice();
        }
        //mRecorder.setOnErrorListener(null);
        try {
            if (mRecorder != null) {
//            mRecorder.reset();
                mRecorder.stop();
                mRecorder.release();
            }
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
