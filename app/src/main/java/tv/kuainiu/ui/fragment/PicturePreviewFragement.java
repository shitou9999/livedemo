package tv.kuainiu.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import tv.kuainiu.R;
import tv.kuainiu.ui.activity.PicturePreviewActivity;
import tv.kuainiu.ui.adapter.PreviewPicturePagerAdapter;
import tv.kuainiu.widget.ViewPagerFixed;

/**
 * @ClassName PicturePreviewFragement
 * @Description 图片预览fragment
 * @author sirius@line365.cn
 * @company 上海蓝因网络有限公司
 * @date 2015年5月10日 下午8:21:31
 * @since 4.2.2
 * @version 1.0
 */
public class PicturePreviewFragement extends Fragment implements
		OnClickListener {

	private ViewPagerFixed mPager;

	private Context context;

	// private ArrayList<String> imageList;
	//
	// private int index = 0;
	// private int count = 0;
	private TextView tv_picture_preview_index;
	private ImageView iv_picture_preview_delete;
	private PreviewPicturePagerAdapter adapter;

	private PicturePreviewActivity activity;

	// 通过pagerTabStrip可以设置标题的属性；本页面根据UE，并未设计到要求有pagerTabStrip

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		activity = (PicturePreviewActivity) getActivity();
		if (mPager == null) {
			mPager = (ViewPagerFixed) inflater.inflate(
					R.layout.common_picture_preview, container, false);

		}
		ViewGroup viewgroup = (ViewGroup) mPager.getParent();
		if (viewgroup != null) {
			viewgroup.removeView(mPager);
		}
		initView();
		dataBind();
		return mPager;
	}

	/** 界面初始化 */
	private void initView() {

		iv_picture_preview_delete = (ImageView) getActivity().findViewById(
				R.id.iv_picture_preview_delete);
		iv_picture_preview_delete.setOnClickListener(this);
		tv_picture_preview_index = (TextView) getActivity().findViewById(
				R.id.tv_picture_preview_index);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				indexDataBind(arg0);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	/**
	 * 数据绑定
	 */
	public void dataBind() {
		adapter = new PreviewPicturePagerAdapter(activity.imageList, activity);
		mPager.setAdapter(adapter);
		mPager.setCurrentItem(activity.index);
		indexDataBind(activity.index);
	}

	/**
	 * 索引数据绑定
	 * 
	 * @param index
	 */
	public void indexDataBind(int index) {
		activity.index = index;
		tv_picture_preview_index.setText((adapter.getCount() > 0 ? (index + 1)
				: 0) + "/" + adapter.getCount());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_picture_preview_delete:
			if (activity.imageList.size() < 1) {
				return;
			}
			activity.imageList.remove(activity.index);
			activity.index--;
			activity.index = activity.index < 0 ? 0 : activity.index;
			dataBind();
			// PREVIEW_SELECTED_PICTURE_KEY
			break;

		default:
			break;
		}

	}

}
