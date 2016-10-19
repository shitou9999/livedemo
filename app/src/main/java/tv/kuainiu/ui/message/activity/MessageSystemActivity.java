package tv.kuainiu.ui.message.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.finalteam.loadingviewfinal.ListViewFinal;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import tv.kuainiu.R;
import tv.kuainiu.command.http.SystemMessageHttpUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.SystemMessage;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.BaseActivity;
import tv.kuainiu.ui.message.adapter.MessageAdapter;
import tv.kuainiu.utils.LoadingProgressDialog;
import tv.kuainiu.utils.LogUtils;

import static tv.kuainiu.modle.cons.Constant.SUCCEED;


/**
 * 系统消息
 */
public class MessageSystemActivity extends BaseActivity {
    @BindView(R.id.ptr_rv_layout)
    PtrClassicFrameLayout ptr_rv_layout;
    @BindView(R.id.lv_message_list)
    ListViewFinal lv_message_list;


    private MessageAdapter mMessageAdapter;
    List<SystemMessage> messageListList = new ArrayList<>();
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initContentView(int layoutId) {
        super.initContentView(R.layout.activity_message);
        setMyTitle(R.string.message);
//        lv_message_list.setNoLoadMoreHideView(true);
        lv_message_list.setHasLoadMore(true);
        ptr_rv_layout.setLastUpdateTimeRelateObject(this);
        ptr_rv_layout.disableWhenHorizontalMove(true);
        LoadingProgressDialog.startProgressDialog(this);
        initHttp();
    }

    private void initHttp() {
        fetcherSystemMessageList();
    }

    private void fetcherSystemMessageList() {
        SystemMessageHttpUtil.fetcherSystemMessageList(this, new SystemMessageHttpUtil.ParamBuilder(page), Action.fetcherSystemMessageList);
    }


    @Override
    protected void initListener() {
        super.initListener();
        lv_message_list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (messageListList.size() > 0 && messageListList.size() > (position - lv_message_list.getHeaderViewsCount())) {
                            if( messageListList.get(position).getIs_read()==0) {
                                Intent intent = new Intent();
                                intent.setAction(Constant.INTENT_ACTION_MESSAGE_HOME_ACTIVITY_MSG_NUM);
                                intent.putExtra(MessageHomeActivity.CHANGE_NUMBER, 1);
                                intent.putExtra(MessageHomeActivity.TYPE, MessageHomeActivity.SYSTEM_MESSAGE);
                                LocalBroadcastManager.getInstance(MessageSystemActivity.this).sendBroadcast(intent);
                                messageListList.get(position).setIs_read(1);
                                dataBindView();
                            }
                            Intent intent = new Intent(MessageSystemActivity.this, MessageDetailsActivity.class);
                            intent.putExtra(MessageDetailsActivity.MESSGE_TITLE, messageListList.get(position - lv_message_list.getHeaderViewsCount()).getTitle());
                            intent.putExtra(MessageDetailsActivity.MESSAGE_ID, messageListList.get(position - lv_message_list.getHeaderViewsCount()).getId());
                            startActivity(intent);

                        }
                    }
                }
        );
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

    }

    private void dataBindView() {
        if (mMessageAdapter == null) {
            mMessageAdapter = new MessageAdapter(this, messageListList);
            lv_message_list.setAdapter(mMessageAdapter);
        } else {
            mMessageAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEvent(HttpEvent event) {
        switch (event.getAction()) {
            case fetcherSystemMessageList:
                LoadingProgressDialog.stopProgressDialog();
                LogUtils.i("fetcherSystemMessageList", "data===" + event.getData());
                ptr_rv_layout.onRefreshComplete();
                if (SUCCEED == event.getCode()) {
                    JsonParser parser = new JsonParser();
                    JsonObject tempJson = (JsonObject) parser.parse(event.getData().toString());
                    JsonArray json = tempJson.getAsJsonObject("data").getAsJsonArray("list");
                    List<SystemMessage> messageListListTemp = new Gson().fromJson(json, new TypeToken<List<SystemMessage>>() {
                    }.getType());

                    if (page == 1) {
                        messageListList.clear();
                    }
                    if (messageListListTemp != null && messageListListTemp.size() > 0) {
                        messageListList.addAll(messageListListTemp);

                        if (messageListListTemp.size() % Constant.CUSTOM_CONTENT_SIZE > 0) {
                            lv_message_list.setHasLoadMore(false);
                            lv_message_list.onLoadMoreComplete();
                        } else {
                            lv_message_list.setHasLoadMore(true);
                        }

                    } else {
                        lv_message_list.setHasLoadMore(false);
                        lv_message_list.onLoadMoreComplete();
                    }
                    if (page > 1) {
                        lv_message_list.onLoadMoreComplete();
                    }
                    dataBindView();
                }
                break;
        }

    }
}
