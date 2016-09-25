package tv.kuainiu.ui.articles.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.NewsItem;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.articles.adapter.PostListAdapter;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.DebugUtils;
import tv.kuainiu.utils.TextColorUtil;
import tv.kuainiu.widget.DividerItemDecoration;
import tv.kuainiu.widget.NetErrAddLoadView;

import static tv.kuainiu.modle.cons.Constant.SUCCEED;


public class NewsListFragment extends BaseFragment {
    private static final String TAG_LIFE = "NewsListFragment_Life";
    private static final Enum[] mActions = {
            Action.fetch_news_list_0, Action.fetch_news_list_1, Action.fetch_news_list_2, Action.fetch_news_list_3,
            Action.fetch_news_list_4, Action.fetch_news_list_5, Action.fetch_news_list_6, Action.fetch_news_list_7,
            Action.fetch_news_list_9, Action.fetch_news_list_9
    };
    public static final String POSITION = "POSITION";
    @BindView(R.id.ptr_rv_layout)
    SwipeRefreshLayout mContentFrameLayout;
    @BindView(R.id.rv_posts)
    RecyclerView mRvPosts;
    @BindView(R.id.err_layout)
    NetErrAddLoadView mErrView;

    private View view = null;
    private String mJpType;
    private int mCatId;
    private int mCurPageNumber;
    private int mCurPosition;
    LinearLayoutManager mLayoutManager;
    /**
     * 默认
     */
    private String mUrl = Api.FIND_NEWS_LIST;

    private PostListAdapter mPostListAdapter;
    private Type TYPE_TOKEN_NEWS;
    private List<NewsItem> mPostList = new ArrayList<>();
    private DataConverter<NewsItem> mDataConverter;
    private boolean loading=false;


    public static NewsListFragment newInstance(int position, int catId) {
        return newInstance(position, catId, "");
    }

    public static NewsListFragment newInstance(int position, int catId, String jpType) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        args.putInt(Constant.KEY_CATID, catId);
        args.putString(Constant.KEY_JPTYPE, jpType);
        fragment.setArguments(args);
        return fragment;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    private void initContentView() {
        Activity activity = getActivity();
        mPostListAdapter = new PostListAdapter(activity);
        mRvPosts.setAdapter(mPostListAdapter);

        mLayoutManager = new LinearLayoutManager(activity);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvPosts.setLayoutManager(mLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL);
        itemDecoration.setItemSize(getResources().getDimensionPixelSize(R.dimen.small_margin));
        itemDecoration.setColor(TextColorUtil.generateColor(R.color.colorGrey200));
        mRvPosts.addItemDecoration(itemDecoration);

        mContentFrameLayout.setColorSchemeColors(Theme.getLoadingColor());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCatId = getArguments().getInt(Constant.KEY_CATID);
            mJpType = getArguments().getString(Constant.KEY_JPTYPE);
            mCurPosition = getArguments().getInt(POSITION);
        }
        mDataConverter = new DataConverter<>();
        TYPE_TOKEN_NEWS = new TypeToken<List<NewsItem>>() {
        }.getType();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_news_list, container, false);
        }
        ViewGroup viewgroup = (ViewGroup) view.getParent();
        if (viewgroup != null) {
            viewgroup.removeView(view);
        }
        ButterKnife.bind(this, view);
        initContentView();
        initListener();
        initHttp();

        return view;
    }

    @Override public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Override public void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPostListAdapter = null;
        if (mPostList != null) {
            mPostList.clear();
            mPostList = null;
        }
    }

    protected void initListener() {
        // 下拉刷新
        mContentFrameLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initHttp();
            }
        });
        // 上拉刷新
        mRvPosts.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        mCurPageNumber += 1;
                        fetchPostList();
                    }
                }
            }
        });

        mErrView.setOnTryCallBack(new NetErrAddLoadView.OnTryCallBack() {
            @Override
            public void callAgain() {
                initHttp();
            }
        });
    }


    private void initHttp() {
        mPostList.clear();
        mCurPageNumber = 1;
        fetchPostList();
    }


    private void fetchPostList() {
        String param = TextUtils.isEmpty(mJpType) ? prepareParam(mCurPageNumber, mCatId, "") :
                prepareParam(mCurPageNumber, mCatId, mJpType);
        OKHttpUtils.getInstance().syncGet(getActivity(), mUrl + param, (Action) mActions[mCurPosition]);
    }

    private String prepareParam(int pageNumber, int catId, String jpType) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constant.KEY_CATID, catId);
        map.put(Constant.KEY_JPTYPE, jpType);
        map.put(Constant.KEY_PAGE, pageNumber);
        map.put(Constant.KEY_SIZE, Constant.REQUEST_NEWS_SIZE);
        return ParamUtil.getParamForGet(map);
    }


    /**
     * 请求数据响应，UI线程回调
     *
     * @param event HttpEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostListEventMainThread(HttpEvent event) {
        if (mActions[mCurPosition] == event.getAction()) {
            mErrView.StopLoading(event.getCode(), event.getMsg());
            if (SUCCEED == event.getCode()) {
                JSONObject object = event.getData().optJSONObject("data");
                JSONArray array = object.optJSONArray("list");
                DebugUtils.dd("News list : " + object.toString());
                List<NewsItem> tempPostList= mDataConverter.JsonToListObject(array.toString(), TYPE_TOKEN_NEWS);
                if (tempPostList!=null && tempPostList.size()>0) {
                    loading=false;
                    mPostList.addAll(tempPostList);
                    mPostListAdapter.setNewsItemList(mPostList);
                    mPostListAdapter.notifyDataSetChanged();
                }
            }

        }
    }
}
