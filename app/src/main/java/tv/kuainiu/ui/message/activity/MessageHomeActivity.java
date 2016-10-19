package tv.kuainiu.ui.message.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.SystemMessageHttpUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.ActivityMessage;
import tv.kuainiu.modle.MessageHome;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.BaseActivity;
import tv.kuainiu.ui.activity.WebActivity;
import tv.kuainiu.ui.message.adapter.MessageHomeAdapter;
import tv.kuainiu.utils.ImageDisplayUtil;
import tv.kuainiu.utils.LoadingProgressDialog;
import tv.kuainiu.utils.PreferencesUtils;
import tv.kuainiu.utils.ScreenUtils;
import tv.kuainiu.widget.NetErrAddLoadView;

import static tv.kuainiu.modle.cons.Constant.SUCCEED;


/**
 * 消息主页
 */
public class MessageHomeActivity extends BaseActivity {
    @BindView(R.id.ptr_rv_layout)
    PtrClassicFrameLayout ptr_rv_layout;
    @BindView(R.id.lv_message_list) ListView lv_message_list;
    //    @BindView(R.id.convenientBanner) ConvenientBanner mConvenientBanner;
    @BindView(R.id.carouselView)
    CarouselView carouselView;
    @BindView(R.id.tv_alt) TextView textAlt;
    @BindView(R.id.err_layout)
    NetErrAddLoadView mErrView;

    private MessageHomeAdapter mMessageAdapter;
    List<MessageHome> messageListList = new ArrayList<>();
    List<ActivityMessage> bannarListList = new ArrayList<>();
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public final static String TYPE = "activity_type";
    public final static String CHANGE_NUMBER = "change_number";
    public final static int ACTIVITY = 2;
    public final static int SYSTEM_MESSAGE = 3;

    @Override
    protected void initContentView(int layoutId) {
        super.initContentView(R.layout.activity_message_home);
        setMyTitle(R.string.message);
        ptr_rv_layout.setLastUpdateTimeRelateObject(this);
        ptr_rv_layout.disableWhenHorizontalMove(true);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        int width = ScreenUtils.getBannarWidth(this);
        int height = ScreenUtils.getBannarHeight(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        carouselView.setLayoutParams(layoutParams);
        registerBroadcast();

        initHttp();

//        OKHttpUtils.getInstance().post(this, Api.TEST_DNS_API_HOST_V2, Api.ACTIVITY_IMGS, ParamUtil.getParam(null), Action.MESSAGE_INDEX2);
    }

    private void registerBroadcast() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == Constant.INTENT_ACTION_MESSAGE_HOME_ACTIVITY_MSG_NUM) {
                    int changeNum = intent.getIntExtra(CHANGE_NUMBER, 0);
                    int type = intent.getIntExtra(TYPE, -1);
                    int msgNum = PreferencesUtils.getInt(MessageHomeActivity.this, Constant.MSG_NUM, 0);
                    msgNum = msgNum - changeNum;
                    PreferencesUtils.putInt(MessageHomeActivity.this, Constant.MSG_NUM, msgNum);
                    LocalBroadcastManager.getInstance(MessageHomeActivity.this).sendBroadcast(new Intent(Constant.INTENT_ACTION_ACTIVITY_MSG_NUM));
                    if (type < messageListList.size() && type >= 0) {
                        messageListList.get(type).setNew_count(messageListList.get(type).getNew_count() - changeNum);
                        dataBindView();
                    }
                }
            }

        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.INTENT_ACTION_MESSAGE_HOME_ACTIVITY_MSG_NUM);
        localBroadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    private void initHttp() {
        fetcherMessageHomeList();
    }

    private void fetcherMessageHomeList() {
        SystemMessageHttpUtil.fetcherMessageHomeList(this, Action.MESSAGE_INDEX);
    }


    @Override
    protected void initListener() {
        super.initListener();
        lv_message_list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (messageListList.size() > 0 && messageListList.size() > (position - lv_message_list.getHeaderViewsCount())) {
                            MessageHome messageHome = messageListList.get(position - lv_message_list.getHeaderViewsCount());
                            switch (messageHome.getType()) {
                                case 1:
//                                    Intent intentIgxHelper = new Intent(MessageHomeActivity.this, MessageIgxHelperActivity.class);
//                                    startActivity(intentIgxHelper);
                                    break;
                                case 2:
                                    Intent intentCommentActivity = new Intent(MessageHomeActivity.this, MessageCommentActivity.class);
                                    startActivity(intentCommentActivity);
                                    int msgNum = PreferencesUtils.getInt(MessageHomeActivity.this, Constant.MSG_NUM, 0);
                                    if (MyApplication.isLogin() && msgNum > 0) {//更新未读消息数量
                                        msgNum = msgNum - messageHome.getNew_count();
                                        PreferencesUtils.putInt(MessageHomeActivity.this, Constant.MSG_NUM, msgNum);
                                        Intent intent = new Intent();
                                        intent.setAction(Constant.INTENT_ACTION_MESSAGE_HOME_ACTIVITY_MSG_NUM);
                                        intent.putExtra(CHANGE_NUMBER, messageHome.getNew_count());
                                        LocalBroadcastManager.getInstance(MessageHomeActivity.this).sendBroadcast(intent);
                                        MessageHomeAdapter.ViewHolder viewHolder = (MessageHomeAdapter.ViewHolder) view.getTag();
                                        viewHolder.tv_message_count.setVisibility(View.INVISIBLE);
                                    }

                                    break;
                                case 3:
                                    Intent intentActivity = new Intent(MessageHomeActivity.this, MessageActivityActivity.class);
                                    startActivity(intentActivity);
                                    break;
                                case 4:
                                    Intent intentSystem = new Intent(MessageHomeActivity.this, MessageSystemActivity.class);
                                    startActivity(intentSystem);
                                    break;
                            }

                        }
                    }
                }
        );
        ptr_rv_layout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                initHttp();
            }
        });
        mErrView.setOnTryCallBack(new NetErrAddLoadView.OnTryCallBack() {
            @Override
            public void callAgain() {
                initHttp();
            }
        });
    }

    private void dataBindView() {
        if (mMessageAdapter == null) {
            mMessageAdapter = new MessageHomeAdapter(this, messageListList);
            lv_message_list.setAdapter(mMessageAdapter);
        } else {
            mMessageAdapter.notifyDataSetChanged();
        }
        if (bannarListList == null || bannarListList.size() < 1) {
            carouselView.setVisibility(View.GONE);
            textAlt.setVisibility(View.GONE);
        } else {
            carouselView.setVisibility(View.VISIBLE);
            textAlt.setVisibility(View.VISIBLE);
            carouselView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if (bannarListList != null && position > -1 && position < bannarListList.size()) {
                        textAlt.setText(bannarListList.get(position).getTitle().trim());
                    }
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            carouselView.setImageListener(new ImageListener() {
                @Override
                public void setImageForPosition(final int position, ImageView imageView) {
                    final ActivityMessage bannar = bannarListList.get(position);
                    ImageDisplayUtil.displayImage(MessageHomeActivity.this, imageView, bannarListList.get(position).getActivity_thumb());
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MessageHomeActivity.this, WebActivity.class);
                            intent.putExtra(Constant.KEY_URL, bannar.getJump_url());
//                        intent.putExtra(Constant.SESSION_ID, session_id);
                            startActivity(intent);
                        }
                    });

                }
            });
            carouselView.setPageCount(bannarListList.size());
        }
    }


    // *******************************
