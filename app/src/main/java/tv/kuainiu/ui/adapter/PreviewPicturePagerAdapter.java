package tv.kuainiu.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tv.kuainiu.R;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.utils.ImageDisplayUtil;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

import static android.app.Activity.RESULT_OK;

public class PreviewPicturePagerAdapter extends PagerAdapter {
    private ArrayList<String> imageList;
    private Context context;

    public PreviewPicturePagerAdapter(ArrayList<String> imageList,
                                      Context context) {
        super();
        this.imageList = imageList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageList == null ? 0 : imageList.size();
    }

    /**
     * 是否由对象生成界面
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    /**
     * 从container中移出当前View
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * return一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager(container)中
     */
    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.common_picture_preview_item, container, false);
        PhotoView PhotoView = (PhotoView) view.findViewById(R.id.pv_photoView);
        PhotoView.setOnPhotoTapListener(new OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View view, float x, float y) {
                Intent intent = new Intent();
                intent.putExtra(Constant.PICTURE_PREVIEW_INDEX_KEY, imageList);
                ((Activity) context).setResult(RESULT_OK, intent);
                ((Activity) context).finish();
            }
        });
        String url = imageList.get(position);
        ImageDisplayUtil.displayImage(context, PhotoView, url);
        container.addView(view);
        return view;
    }
}
