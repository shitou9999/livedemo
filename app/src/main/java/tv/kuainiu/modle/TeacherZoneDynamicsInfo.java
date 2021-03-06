package tv.kuainiu.modle;

import java.io.Serializable;

/**
 * Created by jack on 2016/9/21.
 */
public class TeacherZoneDynamicsInfo implements Serializable {


    /**
     * news_catid : 3
     * news_catname : 创业板
     * news_thumb : http://img.iguxuan.com/uploadfile/2016/0920/t_20160920111208178.png
     * news_inputtime : 1474367219
     * type : 3
     * news_title : 9-20时时分析
     */

    private String id;
    private String news_id;
    private String news_catid;
    private String news_catname;
    private String news_thumb;
    private long news_inputtime;
    private int type;
    private String news_title;
    private String news_video_id;
    private String news_voice_url;
    private String news_voice_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getNews_catid() {
        return news_catid;
    }

    public void setNews_catid(String news_catid) {
        this.news_catid = news_catid;
    }

    public String getNews_catname() {
        return news_catname;
    }

    public void setNews_catname(String news_catname) {
        this.news_catname = news_catname;
    }

    public String getNews_thumb() {
        return news_thumb;
    }

    public void setNews_thumb(String news_thumb) {
        this.news_thumb = news_thumb;
    }

    public long getNews_inputtime() {
        return news_inputtime;
    }

    public void setNews_inputtime(long news_inputtime) {
        this.news_inputtime = news_inputtime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_video_id() {
        return news_video_id;
    }

    public void setNews_video_id(String news_video_id) {
        this.news_video_id = news_video_id;
    }

    public String getNews_voice_url() {
        return news_voice_url;
    }

    public void setNews_voice_url(String news_voice_url) {
        this.news_voice_url = news_voice_url;
    }

    public String getNews_voice_time() {
        return news_voice_time;
    }

    public void setNews_voice_time(String news_voice_time) {
        this.news_voice_time = news_voice_time;
    }
}
