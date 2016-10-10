package tv.kuainiu.ui.down.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bokecc.sdk.mobile.download.Downloader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import tv.kuainiu.R;
import tv.kuainiu.app.ConfigUtil;
import tv.kuainiu.app.DataSet;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.DownloadInfo;
import tv.kuainiu.ui.BaseActivity;
import tv.kuainiu.ui.down.adapter.DownloadEdAdapter;
import tv.kuainiu.widget.DividerItemDecoration;

/**
 * 我的下载
 */
public class DownloadActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.ll_bottom_tab)
    LinearLayout mTabBottom;
    @BindView(R.id.tv_selected_add)
    TextView mTvSelectedAdd;
    @BindView(R.id.tv_delete_selected)
    TextView mTvSelectedDel;
    @BindView(R.id.tv_empty_message)
    TextView mTvEmptyView;
    @BindView(R.id.rl_top)
    RelativeLayout mRlTop;
    @BindView(R.id.tv_dl_count)
    TextView mTvDownloadCount;
    @BindView(R.id.recyView)
    RecyclerView mRecyclerView;

    private LinkedList<DownloadInfo> mDownloadInfos = new LinkedList<>();
    private LinkedList<DownloadInfo> mSelectedInfos = new LinkedList<>();
    private DownloadEdAdapter mEdAdapter;
    private LinearLayoutManager mLayoutManager;

    private int mDownloadCount=0;
    private DownloadedReceiver receiver;
    private Context context;
    private tv.kuainiu.ui.activity.BaseActivity activity;

    public static void intoNewActivity(Context context) {
        Intent intent = new Intent(context, DownloadActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.my_download);
        setRightTitle(getString(R.string.cancel));
        setRightButton(R.mipmap.del);
        setRightButtonVisibility(View.VISIBLE);
        activity = this;
        context = getApplicationContext();
        receiver = new DownloadedReceiver();
        activity.registerReceiver(receiver, new IntentFilter(ConfigUtil.ACTION_DOWNLOADED));
    }

    @Override
    protected void initContentView(int layoutId) {
        super.initContentView(R.layout.activity_download);
        mEdAdapter = new DownloadEdAdapter(this, mDownloadInfos);
        mRecyclerView.setAdapter(mEdAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private class DownloadedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            initData();
            mRecyclerView.invalidate();
        }

    }

    private void initData() {
        mDownloadCount=0;
        List<DownloadInfo> downloadInfos = DataSet.getDownloadInfos();
        mDownloadInfos.clear();
        for (DownloadInfo downloadInfo : downloadInfos) {

            if (downloadInfo.getStatus() != Downloader.FINISH) {
                mDownloadCount++;
                continue;
            }
            mDownloadInfos.add(downloadInfo);
        }
        mEdAdapter.notifyDataSetChanged();

        if (mDownloadCount < 1) {
            mRlTop.setVisibility(View.GONE);
        } else {
            mRlTop.setVisibility(View.VISIBLE);
            mTvDownloadCount.setText(getString(R.string.value_download_ing_count, mDownloadCount));
        }

        if (mDownloadInfos.size() < 1) {
            mRecyclerView.setVisibility(View.GONE);
            mTvEmptyView.setVisibility(View.VISIBLE);
        }else{
            mRecyclerView.setVisibility(View.VISIBLE);
            mTvEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onRightTitleClick() {
        super.onRightTitleClick();
        mEdAdapter.setItemStatusEditing();

        if (mTabBottom.isShown()) {
            setRightTitleVisibility(View.GONE);
            mTabBottom.setVisibility(View.GONE);
            setRightButtonVisibility(View.VISIBLE);
        } else {
            setRightButtonVisibility(View.GONE);
            mTabBottom.setVisibility(View.VISIBLE);
            setRightTitleVisibility(View.VISIBLE);
        }
        if (mSelectedInfos != null) {
            mSelectedInfos.clear();
            mTvSelectedDel.setText(getString(R.string.value_delete_count, mSelectedInfos.size()));
        }
    }


    @Override
    protected void initListener() {
        super.initListener();
        mRlTop.setOnClickListener(this);
        mTvSelectedDel.setOnClickListener(this);
        mTvSelectedAdd.setOnClickListener(this);

        mEdAdapter.setOnSelectedListener(new DownloadEdAdapter.OnSelectListener() {
            @Override
            public void selected(LinkedList<DownloadInfo> selectedInfos) {
                mSelectedInfos = selectedInfos;
                mTvSelectedDel.setText(getString(R.string.value_delete_count, selectedInfos.size()));
                String text = (mSelectedInfos.size() == mDownloadInfos.size()) ? "全不选" : "全选";
                mTvSelectedAdd.setText(text);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_top:
                Intent intent = new Intent(DownloadActivity.this, DownloadIngActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_delete_selected:
                if (mDownloadInfos == null || mDownloadInfos.size() < 1) return;
                if (mSelectedInfos == null || mSelectedInfos.size() < 1) return;

                for (DownloadInfo entry : mSelectedInfos) {
                    DataSet.removeDownloadInfo(entry.getTitle());
                    File file = new File(Environment.getExternalStorageDirectory() + "/" + ConfigUtil.DOWNLOAD_DIR, entry.getTitle() + ".mp4");
                    if (file.exists()) {
                        file.delete();
                    }
                    mDownloadInfos.remove(entry);
                }
                mEdAdapter.notifyDataSetChanged();
                mSelectedInfos.clear();
                mTvSelectedDel.setText(getString(R.string.value_delete_count, mSelectedInfos.size()));
                break;

            case R.id.tv_selected_add:
                if (mDownloadInfos == null || mDownloadInfos.size() < 1) return;
                if (mSelectedInfos == null) return;

                if (mSelectedInfos.size() == mDownloadInfos.size()) {
                    mSelectedInfos.clear();
                    mTvSelectedAdd.setText("全选");
                } else {
                    mSelectedInfos.addAll(mDownloadInfos);
                    mTvSelectedAdd.setText("全不选");
                }
                mEdAdapter.setSelectedInfos(mSelectedInfos);
                mEdAdapter.notifyDataSetChanged();
                mTvSelectedDel.setText(getString(R.string.value_delete_count, mSelectedInfos.size()));
                break;

            default:
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTodo(HttpEvent event) {
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

}
