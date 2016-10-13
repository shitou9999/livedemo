package tv.kuainiu.modle.cons;


/**
 * EventBus 事件响应标识类。</p>
 * 1.
 * Created by guxuan on 2016/3/17.
 */
public enum Action {
    normal,                // 默认。请慎用该值!!!以避免多个事件接收者对同一个post作出响应。
    login,                 // 登录
    off_line,              // 离线
    client_init,           // 初始化请求
    reg_phone,             // 手机号注册
    reg_phone_sendcode,    // 手机号注册发送验证码
    reg_phone_sendcode_voice,    // 手机号注册发送语音验证码
    logout,                // 注销登录
    fetch_userinfo,         // 获取用户最新信息
    update_userinfo,       // 修改用户资料
    iden,                  // 身份认证
    bind_email,            // 绑定邮箱
    bind_email_sendcode,   // 绑定邮箱发送验证码
    update_password,       // 修改密码
    forget_pwd,            // 忘记密码
    forget_pwd_sendcode,   // 忘记密码发送验证码
    phone_valid,           // 验证原手机号
    update_phone,          // 修改手机号
    update_phone_sendcode,
    update_avatar,         // 上传用户头像
    follow_list,             // 我的关注
    me_follow,             // 我的关注
    add_follow,            // 添加关注
    del_follow,            // 取消关注

    upload_push_id,        // 上传推送ID

    me_subscribe,          // 我的订阅
    add_subscribe,         // 添加订阅
    del_subscribe,         // 取消订阅
    add_feedback,          // 添加反馈
    about_me,              // 关于我们
    fetch_subscibe_list,   // 获取订阅列表
    fetch_follow_list,     // 获取关注列表
    find_bannar,           // 发现_bannar
    find_teacher_list,     // 发现_老师列表
    find_home_news_list,        // 发现_视频/文章列表
    find_news_list,        // 发现_视频/文章列表
    find_teacher_news_list,        // 老师_视频/文章列表
    find_important_news,   // 发现_重点资讯、前三条
    dynamicitem_list,             // 视频/文章列表

    industry_news_list,    // 新闻列表
    jiepan_news_list,      // 解盘列表

    video_details,         // 视频详情
    post_details,          // 文章详情

    video_favour,   // 视频点赞
    post_favour,    // 文章点赞

    add_collect,    // 添加收藏
    del_collect,    // 取消收藏
    collect_list,    // 收藏列表
    add_collect_video,    // 添加视频收藏
    del_collect_video,    // 取消视频收藏
    add_download_video,   // 添加视频下载记录

    comment_list,     // 评论列表
    comment_teacher,  // 老师回复列表
    comment_list_hot, // 热门评论
    add_comment,      // 添加评论
    favour_comment,   // 评论点赞
    favour_comment_video,   // 视频评论点赞


    video_play_other,       // 其它视频
    video_play_hot_comment, // 热门评论
    video_play_teacher,     // 解盘名师
    video_play_recommend,   // 视频推荐

    custom_content, //定制内容获取
    custom_fetch_follow_list, //定制获取关注内容
    custom_fetch_subscibe_list, //定制获取订阅内容

    /* 发现 */
    find_get_cats,

    /* 名师 */
    teacher_fg_add_follow,
    teacher_fg_del_follow,
    home_teacher_fg_add_follow,
    home_teacher_fg_del_follow,
    teacher_fg_add_subscribe,
    teacher_fg_del_subscribe,
    teacher_fg_fetch_follow_list,
    teacher_fg_fetch_subscibe_list,
    teacher_fg_fetch_detail,
    teacher_fg_fetch_jie_pan_top3,
    find_dynamics_list,

    /* 节目详情 ProgramZoneActivity */
    program_zone_fetch_info,
    program_zone_fetch_video_list,
    program_zone_fetch_article_list,

    /* 推送设置页 */
    push_setting_fetch_follow_list,
    pubh_setting_fetch_subscibe_list,
    push_setting_fetch_subscibe_list,
    push_setting_all_allow,
    push_setting_all_on,
    push_setting_all_off,
    push_setting_teacher_on,
    push_setting_teacher_off,
    push_setting_program_on,
    push_setting_program_off,

    /*消息*/
    fetcherSystemMessageList,//系统消息列表
    fetcherSystemMessageDetail,//系统消息详情
    MESSAGE_INDEX,//消息首页
    OFFICIAL_NEWS_LIST,//股轩助手
    ACTIVITY_LIST,//活动列表
    ACTIVITY_COLLECT_LIST,//活动收藏列表
    ACTIVITY_INFO,//活动详情
    COMMENT_MESSAGE_LIST,//评论消息列表
    AUTO_LOGIN,//自动登陆
    DynamicMessage,//动态消息
    ActivityMessage,//活动消息登陆

    follow_first_follow_list, // 引导关注--获取老师列表
    follow_first_add_follow,  // 引导关注--关注老师

    switch_teacher_fragment,  // 跳转到名师
    switch_live_fragment,     // 跳转到直播

    sms_login_send_code,      // 短信登录,发送验证码
    sms_login_send_code_voice,// 短信登录,发送语音验证码

    sms_login_send_code_two,      // 短信登录,发送验证码
    sms_login_send_code_voice_two,// 短信登录,发送语音验证码

    sms_login,                // 短信登录
    sms_login_reg,            // 短信登录  新号注册设置密码
    qr_code_login,            // 二维码登录


    inform_teacher_fragment_refresh, // 名师页需要刷新
    inform_me_fragment_sub_follow_count_refresh, // 个人中心，订阅和关注数量需要刷新


    // 获取新闻(文章、视频)列表
    fetch_news_list_0,
    fetch_news_list_1,
    fetch_news_list_2,
    fetch_news_list_3,
    fetch_news_list_4,
    fetch_news_list_5,
    fetch_news_list_6,
    fetch_news_list_7,
    fetch_news_list_8,
    fetch_news_list_9,

    // 直播
    live_fetch_live_list,    // 今日直播列表
    live_fetch_room_info,    // 房间信息
    live_fetch_living_info,  // 正在直播信息
    live_add_like,           // 赞这个直播
    live_history,            // 往期直播视频
    live_teacher_need_refresh, // 直播名师团队需要刷新 XXX
    live_history_plus_one,   // 回放次数+1
    choose_city,   // 选择城市
    hot_point,   // 热门观点
    CUSTOM_LIST,   //2.9 定制列表 - 名师观点   sns/custom_list     圈子-名师观点
    CUSTOM_VIDEO_LIST,   // 圈子-解盘视频
    SUPPORT_DYNAMICS,   // 文章动态点赞
    home_support_dynamics,   // 文章动态点赞
    CUSTOM_LIVE_LIST,   // 圈子-直播
    live_zhi_bo_kan_pan,   // 直播看盘
    live_zhi_bo_kauiniu_tv,   // 官方直播间
    live_zhi_bo_home,   // 直播看盘
    live_hui_fang_kan_pan,   // 直播回放
    live_zhi_bo_yu_gao,   // 直播预告
    teacher_news_tags,   // 1.17 老师标签列表   v1/news/teacher_news_tags
    quote_list,   // 1.15 发布动态时引用的文章列表   news/quote_list
    get_cats,   //  1.16 文章分类列表   news/get_cats
    add_dynamics,   // 1.2 发布文字动态   v1/publish/add_dynamics
}
