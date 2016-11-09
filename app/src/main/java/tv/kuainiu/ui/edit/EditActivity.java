package tv.kuainiu.ui.edit;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import tv.kuainiu.R;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.activity.SelectPictureActivity;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.widget.editview.EditData;
import tv.kuainiu.widget.editview.HTMLDecoder;
import tv.kuainiu.widget.editview.KnifeText;
import tv.kuainiu.widget.editview.RichTextEditor;


/**
 * Created by sirius on 2016/11/7.
 */

public class EditActivity extends BaseActivity {
    private final int REQUEST_CODE_PICK_IMAGE = 200;
    @BindView(R.id.richText)
    RichTextEditor richText;
    @BindView(R.id.bold)
    ImageButton bold;
    @BindView(R.id.italic)
    ImageButton italic;
    @BindView(R.id.underline)
    ImageButton underline;
    @BindView(R.id.strikethrough)
    ImageButton strikethrough;
    @BindView(R.id.quote)
    ImageButton quote;
    @BindView(R.id.link)
    ImageButton link;
    @BindView(R.id.tools)
    HorizontalScrollView tools;
    @BindView(R.id.svScrollView)
    ScrollView svScrollView;
    @BindView(R.id.insert_image)
    ImageButton insertImage;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.llColor)
    LinearLayout toolsImage;
    @BindView(R.id.textColor)
    ImageButton textColor;
    @BindView(R.id.clear)
    ImageButton clear;
    public static String result = "";
    @BindView(R.id.ivBlack)
    ImageView ivBlack;
    @BindView(R.id.ivGray)
    ImageView ivGray;
    @BindView(R.id.ivRed)
    ImageView ivRed;
    @BindView(R.id.ivYellow)
    ImageView ivYellow;
    @BindView(R.id.ivGreen)
    ImageView ivGreen;
    @BindView(R.id.ivBlue)
    ImageView ivBlue;
    @BindView(R.id.ivPurple)
    ImageView ivPurple;
    private LinearLayout.LayoutParams bigLayoutParams;
    private LinearLayout.LayoutParams nomarLayoutParams;
    public View currentColorView = null;
    private static int currentColorId = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        setImageViewShow(currentColorId);
    }

    @OnClick({R.id.ivUndo, R.id.ivRedo, R.id.ivFont, R.id.btnSave, R.id.bold, R.id.italic, R.id.underline, R.id.strikethrough, R.id.quote, R.id.link, R.id.clear, R.id.textColor, R.id.insert_image, R.id.rlBlack, R.id.rlGray, R.id.rlRed, R.id.rlYellow, R.id.rlGreen, R.id.rlBlue, R.id.rlPurple})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivUndo:
                richText.getLastFocusEdit().undo();
                break;
            case R.id.ivRedo:
                richText.getLastFocusEdit().redo();
                break;
            case R.id.ivFont:
                tools.setVisibility(tools.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                toolsImage.setVisibility(View.GONE);
                break;
            case R.id.btnSave:
                List<EditData> dataList = richText.buildEditData();
                StringBuffer stringBuffer = new StringBuffer("");
                if (dataList.size() > 0) {
                    for (int i = 0; i < dataList.size(); i++) {
                        if (dataList.get(i).bitmap != null) {
                            stringBuffer.append("<img_kuainiu>" + Base64.encodeToString(toImageForBin(dataList.get(i).bitmap), Base64.DEFAULT) + "</img_kuainiu>");
                        } else {
                            stringBuffer.append(HTMLDecoder.decode(dataList.get(i).inputStr));

                        }
                    }
                }
                result = stringBuffer.toString().replace("<blockquote>", "<blockquote style=\"PADDING: 5px; MARGIN-LEFT: 5px; BORDER-LEFT: #BDBDBD 4px solid; MARGIN-RIGHT: 0px;background-color:#F1F1F1\">");
                LogUtils.e(TAG, result);
                break;
            case R.id.bold:
                bold.setSelected(!bold.isSelected());
                richText.getLastFocusEdit().bold(bold.isSelected());
                break;
            case R.id.italic:
                richText.getLastFocusEdit().italic(!richText.getLastFocusEdit().contains(KnifeText.FORMAT_ITALIC));
                break;
            case R.id.underline:
                richText.getLastFocusEdit().underline(!richText.getLastFocusEdit().contains(KnifeText.FORMAT_UNDERLINED));
                break;
            case R.id.strikethrough:
                richText.getLastFocusEdit().strikethrough(!richText.getLastFocusEdit().contains(KnifeText.FORMAT_STRIKETHROUGH));
                break;
            case R.id.quote:
                richText.getLastFocusEdit().quote(!richText.getLastFocusEdit().contains(KnifeText.FORMAT_QUOTE));
                break;
            case R.id.link:
                showLinkDialog();
                break;
            case R.id.insert_image:
                Intent intent = new Intent(this, SelectPictureActivity.class);
                intent.putExtra(SelectPictureActivity.OnlyOnePic, true);
                startActivityForResult(intent, Constant.SELECT_PICTURE);
                break;
            case R.id.clear:
                richText.getLastFocusEdit().clearFormats();
                break;
            case R.id.textColor:
                toolsImage.setVisibility(toolsImage.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.rlBlack:
                ctrlTextColor(R.color.edit_black);
                break;
            case R.id.rlGray:
                ctrlTextColor(R.color.edit_gray);
                break;
            case R.id.rlRed:
                ctrlTextColor(R.color.edit_red);
                break;
            case R.id.rlYellow:
                ctrlTextColor(R.color.edit_yellow);
                break;
            case R.id.rlGreen:
                ctrlTextColor(R.color.edit_green);
                break;
            case R.id.rlBlue:
                ctrlTextColor(R.color.edit_blue);
                break;
            case R.id.rlPurple:
                ctrlTextColor(R.color.edit_purple);
                break;
        }
    }

    private void ctrlTextColor(int colorId) {
        if (colorId > 0) {
            richText.getLastFocusEdit().color(getResources().getColor(colorId));
            setImageViewShow(colorId);
        }
    }

    private void setImageViewShow(int colorId) {
        View v;
        switch (colorId) {
            case R.color.edit_gray:
                v = ivGray;
                break;
            case R.color.edit_red:
                v = ivRed;
                break;
            case R.color.edit_yellow:
                v = ivYellow;
                break;
            case R.color.edit_green:
                v = ivGreen;
                break;
            case R.color.edit_blue:
                v = ivBlue;
                break;
            case R.color.edit_purple:
                v = ivPurple;
                break;
            default:
                v = ivBlack;
                break;
        }
        int padding = getResources().getDimensionPixelSize(R.dimen.richtextedit_padding_colorNormal);
        if (currentColorView != null) {
            currentColorView.setPadding(padding, padding, padding, padding);
            currentColorView.invalidate();
        }
        padding = getResources().getDimensionPixelSize(R.dimen.richtextedit_padding_colorBig);
        currentColorView = v;
        if (currentColorView != null) {
            currentColorId = colorId;
            currentColorView.setPadding(padding, padding, padding, padding);
            currentColorView.invalidate();

        }
    }

    private void showLinkDialog() {
        final int start = richText.getLastFocusEdit().getSelectionStart();
        final int end = richText.getLastFocusEdit().getSelectionEnd();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        View view = getLayoutInflater().inflate(R.layout.dialog_link, null, false);
        final EditText editText = (EditText) view.findViewById(R.id.edit);
        builder.setView(view);
        builder.setTitle("");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String link = editText.getText().toString().trim();
                if (TextUtils.isEmpty(link)) {
                    return;
                }
                // When KnifeText lose focus, use this method
                richText.getLastFocusEdit().link(link, start, end);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // DO NOTHING HERE
            }
        });

        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Constant.SELECT_PICTURE && data != null) {
            if (data.getExtras().getStringArrayList(SelectPictureActivity.INTENT_SELECTED_PICTURE) != null) {
                ArrayList<String> stList = data.getExtras().getStringArrayList(SelectPictureActivity.INTENT_SELECTED_PICTURE);
                if (stList != null && stList.size() > 0) {
                    for (int i = 0; i < stList.size(); i++) {
                        LogUtils.e(TAG, stList.get(i));
                        if (!Constant.UPLOADIMAGE.equals(stList.get(i))) {
                            String uri = stList.get(i).replace("file://", "");
                            LogUtils.e(TAG, "uri=" + uri);
                            compress(uri);

                        }
                    }
                }

            }
        }

    }

    private void compress(String uri) {
        if (TextUtils.isEmpty(uri)) {
            return;
        }
        Luban.get(EditActivity.this)
                .load(new File(uri))                     //传人要压缩的图片
                .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                .setCompressListener(new OnCompressListener() { //设置回调

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        LogUtils.e(TAG, "file=" + file.getPath());
                        try {
                            richText.insertImage(file.getPath());
                        } catch (Exception e) {
                            LogUtils.e(TAG, e.getMessage(), e);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        // 当压缩过去出现问题时调用
                        LogUtils.e(TAG, "图片压缩错误", e);
                    }
                }).launch();    //启动压缩
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
    }
}
