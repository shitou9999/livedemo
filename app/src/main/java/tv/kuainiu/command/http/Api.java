package tv.kuainiu.command.http;

/**
 * @author nanck on 2016/3/21.
 */
public class Api {
    public static final String DNS_HOST = "http://client.api.kuainiu.tv";
    public static final String DNS_API_HOST = "http://client.api.kuainiu.tv/v1/";
    /*****************************
     * 正式库
     *************************/
    public static final String TEST_DNS_API_HOST = "http://client.api.kuainiu.tv/v1/";
    public static final String TEST_DNS_API_HOST_V2 = "http://client.api.kuainiu.tv/v1/";
    public static final String TEST_DNS_API_HOST_V21 = "http://client.api.kuainiu.tv/v1/";
    public static final String PUBLIC_KEY = "kntv!$@)";
    public static final String SALT = "kn*()";

    /*****************************
     * 测试库
     *************************/
//    public static final String TEST_DNS_API_HOST = "http://client.api.kuainiu.tv/v1/";
//    public static final String TEST_DNS_API_HOST_V2 = "http://client.api.kuainiu.tv/v1/";
//    public static final String TEST_DNS_API_HOST_V21 = "http://client.api.kuainiu.tv/v1/";
//    public static final String PUBLIC_KEY = "kntv!$@)_test";
//    public static final String SALT = "kn&*(_test";
    /*****************************
     * 测试库
     *************************/

    /* 用户 */
    public static final String LOGIN = "login";                                            // 登录
    public static final String LOGOUT = "logout";                                          // 注销
    public static final String REG_PHONE = "reg/phone";                                    // 手机号注册
    public static final String REG_PHONE_SENDCODE = "reg/phone_sendcode";                  // 手机号注册短信验证码
    public static final String REG_PHONE_SENDCODE_VOICE = "reg/phone_voicecode";           // 手机号注册语音验证码
    public static final String UPDATE_PASSWORD = "account/update_pwd";                     // 修改密码
    public static final String UPDATE_AVATAR = "account/update_avatar";                    // 上传用户头像
    public static final String UPDATE_USERINFO = "account/update_userinfo";                // 完善个人资料
    public static final String FETCH_USERINFO = "account/get_userinfo";                    // 获取用户最新信息
    public static final String FORGET_PWD = "account/forget_pwd";                          // 忘记密码
    public static final String BIND_EMAIL_SENDCODE = "account/bind_email_sendcode";        // 绑定邮箱发送验证码
    public static final String BIND_EMAIL = "account/bind_email";                          // 绑定邮箱
    public static final String UNBIND_EMAIL_SENDCODE = "account/unbind_email_sendcode";    // 解除绑定邮箱发送验证码
    public static final String UNBIND_EMAIL = "account/unbind_email";                      // 解除绑定邮箱
    public static final String FORGET_PWD_SENDCODE = "account/forget_pwd_sendcode";        // 忘记密码发送验证码
    public static final String UPDATE_PHONE_VALID = "account/update_phone_valid";          // 验证原手机号
    public static final String UPDATE_PHONE = "account/update_phone";                      // 修改手机号
    public static final String UPDATE_PHONE_SENDCODE = "account/update_phone_sendcode";    // 修改手机号发送验证码
    public static final String ON_PUSH_SETTING = "sns/add_push_setting";                   // 开启推送
    public static final String OFF_PUSH_SETTING = "sns/del_push_setting";                  // 关闭推送
    public static final String ALLOW_PUSH_SETTING = "sns/allow_push_setting";              // 是否允许推送
    public static final String SMS_LOGING_SEND_CODE_MESSAGE = "login/sms_login_sendcode";  // 短信登录,发送短信验证码
    public static final String SMS_LOGING_SEND_CODE_VOICE = "login/sms_login_voicecode";   // 短信登录,发送语音验证码
    public static final String SMS_LOGING = "login/sms_login";                             // 短信登录
    public static final String SMS_LOGING_REG = "login/sms_login_reg";                     // 短信登录,新号注册设置密码

    /* 老师 */
    //
    public static final String FOLLOW_LSIT = "sns/follow_list";                // 用户关注列表
    public static final String ADD_FOLLOW = "sns/add_follow";                  // 添加关注
    public static final String DEL_FOLLOW = "sns/del_follow";                  // 取消关注
    public static final String FETCH_TEAHCER_INFO = "news/teacher_info";       // 获取老师信息
    public static final String FIND_TEACHER_LIST = "news/teacher_list";        // 发现_老师列表
    public static final String FIND_DYNAMICS_LIST = "news/dynamics_list";        // 名师个人专区-动态

