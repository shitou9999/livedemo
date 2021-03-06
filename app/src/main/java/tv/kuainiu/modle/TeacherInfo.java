package tv.kuainiu.modle;

/**
 * Created by jack on 2016/4/20.
 */
public class TeacherInfo {


    /**
     * id : 1
     * nickname : 陈雨墨
     * avatar : http://kuainiu.oss-cn-shanghai.aliyuncs.com/uploadfile/avatar/file_1460453945638_9354.jpg
     * slogan : 玩转短线 独创理论
     * banner :
     * fans_count : 0
     * is_follow : 0
     */

    private String id;
    private String nickname;
    private String avatar;
    private String slogan;
    private String banner;
    private String introduce;
    private String live_roomid;
    private int fans_count;
    private int is_follow;
    private LiveingInfo live_info;
    private String[] tag_list;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
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

    public String getLive_roomid() {
        return live_roomid;
    }

    public void setLive_roomid(String live_roomid) {
        this.live_roomid = live_roomid;
    }

    /**
     * 仅直播使用
     *
     * @return
     */
    public LiveingInfo getLive_info() {
        return live_info;
    }

    /**
     * 仅直播使用
     *
     * @return
     */
    public void setLive_info(LiveingInfo live_info) {
        this.live_info = live_info;
    }

    public String[] getTag_list() {
        return tag_list;
    }

    public void setTag_list(String[] tag_list) {
        this.tag_list = tag_list;
    }
}
