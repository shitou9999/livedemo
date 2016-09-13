package tv.kuainiu.live.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.app.Theme;
import tv.kuainiu.live.adapter.ReadingTapeAdapter;
import tv.kuainiu.ui.fragment.BaseFragment;

/**
 * 咨询
 */
public class ReadingTapeFragment extends BaseFragment {
    private static final String ARG_POSITION = "ARG_POSITION";

    @BindView(R.id.srlRefresh) SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rvReadingTap) RecyclerView rvReadingTap;

    private int mParentPosition;
    private ReadingTapeAdapter mReadingTapeAdapter;

    public static ReadingTapeFragment newInstance(int parentPosition) {
        ReadingTapeFragment fragment = new ReadingTapeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, parentPosition);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParentPosition = getArguments().getInt(ARG_POSITION);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_reading_tap, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        initListener();

        return view;
    }

    private void initView() {
        srlRefresh.setColorSchemeColors(Theme.getLoadingColor());

    }

    private void initData() {
    }

    private void initListener() {
        dataBind();
    }

    private void dataBind() {
        if (mReadingTapeAdapter == null) {
            mReadingTapeAdapter = new ReadingTapeAdapter(getActivity());
            rvReadingTap.setAdapter(mReadingTapeAdapter);
        } else {
            mReadingTapeAdapter.notifyDataSetChanged();
        }
    }

}
