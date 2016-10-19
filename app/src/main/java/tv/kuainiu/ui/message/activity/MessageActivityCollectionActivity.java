package tv.kuainiu.ui.message.activity;

import android.content.Intent;
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
import tv.kuainiu.command.http.UserHttpRequest;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.ActivityMessage;
import tv.kuainiu.modle.User;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.BaseActivity;
import tv.kuainiu.ui.activity.WebActivity;
import tv.kuainiu.ui.message.adapter.MessageActivityAdapter;
import tv.kuainiu.utils.LoadingProgressDialog;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.PreferencesUtils;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.dialog.LoginPromptDialog;

import static tv.kuainiu.modle.cons.Constant.SUCCEED;


/**
 * 活动收藏
 */
public class MessageActivityCollectionActivity extends BaseActivity {
    public static final String ID = "ID";
    public static final String STATE = "State";
    @BindView(R.id.ptr_rv_layout)
    PtrClassicFrameLayout ptr_rv_layout;
    @BindView(R.id.lv_message_list)
    ListViewFinal lv_message_list;


    List<ActivityMessage> listActivityMessage = new ArrayList<ActivityMessage>();
    MessageActivityAdapter messageActivityAdapter;
    int page = 1;
    private int currentPosition = -1;
    Gson mGson;
    /**
     * 每页数据个数
     */
    public int size = Constant.CUSTOM_CONTENT_SIZE;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initContentView(int layoutId) {
        super.initContentView(R.layout.activity_message);
        setMyTitle(R.string.message_activity_collection);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        mGson = new Gson();
        lv_message_list.setHasLoadMore(true);
        ptr_rv_layout.setLastUpdateTimeRelateObject(this);
        ptr_rv_layout.disableWhenHorizontalMove(true);
        LoadingProgressDialog.startProgressDialog(this);
        initHttp();
    }

    private void initHttp() {
        fetcherActivityList();
    }

    private void fetcherActivityList() {
        CollectionMessageHttpUtil.ParamBuilder builder = new CollectionMessageHttpUtil.ParamBuilder(page);
        CollectionMessageHttpUtil.fetcherActivityCollectList(this, builder, Action.ACTIVITY_COLLECT_LIST);
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
                    String session_id = PreferencesUtils.getString(MessageActivityCollectionActivity.this, Constant.SESSION_ID, "");
                    if (TextUtils.isEmpty(session_id)) {
                        LoadingProgressDialog.startProgressDialog(MessageActivityCollectionActivity.this);
                        UserHttpRequest.authLogin(MessageActivityCollectionActivity.this, user.getUser_id(), user.getPhone(), Action.AUTO_LOGIN);
                    } else {
                        jumpActivityDetail(session_id);
                    }

                } else {
                    new LoginPromptDialog(MessageActivityCollectionActivity.this).show();
                }
            }
        });
    }
    private void jumpActivityDetail(String session_id) {
        if (listActivityMessage.get(currentPosition).getIs_read() == 0) {
            Intent intent = new Intent();
            intent.setAction(Constant.INTENT_ACTION_MESSAGE_HOME_ACTIVITY_MSG_NUM);
            intent.putExtra(MessageHomeActivity.CHANGE_NUMBER, 1);
            intent.putExtra(MessageHomeActivity.TYPE, MessageHomeActivity.ACTIVITY);
            LocalBroadcastManager.getInstance(MessageActivityCollectionActivity.this).sendBroadcast(intent);
        }
        Intent intent = new Intent(MessageActivityCollectionActivity.this, WebActivity.class);
        intent.putExtra(Constant.KEY_URL, (listActivityMessage.get(currentPosition).getJump_url()));
        intent.putExtra(Constant.SESSION_ID, session_id);
        startActivity(intent);
        currentPosition = -1;
    }

    private void dataBindViewContent() {
        if (messageActivityAdapter == null) {
            messageActivityAdapter = new MessageActivityAdapter(this, listActivityMessage, new collectEvent());
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
            case ACTIVITY_COLLECT_LIST:
                LogUtils.i("messageactivity", "data:" + event.getData().toString());
                LoadingProgressDialog.stopProgressDialog();
                if (SUCCEED == event.getCode()) {
                    try {
                        JSONObject json = (JSONObject) new JSONTokener(event.getData().optString("data")).nextValue();

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
                        ActivityMessage activityMessage = listActivityMessage.get(currentPosition);
                        Intent intent = new Intent(Constant.INTENT_ACTION_ACTIVITY_COLLECT);
                        intent.putExtra(ID, activityMessage.getMessage_id());
                        intent.putExtra(STATE, 0);
                        localBroadcastManager.sendBroadcast(intent);
                        listActivityMessage.remove(currentPosition);
                        currentPosition = -1;
                        dataBindViewContent();
                    }
                    ToastUtils.showToast(this, "取消收藏成功");

                } else {
                    ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(event.getMsg(), "取消收藏失败"));
                }
                break;
        }

    }

    class collectEvent implements MessageActivityAdapter.ICollectClick {

        @Override
        public void collectEvent(int position) {
            currentPosition = position;
            addCollect(listActivityMessage.get(position).getMessage_id());

        }

        @Override
        public void deleteCollectEvent(int position) {
            currentPosition = position;
            delCollect(listActivityMessage.get(position).getMessage_id());

        }
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
