package tv.kuainiu.ui.comments.fragmet;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.CommentHttpUtil;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.CommentItem;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.modle.push.SubCommentItem;
import tv.kuainiu.ui.comments.adapter.CommentListAdapter;
import tv.kuainiu.ui.comments.adapter.SimpleCommentAdapter;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.me.activity.LoginActivity;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.KeyBoardUtil;
import tv.kuainiu.utils.LoadingProgressDialog;
import tv.kuainiu.utils.NetUtils;
import tv.kuainiu.utils.TextColorUtil;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.DividerItemDecoration;
import tv.kuainiu.widget.dialog.LoginPromptDialog;

import static tv.kuainiu.modle.cons.Constant.SUCCEED;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostCommentListFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "PostCommentListFragment";
    private static final String TAG_LIFE = "PostCommentListFragment_life";
    private static final int PAGE_SIZE = Constant.REQUEST_SIZE;
    public static final String HINT = "我来评论..(限140个字符)";

    @BindView(R.id.ptr_rv_layout)
    SwipeRefreshLayout mContentFrameLayout;
    @BindView(R.id.rv_items)
    RecyclerView mRvComments;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.btn_publish)
    Button mBtnPublish;

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_MODE = "ARG_MODE";
    public static final int MODE_ARTICLE = 1;
    public static final int MODE_DYNAMIC = 2;


    private CommentListAdapter mPostCommentListAdapter;
    private SimpleCommentAdapter mTeacherCommentAdapter;

    private String mPostId;
    private String dynamics_id;
    private String mCatId;
    private LinkedList<CommentItem> mAllCommentList = new LinkedList<>();
    private LinkedList<CommentItem> mTeacherCommentList = new LinkedList<>();

    private String mTempCommentID;
    private String tempParentNickname;
    private String tempParentId;
    private TextView mTempFavourTextView;
    private CommentItem mTempCommentItem;

    private boolean isReply;
    private int mCurPageIndex;

    private int mPage;
    private int mMode;

    private int hotLength = 0;
    private PopupWindow keyboardPopup;
    private View view;
    private boolean isShowLoginTip = false;
    private boolean loading = false;
    private LinearLayoutManager mLayoutManager;

    public static PostCommentListFragment newInstance(int page, int mode) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putInt(ARG_MODE, mode);
        PostCommentListFragment fragment = new PostCommentListFragment();

        fragment.setArguments(args);
        return fragment;
    }

    private void initData(Intent data) {
        if (data == null) {
            return;
        }
        mPostId = data.getStringExtra(Constant.KEY_ID);
        mCatId = data.getStringExtra(Constant.KEY_CATID);
        if (MODE_DYNAMIC == mMode) {
            dynamics_id = mPostId;
            mPostId = "";
        }
        mCurPageIndex = Constant.DEFAULT_PAGE_NUMBER;
        mTempCommentID = "";
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        mMode = getArguments().getInt(ARG_MODE);

        initData(getActivity().getIntent());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        view = inflater.inflate(R.layout.fragment_post_comment_list, container, false);
        ButterKnife.bind(this, view);

        final FragmentActivity activity = getActivity();

        mLayoutManager = new LinearLayoutManager(activity);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvComments.setLayoutManager(mLayoutManager);

        DividerItemDecoration did = new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL);
        did.setItemSize(2);
        did.setColor(TextColorUtil.generateColor(R.color.def_DividerColor));
        mRvComments.addItemDecoration(did);
        mContentFrameLayout.setColorSchemeColors(Theme.getLoadingColor());

        setAdapter(activity);
        intHttpRequest();
        initListener();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    private void initListener() {
        mBtnPublish.setOnClickListener(this);
        tv_content.setOnClickListener(this);
        mContentFrameLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurPageIndex = Constant.DEFAULT_PAGE_NUMBER;
                intHttpRequest();
            }
        });
        mRvComments.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //向下滚动
                {
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (!loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = true;

                        mCurPageIndex += 1;

                        if (MODE_ARTICLE == mMode) {
                            if (0 == mPage) {
                                fetchAllComment(mCurPageIndex);
                            } else {
                                fetchTeacherComment(mCurPageIndex);
                            }
                        } else {
                            if (0 == mPage) {
                                fetchAllComment(mCurPageIndex);
                            } else {
                                fetchTeacherComment(mCurPageIndex);
                            }
                        }
                    }
                }
            }
        });


        /* 回复事件 */
        mPostCommentListAdapter.setOnRecyclerItemClickListener(new CommentListAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view) {
//                if (!MyApplication.isLogin()) {
//                    new LoginPromptDialog(getActivity()).show();
//                    return;
//                }
                isReply = true;
                CommentItem commentItem = (CommentItem) view.getTag();
                if (commentItem == null) return;
//                mEtContent.setHint("回复:" + commentItem.getNickname());
                tempParentNickname = commentItem.getNickname();
//                tempParentId = commentItem.getNickname();

                mTempCommentID = commentItem.getId();
                showCommentPopupWindows("回复:" + commentItem.getNickname());
//                KeyBoardUtil.showKeyBoard(getActivity(), mEtContent);
            }
        });
