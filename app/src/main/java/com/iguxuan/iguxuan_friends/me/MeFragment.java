package com.iguxuan.iguxuan_friends.me;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iguxuan.iguxuan_friends.R;
import com.iguxuan.iguxuan_friends.app.Theme;
import com.iguxuan.iguxuan_friends.ui.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author nanck on 2016/7/29.
 */
public class MeFragment extends BaseFragment {

    @BindView(R.id.srlRefresh) SwipeRefreshLayout srlRefresh;

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        srlRefresh.setColorSchemeColors(Theme.getLoadingColor());
    }
}
