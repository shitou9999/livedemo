package com.iguxuan.iguxuan_friends.modle.push;

/**
 * Created by jack on 2016/4/18.
 */
public class BaseMeassge {
    private int type;//类型，1视频，2文章，3系统消息，4活动消息，9前设备已下线，
    private int need_alert;//1 需要弹出提示，0 不需要（透传）

    public boolean isNeedAlert() {
        if (1 == need_alert) {
            return true;
        } else {
            return false;
        }
    }
}
