package tv.kuainiu.ui.message.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.finalteam.loadingviewfinal.ListViewFinal;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.CollectionMessageHttpUtil;
import tv.kuainiu.command.http.SystemMessageHttpUtil;
import tv.kuainiu.command.http.UserHttpRequest;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.ActivityMessage;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.BaseActivity;
import tv.kuainiu.ui.activity.WebActivity;
import tv.kuainiu.ui.message.adapter.MessageActivityAdapter;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.LoadingProgressDialog;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.PreferencesUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.dialog.LoginPromptDialog;

import static tv.kuainiu.modle.cons.Constant.SUCCEED;


/**
 * 活动消息
 */
public class MessageActivityActivity extends BaseActivity {
    @BindView(R.id.ptr_rv_layout)
    PtrClassicFrameLayout ptr_rv_layout;
    @BindView(R.id.lv_message_list)
    ListViewFinal lv_message_list;


    List<ActivityMessage> listActivityMessage = new ArrayList<>();
    MessageActivityAdapter messageActivityAdapter;
    int page = 1;
    private int currentPosition = -1;
    Gson mGson;
    /**
     * 每页数据个数
     */
    public int size = Constant.CUSTOM_CONTENT_SIZE;

    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initContentView(int layoutId) {
        super.initContentView(R.layout.activity_message);
        setMyTitle(R.string.message_activity);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mGson = new Gson();
        lv_message_list.setHasLoadMore(true);
        ptr_rv_layout.setLastUpdateTimeRelateObject(this);
        ptr_rv_layout.disableWhenHorizontalMove(true);
        LoadingProgressDialog.startProgressDialog(this);
        setRightTitle("我的收藏");
        setRightTitleVisibility(View.VISIBLE);
        registerBroadcast();
        initHttp();
    }