//
        /* 点赞事件 */

        mPostCommentListAdapter.setOnStart(new CommentListAdapter.OnStart() {
            @Override
            public void onStart(View view) {
                if (!MyApplication.isLogin()) {
                    showLoginTip();
                    return;
                }
                if (!NetUtils.isOnline(getActivity())) {
                    DebugUtils.showToast(getActivity(), R.string.toast_not_network);
                    return;
                }
                mTempFavourTextView = (TextView) view;
                CommentItem commentItem = (CommentItem) view.getTag();
                mTempCommentItem = commentItem;
                favourForCommentId(commentItem.getId());
            }
        });


        // ****************
        // ****************
        mTeacherCommentAdapter.setOnRecyclerItemClickListener(new SimpleCommentAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View v) {
                if (!MyApplication.isLogin()) {
                    showLoginTip();
                    return;
                }
                isReply = true;
                CommentItem commentItem = (CommentItem) v.getTag();
                if (commentItem == null) return;
//                mEtContent.setHint("回复:" + commentItem.getNickname());
                tempParentNickname = commentItem.getNickname();
                mTempCommentID = commentItem.getId();
                showCommentPopupWindows("回复:" + commentItem.getNickname());
//                KeyBoardUtil.showKeyBoard(getActivity(), mEtContent);
            }
        });


        mTeacherCommentAdapter.setOnStart(new SimpleCommentAdapter.OnStart() {
            @Override
            public void onStart(View v) {
                if (!MyApplication.isLogin()) {
                    showLoginTip();
                    return;
                }
                if (!NetUtils.isOnline(getActivity())) {
                    DebugUtils.showToast(getActivity(), R.string.toast_not_network);
                    return;
                }
                mTempFavourTextView = (TextView) v;
                CommentItem commentItem = (CommentItem) v.getTag();
                mTempCommentItem = commentItem;
                favourForCommentId(commentItem.getId());
            }
        });
    }

    private void showLoginTip() {
        if (isShowLoginTip) {
            return;
        }
        isShowLoginTip = true;
        LoginPromptDialog loginPromptDialog = new LoginPromptDialog(getActivity());
        loginPromptDialog.setCallBack(new LoginPromptDialog.CallBack() {
            @Override
            public void onCancel(DialogInterface dialog, int which) {

            }

            @Override
            public void onLogin(DialogInterface dialog, int which) {
                Intent intent = new Intent(PostCommentListFragment.this.getActivity(), LoginActivity.class);
                PostCommentListFragment.this.getActivity().startActivity(intent);
            }

            @Override
            public void onDismiss(DialogInterface dialog) {
                isShowLoginTip = false;

            }
        });
        loginPromptDialog.show();
    }

    @Override
    public void onClick(View v) {
        Activity activity = getActivity();
        if (activity == null) return;

        switch (v.getId()) {
            case R.id.tv_content:
            case R.id.btn_publish:
//                if (!MyApplication.isLogin()) {
//                    new LoginPromptDialog(activity).show();
//                    return;
//                }
//                if (TextUtils.isEmpty(content)) {
//                    DebugUtils.showToast(activity, "评论不能为空");
//                    return;
//                }
//                if (!NetUtils.isOnline(activity)) {
//                    DebugUtils.showToast(activity, R.string.toast_not_network);
//                    return;
//                }
//                LoadingProgressDialog.startProgressDialog(activity);
//                CommentHttpUtil.addComment(activity, mPostId, mCatId, content, mTempCommentID, MyApplication.getUser().getNickname(), "0");
                showCommentPopupWindows(HINT);
                break;
            default:
                break;
        }
    }

    Button button;
    private boolean isShowPop = false;

    private void showCommentPopupWindows(String hint) {
        if (isShowPop) {
            return;
        }
        isShowPop = true;
        View contentView = getActivity().getLayoutInflater().inflate(R.layout.popup_keyboard_edit_comment, null);
        final EditText editComment = (EditText) contentView.findViewById(R.id.et_edit_comment);
        button = (Button) contentView.findViewById(R.id.btn_publish);
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
        keyboardPopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        keyboardPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isShowPop = false;
            }
        });
        KeyBoardUtil.showKeyBoard(getActivity(), editComment);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyApplication.isLogin()) {
                    showLoginTip();
                    return;
                }

                String content = editComment.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showToast(getActivity(), "评论内容不能为空");
                    return;
                }
                KeyBoardUtil.hideSoftInput(getActivity(), editComment);
                String nickname = MyApplication.getUser().getNickname();
                LoadingProgressDialog.startProgressDialog(getActivity());
