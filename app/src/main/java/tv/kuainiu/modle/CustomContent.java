package tv.kuainiu.modle;

import java.util.List;

/**
 * Created by jack on 2016/4/14.
 */
public class CustomContent {
    private String id;
    private String catid;
    private String title;
    private String thumb;
    private String daoshi;
    private String description;
    private String url;
    private String jpType;
    private String inputtime;
    private String updatetime;
    private String upVideoId;
    private String videodm;
    private String is_official;
    private String catname;
    private String daoshi_name;
    private String daoshi_thumb;
    private String zan_count;
    private int views;
    private int comment_count;
    private List<CommentItem> comment_list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getDaoshi() {
        return daoshi;
    }

    public void setDaoshi(String daoshi) {
        this.daoshi = daoshi;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJpType() {
        return jpType;
    }

    public void setJpType(String jpType) {
        this.jpType = jpType;
    }

    public String getInputtime() {
        return inputtime;
    }

    public void setInputtime(String inputtime) {
        this.inputtime = inputtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpVideoId() {
        return upVideoId;
    }

    public void setUpVideoId(String upVideoId) {
        this.upVideoId = upVideoId;
    }

    public String getVideodm() {
        return videodm;
    }

    public void setVideodm(String videodm) {
        this.videodm = videodm;
    }

    public String getIs_official() {
        return is_official;
    }

    public void setIs_official(String is_official) {
        this.is_official = is_official;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getDaoshi_name() {
        return daoshi_name;
    }

    public void setDaoshi_name(String daoshi_name) {
        this.daoshi_name = daoshi_name;
    }

    public String getDaoshi_thumb() {
        return daoshi_thumb;
    }

    public void setDaoshi_thumb(String daoshi_thumb) {
        this.daoshi_thumb = daoshi_thumb;
    }

    public String getZan_count() {
        return zan_count;
    }

    public void setZan_count(String zan_count) {
        this.zan_count = zan_count;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public List<CommentItem> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<CommentItem> comment_list) {
        this.comment_list = comment_list;
    }
}