    /* 节目 */
    //
    public static final String FETCH_PROGRAM_INFO = "news/program_cat_info";   // 获取节目详情
    public static final String SUBSCRIBER_LSIT = "sns/subscibe_list";          // 获取订阅列表
    public static final String ADD_SUBSCRIBER = "sns/add_subscibe";            // 添加订阅
    public static final String DEL_SUBSCRIBER = "sns/del_subscibe";            // 取消订阅
    public static final String FETCH_HISTORY = "live/playback_list";           // 获取历史

    /* 新闻(视频/文章) */
    //
    public static final String ADD_COLLECT = "sns/add_collect";                // 添加收藏
    public static final String DEL_COLLECT = "sns/del_collect";                // 取消收藏
    public static final String COLLECT_LIST = "sns/collect_list";              // 收藏列表
    public static final String ADD_DOWNLOAD = "sns/add_download";              // 添加下载记录
    public static final String INDUSTRY_NEWS_LIST = "news/industry_news_list"; // 新闻列表
    public static final String VIDEO_OR_POST_DETAILS = "news/get_news";        // 视频/文章详情
    public static final String VIDEO_OR_POST_FAVOUR = "news/support_news";     // 视频/文章点赞
    public static final String COMMENT_LIST = "sns/comment_list";              // 评论列表
    public static final String COMMENT_LIST_HOT = "sns/hot_comment";       // 文章/视频最热评论列表
    public static final String ADD_COMMENT = "sns/add_comment";                // 添加评论
    public static final String FAVOUR_COMMENT = "sns/support_comment";         // 评论点赞
    public static final String VIDEO_RECOMMEND = "news/recommend_video_list";  // 推荐视频列表

    /* 直播 */
    //
    public static final String LIVE_LIST = "live/live_list";                   // 直播列表(v21)
    public static final String LIVE_ROOM_INFO = "live/room_info";              // 房间信息(v21)
    public static final String LIVE_LIVEING = "live/liveing";                  // 正在直播信息(top v21)
    public static final String LIVE_LIKE = "live/support_live";                // 点赞(v21)
    public static final String LIVE_HISTORY = "live/playback_list";            // 往期直播视频(v21)
    public static final String LIVE_HISTORY_PLUS_ONE = "live/playback_live";   // 回放次数统计+1(v21)


    /* Other */
    //
    public static final String CLIENT_INIT = "sys/client_init";                // 初始化请求
    public static final String UPLOAD_PUSH_ID = "sys/push_register";           // 上传推送ID
    public static final String ADD_FEEDBACK = "sns/add_feedback";              // 添加反馈
    public static final String ABOUT_ME = "news/about";                        // 关于我们
    public static final String FIND_BANNAR = "news/banner_list";               // 发现_Bannar
    public static final String index_recom_live = "news/index_recom_live";               // 发现_Bannar
    public static final String FIND_IMPORTANT_NEWS = "news/important_news";    // 发现_重点资讯、前三条
    public static final String FIND_NEWS_LIST = "news/news_list";              // 发现_视频/文章 列表
    public static final String FIND_PROGRAM_LIST = "news/program_list";        // 发现_栏目列表


    public static final String CUSTOM_CONTENT = "sns/custom_news_list"; // 2.15 定制文章列表
    public static final String VERSION = "sys/version"; //系统更新

    public static final String SYSTEM_MSG_LIST = "sns/system_msg_list";        // 系统消息列表
    public static final String SYSTEM_MESSAGE_DETAIL = "sns/view_system_msg";  // 系统消息详情
    public static final String MESSAGE_INDEX = "sns/message_index";            //1.3总消息首页（V2)
    public static final String OFFICIAL_NEWS_LIST = "news/official_news_list"; //1.4股轩助手文章列表（V2)
    public static final String COMMENT_MESSAGE_LIST = "sns/comment_message_list"; //1.5评论消息列表（V2)
    public static final String ACTIVITY_LIST = "sns/activity_list";            //1.6 活动消息列表 （V2)
    public static final String ACTIVITY_INFO = "sns/activity_info";            // 1.6.1 活动消息详情页 （V2)
    public static final String ACTIVITY_COLLECT_LIST = "sns/activity_collect_list"; //1.14 用户活动收藏列表 （V2)
    public static final String AUTH_LOGIN = "account/auth_login_android";      // 用户网页同步登录接口（V2)
    public static final String QRCODE_LOGIN = "qrcode/login";                  // 二维码登录接口（V2)
    public static final String GET_CATS = "news/get_cats";                     // 首页Flag
    public static final String HOT_POINT = "news/hot_point";                     // 首页热门观点
    public static final String CUSTOM_LIST = "sns/custom_list";                     //2.9 定制列表 - 名师观点   sns/custom_list     圈子-名师观点
    public static final String CUSTOM_VIDEO_LIST = "sns/custom_video_list"; //2.10 定制列表 - 解盘视频   sns/custom_video_list     圈子-解盘视频
    public static final String custom_live_list = "sns/custom_live_list"; //2.10 定制列表 - 解盘视频   sns/custom_video_list     圈子-解盘视频
    public static final String SUPPORT_DYNAMICS = "news/support_dynamics"; //1.7-1 文字动态点赞  news/support_dynamics
    public static final String LIVE_INDEX = "live/live_index"; //3.4 直播界面首页  live/live_index（v1）     用于底栏"直播"的界面

