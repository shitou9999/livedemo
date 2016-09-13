package tv.kuainiu.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.widget.HeaderTabView;
import tv.kuainiu.widget.MyHeader;

/**
 * @author nanck on 2016/7/29.
 */
public class TabTempFragment extends BaseFragment {
    private static final String ARG_COLOR = "ARG_COLOR";
    private static final String ARG_CONTENT = "ARG_CONTENT";

    @BindView(R.id.fl_tab_frag) FrameLayout mFlPage;
    @BindView(R.id.tv_tab_frag_content) TextView mTvTabFragContent;

    HeaderTabView myhead;

    private int mPageColor;
    private String mPageContent;

    public static TabTempFragment newInstance(int pageColor, String pageContent) {
        TabTempFragment fragment = new TabTempFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLOR, pageColor);
        args.putString(ARG_CONTENT, pageContent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPageColor = getArguments().getInt(ARG_COLOR);
            mPageContent = getArguments().getString(ARG_CONTENT);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_temp, container, false);
        ButterKnife.bind(this, view);
        myhead = (HeaderTabView) view.findViewById(R.id.myhead);
        mFlPage.setBackgroundColor(mPageColor);
        mTvTabFragContent.setText(mPageContent);
        List<MyHeader> list = new ArrayList<>();
        MyHeader header = new MyHeader("回火", "#ff0000");
        list.add(header);
        header = new MyHeader("大大", "#ff0000");
        list.add(header);
        header = new MyHeader("升水", "#ff0000");
        list.add(header);
        myhead.setData(list);
        return view;
    }
}
