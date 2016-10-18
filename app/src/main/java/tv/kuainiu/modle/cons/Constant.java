package tv.kuainiu.modle.cons;

/**
 * 常量类
 * Created by guxuan on 2016/2/29.
 */
public class Constant {
    public static final String PRIVATE_KEY = "private_key";
    public static final String INTENT_ACTION_GET_WEB = "INTENT_ACTION_GET_WEB";
    public static final String INTENT_ACTION_GET_FIND = "INTENT_ACTION_GET_FIND";
    public static final String INTENT_ACTION_GET_CUSTOM = "INTENT_ACTION_GET_CUSTOM";
    public static final String INTENT_ACTION_ACTIVITY_COLLECT = "INTENT_ACTION_ACTIVITY_COLLECT";
    public static final String INTENT_ACTION_ACTIVITY_MSG_NUM = "INTENT_ACTION_ACTIVITY_MSG_NUM";
    public static final String INTENT_ACTION_MESSAGE_HOME_ACTIVITY_MSG_NUM = "INTENT_ACTION_MESSAGE_HOME_ACTIVITY_MSG_NUM";
    /**
     * 一万
     */
    public static int TEN_THOUSAND = 10000;

    private Constant() {
        throw new IllegalArgumentException("Don't construct Constant this class");
    }

    public static final String PUSH_TYPE_ANDROID = "Android";

    /**
     * 表示用户行为:已经发生
     */
    public static final int BASE_TRUE = 1;
    /**
     * 表示用户行为:未发生或已取消
     */
    public static final int BASE_FALSE = 0;
    /**
     * 已关注
     */
    public static final int FOLLOWED = BASE_TRUE;
    /**
     * 未关注
     */
    public static final int UNFOLLOW = BASE_FALSE;
    /**
     * 已收藏
     */
    public static final int COLLECTED = BASE_TRUE;
    /**
     * 未收藏
     */
    public static final int UNCOLLECT = BASE_FALSE;
    /**
     * 已赞
     */
    public static final int FAVOURED = BASE_TRUE;
    /**
     * 未赞
     */
    public static final int UNFAVOUR = BASE_FALSE;
    /**
     * 已订阅
     */
    public static final int SUBSCRIBEED = BASE_TRUE;
    /**
     * 未订阅
     */
    public static final int UNSUBSCRIBE = BASE_FALSE;

    /**
     * TYPE-视频
     */
    public static final int NEWS_TYPE_VIDEO = 2;

    /**
     * TYPE-声音
     */
    public static final int NEWS_TYPE_VOICE = 3;

    /**
     * TYPE-文章
     */
    public static final int NEWS_TYPE_ARTICLE = 1;
    /**
     * TYPE-活动
     */
    public static final int NEWS_TYPE_ACTIVITY = 11;
    /**
     * 请求评论默认条数
     */
    public static final int REQUEST_SIZE = 15;
    /**
     * 请求默认条数15
     */
    public static final int REQUEST_SIZE15 = 15;
    /**
     * 请求新闻默认条数
     */
    public static final int REQUEST_NEWS_SIZE = 15;
    /**
     * 定制内容默认条数
     */
    public static final int CUSTOM_CONTENT_SIZE = 10;
    /**
     * 请求热门评论默认条数
     */
    public static final int HOT_COMMENT_SIZE = 5;
    // ========================================================================================================
    // ---------------------|                                                                                 ||
    // |      常用键值                                                                                          }}
    // |                    |                                                                                 ||
    // ---------------------|                                                                                 ||
    // ========================================================================================================
    public static final String PARAM_KEY_UID = "user_id";
    public static final String PARAM_KEY_TIME = "time";
    public static final String PARAM_KEY_SIGN = "sign";
    public static final String KEY_URL = "url";
    public static final String KEY_ISNEED = "isNeed";
    /**
     * ID
     */
    public static final String KEY_ID = "id";
    /**
     * 动态id
     */
    public static final String KEY_DYNAMICS_ID = "key_dynamics_id";
    /**
     * 栏目ID
     */
    public static final String KEY_CATID = "catid";
    /**
     * 栏目名称
     */
    public static final String KEY_CATNAME = "catname";
    /**
     * 用户ID
     */
    public static final String KEY_USERID = "user_id";
    /**
     * 解盘类型
     */
    public static final String KEY_JPTYPE = "jpType";
    /**
     * 页码
     */
    public static final String KEY_PAGE = "page";
    public static final String KEY_SIZE = "size";
    /**
     * 类型
     */
    public static final String KEY_TYPE = "type";
    /**
     * 老师 id
     */
    public static final String KEY_TEACHERID = "teacher_id";
    /**
     * 导师 id
     */
    public static final String KEY_DAOSHI = "teacher_id";
    /**
     * 视频 id
     */
    public static final String KEY_VIDEO_ID = "upVideoId";
    /**
     * 分页请求默认PageNumber
     */
    public static final int DEFAULT_PAGE_NUMBER = 1;
    /**
     * 地区号
     */
    public static final String KEY_AREA = "area";
    public static final String KEY_PHONE = "phone";
    // ========================================================================================================
    // ---------------------|   YouTube标准：                                                                  ||
    // |      视频常量            144p, 288p, 360p, 480p, 720p(HD), 1080p(HD), 1440p(HD), 2160p(4K), 4320p(8K)  }}
    // |                    |                                                                                 ||
    // ---------------------|                                                                                 ||
    // ========================================================================================================
    public static final int VIDEO_SHARPNESS_STANDARD = 0x1; // 流畅(标清)
    public static final int VIDEO_SHARPNESS_HIGH = 0x2;     // 高清
    public static final int VIDEO_SHARPNESS_SUPER = 0x3;    // 超清