   /*发布*/
    /**
     * 1.17 老师标签列表   v1/news/teacher_news_tags
     * 通信方式：无
     * 参数列表：
     * teacher_id     必传     老师ID
     * 备注：参与加密
     */
    public static final String teacher_news_tags = "news/teacher_news_tags";

    //    1.15 发布动态时引用的文章列表   news/quote_list
    public static final String quote_list = "news/quote_list";

    //    1.16 文章分类列表   news/get_cats
    public static final String get_cats = "news/get_cats";
    public static final String live_permissions = "news/live_permission_list";
    /**
     * 1.2 发布文字动态   v1/publish/add_dynamics
     * <p>
     * 通信方式：加密方式
     * 参数列表：
     * news_id     可选     引用的文章ID
     * user_id     必传     用户ID
     * description     必传     文字内容
     * thumb     缩略图     多个以英文逗号隔开
     * synchro_wb     可选      是否同步微博     1是 0否
     * <p>
     * 备注：参与加密
     * {"status"="0","data"="json"}
     */
    public static final String add_dynamics = "publish/add_dynamics";

    /**
     * 通信方式：加密方式
     * 参数列表：
     * type     必传     发布类型 1文章 2视频 3声音
     * cat_id     必传     栏目ID
     * title     必传     标题
     * thumb      必传     缩略图
     * description     可选      描述
     * tag     可选      标签     选择已有标签(标签ID)     eg:1,2,3,6
     * tag_new     可选      新自定义标签（标签字符串）  eg:基本面,涨停,大涨
     * user_id     必传     用户ID
     * video_id     视频必传     视频ID
     * voice     声音必传     声音文件
     * content     必传     内容体
     * permission     可选      权限 1,公开开放   2,粉丝可见
     * allow_comment     可选      是否允许评论     1允许 0否
     * synchro_wb     可选      是否同步微博     1是 0否
     * synchro_dynamics     可选     是否同步动态     1是0否
     * dynamics_desc     同步动态时必传     动态描述文字
     */
    public static final String add_news = "publish/add_news";
    public static final String add_live = "publish/add_live";
    /**
     * 3.5 添加直播预约  live/add_live_appointment（v1）
     * <p>
     * 固定参数  user_id, time ,sign
     * 业务参数：
     * user_id    必传     用户ID
     * teacher_id     必传     老师ID
     * live_id     必传      直播记录ID
     * room_id     必传     直播间ID
     * 备注：需要签名跟加密
     */
    public static final String add_live_appointment = "live/add_live_appointment";


    /**
     * 3.6
     * 取消直播预约 live/del_live_appointment（v1）
     * <p>
     * 固定参数 user_id, time, sign
     * 业务参数：
     * user_id 必传
     * 用户ID
     * live_id 必传
     * 直播记录ID
     * <p>
     * 备注：需要签名跟加密
     */
    public static final String del_live_appointment = "live/del_live_appointment";
    /**
     * 3.7 直播预约列表  live/live_appointment_list（v1）
     * <p>
     * 固定参数  user_id, time ,sign
     * 业务参数：
     * user_id    必传     用户ID
     * type     必传     预约列表 1即将直播 2历史回放
     * page     必传     页数   默认1
     * size     必传     每页条数     默认10
     */
    public static final String live_appointment_list = "live/live_appointment_list";
    public static final String my_live_list = "live/my_live_list";
    public static final String del_system_msg = "sns/del_system_msg";
    public static final String custom_teacher_list = "sns/custom_teacher_list";
    //直播详情
    public static final String live_info = "live/live_info";
    public static final String self_check_permission = "cc/self_check_permission";


}
