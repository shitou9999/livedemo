package com.iguxuan.iguxuan_friends.region;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.quicksidebar.QuickSideBarTipsView;
import com.bigkoo.quicksidebar.QuickSideBarView;
import com.bigkoo.quicksidebar.listener.OnQuickSideBarTouchListener;
import com.iguxuan.iguxuan_friends.R;
import com.iguxuan.iguxuan_friends.ui.activity.BaseActivity;
import com.iguxuan.iguxuan_friends.widget.TitleBarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegionSelectionActivity extends BaseActivity implements OnQuickSideBarTouchListener {


    HashMap<String, Integer> letters = new HashMap<>();
    @BindView(R.id.tbv_title) TitleBarView mTbvTitle;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.quickSideBarView) QuickSideBarView mQuickSideBarView;
    @BindView(R.id.quickSideBarTipsView) QuickSideBarTipsView mQuickSideBarTipsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_selection);
        ButterKnife.bind(this);

        mTbvTitle.setText("选择国家和地区代码");
        mTbvTitle.setOnClickListening(new TitleBarView.OnClickListening() {
            @Override public void leftClick() {
                finish();
            }

            @Override public void rightClick() {

            }

            @Override public void titleClick() {

            }
        });
        //设置监听
        mQuickSideBarView.setOnQuickSideBarTouchListener(this);


        //设置列表数据和浮动header
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        // Add the sticky headers decoration
        // CityListWithHeadersAdapter adapter = new CityListWithHeadersAdapter();
        RegionListWithHeadersAdapter adapter = new RegionListWithHeadersAdapter();

        LinkedList<Region> cities = RegionDataHelper.getDataFromJson(this);

        ArrayList<String> customLetters = new ArrayList<>();

        int position = 0;
        for (Region region : cities) {
            String letter = region.getFirstLetter();
            //如果没有这个key则加入并把位置也加入
            if (!letters.containsKey(letter) && !TextUtils.isEmpty(letter)) {
                letters.put(letter, position);
                customLetters.add(letter);
            }
            position++;
        }

        //不自定义则默认26个字母
        mQuickSideBarView.setLetters(customLetters);
        adapter.addAll(cities);
        mRecyclerView.setAdapter(adapter);
        // Add decoration for dividers between list items
        mRecyclerView.addItemDecoration(new DividerDecoration(this));
    }


    @Override
    public void onLetterChanged(String letter, int position, int itemHeight) {
        mQuickSideBarTipsView.setText(letter, position, itemHeight);
        //有此key则获取位置并滚动到该位置
        if (letters.containsKey(letter)) {
            mRecyclerView.scrollToPosition(letters.get(letter));
        }
    }

    @Override
    public void onLetterTouching(boolean touching) {
        //可以自己加入动画效果渐显渐隐
        mQuickSideBarTipsView.setVisibility(touching ? View.VISIBLE : View.INVISIBLE);
    }


    private class RegionListWithHeadersAdapter extends RegionListAdapter<ViewHolder> {

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            String head = getItem(position).getFirstLetter();
            holder.tv_city.setText(String.format(Locale.CHINA, "%s(+%d)", getItem(position).getRegionName(), getItem(position).getRegionCode()));
            if (head != null && letters.get(head) != null && position == letters.get(head)) {
                holder.tv_head_top.setVisibility(View.VISIBLE);
            } else {
                holder.tv_head_top.setVisibility(View.GONE);
            }
            holder.tv_head_top.setText(head);
            holder.tv_city.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("region", getItem(position));
                    intent.putExtras(bundle);
                    setResult(300, intent);
                    finish();
                }
            });
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item, parent, false);
            return new ViewHolder(view);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_head_top) TextView tv_head_top;
        @BindView(R.id.tv_city) TextView tv_city;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
