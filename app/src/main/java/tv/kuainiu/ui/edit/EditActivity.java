package tv.kuainiu.ui.edit;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.kuainiu.R;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.activity.SelectPictureActivity;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.ToastUtils;
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
    @BindView(R.id.toolsImage)
    HorizontalScrollView toolsImage;
    @BindView(R.id.textColor)
    ImageButton textColor;
    @BindView(R.id.clear)
    ImageButton clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        svScrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richText.showKeyBoard();
                ToastUtils.showToast(EditActivity.this, "ssd");
            }
        });
    }

    @OnClick({R.id.btnSave, R.id.bold, R.id.italic, R.id.underline, R.id.strikethrough, R.id.quote, R.id.link, R.id.clear, R.id.textColor, R.id.insert_image, R.id.ivBlack, R.id.ivGray, R.id.ivRed, R.id.ivYellow, R.id.ivGreen, R.id.ivBlue, R.id.ivPurple})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                List<EditData> dataList = richText.buildEditData();
                StringBuffer stringBuffer = new StringBuffer("<html><head><meta charset=\"utf-8\"></head><body color =\"#636363\">");
                if (dataList.size() > 0) {
                    for (int i = 0; i < dataList.size(); i++) {
                        if (dataList.get(i).bitmap != null) {
                            stringBuffer.append("<img  width=\"100%\" src=\"" + dataList.get(i).imagePath + "\"/>");
                        } else {
                            stringBuffer.append(HTMLDecoder.decode(dataList.get(i).inputStr));

                        }
                    }
                    stringBuffer.append("</body></html>");
                }
                LogUtils.e(TAG, stringBuffer.toString());
                break;
            case R.id.bold:
                richText.getLastFocusEdit().bold(!richText.getLastFocusEdit().contains(KnifeText.FORMAT_BOLD));
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
                ctrlTextColor();
                break;
            case R.id.ivBlack:
                richText.getLastFocusEdit().color(getResources().getColor(R.color.edit_black), true);
                ctrlTextColor();
                break;
            case R.id.ivGray:
                richText.getLastFocusEdit().color(getResources().getColor(R.color.edit_gray), true);
                ctrlTextColor();
                break;
            case R.id.ivRed:
                richText.getLastFocusEdit().color(getResources().getColor(R.color.edit_red), true);
                ctrlTextColor();
                break;
            case R.id.ivYellow:
                richText.getLastFocusEdit().color(getResources().getColor(R.color.edit_yellow), true);
                ctrlTextColor();
                break;
            case R.id.ivGreen:
                richText.getLastFocusEdit().color(getResources().getColor(R.color.edit_green), true);
                ctrlTextColor();
                break;
            case R.id.ivBlue:
                richText.getLastFocusEdit().color(getResources().getColor(R.color.edit_blue), true);
                ctrlTextColor();
                break;
            case R.id.ivPurple:
                richText.getLastFocusEdit().color(getResources().getColor(R.color.edit_purple), true);
                ctrlTextColor();
                break;
        }
    }

    private void ctrlTextColor() {
        toolsImage.setVisibility(toolsImage.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
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
                            try {
                                richText.insertImage(uri);
                            } catch (Exception e) {
                                LogUtils.e(TAG, e.getMessage(), e);
                            }
                        }
                    }
                }

            }
        }
    }

}
