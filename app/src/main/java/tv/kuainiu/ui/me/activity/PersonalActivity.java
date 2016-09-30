package tv.kuainiu.ui.me.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.kuainiu.IGXApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.NetUtils;
import tv.kuainiu.utils.PermissionManager;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.TakePhotoActivity;
import tv.kuainiu.widget.PermissionDialog;
import tv.kuainiu.widget.dialog.SelectPicPopupWindow;


/**
 * 个人资料
 */
public class PersonalActivity extends BaseActivity implements View.OnClickListener {
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";
    /**
     * 请求相机权限
     */
    private static final int CAMERA_REQUEST_CODE = 110;
    /**
     * 相册选图
     */
    private static final int REQUEST_PICK = 0;
    /**
     * 拍照
     */
    private static final int REQUEST_TAKE = 1;
    /**
     * 图片裁切标记
     */
    private static final int REQUEST_CUTTING = 2;

    @BindView(R.id.rl_personalPerfect) RelativeLayout mRlPerfect;
    @BindView(R.id.rl_personalLogout) RelativeLayout mRlLogout;
    @BindView(R.id.rl_personalAccount) RelativeLayout mRlAccount;
    @BindView(R.id.rl_personalEmail) RelativeLayout mRlBindEmail;
    @BindView(R.id.rl_personalIdentity) RelativeLayout mRlIdentity;
    @BindView(R.id.rl_personalPassword) RelativeLayout mRlPassword;
    @BindView(R.id.tv_personalPhoneNumberTop) TextView mTvPhoneNumberTop;
    @BindView(R.id.tv_personalPhoneNumber2) TextView mTvPhoneNumber2;
    @BindView(R.id.tv_personal_idno_status) TextView mTvIdentityStatus;
    @BindView(R.id.tv_personalEmail) TextView mTvEmail;
    @BindView(R.id.tv_personal_identity_Card) TextView mTvIdentityCard;
    @BindView(R.id.cimg_photo) CircleImageView mPhoto;