//    public class NetworkImageHolderView implements Holder<String> {
//        private ImageView imageView;
//
//        @Override public View createView(Context context) {
//            imageView = new ImageView(context);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            return imageView;
//        }
//
//        @Override
//        public void UpdateUI(Context context, int position, String data) {
//            ImageDisplayUtil.displayImage(context, imageView, data);
//
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEvent(HttpEvent event) {
        switch (event.getAction()) {
            case MESSAGE_INDEX:
                LoadingProgressDialog.stopProgressDialog();
                ptr_rv_layout.onRefreshComplete();
                if (SUCCEED == event.getCode()) {
                    JsonParser parser = new JsonParser();
                    JsonObject tempJson = (JsonObject) parser.parse(event.getData().toString());
                    JsonArray jsonMessage = tempJson.getAsJsonObject("data").getAsJsonArray("module_list");
                    JsonArray jsonBannar = tempJson.getAsJsonObject("data").getAsJsonArray("activity_rec_list");
                    List<MessageHome> messageListListTemp = new Gson().fromJson(jsonMessage, new TypeToken<List<MessageHome>>() {
                    }.getType());
                    List<ActivityMessage> bannarListListTemp = new Gson().fromJson(jsonBannar, new TypeToken<List<ActivityMessage>>() {
                    }.getType());

                    if (messageListListTemp != null && messageListListTemp.size() > 0) {
                        messageListList.clear();
                        messageListList.addAll(messageListListTemp);
                    }
                    if (bannarListListTemp != null && bannarListListTemp.size() > 0) {
                        bannarListList.clear();
                        bannarListList.addAll(bannarListListTemp);
                    }
                    dataBindView();
                }
                mErrView.StopLoading(event.getCode(), event.getMsg());
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (localBroadcastManager != null & mReceiver != null) {
            localBroadcastManager.unregisterReceiver(mReceiver);
        }
    }
}
