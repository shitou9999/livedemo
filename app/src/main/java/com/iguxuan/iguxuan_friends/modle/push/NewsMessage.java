package com.iguxuan.iguxuan_friends.modle.push;

/**
 * Created by jack on 2016/4/18.
 */
public class NewsMessage extends BaseMeassge{
    private int daoshi;//老师ID
    private String url;//文章URL

    public int getDaoshi() {
        return daoshi;
    }

    public void setDaoshi(int daoshi) {
        this.daoshi = daoshi;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
