package tv.kuainiu.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class GuideAdapter extends PagerAdapter {

	// 界面列表
	private List<View> views;
	private String[] title;

	public GuideAdapter(List<View> views, String[] title) {
		this.views = views;
		this.title = title;
	}

	public GuideAdapter(List<View> views, String[] title,
						boolean click) {
		this.views = views;
		this.title = title;
	}

	/** 获取当前窗体界面数 */
	@Override
	public int getCount() {
		return views == null ? 0 : views.size();
	}

	/** return一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager(container)中 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(views.get(position));

		return views.get(position);
	}

	/** 是否由对象生成界面 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	/** 从container中移出当前View */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	/**
	 * 获取pagetitle数据
	 */
	@Override
	public CharSequence getPageTitle(int position) {
		return title[position];
	}

}
