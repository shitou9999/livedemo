package tv.kuainiu.modle;


import java.io.Serializable;

/**
 * 初始化信息
 *
 * @author nanck on 2016/4/19.
 */
public class InitInfo implements Serializable {


    /**
     * user_id : 30
     * custom_num : 0
     * msg_num : 1
     * follow_count : 6
     * subscibe_count : 2
     * ke_url : http: \/\/cliet.apitest.iguxuan.com\/html\/test
     * download_see_login : 0
     */

    private String user_id;
    private String private_key;
    private int custom_num;
    private int msg_num=0;
    private int follow_count;
    private int live_count;
    private int fans_count;
    private int subscibe_count;
    private String ke_url;
    private int download_see_login;
    private int is_teacher;
    private int live_wait_count;
    private int appointment_count;
    private ThirdBind third_bind;

    public ThirdBind getThird_bind() {
        return third_bind;
    }

    public void setThird_bind(ThirdBind third_bind) {
        this.third_bind = third_bind;
    }

    public int getLive_count() {
        return live_count;
    }

    public void setLive_count(int live_count) {
        this.live_count = live_count;
    }

    public int getFans_count() {
        return fans_count;
    }

    public void setFans_count(int fans_count) {
        this.fans_count = fans_count;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public int getCustom_num() {
        return custom_num;
    }

    public void setCustom_num(int custom_num) {
        this.custom_num = custom_num;
    }

    public int getMsg_num() {
        return msg_num;
    }

    public void setMsg_num(int msg_num) {
        this.msg_num = msg_num;
    }

    public int getFollow_count() {
        return follow_count;
    }

    public void setFollow_count(int follow_count) {
        this.follow_count = follow_count;
    }

    public int getSubscibe_count() {
        return subscibe_count;
    }

    public void setSubscibe_count(int subscibe_count) {
        this.subscibe_count = subscibe_count;
    }

    public String getKe_url() {
        return ke_url;
    }

    public void setKe_url(String ke_url) {
        this.ke_url = ke_url;
    }

    public int getDownload_see_login() {
        return download_see_login;
    }

    public void setDownload_see_login(int download_see_login) {
        this.download_see_login = download_see_login;
    }

    public int getIs_teacher() {
        return is_teacher;
    }

    public void setIs_teacher(int is_teacher) {
        this.is_teacher = is_teacher;
    }

    public int getLive_wait_count() {
        return live_wait_count;
    }

    public void setLive_wait_count(int live_wait_count) {
        this.live_wait_count = live_wait_count;
    }

    public int getAppointment_count() {
        return appointment_count;
    }

    public void setAppointment_count(int appointment_count) {
        this.appointment_count = appointment_count;
    }

    @Override public String toString() {
        return "InitInfo{" +
                "custom_num=" + custom_num +
                ", user_id='" + user_id + '\'' +
                ", msg_num=" + msg_num +
                ", follow_count=" + follow_count +
                ", subscibe_count=" + subscibe_count +
                ", ke_url='" + ke_url + '\'' +
                ", download_see_login=" + download_see_login +
                '}';
    }
}
