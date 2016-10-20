package tv.kuainiu.ui.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tv.kuainiu.R;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.utils.FileUtils2;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.TakePhotoActivity;
import tv.kuainiu.utils.ToastUtils;

//import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 选择图片
 * 
 * @author sirius
 *
 */
public class SelectPictureActivity extends tv.kuainiu.ui.BaseActivity implements
		OnClickListener {

	/**
	 * 最多选择图片的个数
	 */
	private int MAX_NUM = Constant.UPLOAD_IMAGE_MAX_NUMBER;
	private static final int TAKE_PICTURE = 520;

	public static final String INTENT_SELECTED_PICTURE = "intent_selected_picture";
	public static final String INTENT_SELECTED_PICTURE_INDEX = "intent_selected_picture_index";
	public static final String OnlyOnePic = "ONLY_ONE_PIC";

	private Context context;
	private GridView gridview;
	private PictureAdapter adapter;
	/**
	 * 临时的辅助类，用于防止同一个文件夹的多次扫描
	 */
	private HashMap<String, Integer> tmpDir;
	private ArrayList<ImageFloder> mDirPaths;

	// private ImageLoader loader;
	private ContentResolver mContentResolver;
	private Button btn_select, btn_ok;
	private ListView listview;
	private FolderAdapter folderAdapter;
	private ImageFloder imageAll, currentImageFolder;
	private int index = 0;
	/**
	 * 已选择的图片
	 */
	private ArrayList<String> selectedPicture = new ArrayList<String>();
	private Button btn_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_imageserver_activity_select_picture);
		/*
		 * Bundle bundle = getIntent().getExtras(); // 反馈页面传过来的值最多选择图片一张 if
		 * (bundle != null) { MAX_NUM = bundle.getInt("MAX_NUM"); }
		 */
		if (getIntent().getExtras().getBoolean(OnlyOnePic, false)) {
			MAX_NUM = 1;
		} else {
			MAX_NUM = Constant.UPLOAD_IMAGE_MAX_NUMBER;
		}
		context = this;
		mContentResolver = getContentResolver();
		// loader = ImageLoader.getInstance();
		initView();
		initData();
		dataBind();
	}

	/** 选择文件夹 */
	public void select() {
		if (listview.getVisibility() == View.VISIBLE) {
			hideListAnimation();
		} else {
			listview.setVisibility( View.VISIBLE);
			showListAnimation();
			folderAdapter.notifyDataSetChanged();
		}
	}

	/** 控件初始化 */
	private void initView() {

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_select = (Button) findViewById(R.id.btn_select);
		btn_select.setOnClickListener(this);
		btn_ok.setText("完成");
		btn_ok.setOnClickListener(this);
		gridview = (GridView) findViewById(R.id.gridview);

		listview = (ListView) findViewById(R.id.listview);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				currentImageFolder = mDirPaths.get(position);
				hideListAnimation();
				adapter.notifyDataSetChanged();
				btn_select.setText(currentImageFolder.getName());
			}
		});

	}

	/** 数据初始化 */
	private void initData() {
		index = getIntent().getIntExtra(INTENT_SELECTED_PICTURE_INDEX, 0);
		selectedPicture = getIntent().getStringArrayListExtra(
				INTENT_SELECTED_PICTURE);
		selectedPicture = selectedPicture == null ? new ArrayList<String>()
				: selectedPicture;
		tmpDir = new HashMap<String, Integer>();
		mDirPaths = new ArrayList<ImageFloder>();
		imageAll = new ImageFloder();
		imageAll.setDir("所有图片");
		currentImageFolder = imageAll;
		mDirPaths.add(imageAll);
		getThumbnail();
	}

	/** 数据绑定 */
	private void dataBind() {
		adapter = new PictureAdapter();
		gridview.setAdapter(adapter);
		folderAdapter = new FolderAdapter();
		listview.setAdapter(folderAdapter);
	}

	/** 文件夹列表显示动画 */
	public void showListAnimation() {
		TranslateAnimation ta = new TranslateAnimation(1, 0f, 1, 0f, 1, 1f, 1,
				0f);
		ta.setDuration(200);
		listview.startAnimation(ta);
	}

	/** 文件夹列表隐藏动画 */
	public void hideListAnimation() {
		TranslateAnimation ta = new TranslateAnimation(1, 0f, 1, 0f, 1, 0f, 1,
				1f);
		ta.setDuration(200);
		listview.startAnimation(ta);
		ta.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				listview.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * 使用相机拍照
	 * 
	 */
	protected void goCamare() {
		if (selectedPicture.size() + 1 > MAX_NUM) {
			Toast.makeText(context, "最多选择" + MAX_NUM + "张", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		// Intent intent = new Intent(
		// android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		//
		// intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
		// imageFileUri);
		//
		// startActivityForResult(intent, TAKE_PICTURE);

		Intent intent = new Intent(SelectPictureActivity.this,
				TakePhotoActivity.class);
		startActivityForResult(intent, TAKE_PICTURE);
	}

	/**
	 * 用于拍照时获取输出的Uri
	 * 
	 * @return
	 */

	// protected Uri getOutputMediaFileUri() {
	// File mediaStorageDir = new File(
	// Environment
	// .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
	// "Night");
	// if (!mediaStorageDir.exists()) {
	// if (!mediaStorageDir.mkdirs()) {
	// Log.d("MyCameraApp", "failed to create directory");
	// return null;
	// }
	// }
	// String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
	// .format(new Date());
	// File mediaFile = new File(mediaStorageDir.getPath() + File.separator
	// + "IMG_" + timeStamp + ".jpg"); // cameraPath =
	// mediaFile.getAbsolutePath();
	// return Uri.fromFile(mediaFile);
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == TAKE_PICTURE
				&& data != null) {
			selectedPicture.add("file://"
					+ data.getStringExtra(TakePhotoActivity.IMAGE_PATH));

			Intent intent = new Intent();
			intent.putExtra(INTENT_SELECTED_PICTURE_INDEX, index);
			intent.putExtra(INTENT_SELECTED_PICTURE, selectedPicture);
			setResult(RESULT_OK, intent);
			this.finish();
			/*
			 * initData(); SelectImagePosition = 1;
			 * adapter.notifyDataSetChanged();
			 * folderAdapter.notifyDataSetChanged();
			 */
		}
	}

	/**
	 * 图片adapter
	 * 
	 * @author sirius
	 *
	 */
	class PictureAdapter extends BaseAdapter {
		private Drawable drable = null;

		public PictureAdapter() {
			super();
			drable = getResources().getDrawable(
					R.drawable.common_imageserver_pickphotos_to_camera_normal);
		}

		@Override
		public int getCount() {
			return currentImageFolder.images.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			return currentImageFolder.images.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(context,
						R.layout.common_imageserver_grid_item_picture, null);
				holder = new ViewHolder();
				holder.iv = (ImageView) convertView.findViewById(R.id.iv_image);
				holder.checkBox = (Button) convertView
						.findViewById(R.id.btn_image_check);
				holder.item_view = convertView.findViewById(R.id.v_item_view);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position == 0) {
				holder.iv.setImageDrawable(drable);
				holder.checkBox.setVisibility(View.INVISIBLE);
				holder.item_view.setVisibility(View.INVISIBLE);
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						goCamare();
					}
				});

			} else {
				position = position - 1;
				holder.checkBox.setVisibility(View.VISIBLE);
				holder.item_view.setVisibility(View.VISIBLE);
				final ImageItem item = currentImageFolder.images.get(position);
				final String image_path = "file://" + item.path;
				ImageDisplayUtil.displayImage(SelectPictureActivity.this,
						holder.iv, image_path);
				// loader.displayImage(image_path, holder.iv,
				// DisplayImageOptionsConfig.options);
				/*
				 * if (SelectImagePosition == DEFAULTSELECTPOSITION) {
				 * selectedPicture.add(image_path); SelectImagePosition = -1; }
				 */
				btn_ok.setText("完成(" + selectedPicture.size() + "/" + MAX_NUM
						+ ")");
				boolean isSelected = selectedPicture.contains(image_path);
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ViewHolder holder = (ViewHolder) v.getTag();
						if (!holder.checkBox.isSelected()
								&& selectedPicture.size() + 1 > MAX_NUM) {
							Toast.makeText(context, "最多选择" + MAX_NUM + "张",
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (selectedPicture.contains(image_path)) {
							selectedPicture.remove(image_path);
						} else {
							if (FileUtils2.isFileExist(item.path)) {
								selectedPicture.add(image_path);
							} else {
								ToastUtils.showToast(context,"该图片无效");
							}
						}
						btn_ok.setText("完成(" + selectedPicture.size() + "/"
								+ MAX_NUM + ")");
						boolean isSelected=selectedPicture.contains(image_path);
						holder.checkBox.setSelected(isSelected);
						holder.item_view.setSelected(isSelected);
					}
				});
				holder.checkBox.setSelected(isSelected);
				holder.item_view.setSelected(isSelected);

			}
			return convertView;
		}
	}

	/**
	 * 图片ViewHolder
	 * 
	 * @author sirius
	 *
	 */
	class ViewHolder {
		ImageView iv;
		View item_view;
		Button checkBox;
	}

	/**
	 * 文件夹adapter
	 * 
	 * @author sirius
	 *
	 */
	class FolderAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mDirPaths.size();
		}

		@Override
		public Object getItem(int position) {
			return mDirPaths.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FolderViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(context,
						R.layout.common_imageserver_list_dir_item, null);
				holder = new FolderViewHolder();
				holder.id_dir_item_image = (ImageView) convertView
						.findViewById(R.id.id_dir_item_image);
				holder.id_dir_item_name = (TextView) convertView
						.findViewById(R.id.id_dir_item_name);
				holder.id_dir_item_count = (TextView) convertView
						.findViewById(R.id.id_dir_item_count);
				holder.choose = (ImageView) convertView
						.findViewById(R.id.choose);
				convertView.setTag(holder);
			} else {
				holder = (FolderViewHolder) convertView.getTag();
			}
			ImageFloder item = mDirPaths.get(position);
			ImageDisplayUtil.displayImage(SelectPictureActivity.this,
					holder.id_dir_item_image,
					"file://" + item.getFirstImagePath());
			// loader.displayImage("file://" + item.getFirstImagePath(),
			// holder.id_dir_item_image, DisplayImageOptionsConfig.options);
			holder.id_dir_item_count.setText(item.images.size() + "张");
			holder.id_dir_item_name.setText(item.getName());
			holder.choose
					.setVisibility(currentImageFolder == item ? View.VISIBLE
							: View.GONE);
			return convertView;
		}
	}

	/** click事件 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_select:// 线上文件夹列表
			if (listview.getVisibility() == View.VISIBLE) {
				hideListAnimation();
			} else {
				listview.setVisibility(View.VISIBLE);
				showListAnimation();
				folderAdapter.notifyDataSetChanged();
			}
			break;
		case R.id.btn_ok:// 完成按钮点击事件
			Intent intent = new Intent();
			intent.putExtra(INTENT_SELECTED_PICTURE, selectedPicture);
			intent.putExtra(INTENT_SELECTED_PICTURE_INDEX, index);
			/**
			 * 如果在startActivityForResult起来的Activity里面设置setResult,
			 * 结果并不会马上返回给parent的Activity,只有当前Activity被finish,
			 * 结果才会被发送给parent的onActivityResult去处理!
			 */
			setResult(RESULT_OK, intent);
			this.finish();
			break;
		case R.id.btn_back:// 完成按钮点击事件
			onBackPressed();
			break;
		}

	}

	/**
	 * 得到缩略图
	 */
	private void getThumbnail() {
		Cursor mCursor = mContentResolver.query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.ImageColumns.DATA }, "", null,
				MediaStore.MediaColumns.DATE_ADDED + " DESC");
		if (mCursor.moveToFirst()) {
			int _date = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
			do {
				// 获取图片的路径
				String path = mCursor.getString(_date);
				if (imageAll.getFirstImagePath() == null) {
					imageAll.setFirstImagePath(path);
				}

				if (FileUtils2.isFileExist(path)) {
					imageAll.images.add(new ImageItem(path));
				}
				// 获取该图片的父路径名
				File parentFile = new File(path).getParentFile();
				if (parentFile == null) {
					continue;
				}
				ImageFloder imageFloder = null;
				String dirPath = parentFile.getAbsolutePath();
				if (!tmpDir.containsKey(dirPath)) {
					// 初始化imageFloder
					imageFloder = new ImageFloder();
					imageFloder.setDir(dirPath);
					imageFloder.setFirstImagePath(path);
					mDirPaths.add(imageFloder);
					tmpDir.put(dirPath, mDirPaths.indexOf(imageFloder));
				} else {
					imageFloder = mDirPaths.get(tmpDir.get(dirPath));
				}
				imageFloder.images.add(new ImageItem(path));
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		tmpDir = null;
	}

	/**
	 * 文件夹bean
	 * 
	 * @author sirius
	 *
	 */
	class FolderViewHolder {
		ImageView id_dir_item_image;
		ImageView choose;
		TextView id_dir_item_name;
		TextView id_dir_item_count;
	}

	/**
	 * 文件夹图片首张图片及文件夹下图片集bean
	 * 
	 * @author sirius
	 *
	 */

	class ImageFloder {
		/**
		 * 图片的文件夹路径
		 */
		private String dir;

		/**
		 * 第一张图片的路径
		 */
		private String firstImagePath;
		/**
		 * 文件夹的名称
		 */
		private String name;

		public List<ImageItem> images = new ArrayList<ImageItem>();

		public String getDir() {
			return dir;
		}

		public void setDir(String dir) {
			this.dir = dir;
			int lastIndexOf = this.dir.lastIndexOf("/") + 1;
			this.name = this.dir.substring(lastIndexOf);
		}

		public String getFirstImagePath() {
			return firstImagePath;
		}

		public void setFirstImagePath(String firstImagePath) {
			this.firstImagePath = firstImagePath;
		}

		public String getName() {
			return name;
		}

	}

	/**
	 * 图片bean
	 * 
	 * @author sirius
	 *
	 */
	class ImageItem {
		String path;

		public ImageItem(String p) {
			this.path = p;
		}
	}
}
