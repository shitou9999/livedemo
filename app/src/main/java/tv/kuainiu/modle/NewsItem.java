package tv.kuainiu.modle;

import java.io.Serializable;

/**
 *
 */
public class NewsItem implements Serializable {

    /**
     * id : 13
     * cat_id : 5
     * type : 1
     * title : 主力将利用缺口大举拿货
     * thumb : http://kuainiu.oss-cn-shanghai.aliyuncs.com/uploadfile/20160914001.jpg
     * url : http://139.224.1.41/v1/news/show_news?id=13
     * user_id : 2
     * description : 金子大涨的描述
     * inputtime : 1473482864
     * video_id :
     * support_num : 5
     * comment_num : 3
     * catname : 股指期货
     * nickname : 蛋哥
     * avatar : http://img.kuainiu.tv/uploadfile/avatar/201610/2_31504.jpg
     * is_support : 0
     */

    private String id;
    private String cat_id;
    private int type;
    private String title;
    private String thumb;
    private String url;
    private String user_id;
    private String description;
    private long inputtime;
    private String video_id;
    private String support_num;
    private String comment_num;
    private String catname;
    private String nickname;
    private String avatar;
    private int is_support;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getInputtime() {
        return inputtime;
    }

    public void setInputtime(long inputtime) {
        this.inputtime = inputtime;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getSupport_num() {
        return support_num;
    }

    public void setSupport_num(String support_num) {
        this.support_num = support_num;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getIs_support() {
        return is_support;
    }

    public void setIs_support(int is_support) {
        this.is_support = is_support;
    }
}

