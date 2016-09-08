package com.iguxuan.iguxuan_friends.modle.cons;

/**
 * @author nanck on 2016/3/9.
 */
public enum MessageType {
    VideoType("1"),//视频
    NewsType("2"),//文章信息
    SystemType("3"),//系统信息
    ActivityType("4"),// 活动消息
    DynamicMessageType("8"),// 动态消息
    OffLineType("9");//离线信息

    MessageType(String type) {
        this.type = type;
    }

    private String type;

    public String type() {
        return this.type;
    }

}