    public static final String CONFIG_FILE_NAME = "config";
    /**
     * 是否允许非 wi-fi 网络缓存
     */
    public static final String CONFIG_KEY_NOTWIFI_DOWNLOAD = "notwifi_download";
    /**
     * 视频清晰度
     */
    public static final String CONFIG_KEY_VIDEO_SHARPNESS = "video_sharpness";
    /**
     * 是否开启推送
     */
    public static final String CONFIG_KEY_ALLOW_PUSH = "push_switch";
    /**
     * App 当前版本号
     */
    public static final String CURRENT_VERSION_CODE = "current_version_code";
    /**
     * 是否首次安装
     */
    public static final String IS_FIRST_OPENED_APP = "is_first_installation";
    /**
     * 课程负链接
     */
    public static final String COURSE_URL = "course_url";
    /**
     * 消息总数量
     */
    public static final String MSG_NUM = "tMsg_num";


    /**
     * 网络请求常量类
     */
    public static final int SUCCEED = 0;
    public static final int ERROR = -1;
    public static final int HAS_SUCCEED = -2;
    public static final int NETWORK_ERROR = -20016;
    public static final int NULL_FOR_DATA = -10086;
    public static final int NULL_FOR_LIST = -10010;

    /**
     * 是否已检查过更新
     */
    public static boolean First_Open = false;
    public static final int MUST_UPDATE = 1;
    public static final int FREE_UPDATE = 0;

    public static boolean IS_FIRIST = true;

    public static String CUSTOM_CONTENT_LASTTIME = "Custom_Content_LastTime";
    public static String CUSTOM_FOLLOW_LASTTIME = "Custom_follow_LastTime";
    public static String SESSION_ID = "session_id";


    /**
     * 验证码长度
     */
    public static final int YZM_LENGTH = 4;

    /**
     * 验证码内容
     */
    public static final String YZM_CONTENTS_HEAD = "【快牛TV】";
    /**
     * 验证码短信号码
     */
    public static final String YZM_SEDN_PHONE_NUMBER_HRAD = "106";

    /**
     * 当日视频下载次数
     */
    public static final int DOWN_VIDEO_NUMBER = 10;

    /**
     * ERROR REMINDER
     */
    public static final String AREA_CODE_ERROR = "国家代码无效";

    /**
     * 客户端ID
     */
    public static final String QR_CODE_LOGIN = "qrcode_login";
    /**
     * 客户端ID
     */
    public static final String CLIENT_ID = "client_id";
    /**
     * 二维码扫描时间
     */
    public static final String CONNECT_TIME = "connect_time";

}
