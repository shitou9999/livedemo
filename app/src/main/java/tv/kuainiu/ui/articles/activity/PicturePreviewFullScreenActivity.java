package tv.kuainiu.ui.articles.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

import com.synnapps.carouselview.CirclePageIndicator;

import java.util.ArrayList;

import tv.kuainiu.R;
import tv.kuainiu.ui.articles.adapter.PreviewPicturePagerAdapter;
import tv.kuainiu.widget.ViewPagerFixed;


/**
 * @ClassName PicturePreviewFullScreenActivity
 * @Description 图片预览activity
 */
public class PicturePreviewFullScreenActivity extends FragmentActivity {

    public static String PICTURE_PREVIEW_KEY = "PicturePreviewFullScreenActivity_PICTURE_PREVIEW_KEY";
    public static String PICTURE_PREVIEW_INDEX_KEY = "PicturePreviewFullScreenActivity_PICTURE_PREVIEW_INDEX_KEY";
    private PreviewPicturePagerAdapter vpAdapter;
    private ViewPagerFixed vp;
    private CirclePageIndicator cpi_CirclePageIndicator;
    public ArrayList<String> imageList;
    private int index = 0;

    @Override
    protected void onCreate(Bundle arg0) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(arg0);
        setContentView(R.layout.common_picture_preview_full_screen);
        imageList = new ArrayList<String>();
        imageList = getIntent().getExtras().getStringArrayList(
                PICTURE_PREVIEW_KEY);
        index = getIntent().getExtras().getInt(
                PICTURE_PREVIEW_INDEX_KEY);

        vp = (ViewPagerFixed) findViewById(R.id.viewpager);
        // 初始化Adapter
        vpAdapter = new PreviewPicturePagerAdapter(imageList, this);
        vp.setAdapter(vpAdapter);
        if (null != imageList && imageList.size() > 1) {
            cpi_CirclePageIndicator = (CirclePageIndicator) findViewById(R.id.cpi_circle_page_indicator);
            cpi_CirclePageIndicator.setViewPager(vp);
        }
        vp.setCurrentItem(index, false);

    }

}
