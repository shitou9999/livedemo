package com.kuainiutv.modle;

/**
 * Created by jack on 2016/4/20.
 */
public class TeacherInfo {

    /**
     * id : 8
     * catid : 28
     * title : 张清华
     * url : http://www.iguxuan.com/index.php?m=content&c=teacher&a=index&teacherid=8
     * description : BKD战法 战胜人性弱点 创造股市财富
     * thumb : http://img.iguxuan.com/statics/images/gx/TouXiang/ZhangQingHua.jpg
     * banner : http://img.iguxuan.com/statics/images/gx/special/zqh.jpg
     * class : teacher
     * vedio_count : 102
     * news_count : 275
     * fans_count : 17
     * is_follow : 1
     */

    public String id;
    public String catid;
    public String title;
    public String description;
    public String thumb;
    public String banner;
    public String classification;
    public int vedio_count;
    public int news_count;
    public int fans_count;
    public int is_follow;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public int getVedio_count() {
        return vedio_count;
    }

    public void setVedio_count(int vedio_count) {
        this.vedio_count = vedio_count;
    }

    public int getNews_count() {
        return news_count;
    }

    public void setNews_count(int news_count) {
        this.news_count = news_count;
    }

    public int getFans_count() {
        return fans_count;
    }

    public void setFans_count(int fans_count) {
        this.fans_count = fans_count;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
