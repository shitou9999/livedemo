package tv.kuainiu.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import tv.kuainiu.R;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.activity.PicturePreviewActivity;
import tv.kuainiu.ui.activity.SelectPictureActivity;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.ScreenUtils;

import static tv.kuainiu.modle.cons.Constant.UPLOADIMAGE;

public class UpLoadImageAdapter extends BaseAdapter {
    private ArrayList<String> imageList;
    private BaseActivity activity;
    private Context context;

    private int width = 0;
    private int height = 0;
    private RelativeLayout.LayoutParams paramsFrameLayout;
    private Fragment fragment;
    private int i = 0;
    private boolean only_preview = false;

    public UpLoadImageAdapter(ArrayList<String> imageList,
                              BaseActivity activity) {
        super();
        this.activity = activity;
        this.imageList = imageList;
        this.context = activity;
        width = (ScreenUtils.getScreenWidth(context)
                - 2
                * context.getResources().getDimensionPixelSize(
                R.dimen.expgv_setting_service_image_margin) - 5 * context
                .getResources().getDimensionPixelSize(
                        R.dimen.expgv_setting_service_image_horizontalSpacing)) / 6;
        height = width;
        paramsFrameLayout = new RelativeLayout.LayoutParams(width, height);
    }

    public UpLoadImageAdapter(ArrayList<String> imageList,BaseActivity activity, int i) {
        super();
        this.imageList = imageList;
        this.activity = activity;
        this.imageList = imageList;
        this.context = activity;
        this.i = i;
        width = (ScreenUtils.getScreenWidth(context) - 4
                * context.getResources().getDimensionPixelSize(R.dimen.ten_dp) - 5 * context
                .getResources().getDimensionPixelSize(
                        R.dimen.expgv_setting_service_image_horizontalSpacing)) / 6;
        height = width;
        paramsFrameLayout = new RelativeLayout.LayoutParams(width, height);
    }

    public UpLoadImageAdapter(ArrayList<String> imageList,
                              BaseActivity activity, int i, boolean only_preview) {
        super();
        this.imageList = imageList;
        this.activity = activity;
        this.imageList = imageList;
        this.context = activity;
        this.only_preview = only_preview;
        this.i = i;
        width = (ScreenUtils.getScreenWidth(context) - 4
                * context.getResources().getDimensionPixelSize(R.dimen.ten_dp) - 5 * context
                .getResources().getDimensionPixelSize(
                        R.dimen.expgv_setting_service_image_horizontalSpacing)) / 6;
        height = width;
        paramsFrameLayout = new RelativeLayout.LayoutParams(width, height);
    }

    public UpLoadImageAdapter(ArrayList<String> imageList, Fragment fragment,
                              int i) {
        super();
        this.imageList = imageList;
        this.fragment = fragment;
        this.i = i;
        this.context = fragment.getActivity();
        width = (ScreenUtils.getScreenWidth(context) - 4
                * context.getResources().getDimensionPixelSize(R.dimen.ten_dp) - 5 * context
                .getResources().getDimensionPixelSize(
                        R.dimen.expgv_setting_service_image_horizontalSpacing)) / 6;
        height = width;
        paramsFrameLayout = new RelativeLayout.LayoutParams(width, height);
    }

    @Override
    public int getCount() {
        return imageList == null ? 0
                : (imageList.size() < Constant.UPLOAD_IMAGE_MAX_NUMBER ? imageList
                .size() : Constant.UPLOAD_IMAGE_MAX_NUMBER);
    }

    @Override
    public String getItem(int position) {
        return imageList == null ? null : imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodle vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.common_imageserver_grid_item_picture, parent,
                    false);
            vh = new ViewHodle();
            vh.iv = (ImageView) convertView.findViewById(R.id.iv_image);
            vh.iv.setLayoutParams(paramsFrameLayout);
            vh.v_item_view = convertView.findViewById(R.id.v_item_view);
            vh.v_item_view.setLayoutParams(paramsFrameLayout);
            convertView.setTag(vh);
        } else {
            vh = (ViewHodle) convertView.getTag();
        }
        String uri = getItem(position);
        if (UPLOADIMAGE.equals(uri.trim())) {
            vh.iv.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.common_photograph_btn_selector));
            vh.iv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,
                            SelectPictureActivity.class);
                    ArrayList<String> list = new ArrayList<String>();
                    list.addAll(imageList);
                    if (list.size() > getCount()) {
                        for (int i = list.size(); i > getCount(); i--) {
                            list.remove(i - 1);
                        }
                    }
                    if (list.size() > 0
                            && UPLOADIMAGE.equals(getItem(list.size() - 1))) {
                        list.remove(list.size() - 1);
                    }
                    intent.putExtra(
                            SelectPictureActivity.INTENT_SELECTED_PICTURE,
                            list);
                    intent.putExtra(
                            SelectPictureActivity.INTENT_SELECTED_PICTURE_INDEX,
                            i);

                    if (fragment != null) {
                        fragment.startActivityForResult(intent,
                                Constant.SELECT_PICTURE);
                    } else {
                        ((Activity) context).startActivityForResult(intent,
                                Constant.SELECT_PICTURE);
                    }
                }
            });

        } else {
            vh.iv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,
                            PicturePreviewActivity.class);
                    ArrayList<String> list = new ArrayList<String>();
                    list.addAll(imageList);
                    if (list.size() > getCount()) {
                        for (int i = list.size(); i > getCount(); i--) {
                            list.remove(i - 1);
                        }
                    }
                    if (list.size() > 0
                            && UPLOADIMAGE.equals(list
                            .get(list.size() - 1))) {
                        list.remove(list.size() - 1);
                    }
                    intent.putExtra(PicturePreviewActivity.ONLY_PREVIEW,only_preview);
                    intent.putExtra(
                            Constant.PICTURE_PREVIEW_KEY,
                            list);
                    intent.putExtra(
                            Constant.PICTURE_PREVIEW_INDEX_KEY,
                            position);
                    intent.putExtra(
                            SelectPictureActivity.INTENT_SELECTED_PICTURE_INDEX,
                            i);
                    if (fragment != null) {
                        fragment.startActivityForResult(intent,
                                Constant.PICTURE_PREVIEW);
                    } else {
                        activity.startActivityForResult(intent,
                                Constant.PICTURE_PREVIEW);
                    }

                }
            });
            if (fragment != null) {
                ImageDisplayUtil.displayImage(context, vh.iv, uri);
            } else {
                ImageDisplayUtil.displayImage(activity, vh.iv, uri);
            }
        }

        return convertView;
    }

    class ViewHodle {
        ImageView iv;
        View v_item_view;
    }
}
