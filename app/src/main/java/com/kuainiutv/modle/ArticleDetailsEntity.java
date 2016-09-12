package com.kuainiutv.modle;

import com.google.gson.annotations.SerializedName;

/**
 * @author nanck on 2016/5/26.
 */
public class ArticleDetailsEntity {
    private String comment_count;
    private String updatetime;
    private String daoshi;
    private int collected;
    private String videodm;
    private String daoshi_description;
    private String url;
    private String inputtime;
    private String catname;
    private String daoshi_thumb;
    private String id;
    private String content;
    private String title;


    private DaoshiInfoEntity daoshi_info;
    private CatInfoEntity cat_info;

    private String jpType;
    private String description;
    private String views;
    private int is_support;
    private String catid;
    private String thumb;
    private String zan_count;
    private String daoshi_name;
    private String upVideoId;

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getDaoshi() {
        return daoshi;
    }

    public void setDaoshi(String daoshi) {
        this.daoshi = daoshi;
    }

    public CatInfoEntity getCat_info() {
        return cat_info;
    }

    public void setCat_info(CatInfoEntity cat_info) {
        this.cat_info = cat_info;
    }

    public int getCollected() {
        return collected;
    }

    public void setCollected(int collected) {
        this.collected = collected;
    }

    public String getVideodm() {
        return videodm;
    }

    public void setVideodm(String videodm) {
        this.videodm = videodm;
    }

    public String getDaoshi_description() {
        return daoshi_description;
    }

