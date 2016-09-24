package tv.kuainiu.modle;

import java.io.Serializable;

/**
 * @author nanck on 2016/4/20.
 */
public class NewsItem implements Serializable {
    /**
     * id : 3075
     * catid : 10
     * title : 毛鹏皓解盘——政策护盘，操盘标的 2016-01-06
     * thumb : http://img.iguxuan.com/statics/images/gx/slt/MaoPengHao.jpg
     * daoshi : 6
     * description : 政策护盘，操盘标的人民币继续贬值，是否是国家经济走软露疲态？板块的不可思议的上涨趋势，即是预计，又旁人惊奇，让我们把握操盘操作技巧。
     * url : /html/2016/jp_0106/3075.html
     * jpType : 晚盘
     * inputtime : 1452078451
     * updatetime : 1452510747
     * upVideoId : 05714ecace07be1bf03402459217bcac_0
     * videodm :
     * catname : 解盘
     * daoshi_name : 毛鹏皓
     * views : 7338
     */
    private String id;
    private String cat_id;
    private String title;
    private String thumb;
    private String daoshi;
    private String description;
    private String url;
    private String jpType;
    private long inputtime;
    private long updatetime;
    private String upVideoId;
    private String videodm;
    private String catname;
    private String daoshi_name;
    private int views;
    private int stopPosition;

    public int getStopPosition() {
        return stopPosition;
    }

    public void setStopPosition(int stopPosition) {
        this.stopPosition = stopPosition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatId() {
        return cat_id;
    }

    public void setCatId(String catid) {
        this.cat_id = catid;
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

    public long getInputtime() {
        return inputtime;
    }

    public void setInputtime(long inputtime) {
        this.inputtime = inputtime;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(long updatetime) {
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

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    @Override public String toString() {
        return "NewsItem{" +
                "catid='" + cat_id + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", thumb='" + thumb + '\'' +
                ", daoshi='" + daoshi + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", jpType='" + jpType + '\'' +
                ", inputtime=" + inputtime +
                ", updatetime=" + updatetime +
                ", upVideoId='" + upVideoId + '\'' +
                ", videodm='" + videodm + '\'' +
                ", catname='" + catname + '\'' +
                ", daoshi_name='" + daoshi_name + '\'' +
                ", views=" + views +
                ", stopPosition=" + stopPosition +
                '}';
    }
}