    private void registerBroadcast() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == Constant.INTENT_ACTION_ACTIVITY_COLLECT) {
                    String id = intent.getStringExtra(MessageActivityCollectionActivity.ID);
                    int state = intent.getIntExtra(MessageActivityCollectionActivity.STATE, 0);
                    for (int i = 0; i < listActivityMessage.size(); i++) {
                        if (id.equals(listActivityMessage.get(i).getId())) {
                            listActivityMessage.get(i).setIs_collect(state);
                            dataBindViewContent();
                            break;
                        }
                    }
                }
            }

        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.INTENT_ACTION_ACTIVITY_COLLECT);
        localBroadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onRightTitleClick() {
        super.onRightTitleClick();
        Intent intent = new Intent(MessageActivityActivity.this, MessageActivityCollectionActivity.class);
        startActivity(intent);
    }

    private void initHttp() {
        fetcherActivityList();
    }

    private void fetcherActivityList() {
        SystemMessageHttpUtil.ParamBuilder builder = new SystemMessageHttpUtil.ParamBuilder(page);
        SystemMessageHttpUtil.fetcherActivityList(this, builder, Action.ACTIVITY_LIST);
    }


    @Override
    protected void initListener() {
        super.initListener();
        lv_message_list.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                page += 1;
                initHttp();
            }
        });
        ptr_rv_layout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                initHttp();
            }
        });
        lv_message_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listActivityMessage == null || listActivityMessage.size()<1|| listActivityMessage.size() <= position) {
                    return;
                }
                User user = MyApplication.getUser();
                currentPosition = position;
                if (user != null) {
                    String session_id = PreferencesUtils.getString(MessageActivityActivity.this, Constant.SESSION_ID, "");
                    if (TextUtils.isEmpty(session_id)) {
                        LoadingProgressDialog.startProgressDialog(MessageActivityActivity.this);
                        UserHttpRequest.authLogin(MessageActivityActivity.this, user.getUser_id(), user.getPhone(), Action.AUTO_LOGIN);
                    } else {
                        jumpActivityDetail(session_id);
                    }

                } else {
                    new LoginPromptDialog(MessageActivityActivity.this).show();
                }
            }
        });
    }

    private void dataBindViewContent() {
        if (messageActivityAdapter == null) {
            messageActivityAdapter = new MessageActivityAdapter(this, listActivityMessage, new myCollectEvent());
            lv_message_list.setAdapter(messageActivityAdapter);
        } else {
            messageActivityAdapter.notifyDataSetChanged();
        }
    }

    private void addCollect(String mId) {
        LoadingProgressDialog.startProgressDialog("正在收藏", this);
        CollectionMessageHttpUtil.addCollect(this, mId, String.valueOf(Constant.NEWS_TYPE_ACTIVITY));
    }

    private void delCollect(String mId) {
        LoadingProgressDialog.startProgressDialog("正在取消收藏", this);
        CollectionMessageHttpUtil.delCollect(this, mId, String.valueOf(Constant.NEWS_TYPE_ACTIVITY));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEvent(HttpEvent event) {
        switch (event.getAction()) {
            case ACTIVITY_LIST:
                LoadingProgressDialog.stopProgressDialog();
                if (SUCCEED == event.getCode()) {
                    try {
                        JSONObject json = (JSONObject) new JSONTokener(event.getData().optString("data")).nextValue();

                        // XXX
                        DebugUtils.dd("activity msg : " + json.toString());
                        JsonParser parser = new JsonParser();
                        JsonArray jArray = parser.parse(json.optString("list")).getAsJsonArray();
                        List<ActivityMessage> tempList = mGson.fromJson(jArray, new TypeToken<List<ActivityMessage>>() {
                        }.getType());

                        if (page == 1) {
                            listActivityMessage.clear();
                        }
                        if (tempList != null && tempList.size() > 0) {
                            listActivityMessage.addAll(tempList);
                            lv_message_list.setHasLoadMore(true);
                            lv_message_list.onLoadMoreComplete();
                        } else {
                            lv_message_list.setHasLoadMore(false);
                            lv_message_list.onLoadMoreComplete();
                        }
                        dataBindViewContent();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (page > 1) {
                                lv_message_list.onLoadMoreComplete();
                            }
                            ptr_rv_layout.onRefreshComplete();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    LogUtils.e("jhdhdjkh", "djkhdsjkh:" + event.getMsg());
                    if (lv_message_list != null) {

                        lv_message_list.onLoadMoreComplete();
                    }
                    if (ptr_rv_layout != null && ptr_rv_layout.isPullToRefresh()) {
                        ptr_rv_layout.onRefreshComplete();
                    }
                }
                break;
            case add_collect:
                LoadingProgressDialog.stopProgressDialog();
                LogUtils.i("data", "add_collect_data:" + StringUtils.replaceNullToEmpty(event.getData()));
                if (SUCCEED == event.getCode()) {
                    if (currentPosition > -1) {
                        listActivityMessage.get(currentPosition).setIs_collect(1);
                        currentPosition = -1;
                        dataBindViewContent();
                        ToastUtils.showToast(this, "收藏成功");
                    }
                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "添加收藏失败"));
                }
                break;
            case del_collect:
                LoadingProgressDialog.stopProgressDialog();
                LogUtils.i("data", "del_collect_data:" + StringUtils.replaceNullToEmpty(event.getData()));
                if (SUCCEED == event.getCode()) {
                    if (currentPosition > -1) {
                        listActivityMessage.get(currentPosition).setIs_collect(0);
                        currentPosition = -1;
                        dataBindViewContent();
                    }
                    ToastUtils.showToast(this, "取消收藏成功");

                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "取消收藏失败"));
                }
                break;
            case AUTO_LOGIN:
                if (SUCCEED == event.getCode()) {
                    JSONObject jsonObject = event.getData();
                    try {
                        if (jsonObject != null && jsonObject.has("session_id")) {
                            String session_id = jsonObject.getString("session_id");
                            PreferencesUtils.putString(this, Constant.SESSION_ID, session_id);
                            jumpActivityDetail(session_id);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        currentPosition = -1;
                    }
                } else {
                    ToastUtils.showToast(MessageActivityActivity.this, event.getMsg());
                }

                break;
        }

    }

    private void jumpActivityDetail(String session_id) {
        if (listActivityMessage.get(currentPosition).getIs_read() == 0) {
            Intent intent = new Intent();
            intent.setAction(Constant.INTENT_ACTION_MESSAGE_HOME_ACTIVITY_MSG_NUM);
            intent.putExtra(MessageHomeActivity.CHANGE_NUMBER, 1);
            intent.putExtra(MessageHomeActivity.TYPE, MessageHomeActivity.ACTIVITY);
            LocalBroadcastManager.getInstance(MessageActivityActivity.this).sendBroadcast(intent);
        }
        Intent intent = new Intent(MessageActivityActivity.this, WebActivity.class);
        intent.putExtra(Constant.KEY_URL, (listActivityMessage.get(currentPosition).getJump_url()));
        intent.putExtra(Constant.SESSION_ID, session_id);
        startActivity(intent);
        currentPosition = -1;
    }

    class myCollectEvent implements MessageActivityAdapter.ICollectClick {

        @Override
        public void collectEvent(int position) {
            currentPosition = position;
            addCollect(listActivityMessage.get(position).getId());
        }

        @Override
        public void deleteCollectEvent(int position) {
            currentPosition = position;
            delCollect(listActivityMessage.get(position).getId());
        }
    }

    @Override
    protected void onDestroy() {
        if (localBroadcastManager != null && mReceiver != null) {
            localBroadcastManager.unregisterReceiver(mReceiver);
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
