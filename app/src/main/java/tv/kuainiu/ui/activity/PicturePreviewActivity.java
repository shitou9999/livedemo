package tv.kuainiu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import tv.kuainiu.R;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.fragment.PicturePreviewFragement;


/**
 * @ClassName PicturePreviewActivity
 * @Description 图片预览activity
 */
public class PicturePreviewActivity extends BaseActivity {

    private SparseArray<Fragment> saFragemnet = new SparseArray<Fragment>();
    private TextView tv_picture_preview_index;
    public ArrayList<String> imageList;

    public int index = 0;
    public int list_item_index = 0;
    public int activty_index = 0;
    public int count = 0;
    private boolean onlyPreview = false;
    public static final String ONLY_PREVIEW = "only_preview";

    private ImageView iv_picture_preview_delete;
    private RelativeLayout rl_title_bar_left;

    @Override
    protected void onCreate(Bundle arg0) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(arg0);
        setContentView(R.layout.common_picture_preview_main);
        initView();
        initData();
        dataBind(activty_index);
    }

    private void dataBind(int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_picture_preview, saFragemnet.get(index));
        ft.commit();
    }

    /**
     * 数据初始化
     */
    private void initData() {
        PicturePreviewFragement picturePreviewFragement = new PicturePreviewFragement();

        saFragemnet.append(0, picturePreviewFragement);
        imageList = new ArrayList<String>();
        imageList = getIntent().getExtras().getStringArrayList(
                Constant.PICTURE_PREVIEW_KEY);
        index = getIntent().getExtras().getInt(
                Constant.PICTURE_PREVIEW_INDEX_KEY);
        list_item_index = getIntent().getExtras().getInt(
                SelectPictureActivity.INTENT_SELECTED_PICTURE_INDEX);
        count = imageList == null ? 0 : imageList.size();
        onlyPreview = getIntent().getExtras().getBoolean(ONLY_PREVIEW, false);
        iv_picture_preview_delete.setVisibility(onlyPreview ? View.INVISIBLE
                : View.VISIBLE);
    }

    private void initView() {
        rl_title_bar_left = (RelativeLayout) findViewById(R.id.rl_title_bar_left);
        iv_picture_preview_delete = (ImageView) findViewById(R.id.iv_picture_preview_delete);
        tv_picture_preview_index = (TextView) findViewById(R.id.tv_picture_preview_index);
        rl_title_bar_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!onlyPreview) {
                    Intent intent = new Intent();
                    intent.putExtra(Constant.PICTURE_PREVIEW_INDEX_KEY, imageList);
                    setResult(RESULT_OK, intent);
                }
                PicturePreviewActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(!onlyPreview) {
            Intent intent = new Intent();
            intent.putExtra(Constant.PICTURE_PREVIEW_INDEX_KEY, imageList);
            setResult(RESULT_OK, intent);
        }
        this.finish();
    }

}
