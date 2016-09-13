package tv.kuainiu.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import tv.kuainiu.R;
import tv.kuainiu.onekeyshare.OnekeyShare;
import tv.kuainiu.onekeyshare.ShareContentCustomizeCallback;
import tv.kuainiu.umeng.UMEventManager;

/**
 * Created by Administrator on 2016/4/22.
 */
public class ShareUtils {
    public static final String APP = "app";
    public static final String ARTICLE = "aritcle";
    public static final String VIDEO = "video";


    /**
     * 分享
     *
     * @param context     上下文对象
     * @param title       分享标题
     * @param content     分享内容
     * @param imagePath   图片网络地址（imagePath和viewToShare，二传一）
     * @param url         分享链接
     * @param viewToShare 要分享的控件，会对这个空间截图分享（imagePath和viewToShare，二传一）
     * @deprecated 请参考 {@link #showShare(String, Context, String, String, String, String, View)}
     */

    public static void showShare(Context context, String title, String content, String imagePath, String url, View viewToShare) {
        showShare("none", context, title, content, imagePath, url, viewToShare);
    }

    /**
     * 一键分享
     *
     * @param type        分享类型 <STROM>1.app 2.video 3.article</STROM>
     * @param context     上下文对象
     * @param title       分享标题
     * @param content     分享内容
     * @param imagePath   图片网络地址（imagePath和viewToShare，二传一）
     * @param url         分享链接
     * @param viewToShare 要分享的控件，会对这个空间截图分享（imagePath和viewToShare，二传一）
     * @since v2.1.0
     */
    public static void showShare(final String type, final Context context, String title, String content, String imagePath, String url, View viewToShare) {
        if (context == null) {
            return;
        }
        title = StringUtils.replaceNullToEmpty(title);
        content = StringUtils.replaceNullToEmpty(content);
        imagePath = StringUtils.replaceNullToEmpty(imagePath);
        url = StringUtils.replaceNullToEmpty(url);

        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();

//        oks.setTheme(OnekeyShareTheme.CLASSIC);
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        // 分享时Notification的图标和文字
        // oks.setNotification(R.drawable.ic_launcher,
        // getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        if (!TextUtils.isEmpty(title) && title.length() > 30) {
            title = String.format("%s", title.substring(0, 30));
        }

//        oks.setText();
        oks.setTitle(title);// 文本：分享
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        // 此处就是用户输入的内容，如果用户没有输入，直接使用默认值
        // if (!Utils.isEmpty(content) && content.length() > 40) {
        // content = String.format("%s", content.substring(0, 40));
        // }
        oks.setText(content);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        if (viewToShare != null) {
            try {
                oks.setViewToShare(viewToShare);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        //  oks.setImagePath(imagePath);// 确保SDcard下面存在此张图片
        oks.setImageUrl(imagePath);// 确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        // oks.setComment(getString(R.string.app_name));
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);
        // 新浪微博单独设置文本和链接
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeForSinaWeibo());
        if (VIDEO.equals(type)) {
            oks.setVideoUrl(url);
        }

        // 设置自定义的外部回调
        oks.setCallback(new OneKeyShareCallback(context, type));


        // 启动分享GUI
        oks.show(context);
    }


    static class ShareContentCustomizeForSinaWeibo implements ShareContentCustomizeCallback {

        @Override
        public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
            if (SinaWeibo.NAME.equals(platform.getName())) {
                // 新浪微博不支持Title字段
                paramsToShare.setText("#爱股轩看盘#".concat(paramsToShare.getText()).concat(paramsToShare.getUrl()));
            }
        }
    }


    public static class OneKeyShareCallback implements PlatformActionListener {
        private Context context;
        private String type;

        public OneKeyShareCallback(Context context, String type) {
            this.context = context;
            this.type = type;
            DebugUtils.dd("分享 构造方法: context = " + context.toString() + " type = " + type);
        }

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            HashMap<String, String> map = new HashMap<>();
            map.put("type", type);
            map.put("platform", platform.getName());
            UMEventManager.onEvent(context, UMEventManager.ID_SHARE, map);

            DebugUtils.dd("分享成功：type = " + type + " platform  = " + platform.getName());
        }

        @Override public void onCancel(Platform platform, int i) {
            DebugUtils.dd("分享失败：type = " + type + " platform" + platform.getName());
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            DebugUtils.dd("分享取消：type = " + type + " platform" + platform.getName());
        }
    }
}
