package tv.kuainiu.modle;

/**
 * Created by jack on 2016/9/21.
 */
public class TeacherZoneDynamics {

    /**
     * id : 19
     * user_id : 1
     * support_num : 0
     * comment_num : 0
     * nickname :
     * avatar :
     * type : 1
     * news_id : 0
     * description : 今日大盘走势情况很乐观，同志们加油
     * thumb :
     * create_date : 1474360580
     * synchro_wb : 1
     * status : 1
     */

    private int id;
    private int user_id;
    private int support_num;
    private String comment_num;
    private String nickname;
    private String avatar;
    private int type;
    private int news_id;
    private String description;
    private String thumb;
    private int create_date;
    private int synchro_wb;
    private int status;
    private int is_support;
    private TeacherZoneDynamicsInfo news_info;
    private LiveInfo live_info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getSupport_num() {
        return support_num;
    }

    public void setSupport_num(int support_num) {
        this.support_num = support_num;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getCreate_date() {
        return create_date;
    }

    public void setCreate_date(int create_date) {
        this.create_date = create_date;
    }

    public int getSynchro_wb() {
        return synchro_wb;
    }

    public void setSynchro_wb(int synchro_wb) {
        this.synchro_wb = synchro_wb;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public TeacherZoneDynamicsInfo getNews_info() {
        return news_info;
    }

    public void setNews_info(TeacherZoneDynamicsInfo news_info) {
        this.news_info = news_info;
    }

    public int getIs_support() {
        return is_support;
    }

    public void setIs_support(int is_support) {
        this.is_support = is_support;
    }

    public LiveInfo getLive_info() {
        return live_info;
    }

    public void setLive_info(LiveInfo live_info) {
        this.live_info = live_info;
    }
}
