package com.iguxuan.iguxuan_friends.command.http;

/**
 * @author nanck on 2016/3/21.
 */
public class Api {
    public static final String DNS_HOST = "http://client.api.iguxuan.com";
    public static final String DNS_API_HOST = "http://client.api.iguxuan.com/v1/";
    /*****************************
     * 正式库
     *************************/
    public static final String TEST_DNS_API_HOST = "http://139.224.1.41/v1/";
    public static final String TEST_DNS_API_HOST_V2 = "http://139.224.1.41/v1/";
    public static final String TEST_DNS_API_HOST_V21 = "http://139.224.1.41/v1/";
    public static final String PUBLIC_KEY = "kntv!$@)";
    public static final String SALT = "kn&!$@)";

    /*****************************
     * 测试库
     *************************/
//    public static final String TEST_DNS_API_HOST = "http://139.224.1.41/v1/";
//    public static final String TEST_DNS_API_HOST_V2 = "http://139.224.1.41/v1/";
//    public static final String TEST_DNS_API_HOST_V21 = "http://139.224.1.41/v1/";
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
    public static final String FETCH_FOLLOW_LIST = "sns/follow_list";          // 获取关注列表
    public static final String ADD_FOLLOW = "sns/add_follow";                  // 添加关注
    public static final String DEL_FOLLOW = "sns/del_follow";                  // 取消关注
    public static final String FETCH_TEAHCER_INFO = "news/teacher_info";       // 获取老师信息
    public static final String FIND_TEACHER_LIST = "news/teacher_list";        // 发现_老师列表

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
    public static final String COMMENT_LIST_HOT = "sns/hottest_comment";       // 文章/视频最热评论列表
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

}
