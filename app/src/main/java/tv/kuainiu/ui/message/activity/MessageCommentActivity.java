package tv.kuainiu.ui.message.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

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
import tv.kuainiu.command.http.SystemMessageHttpUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.CommentMessage;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.BaseActivity;
import tv.kuainiu.ui.articles.activity.PostZoneActivity;
import tv.kuainiu.ui.message.adapter.MessageCommentAdapter;
import tv.kuainiu.ui.video.VideoActivity;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.KeyBoardUtil;
import tv.kuainiu.utils.LoadingProgressDialog;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.dialog.LoginPromptDialog;

import static tv.kuainiu.modle.cons.Constant.SUCCEED;


/**
 * 评论消息
 */
public class MessageCommentActivity extends BaseActivity {
    @BindView(R.id.ptr_rv_layout)
    PtrClassicFrameLayout ptr_rv_layout;
    @BindView(R.id.lv_message_list)
    ListViewFinal lv_message_list;


    List<CommentMessage> listCommentMessage = new ArrayList<CommentMessage>();
    MessageCommentAdapter customFragmentAdapter;
    int page = 1;
    Gson mGson;
    /**
     * 每页数据个数
     */
    public int size = Constant.CUSTOM_CONTENT_SIZE;
    private PopupWindow keyboardPopup;
    private String mId;
    private String mCatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initContentView(int layoutId) {
        super.initContentView(R.layout.activity_message);
        setMyTitle(R.string.message_comment);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mGson = new Gson();
        lv_message_list.setHasLoadMore(true);
        ptr_rv_layout.setLastUpdateTimeRelateObject(this);
        ptr_rv_layout.disableWhenHorizontalMove(true);
        LoadingProgressDialog.startProgressDialog(this);
        initHttp();
    }

    private void initHttp() {
        fetcherCommentList();
    }

    private void fetcherCommentList() {
        SystemMessageHttpUtil.ParamBuilder builder = new SystemMessageHttpUtil.ParamBuilder(page);
        SystemMessageHttpUtil.fetcherCommentList(this, builder, Action.COMMENT_MESSAGE_LIST);
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

    }

