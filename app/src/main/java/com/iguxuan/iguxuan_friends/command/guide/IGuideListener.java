package com.iguxuan.iguxuan_friends.command.guide;

/**
 * 引导接口。用于程序功能更新時给用户一个简单的"教程"
 *
 * @author nanck on 2016/5/11.
 * @deprecated
 */
public interface IGuideListener {
    void onStart();

    void onShow();

    void onComplete();
}