    public void setDaoshi_description(String daoshi_description) {
        this.daoshi_description = daoshi_description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInputtime() {
        return inputtime;
    }

    public void setInputtime(String inputtime) {
        this.inputtime = inputtime;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getDaoshi_thumb() {
        return daoshi_thumb;
    }

    public void setDaoshi_thumb(String daoshi_thumb) {
        this.daoshi_thumb = daoshi_thumb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DaoshiInfoEntity getDaoshi_info() {
        return daoshi_info;
    }

    public void setDaoshi_info(DaoshiInfoEntity daoshi_info) {
        this.daoshi_info = daoshi_info;
    }

    public String getJpType() {
        return jpType;
    }

    public void setJpType(String jpType) {
        this.jpType = jpType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public int getIs_support() {
        return is_support;
    }

    public void setIs_support(int is_support) {
        this.is_support = is_support;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getZan_count() {
        return zan_count;
    }

    public void setZan_count(String zan_count) {
        this.zan_count = zan_count;
    }

    public String getDaoshi_name() {
        return daoshi_name;
    }

    public void setDaoshi_name(String daoshi_name) {
        this.daoshi_name = daoshi_name;
    }

    public String getUpVideoId() {
        return upVideoId;
    }

    public void setUpVideoId(String upVideoId) {
        this.upVideoId = upVideoId;
    }

    public static class DaoshiInfoEntity {
        private String id;
        private int is_follow;
        private String title;
        private String description;
        private int vedio_count;
        private int news_count;
        private String fans_count;
        @SerializedName("class") private String classX;
        private String catid;
        private String thumb;
        private String url;
        private String banner;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getIs_follow() {
            return is_follow;
        }

        public void setIs_follow(int is_follow) {
            this.is_follow = is_follow;
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

        public String getFans_count() {
            return fans_count;
        }

        public void setFans_count(String fans_count) {
            this.fans_count = fans_count;
        }

        public String getClassX() {
            return classX;
        }

        public void setClassX(String classX) {
            this.classX = classX;
        }

        public String getCatid() {
            return catid;
        }

        public void setCatid(String catid) {
            this.catid = catid;
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

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }

        @Override public String toString() {
            return "DaoshiInfoEntity{" +
                    "banner='" + banner + '\'' +
                    ", id='" + id + '\'' +
                    ", is_follow=" + is_follow +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", vedio_count=" + vedio_count +
                    ", news_count=" + news_count +
                    ", fans_count='" + fans_count + '\'' +
                    ", classX='" + classX + '\'' +
                    ", catid='" + catid + '\'' +
                    ", thumb='" + thumb + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }


    public static class CatInfoEntity {

        /**
         * catdir : gdqctxt
         * app_thumb3 : http://img.iguxuan.com/statics/images/gx/app/jm/gdqc/gdqc_ad_ipad.jpg
         * app_thumb2 : http://img.iguxuan.com/statics/images/gx/app/jm/gdqc/gdqc_ad.jpg
         * parentid : 63
         * type : 0
         * catname : 股动钱潮
         * child : 0
         * setting : array (
         * 'workflowid' => '',
         * 'ishtml' => '0',
         * 'content_ishtml' => '1',
         * 'create_to_html_root' => '0',
         * 'template_list' => 'default',
         * 'category_template' => 'category',
         * 'list_template' => 'list_jm',
         * 'show_template' => 'show_jm',
         * 'meta_title' => '',
         * 'meta_keywords' => '',
         * 'meta_description' => '',
         * 'presentpoint' => '1',
         * 'defaultchargepoint' => '0',
         * 'paytype' => '0',
         * 'repeatchargedays' => '1',
         * 'category_ruleid' => '6',
         * 'show_ruleid' => '11',
         * )
         * arrchildid : 66
         * style :
         * listorder : 66
         * description :
         * arrparentid : 0,63
         * app_thumb : http://img.iguxuan.com/statics/images/gx/app/jm/gdqc/gdqc_banner.jpg
         * siteid : 1
         * ismenu : 0
         * catid : 66
         * module : content
         * jump_url : http://gdqc.iguxuan.com/wap.html
         * subscibe_count : false
         * letter : gudongqianchaojingxuan
         * sethtml : 0
         * modelid : 1
         * image : http://img.iguxuan.com/statics/images/gx/app/jm/gdqc/gdqc_logo.jpg
         * is_subscibe : 0
         * url : http://www.iguxuan.com/index.php?m=content&c=index&a=lists&catid=66
         * hits : 0
         * items : 23
         * vedio_count : 24
         * news_count : 1
         * parentdir : jm/
         * usable_type :
         */

        private String catdir;
        private String app_thumb3;
        private String app_thumb2;
        private String parentid;
        private String type;
        private String catname;
        private String child;
        private String setting;
        private String arrchildid;
        private String style;
        private String listorder;
        private String description;
        private String arrparentid;
        private String app_thumb;
        private String siteid;
        private String ismenu;
        private String catid;
        private String module;
        private String jump_url;
        private boolean subscibe_count;
        private String letter;
        private String sethtml;
        private String modelid;
        private String image;
        private int is_subscibe;
        private String url;
        private String hits;
        private String items;
        private int vedio_count;
        private int news_count;
        private String parentdir;
        private String usable_type;

        public String getCatdir() {
            return catdir;
        }

        public void setCatdir(String catdir) {
            this.catdir = catdir;
        }

        public String getApp_thumb3() {
            return app_thumb3;
        }

        public void setApp_thumb3(String app_thumb3) {
            this.app_thumb3 = app_thumb3;
        }

        public String getApp_thumb2() {
            return app_thumb2;
        }

        public void setApp_thumb2(String app_thumb2) {
            this.app_thumb2 = app_thumb2;
        }

        public String getParentid() {
            return parentid;
        }

        public void setParentid(String parentid) {
            this.parentid = parentid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCatname() {
            return catname;
        }

        public void setCatname(String catname) {
            this.catname = catname;
        }

        public String getChild() {
            return child;
        }

        public void setChild(String child) {
            this.child = child;
        }

        public String getSetting() {
            return setting;
        }

        public void setSetting(String setting) {
            this.setting = setting;
        }

        public String getArrchildid() {
            return arrchildid;
        }

        public void setArrchildid(String arrchildid) {
            this.arrchildid = arrchildid;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getListorder() {
            return listorder;
        }

        public void setListorder(String listorder) {
            this.listorder = listorder;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getArrparentid() {
            return arrparentid;
        }

        public void setArrparentid(String arrparentid) {
            this.arrparentid = arrparentid;
        }

        public String getApp_thumb() {
            return app_thumb;
        }

        public void setApp_thumb(String app_thumb) {
            this.app_thumb = app_thumb;
        }

        public String getSiteid() {
            return siteid;
        }

        public void setSiteid(String siteid) {
            this.siteid = siteid;
        }

        public String getIsmenu() {
            return ismenu;
        }

        public void setIsmenu(String ismenu) {
            this.ismenu = ismenu;
        }

        public String getCatid() {
            return catid;
        }

        public void setCatid(String catid) {
            this.catid = catid;
        }

        public String getModule() {
            return module;
        }

        public void setModule(String module) {
            this.module = module;
        }

        public String getJump_url() {
            return jump_url;
        }

        public void setJump_url(String jump_url) {
            this.jump_url = jump_url;
        }

        public boolean isSubscibe_count() {
            return subscibe_count;
        }

        public void setSubscibe_count(boolean subscibe_count) {
            this.subscibe_count = subscibe_count;
        }

        public String getLetter() {
            return letter;
        }

        public void setLetter(String letter) {
            this.letter = letter;
        }

        public String getSethtml() {
            return sethtml;
        }

        public void setSethtml(String sethtml) {
            this.sethtml = sethtml;
        }

        public String getModelid() {
            return modelid;
        }

        public void setModelid(String modelid) {
            this.modelid = modelid;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getIs_subscibe() {
            return is_subscibe;
        }

        public void setIs_subscibe(int is_subscibe) {
            this.is_subscibe = is_subscibe;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getHits() {
            return hits;
        }

        public void setHits(String hits) {
            this.hits = hits;
        }

        public String getItems() {
            return items;
        }

        public void setItems(String items) {
            this.items = items;
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

        public String getParentdir() {
            return parentdir;
        }

        public void setParentdir(String parentdir) {
            this.parentdir = parentdir;
        }

        public String getUsable_type() {
            return usable_type;
        }

        public void setUsable_type(String usable_type) {
            this.usable_type = usable_type;
        }


        @Override public String toString() {
            return "CatInfoEntity{" +
                    "app_thumb2='" + app_thumb2 + '\'' +
                    ", catdir='" + catdir + '\'' +
                    ", app_thumb3='" + app_thumb3 + '\'' +
                    ", parentid='" + parentid + '\'' +
                    ", type='" + type + '\'' +
                    ", catname='" + catname + '\'' +
                    ", child='" + child + '\'' +
                    ", setting='" + setting + '\'' +
                    ", arrchildid='" + arrchildid + '\'' +
                    ", style='" + style + '\'' +
                    ", listorder='" + listorder + '\'' +
                    ", description='" + description + '\'' +
                    ", arrparentid='" + arrparentid + '\'' +
                    ", app_thumb='" + app_thumb + '\'' +
                    ", siteid='" + siteid + '\'' +
                    ", ismenu='" + ismenu + '\'' +
                    ", catid='" + catid + '\'' +
                    ", module='" + module + '\'' +
                    ", jump_url='" + jump_url + '\'' +
                    ", subscibe_count=" + subscibe_count +
                    ", letter='" + letter + '\'' +
                    ", sethtml='" + sethtml + '\'' +
                    ", modelid='" + modelid + '\'' +
                    ", image='" + image + '\'' +
                    ", is_subscibe=" + is_subscibe +
                    ", url='" + url + '\'' +
                    ", hits='" + hits + '\'' +
                    ", items='" + items + '\'' +
                    ", vedio_count=" + vedio_count +
                    ", news_count=" + news_count +
                    ", parentdir='" + parentdir + '\'' +
                    ", usable_type='" + usable_type + '\'' +
                    '}';
        }
    }

    @Override public String toString() {
        return "ArticleDetailsEntity{" +
                "catid='" + catid + '\'' +
                ", comment_count='" + comment_count + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", daoshi='" + daoshi + '\'' +
                ", collected=" + collected +
                ", videodm='" + videodm + '\'' +
                ", daoshi_description='" + daoshi_description + '\'' +
                ", url='" + url + '\'' +
                ", inputtime='" + inputtime + '\'' +
                ", catname='" + catname + '\'' +
                ", daoshi_thumb='" + daoshi_thumb + '\'' +
                ", id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", daoshi_info=" + daoshi_info +
                ", jpType='" + jpType + '\'' +
                ", description='" + description + '\'' +
                ", views='" + views + '\'' +
                ", is_support=" + is_support +
                ", thumb='" + thumb + '\'' +
                ", zan_count='" + zan_count + '\'' +
                ", daoshi_name='" + daoshi_name + '\'' +
                ", upVideoId='" + upVideoId + '\'' +
                '}';
    }
}
