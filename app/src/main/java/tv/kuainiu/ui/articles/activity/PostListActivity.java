package tv.kuainiu.ui.articles.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.modle.NewsFlag;
import tv.kuainiu.ui.activity.BaseActivity;
import tv.kuainiu.ui.articles.fragment.NewsListFragment;
import tv.kuainiu.widget.TitleBarView;

/**
 * 文章列表页
 */
public class PostListActivity extends BaseActivity {
    public static final String NEWS_FLAG = "NEWS_FLAG";
    @BindView(R.id.tbv_title)
    TitleBarView tbvTitle;

    private NewsFlag mNewsFlag;

//    private int[] getParamForCatname(String catName) {
//        int[] arr = new int[2];
//        // 盘前
//        if ("盘前".equals(catName)) {
//            arr[0] = CatalogType.CatalogTypePredict.type();
//            arr[1] = R.mipmap.panqian_actionbar_ic;
//        }// 盘中
//        else if ("盘中".equals(catName)) {
//            arr[0] = CatalogType.CatalogTypeParsing.type();
//            arr[1] = R.mipmap.panzhong_actionbar_ic;
//        }// 盘后
//        else if ("盘后".equals(catName)) {
//            arr[0] = CatalogType.CatalogTypeParsed.type();
//            arr[1] = R.mipmap.panhou_actionbar_ic;
//        }// 知识
//        else if ("知识".equals(catName)) {
//            arr[0] = CatalogType.CatalogTypeKnowledge.type();
//            arr[1] = R.mipmap.knowledge_actionbar_ic;
//        }// 心法
//        else if ("心法".equals(catName)) {
//            arr[0] = CatalogType.CatalogTypeTips.type();
//            arr[1] = R.mipmap.citta_actionbar_ic;
//        }// 讲座
//        else if ("讲座".equals(catName)) {
//            arr[0] = CatalogType.CatalogTypeLecture.type();
//            arr[1] = R.mipmap.lecture_actionbar_ic;
//        } else {
//            throw new IllegalArgumentException("Invalid parameter for the catname!");
//        }
//        return arr;
//    }


    public static void intoNewIntent(Context context, NewsFlag newsFlag) {
        Intent intent = new Intent(context, PostListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(NEWS_FLAG, newsFlag);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    void initData(Intent intent) {
        if (intent == null) return;
        mNewsFlag = intent.getExtras().getParcelable(NEWS_FLAG);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        ButterKnife.bind(this);
        initData(getIntent());
        tbvTitle.setText(mNewsFlag.getCatname());
//        mToolbar.setBackgroundColor(Color.WHITE);
//        mToolbar.setNavigationIcon(R.mipmap.back_black_btn);
//        int resId = getParamForCatname(mNewsFlag.getCatname())[1];
//        mImgTop.setImageResource(resId);

        NewsListFragment fragment = NewsListFragment.newInstance(0, mNewsFlag.getCatid());

        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fl_fragment, fragment);
        transaction.show(fragment);
        transaction.commit();

    }

}