    private static ProgressDialog pd;             // 等待进度圈
    private SelectPicPopupWindow menuWindow;      // 头像弹出框
    private AlertDialog logoutDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        bindDataForView();
        initListener();
    }


    private void bindDataForView() {
        if (!IGXApplication.isLogin()) {
            return;
        }
        User user = IGXApplication.getUser();
        initAvatar(user.getAvatar());
        mTvPhoneNumberTop.setText(StringUtils.getX(user.getPhone()));
        mTvPhoneNumber2.setText(StringUtils.getX(user.getPhone()));
        mTvEmail.setText(StringUtils.getX(user.getEmail()));
        mTvIdentityCard.setText(StringUtils.getX(user.getIdno()));
        if (TextUtils.isEmpty(user.getIdno())) {
            mTvIdentityStatus.setText(getString(R.string.uncertification));
        } else {
            mTvIdentityStatus.setText(getString(R.string.certificationed));
        }

    }

    private void initAvatar(String imagePath) {
        if (TextUtils.isEmpty(imagePath) || "".equals(imagePath)) {
            ImageDisplayUtil.displayImage(PersonalActivity.this, mPhoto, R.mipmap.default_avatar);
        } else {
            ImageDisplayUtil.displayImage(PersonalActivity.this, mPhoto, StringUtils.replaceNullToEmpty(imagePath), R.mipmap.default_avatar);
        }
    }

    protected void initListener() {
        mRlPerfect.setOnClickListener(this);
        mRlAccount.setOnClickListener(this);
        mRlLogout.setOnClickListener(this);
        mRlBindEmail.setOnClickListener(this);
        mRlIdentity.setOnClickListener(this);
        mRlPassword.setOnClickListener(this);
        mPhoto.setOnClickListener(this);
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_personalPerfect:
                Intent perfect = new Intent(PersonalActivity.this, PerfectPersonalActivity.class);
                startActivity(perfect);
                break;
            case R.id.rl_personalAccount:
                Intent account = new Intent(PersonalActivity.this, UpdatePhone1Activity.class);
                startActivity(account);
                break;
            case R.id.rl_personalEmail:
                Intent email = new Intent(PersonalActivity.this, BindEmailActivity.class);
                startActivity(email);
                break;
            case R.id.rl_personalPassword:
                Intent password = new Intent(PersonalActivity.this, AlterPasswordActivity.class);
                startActivity(password);
                break;
            case R.id.rl_personalIdentity:
                Intent identity = new Intent(PersonalActivity.this, IdentityActivity.class);
                startActivity(identity);
                break;
            case R.id.rl_personalLogout:
                if (!NetUtils.isOnline(getApplicationContext())) {
                    return;
                }
                logout();
                break;
            case R.id.cimg_photo:
                menuWindow = new SelectPicPopupWindow(this, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.mainLayout),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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


    private void logout() {
        logoutDialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("您确定要退出登录吗?")
                .setCancelable(false)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logoutDialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OKHttpUtils.getInstance().post(PersonalActivity.this, Api.LOGOUT, ParamUtil.getParam(null), Action.logout);
                        logoutDialog.dismiss();
                    }
                })
                .create();
        logoutDialog.show();
    }


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


    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.takePhotoBtn:
                    if (!PermissionManager.checkPermission(PersonalActivity.this,
                            Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions(PersonalActivity.this,
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        switch (requestCode) {
            case REQUEST_PICK:// 直接从相册获取
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case REQUEST_TAKE:// 调用相机拍照
                File temp = new File(data.getStringExtra(TakePhotoActivity.IMAGE_PATH));
                startPhotoZoom(Uri.fromFile(temp));

                break;
            case REQUEST_CUTTING:// 取得裁剪后的图片
                setPicToView(data);
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);

        if (Build.VERSION.SDK_INT >= 23) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(getExternalCacheDir(), IMAGE_FILE_NAME)));
            intent.putExtra("return-data", false);
        } else {
            intent.putExtra("return-data", true);
        }
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
            Bitmap bitmap = extras.getParcelable("data");
            if (null == bitmap) return;
            Drawable drawable = new BitmapDrawable(null, bitmap);
            mPhoto.setImageDrawable(drawable);

            // 新线程后台上传服务端
            pd = ProgressDialog.show(this, null, "正在上传图片，请稍候...");
            uploadImage(bitmap);

        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                final File file = new File(getExternalCacheDir(), IMAGE_FILE_NAME);
                DebugUtils.dd("file exists : " + file.exists());
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (null == bitmap) return;
                // 新线程后台上传服务端
                pd = ProgressDialog.show(this, null, "正在上传图片，请稍候...");
                uploadImage(bitmap);
            }
        }
    }


    private byte[] toImageForBin(Bitmap photo) {

        int size = photo.getWidth() * photo.getHeight() * 4;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
        photo.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] bytes = baos.toByteArray();
        photo.recycle();
        return bytes;
    }

    private void uploadImage(final Bitmap photo) {
        new Thread() {
            @Override public void run() {
                Map<String, String> map = new HashMap<>();
                map.put("avatar", Base64.encodeToString(toImageForBin(photo), Base64.DEFAULT));
                OKHttpUtils.getInstance().post(PersonalActivity.this, Api.UPDATE_AVATAR, ParamUtil.getParam(map), Action.update_avatar);
            }
        }.start();
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // EventBus call backs  **
    // -------------------------------------------------------------------------------------------------------------------------------
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HttpEvent event) {
        switch (event.getAction()) {
            case logout:
                if (event.getCode() == Constant.SUCCEED) {
                    DebugUtils.showToast(PersonalActivity.this, "注销成功");
                    IGXApplication.setUser(null);
                    finish();
                } else {
                    DebugUtils.showToast(PersonalActivity.this, event.getMsg());
                }
                break;
            case update_avatar:
                if (pd != null) {
                    pd.dismiss();
                }
                if (event.getCode() == Constant.SUCCEED) {
                    try {
                        JSONObject jsonObject = new JSONObject(event.getData().optString("data"));
                        String imageUrl = jsonObject.optString("avatar");
                        initAvatar(imageUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
