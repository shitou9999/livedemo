package tv.kuainiu.ui.me.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import cn.finalteam.loadingviewfinal.ListViewFinal;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.NewsItem;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.ui.BaseActivity;
import tv.kuainiu.ui.me.adapter.EnshrineAdapter;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.ToastUtils;
import tv.kuainiu.widget.NetErrAddLoadView;

import static tv.kuainiu.modle.cons.Constant.SUCCEED;

/**
 * 我的收藏
 */

public class CollectActivity extends BaseActivity {
    private int page = 1;
    private int size = 20;
    private List<NewsItem> listCollect = new ArrayList<>();
    private List<NewsItem> selectedListCollect = new ArrayList<>();
    private List<NewsItem> deleteListCollect = new ArrayList<>();
    private EnshrineAdapter adapterCollectAdapter;
    @BindView(R.id.ptr_rv_layout)
    PtrClassicFrameLayout mContentFrameLayout;
    @BindView(R.id.lv_custom)
    ListViewFinal lv_custom;

    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.ll_bottom_tab)
    LinearLayout ll_bottom_tab;
    @BindView(R.id.tv_selected_add)
    TextView tv_selected_add;
    @BindView(R.id.tv_delete_selected)
    TextView tv_delete_selected;

    @BindView(R.id.nav_tip)
    NetErrAddLoadView nav_tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initContentView(int layoutId) {
        super.initContentView(R.layout.activity_enshrine);
        setMyTitle("我的收藏");
        mContentFrameLayout.setLastUpdateTimeRelateObject(this);
        mContentFrameLayout.disableWhenHorizontalMove(true);
        lv_custom.setHasLoadMore(true);
        getCollectList();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mContentFrameLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getCollectList();
            }
        });
        lv_custom.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                page += 1;
                getCollectList();
            }
        });
