package tv.kuainiu.modle;

/**
 * Created by jack on 2016/9/19.
 */
public class HotPonit {
private String nickname;//"曾国良",
    private String avatar;//"http:\/\/kuainiu.oss-cn-shanghai.aliyuncs.com\/uploadfile\/avatar\/zengguoliang.jpg",
    private String id;//"9",
    private String user_id;//"2",
    private String description;//"A股评论：成交量4549亿元，1，万科A在8月16日随出货式漲停走票后在25日補8月12日跳空缺口时作龙回首观察，昨天以61万手強锁涨停",
    private String support_num;//"56",
    private String news_id;//"13",
    private String create_date;//"1473482663",
    private String[] tag_list;// ["新1", "金子", "大涨"],
    private String fans_count;//"1",
    private int is_follow;//0
    private int is_support;//0

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSupport_num() {
        return support_num;
    }

    public void setSupport_num(String support_num) {
        this.support_num = support_num;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String[] getTag_list() {
        return tag_list;
    }

    public void setTag_list(String[] tag_list) {
        this.tag_list = tag_list;
    }

    public String getFans_count() {
        return fans_count;
    }

    public void setFans_count(String fans_count) {
        this.fans_count = fans_count;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public int getIs_support() {
        return is_support;
    }

    public void setIs_support(int is_support) {
        this.is_support = is_support;
    }
}