    private void dataBindViewContent() {
        if (customFragmentAdapter == null) {
            customFragmentAdapter = new MessageCommentAdapter(this, listCommentMessage, new myItemClick());
            lv_message_list.setAdapter(customFragmentAdapter);
        } else {
            customFragmentAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEvent(HttpEvent event) {
        switch (event.getAction()) {
            case COMMENT_MESSAGE_LIST:
                LoadingProgressDialog.stopProgressDialog();
                if (SUCCEED == event.getCode()) {
                    try {
                        JSONObject json = (JSONObject) new JSONTokener(event.getData().optString("data")).nextValue();
                        DebugUtils.dd(json.toString());
                        JsonParser parser = new JsonParser();
                        JsonArray jArray = parser.parse(json.optString("list_new")).getAsJsonArray();
                        List<CommentMessage> tempList = mGson.fromJson(jArray, new TypeToken<List<CommentMessage>>() {
                        }.getType());

                        if (page == 1) {
                            listCommentMessage.clear();
                        }
                        if (tempList != null && tempList.size() > 0) {
                            listCommentMessage.addAll(tempList);
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
            case add_comment:
                LoadingProgressDialog.stopProgressDialog();
//            isReply = false;
                if (SUCCEED == event.getCode()) {
                    DebugUtils.dd("comment result : " + event.getData().toString());
                    page = 1;
                    initHttp();
                    DebugUtils.showToast(this, "评论成功");
                    if (keyboardPopup != null && keyboardPopup.isShowing()) {
                        keyboardPopup.dismiss();
                    }
                } else {
                    DebugUtils.showToastResponse(this, event.getMsg());
                }
                break;
        }

    }

    private void showCommentPopuWindows(final String mId, final String mCatId, final String comment_id, String hint) {
        View contentView = getLayoutInflater().inflate(R.layout.popup_keyboard_edit_comment, null);
        final EditText editComment = (EditText) contentView.findViewById(R.id.et_edit_comment);
        final Button button = (Button) contentView.findViewById(R.id.btn_publish);
        final View viewTopArea = contentView.findViewById(R.id.v_close);
        if (!TextUtils.isEmpty(hint)) {
            editComment.setHint(hint);
        }
        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
        keyboardPopup = new PopupWindow(contentView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, true);
        keyboardPopup.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#88333333")));
        keyboardPopup.setAnimationStyle(R.style.PopupAnimation);
        keyboardPopup.setOutsideTouchable(true);
        keyboardPopup.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        keyboardPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        keyboardPopup.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        KeyBoardUtil.showKeyBoard(MessageCommentActivity.this, editComment);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtil.hideSoftInput(MessageCommentActivity.this, editComment);
//                isReply = false;
                if (!MyApplication.getInstance().isLogin()) {
                    new LoginPromptDialog(MessageCommentActivity.this).show();
                    return;
                }
                String content = editComment.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showToast(MessageCommentActivity.this, "评论内容不能为空");
                    return;
                } else {

                    String nickname = MyApplication.getInstance().getUser().getNickname();
                    addComment(mId, mCatId, content, comment_id, nickname, "1");
                    editComment.setText("");
                }
            }
        });

        viewTopArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtil.hideSoftInput(MessageCommentActivity.this, editComment);
                if (null != keyboardPopup && keyboardPopup.isShowing()) {
                    keyboardPopup.dismiss();
                }
            }
        });

    }

    /**
     * 新评论、回复评论
     */
    private void addComment(String mId, String mCatId, String content, String parentId, String nickname, String isReply) {
//        Map<String, String> map = new HashMap<>();
//        map.put("nick_name", nickname);
//        map.put("id", mId);
//        map.put("catid", mCatId);
//        map.put("comment_id", parentId);
//        map.put("content", content);
//        map.put("is_reply", isReply);
//        String param = ParamUtil.getParam(map);
//        OKHttpUtils.getInstance().post(this, Api.TEST_DNS_API_HOST_V2, Api.ADD_COMMENT, param, Action.add_comment);
//TODO： 评论
//        CommentHttpUtil.addComment(this, mId, mCatId, content, parentId, nickname, isReply);
    }

    class myItemClick implements MessageCommentAdapter.IItemClick {
        @Override
        public void itemClick(int position) {
            CommentMessage commentMessage = listCommentMessage.get(position);
            showCommentPopuWindows(commentMessage.getNews_id(), commentMessage.getCatid(), commentMessage.getComment_id(), "回复：" + commentMessage.getFrom_user_nickname());
        }

        @Override
        public void itemButtomClick(int position) {
            CommentMessage commentMessage = listCommentMessage.get(position);
            //内容跳转
            Intent i = new Intent();
            if (TextUtils.isEmpty(commentMessage.getNews_upVideoId())) {
                i.setClass(MessageCommentActivity.this, PostZoneActivity.class);
                i.putExtra(Constant.KEY_ID, commentMessage.getNews_id());
                i.putExtra(Constant.KEY_CATID, commentMessage.getCatid());
                i.putExtra(Constant.KEY_CATNAME, commentMessage.getCatname());
            } else {
                i.setClass(MessageCommentActivity.this, VideoActivity.class);
                i.putExtra(VideoActivity.NEWS_ID, commentMessage.getNews_id());
                i.putExtra(VideoActivity.VIDEO_NAME, commentMessage.getNews_title());
                i.putExtra(VideoActivity.CAT_ID, commentMessage.getCatid());
                i.putExtra(VideoActivity.VIDEO_ID, commentMessage.getNews_upVideoId());
//                i.setClass(MessageCommentActivity.this, PlayActivity.class);
//                i.putExtra(Constant.KEY_ID, commentMessage.getNews_id());
//                i.putExtra(Constant.KEY_CATID, commentMessage.getCatid());
//                i.putExtra(Constant.KEY_VIDEO_ID, commentMessage.getNews_upVideoId());
//                i.putExtra(Constant.KEY_DAOSHI, commentMessage.getNews_teacher_id());
            }
            startActivity(i);
        }
    }
}
