package tv.kuainiu.ui.publishing.pick.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.MyApplication;
import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.command.http.core.OKHttpUtils;
import tv.kuainiu.command.http.core.ParamUtil;
import tv.kuainiu.event.HttpEvent;
import tv.kuainiu.modle.cons.Action;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.modle.push.CustomVideo;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.publishing.pick.PickArticleAdapter;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.StringUtils;
import tv.kuainiu.utils.ToastUtils;

import static android.app.Activity.RESULT_OK;
import static tv.kuainiu.ui.publishing.pick.PickArticleActivity.NEWS_ITEM;

/**
 */
public class PickArticleFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    @BindView(R.id.lvArticleList)
    ListView lvArticleList;
    private List<CustomVideo> customVideoList = new ArrayList<>();
    private View view;
    private PickArticleAdapter mPickArticleAdapter;
    private int page = 1;
    private int pageSize = 10;

    public PickArticleFragment() {

    }

    public static PickArticleFragment newInstance(String param1, String param2) {
        PickArticleFragment fragment = new PickArticleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_pick_article, container, false);
            ButterKnife.bind(this, view);
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
            initView();
            initData();
        } else {
            ViewGroup viewgroup = (ViewGroup) view.getParent();
            if (viewgroup != null) {
                viewgroup.removeView(view);
            }
        }
        return view;
    }

    private void initView() {
        lvArticleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(NEWS_ITEM, customVideoList.get(position));
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
            }
        });
    }

    private void initData() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("size", String.valueOf(pageSize));
        map.put("teacher_id", MyApplication.getUser() == null ? "" : MyApplication.getUser().getUser_id());
        map.put("type", "1");
        OKHttpUtils.getInstance().syncGet(getActivity(), Api.FIND_NEWS_LIST + ParamUtil.getParamForGet(map), Action.teacher_zone_appoint);
    }

    private void dataBindView() {
        if (mPickArticleAdapter == null) {
            mPickArticleAdapter = new PickArticleAdapter(getActivity(), customVideoList);
            lvArticleList.setAdapter(mPickArticleAdapter);
        } else {
            mPickArticleAdapter.notifyDataSetChanged();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event) {
        switch (event.getAction()) {
            case teacher_zone_appoint:
                if (page == 1) {
                    customVideoList.clear();
                }
                if (Constant.SUCCEED == event.getCode()) {
                    if (event.getData() != null && event.getData().has("data")) {
                        try {
                            JSONObject jsonObject = event.getData().getJSONObject("data");
                            List<CustomVideo> tempCustomVideoList = new DataConverter<CustomVideo>().JsonToListObject(jsonObject.getString("list"), new TypeToken<List<CustomVideo>>() {
                            }.getType());
                            if (tempCustomVideoList != null && tempCustomVideoList.size() > 0) {
                                int size = customVideoList.size();
                                customVideoList.addAll(tempCustomVideoList);
                                dataBindView();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showToast(getActivity(), "文章信息解析失败");
                        }

                    }
                } else {
                    ToastUtils.showToast(getActivity(), StringUtils.replaceNullToEmpty(event.getMsg(), "获取文章信息失败"));
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