//                TODO: 添加评论
//                  type          评论类型     必传      1是对文章的评论 2是对动态的评论
//                nick_name     用户名     非必传
//                news_id               文章ID     type为1时必传
//                dynamics_id     动态ID     type为2时必传
//                is_reply          非必传     是否为回复评论   否0是1
//                comment_id  父评论ID      选填      如为回复的话，需传
//                content      评论内容    必传
//                key_id          评论消息列表的key_id
//                reply_teacher         是否为回复老师的评论
                CommentHttpUtil.addComment(getActivity(), String.valueOf(mMode), nickname,
                        mPostId, dynamics_id, isReply ? "1" : "0",mTempCommentID, content, mTempCommentID, "0");

                editComment.setHint(HINT);
                editComment.setText("");
                keyboardPopup.dismiss();

            }
        });

        viewTopArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtil.hideSoftInput(getActivity(), editComment);
                if (null != keyboardPopup && keyboardPopup.isShowing()) {
                    keyboardPopup.dismiss();
                }
            }
        });

    }

    private void setAdapter(Activity activity) {
        mPostCommentListAdapter = new CommentListAdapter(activity);
        mTeacherCommentAdapter = new SimpleCommentAdapter(activity);

        if (MODE_ARTICLE == mMode) {
            if (0 == mPage) {
                mRvComments.setAdapter(mPostCommentListAdapter);
            } else {
                mRvComments.setAdapter(mTeacherCommentAdapter);
            }
        } else {
            mRvComments.setAdapter(mTeacherCommentAdapter);
        }
    }

    private void intHttpRequest() {
        if (MODE_ARTICLE == mMode) {
            if (0 == mPage) {
                fetchHotComment();
            } else {
                fetchTeacherComment(Constant.DEFAULT_PAGE_NUMBER);
            }
        } else {
            if (0 == mPage) {
                fetchAllComment(Constant.DEFAULT_PAGE_NUMBER);
            } else {
                fetchTeacherComment(Constant.DEFAULT_PAGE_NUMBER);
            }
        }
    }


    private void fetchAllComment(int page) {
        fetchAllComment(page, false, Action.comment_list);
    }

    private void fetchTeacherComment(int page) {
        fetchAllComment(page, true, Action.comment_teacher);
    }

    /* 获取全部评论 */
    private void fetchAllComment(int page, boolean just_teacher, Action action) {
        String just = just_teacher ? "1" : "0";
        Map<String, String> map = new HashMap<>();
        map.put("type", String.valueOf(mMode));
        map.put("news_id", mPostId);
        map.put("dynamics_id", mPostId);
        map.put("cat_id", mCatId);
        map.put(Constant.KEY_PAGE, String.valueOf(page));
        map.put("just_teacher", just);
        String param = ParamUtil.getParam(map);
        OKHttpUtils.getInstance().post(getActivity(), Api.TEST_DNS_API_HOST, Api.COMMENT_LIST, param, action);
    }


    /* 获取热门评论 */
    private void fetchHotComment() {
        Map<String, String> map = new HashMap<>();
        map.put("type", String.valueOf(mMode));
        map.put("news_id", mPostId);
        map.put("dynamics_id", mPostId);
        map.put("cat_id", mCatId);
        String param = ParamUtil.getParam(map);
        OKHttpUtils.getInstance().post(getActivity(), Api.TEST_DNS_API_HOST, Api.COMMENT_LIST_HOT, param, Action.comment_list_hot);
    }


    /**
     * 赞某条评论
     *
     * @param commentId 评论ID
     */
    private void favourForCommentId(String commentId) {
        if (MyApplication.isLogin()) {
            Map<String, String> map = new HashMap<>();
            map.put("id", commentId);
            String param = ParamUtil.getParam(map);
            OKHttpUtils.getInstance().post(getActivity(), Api.TEST_DNS_API_HOST_V2, Api.FAVOUR_COMMENT, param, Action.favour_comment);
        } else {
            new LoginPromptDialog(getActivity()).show();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFetchHotComment(HttpEvent event) {

        if (Action.comment_list_hot == event.getAction()) {
            LoadingProgressDialog.stopProgressDialog();
            if (Constant.SUCCEED == event.getCode()) {
                // log
                DebugUtils.dd("hot string : " + event.getData().toString());

                JSONObject jsonObject = event.getData().optJSONObject("data");
                JSONArray jsonArray = jsonObject.optJSONArray("list");

                List<CommentItem> list = new DataConverter<CommentItem>().JsonToListObject(jsonArray.toString(), new TypeToken<List<CommentItem>>() {
                }.getType());

                hotLength = list.size();
                mPostCommentListAdapter.setHotData(list);
            }
            fetchAllComment(Constant.DEFAULT_PAGE_NUMBER);
        }
    }

    // EventBus call all comment list
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFetchAllComment(HttpEvent event) {
        if (Action.comment_list == event.getAction()) {
            mContentFrameLayout.setRefreshing(false);
            if (SUCCEED == event.getCode()) {
                DebugUtils.dd("comment list : " + event.getData().toString());

                JSONObject jsonObject = event.getData().optJSONObject("data");
                JSONArray jsonArray = jsonObject.optJSONArray("list");

                if (jsonArray != null) {
                    List<CommentItem> list = new DataConverter<CommentItem>().JsonToListObject(jsonArray.toString(), new TypeToken<List<CommentItem>>() {
                    }.getType());
                    if (mCurPageIndex == 1) {
                        mAllCommentList.clear();
                    }
                    if (list != null && list.size() > 0) {
                        loading = false;
                        mAllCommentList.addAll(list);
                    }

                    if (MODE_ARTICLE == mMode) {
                        if (0 == mPage) {
                            mPostCommentListAdapter.setAllData(mAllCommentList);
                            mPostCommentListAdapter.notifyDataSetChanged();
                        }
                    } else {
                        if (0 == mPage) {
                            mTeacherCommentAdapter.setAllData(mAllCommentList);
                            mTeacherCommentAdapter.notifyDataSetChanged();
                        }
                    }

                }
            }
        }
    }


    // EventBus call teacher comment list
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFetchTeacherComment(HttpEvent event) {
        if (Action.comment_teacher == event.getAction()) {
            mContentFrameLayout.setRefreshing(false);
            LoadingProgressDialog.stopProgressDialog();
            if (SUCCEED == event.getCode()) {
                DebugUtils.dd("comment list : " + event.getData().toString());
                JSONObject jsonObject = event.getData().optJSONObject("data");
                JSONArray jsonArray = jsonObject.optJSONArray("list");

                if (jsonArray != null) {
                    List<CommentItem> list = new DataConverter<CommentItem>().JsonToListObject(jsonArray.toString(), new TypeToken<List<CommentItem>>() {
                    }.getType());
                    if (mCurPageIndex == 1) {
                        mTeacherCommentList.clear();
                    }
                    if (list != null && list.size() > 0) {
                        loading = false;
                        mTeacherCommentList.addAll(list);
                    }
                    mTeacherCommentAdapter.setAllData(mTeacherCommentList);
                    mTeacherCommentAdapter.notifyDataSetChanged();
                }
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAddComment(HttpEvent event) {
        if (Action.add_comment == event.getAction()) {
            // 关闭进度条
            LoadingProgressDialog.stopProgressDialog();

            if (SUCCEED == event.getCode()) {
//                KeyBoardUtil.hideSoftInput(getActivity(), mEtContent);
//                mEtContent.setHint("我来说两句");
//                mEtContent.setText("");


                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(event.getData().toString());
                String jsonDataString = json.getAsJsonObject("data").toString();
                SubCommentItem subCommentItem = new DataConverter<SubCommentItem>().JsonToObject(jsonDataString, SubCommentItem.class);

                // log
                DebugUtils.dd("comment result : " + event.getData().toString());
                DebugUtils.dd("sub comment result :" + subCommentItem.toString());

                CommentItem commentItem = new CommentItem();
                commentItem.setParent_comment_id(mTempCommentID);
                commentItem.setId(subCommentItem.getComment_data_id());

                commentItem.setNickname(subCommentItem.getComment_info().getUser_name());
                commentItem.setAvatar(subCommentItem.getComment_info().getDefault_avatar());

                commentItem.setIs_support(0);
                commentItem.setSupport(0);

                commentItem.setIs_teacher(subCommentItem.getComment_info().getIs_teacher());

                commentItem.setParent_is_teacher(subCommentItem.getComment_info().getParent_is_teacher());
                commentItem.setCreate_date(String.valueOf(subCommentItem.getComment_info().getCreate_date()));

                commentItem.setContent(subCommentItem.getComment_info().getContent());

                commentItem.setSource_show(subCommentItem.getComment_info().getSource_show());

                if (isReply) {
                    commentItem.setParent_comment_id(mTempCommentID);
                    commentItem.setParent_user_name(tempParentNickname);
                    commentItem.setParent_comment_content(subCommentItem.getComment_info().getParent_comment_content());
                    commentItem.setParent_is_teacher(subCommentItem.getComment_info().getParent_is_teacher());
                }


                if (MODE_ARTICLE == mMode) {
                    if (0 == mPage) {
                        mAllCommentList.addFirst(commentItem);
                        mRvComments.smoothScrollToPosition(2 + hotLength - 1);
                        mPostCommentListAdapter.notifyItemInserted(2 + hotLength - 1);
                    } else {
                        if (Constant.BASE_TRUE == subCommentItem.getComment_info().getIs_teacher()) {
                            mTeacherCommentList.addFirst(commentItem);
                            mRvComments.smoothScrollToPosition(0);
                            mTeacherCommentAdapter.notifyItemInserted(0);
                        }
                    }
                } else {
                    if (0 == mPage) {
                        mAllCommentList.addFirst(commentItem);
                        mRvComments.smoothScrollToPosition(0);
                        mTeacherCommentAdapter.notifyItemInserted(0);
//                        mRvComments.scrollTo(0, 0);

                    } else {
                        if (Constant.BASE_TRUE == subCommentItem.getComment_info().getIs_teacher()) {
                            mTeacherCommentList.addFirst(commentItem);
                            mRvComments.smoothScrollToPosition(0);
                            mTeacherCommentAdapter.notifyItemInserted(0);
                        }
                    }
                }

                DebugUtils.showToast(getActivity(), "评论成功");
                mTempCommentID = "";


            } else {
                DebugUtils.showToastResponse(getActivity(), event.getMsg());
            }
            isReply = false;


        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventFavourForCommentonEventFavourForComment(HttpEvent event) {
        if (Action.favour_comment == event.getAction()) {
            if (SUCCEED == event.getCode()) {
                if (null != mTempFavourTextView && null != mTempCommentItem) {
                    mTempCommentItem.setIs_support(Constant.FAVOURED);
                    mTempCommentItem.setSupport(mTempCommentItem.getSupport() + 1);
                    mPostCommentListAdapter.resetFavourView(mTempFavourTextView, mTempCommentItem);
                }
                DebugUtils.showToast(getActivity(), "点赞成功");
            } else {
                DebugUtils.showToastResponse(getActivity(), event.getMsg());
            }
        }
    }

}
