package tv.kuainiu.ui.publishing.share;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import tv.kuainiu.R;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.activity.SelectPictureActivity;
import tv.kuainiu.ui.adapter.UpLoadImageAdapter;
import tv.kuainiu.utils.FileUtils;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.widget.ExpandGridView;
import tv.kuainiu.widget.TitleBarView;
import tv.kuainiu.widget.tagview.Tag;

public class PublishShareActivity extends BaseActivity {

    public static final String PLAT_FROM = "PLAT_FROM";
    public static final String DYNAMICS_DESC = "dynamics_desc";
    public static final String DYNAMICS_IMAGE_PATH = "dynamics_image_path";
    @BindView(R.id.tbv_title)
    TitleBarView tbvTitle;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.exgv_appraisal_pic)
    ExpandGridView exgv_appraisal_pic;

    private List<Tag> mTags = new ArrayList<Tag>();
    private List<Tag> mNewTagList = new ArrayList<Tag>();
    private ArrayList<String> stList;// 用户上传图片的URL集合

    private String thumb = "";//
    private boolean isSubmiting = false;
    private UpLoadImageAdapter mUpLoadImageAdapter;
    private int showNumberInline = 0;
    private int platFromId = 0;
    private String dynamics_desc = "";    //同步动态时必传     动态描述文字
    private String dynamics_image_path = "";    //同步微博时必传 微博图片

    public static void intoNewActivity(BaseActivity mContext, String dynamics_desc, String dynamics_image_path, int requestCode) {
        Intent intentPublishShareActivity = new Intent(mContext, PublishShareActivity.class);
        intentPublishShareActivity.putExtra(DYNAMICS_DESC, dynamics_desc);
        intentPublishShareActivity.putExtra(DYNAMICS_IMAGE_PATH, dynamics_image_path);
        mContext.startActivityForResult(intentPublishShareActivity, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_share);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        dynamics_desc = getIntent().getStringExtra(DYNAMICS_DESC);
        dynamics_image_path = getIntent().getStringExtra(DYNAMICS_IMAGE_PATH);
        initView();
        List<String> list = new ArrayList<>();
        if (!TextUtils.isEmpty(dynamics_image_path)) {
            list.add(dynamics_image_path);
        }
        getImageList(list);// 占位符
        showNumberInline = getResources().getInteger(R.integer.show_line_image_number);
        mUpLoadImageAdapter = new UpLoadImageAdapter(stList, this, 1, showNumberInline, 1);
        exgv_appraisal_pic.setAdapter(mUpLoadImageAdapter);
    }

    int j = 0;
    int i = 0;

    private void initView() {
        etContent.setText(dynamics_desc);
        etContent.setSelection(etContent.length(), etContent.length());
        tbvTitle.setOnClickListening(new TitleBarView.OnClickListening() {
            @Override
            public void leftClick() {
                finishActivty();
            }

            @Override
            public void rightClick() {

            }

            @Override
            public void titleClick() {

            }
        });
    }

    private void press() {
        String path = stList.get(i).replace("file://", "");
        LogUtils.e(TAG, "path=" + path);
        Luban.get(PublishShareActivity.this)
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

    /**
     * 数据验证
     */
    private boolean dataVerify() {
        boolean flag = true;
        dynamics_desc = "";//必传     文字内容
        dynamics_desc = etContent.getText().toString();
        if (TextUtils.isEmpty(dynamics_desc)) {
            flag = false;
            etContent.setError("同步内容不能为空");
        } else {
            etContent.setError(null);
        }
        return flag;
    }

    private void submitData() {
    }

    @OnClick({})
    public void onClick(View v) {
        switch (v.getId()) {

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
        if (1 > stList.size()) {
            stList.add(Constant.UPLOADIMAGE);
        }

        return stList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.SELECT_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    if (data.getExtras().getStringArrayList(SelectPictureActivity.INTENT_SELECTED_PICTURE) != null) {
                        stList = getImageList(data.getExtras().getStringArrayList(SelectPictureActivity.INTENT_SELECTED_PICTURE));
                        mUpLoadImageAdapter = new UpLoadImageAdapter(stList, this, 1, showNumberInline, 1);
                        exgv_appraisal_pic.setAdapter(mUpLoadImageAdapter);
                    }
                }
                break;
            case Constant.PICTURE_PREVIEW:// 图片预览
                if (resultCode == RESULT_OK && data != null) {
                    if (data.getExtras().getStringArrayList(Constant.PICTURE_PREVIEW_INDEX_KEY) != null) {
                        stList = getImageList(data.getExtras().getStringArrayList(Constant.PICTURE_PREVIEW_INDEX_KEY));
                        mUpLoadImageAdapter = new UpLoadImageAdapter(stList, this, 1, showNumberInline, 1);
                        exgv_appraisal_pic.setAdapter(mUpLoadImageAdapter);
                    }
                }
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
//            case add_dynamics:
//                LoadingProgressDialog.stopProgressDialog();
//                isSubmiting = false;
//                if (event.getCode() == Constant.SUCCEED) {
//                    ToastUtils.showToast(this, "发布动态成功");
//                    finish();
//                } else {
//                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "发布动态直播失败"));
//                }
//                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            finishActivty();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void finishActivty() {
        if(dataVerify()) {
            Intent intent = new Intent();
            intent.putExtra(DYNAMICS_DESC, dynamics_desc);
            intent.putExtra(DYNAMICS_IMAGE_PATH, dynamics_image_path);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
