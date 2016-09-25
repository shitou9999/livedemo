package tv.kuainiu.ui.articles.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

import tv.kuainiu.R;
import tv.kuainiu.utils.ImageDisplayUtil;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


public class PreviewPicturePagerAdapter extends PagerAdapter {
	private ArrayList<String> imageList;
	private Context context;
	PhotoViewAttacher mAttacher;
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

	/** 是否由对象生成界面 */
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	/** 从container中移出当前View */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	/** return一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager(container)中 */
	@Override
	public View instantiateItem(ViewGroup container, int position) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.common_picture_preview_item, container, false);
		PhotoView imageView = (PhotoView) view.findViewById(R.id.pv_photoView);
		imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View view, float x, float y) {
				((Activity) context).finish();
			}
		});
//		imageView.setRotationBy(90);
//		imageView.canZoom();
		String url = imageList.get(position);
		ImageDisplayUtil.displayImage(context,imageView,url);
		container.addView(view);
		return view;
	}
}
