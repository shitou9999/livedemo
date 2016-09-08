package com.iguxuan.iguxuan_friends.modle.push;

/**
 * Created by jack on 2016/4/18.
 */
public class VideoMessage extends BaseMeassge{
    private String id;
    private int daoshi;//老师ID
    private String catid;//栏目ID
    private String upvideoid;//视频ID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDaoshi() {
        return daoshi;
    }

    public void setDaoshi(int daoshi) {
        this.daoshi = daoshi;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getUpvideoid() {
        return upvideoid;
    }

    public void setUpvideoid(String upvideoid) {
        this.upvideoid = upvideoid;
    }
}