//        lv_custom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (listCollect.size() > 0 && listCollect.size() > (position - lv_custom.getHeaderViewsCount())) {
//                    NewsItem newsItem = listCollect.get(position - lv_custom.getHeaderViewsCount());
//                    jump(newsItem);
//                }
//            }
//        });
        setRightButton(R.mipmap.del);
        setRightButtonVisibility(View.VISIBLE);
        setRightTitle("取消");
        setRightTitleVisibility(View.GONE);
        tv_selected_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedListCollect.size() != listCollect.size()) {
                    selectedListCollect.clear();
                    selectedListCollect.addAll(listCollect);
                    tv_selected_add.setText("全不选");
                    notfiySelectState();
                } else {
                    selectedListCollect.clear();
                    notfiySelectState();
                    tv_selected_add.setText("全　选");
                }
            }
        });
        tv_delete_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListCollect.clear();
                deleteListCollect.addAll(selectedListCollect);
                delCollect(deleteListCollect);
            }
        });
        nav_tip.setOnTryCallBack(new NetErrAddLoadView.OnTryCallBack() {
            @Override
            public void callAgain() {
                page = 1;
                getCollectList();
            }
        });
    }

    @Override
    protected void onRightTitleClick() {
        super.onRightTitleClick();
        if (adapterCollectAdapter == null) {
            return;
        }
        if (!adapterCollectAdapter.getEdit()) {
            setRightButtonVisibility(View.GONE);
            setRightTitleVisibility(View.VISIBLE);
        } else {
            setRightButtonVisibility(View.VISIBLE);
            setRightTitleVisibility(View.GONE);
        }

        adapterCollectAdapter.setIsEdit(!adapterCollectAdapter.getEdit());
        adapterCollectAdapter.notifyDataSetChanged();
        ll_bottom_tab.setVisibility(adapterCollectAdapter.getEdit() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();

    }

    /* 添加收藏  */
    private void getCollectList() {
        LogUtils.i("collect", "getCollectList=");
        Map map = new HashMap();
        map.put("page", String.valueOf(page));
        map.put("size", String.valueOf(size));
        OKHttpUtils.getInstance().post(this, Api.COLLECT_LIST, ParamUtil.getParam(map), Action.collect_list);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case collect_list:

                LogUtils.i("collect", "getData=" + event.getData());
                LogUtils.i("collect", "getMsg=" + event.getMsg());

                if (SUCCEED == event.getCode()) {
                    try {
                        JsonParser parser = new JsonParser();
                        JsonObject tempJson = (JsonObject) parser.parse(event.getData().toString());
                        JsonArray json = tempJson.getAsJsonObject("data").getAsJsonArray("list");
                        List<NewsItem> listTeacherTemp = new Gson().fromJson(json, new TypeToken<List<NewsItem>>() {
                        }.getType());
                        if (page == 1) {
                            listCollect.clear();
                        }
                        if (listTeacherTemp != null && listTeacherTemp.size() > 0) {
                            listCollect.addAll(listTeacherTemp);
                            if (listTeacherTemp.size() < size) {
                                lv_custom.setHasLoadMore(false);
                                lv_custom.onLoadMoreComplete();
                            } else {
                                lv_custom.setHasLoadMore(true);
                            }
                        } else {
                            lv_custom.setHasLoadMore(false);
                            lv_custom.onLoadMoreComplete();
                        }

                        dataBindView();
                    } catch (Exception e) {
                        LogUtils.e(CollectActivity.class.getSimpleName(), "收藏json解析异常", e);
                    } finally {
                        try {
                            if (page > 1) {
                                lv_custom.onLoadMoreComplete();
                            }
                            mContentFrameLayout.onRefreshComplete();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                    }
                }
                nav_tip.StopLoading(event.getCode(), event.getMsg());
                break;
            case del_collect:
                if (SUCCEED == event.getCode()) {
                    if (deleteListCollect != null && deleteListCollect.size() > 0) {
                        for (NewsItem item : deleteListCollect) {
                            if (selectedListCollect.contains(item)) {
                                selectedListCollect.remove(item);
                            }
                            if (listCollect.contains(item)) {
                                listCollect.remove(item);
                            }
                        }
                    }
                    tv_delete_selected.setText(String.format(Locale.CHINA, "删除（%d）", selectedListCollect.size()));
                    dataBindView();
                    ToastUtils.showToast(this, "删除成功");
                } else {
                    ToastUtils.showToast(this, event.getMsg());
                }
                break;
        }
    }

    private void dataBindView() {
        if (adapterCollectAdapter == null) {
            adapterCollectAdapter = new EnshrineAdapter(this, listCollect);
            adapterCollectAdapter.setOnItemDelete(new EnshrineAdapter.IdeletItem() {
                @Override
                public void delete(SwipeLayout swipeLayout, int position, NewsItem newsItem) {
                    deleteListCollect.clear();
                    deleteListCollect.add(newsItem);
                    delCollect(deleteListCollect);
                    swipeLayout.close(true);
                }

                @Override
                public void selected(NewsItem newsItem) {
                    selectedListCollect.add(newsItem);
                    notfiySelectState();
                }

                @Override
                public void unelected(NewsItem newsItem) {
                    if (selectedListCollect.contains(newsItem)) {
                        selectedListCollect.remove(newsItem);
                        notfiySelectState();
                    }
                }
            });
            lv_custom.setAdapter(adapterCollectAdapter);
        } else {
            adapterCollectAdapter.notifyDataSetChanged();
        }
        deleteListCollect.clear();
    }

    private void notfiySelectState() {
        if (adapterCollectAdapter == null) {
            return;
        }
        if (selectedListCollect.size() != listCollect.size()) {
            tv_selected_add.setText("全　选");
        } else {
            tv_selected_add.setText("全不选");
        }
        adapterCollectAdapter.setSelectedListCollect(selectedListCollect);
        adapterCollectAdapter.notifyDataSetChanged();
        tv_delete_selected.setText(String.format(Locale.CHINA, "删除（%d）", selectedListCollect.size()));
    }

    /* 取消收藏 */
    private void delCollect(List<NewsItem> deleteListCollect) {
        if (deleteListCollect == null || deleteListCollect.size() < 1) {
            ToastUtils.showToast(CollectActivity.this, "请选择删除项");
            return;
        }
        String news_id = "";
        for (int i = 0; i < deleteListCollect.size(); i++) {
            news_id += deleteListCollect.get(i).getId();
            if (i < deleteListCollect.size() - 1) {
                news_id += ",";
            }
        }
        Map map = new HashMap();
        map.put("news_id", news_id);
        OKHttpUtils.getInstance().post(this, Api.DEL_COLLECT, ParamUtil.getParam(map), Action.del_collect);
    }

}
