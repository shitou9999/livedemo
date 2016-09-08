package com.iguxuan.iguxuan_friends.modle;

/**
 * Created by Administrator on 2016/4/22.
 */
public class MessageHome {


    /**
     * new_message : 刘军解盘——缩量反弹，谨慎应对 2016-01-06
     * title : 股轩助手
     * new_count : 0
     * type : 1
     * datetime : 1452076080
     * thumb : http://img.iguxuan.com/gx/app/img/zhushou.png
     */

    private String new_message;
    private String title;
    private int new_count;
    private int type;
    private String datetime;
    private String thumb;

    public String getNew_message() {
        return new_message;
    }

    public void setNew_message(String new_message) {
        this.new_message = new_message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNew_count() {
        return new_count;
    }

    public void setNew_count(int new_count) {
        this.new_count = new_count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
