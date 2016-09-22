package tv.kuainiu.ui.friends.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tv.kuainiu.R;
import tv.kuainiu.ui.fragment.BaseFragment;
import tv.kuainiu.ui.friends.model.Message;

/**
 * @author nanck on 2016/7/29.
 */
public class FriendsTabFragment extends BaseFragment {
    private static final String TAG = "FriendsTabFragment";

    @BindView(R.id.rv_fragment_friends_tab) RecyclerView mRecyclerView;
    private List<Message> mMessages = new ArrayList<>();

    public static FriendsTabFragment newInstance() {
        Bundle args = new Bundle();
        FriendsTabFragment fragment = new FriendsTabFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_tab, container, false);
        ButterKnife.bind(this, view);

        Activity context = getActivity();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        parseJson();

//        FriendsPostAdapter adapter = new FriendsPostAdapter(context, mMessages);
//        mRecyclerView.setAdapter(adapter);

        return view;
    }


    private static final String JSON = "[\n" +
            "  {\n" +
            "    \"publish_timestamp\": 2342342342,\n" +
            "    \"head_photo\": \"http://img4.imgtn.bdimg.com/it/u=687186100,1032324030&fm=21&gp=0.jpg\",\n" +
            "    \"nickname\": \"王丽\",\n" +
            "    \"message_type\": 1,\n" +
            "    \"message_content\": \"写完此文, 偶然机会在InfoQ上看到Uber的技术主管Raffi Krikorian在 O’Reilly Software Architecture conference上谈及的关于架构重构的12条规则, 共勉之。\",\n" +
            "    \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "    \"comment_count\": 233,\n" +
            "    \"like_count\": 134,\n" +
            "    \"like_before\": 0,\n" +
            "    \"type\": 2\n" +
            "  },\n" +
            "  {\n" +
            "    \"publish_timestamp\": 2342342342,\n" +
            "    \"head_photo\": \"http://img1.imgtn.bdimg.com/it/u=380201267,4002174318&fm=23&gp=0.jpg\",\n" +
            "    \"nickname\": \"angel\",\n" +
            "    \"message_type\": 2,\n" +
            "    \"message_content\": \"写完此文, 偶然机会在InfoQ上看到Uber的技术主管Raffi Krikorian在 O’Reilly Software Architecture conference上谈及的关于架构重构的12条规则, 共勉之。\",\n" +
            "    \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "    \"comment_count\": 456,\n" +
            "    \"like_count\": 764,\n" +
            "    \"like_before\": 0,\n" +
            "    \"type\": 1\n" +
            "  },\n" +
            "  {\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"miss\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"鉴于20个设计的核心要素和原则（上）取得了不错的反响，所以我决定把下篇也出了，希望能帮到大家！\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\": 4\n" +
            "    },\n" +
            "   {\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"周小川\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"写完此文, 偶然机会在InfoQ上看到Uber的技术主管Raffi Krikorian在 O’Reilly Software Architecture conference上谈及的关于架构重构的12条规则, 共勉之。\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\": 4\n" +
            "    },\n" +
            "  {\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"papi酱\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"鉴于20个设计的核心要素和原则（上）取得了不错的反响，所以我决定把下篇也出了，希望能帮到大家！\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\": 1\n" +
            "    },\n" +
            "   {\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"周小川\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"写完此文, 偶然机会在InfoQ上看到Uber的技术主管Raffi Krikorian在 O’Reilly Software Architecture conference上谈及的关于架构重构的12条规则, 共勉之。\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\": 2\n" +
            "    },\n" +
            "\t {\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"刘念\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"鉴于20个设计的核心要素和原则（上）取得了不错的反响，所以我决定把下篇也出了，希望能帮到大家！\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\": 4\n" +
            "    },\n" +
            "\t{\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"李青\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"鉴于20个设计的核心要素和原则（上）取得了不错的反响，所以我决定把下篇也出了，希望能帮到大家！\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\":2\n" +
            "    },\n" +
            "\t{\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"大冰\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"鉴于20个设计的核心要素和原则（上）取得了不错的反响，所以我决定把下篇也出了，希望能帮到大家！\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\": 0\n" +
            "    },\n" +
            "\t{\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"王丽\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"鉴于20个设计的核心要素和原则（上）取得了不错的反响，所以我决定把下篇也出了，希望能帮到大家！\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\": 3\n" +
            "    },\n" +
            "\t{\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"李青\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"鉴于20个设计的核心要素和原则（上）取得了不错的反响，所以我决定把下篇也出了，希望能帮到大家！\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\": 3\n" +
            "    },\n" +
            "\t{\n" +
            "      \"publish_timestamp\": 2342342342,\n" +
            "      \"head_photo\": \"http://img2.imgtn.bdimg.com/it/u=1397191400,1806124380&fm=21&gp=0.jpg\",\n" +
            "      \"nickname\": \"miss\",\n" +
            "      \"message_type\": 2,\n" +
            "      \"message_content\": \"鉴于20个设计的核心要素和原则（上）取得了不错的反响，所以我决定把下篇也出了，希望能帮到大家！\",\n" +
            "      \"message_image\": \"http://mmbiz.qpic.cn/mmbiz_png/EcVEm2j3oz3rSODQniaCSoibs2icPyQwQaXpQyAHjBibuhPyNNEibGWF3O6zxqn9qKWhgQ4ibf35pYA0GCd3d12oLyVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1\",\n" +
            "      \"comment_count\": 26,\n" +
            "      \"like_count\": 7,\n" +
            "      \"like_before\": 0,\n" +
            "      \"type\": 3\n" +
            "    }\n" +
            "]";

    private void parseJson() {
//        try {
//            InputStream in = getActivity().getAssets().open("friends_message.txt");
//            InputStreamReader ir = new InputStreamReader(in, "UTF-8");
//            BufferedReader br = new BufferedReader(ir);
//            String info;
        Gson gson = new Gson();
        Type type = new TypeToken<List<Message>>() {
        }.getType();
//            while ((info = br.readLine()) != null) {
        mMessages = gson.fromJson(JSON, type);
//            }
//            br.close();
//            ir.close();
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
