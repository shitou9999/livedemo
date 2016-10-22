package tv.kuainiu.modle;

/**
 * Created by jack on 2016/9/27.
 */

public class LiveInfo {


    /**
     * live_msg : 已结束
     * ins_id : 0
     * date : 2016-09-27
     * anchor : 曾国良
     * id : 4
     * end_date : 2016-09-27 15:00:00
     * title : 谷底篇，均线测度
     * create_date : 2016-09-25 22:23:09
     * playback_id :
     * description : 谷底篇，均线测度
     * is_supported : 0
     * live_status : 1
     * user_id : 6
     * is_follow : 0
     * support : 1
     * status : 1
     * permission : 0
     * avatar : http://kuainiu.oss-cn-shanghai.aliyuncs.com/uploadfile/avatar/zengguoliang.jpg
     * teacher_info : {"avatar":"http://kuainiu.oss-cn-shanghai.aliyuncs.com/uploadfile/avatar/zengguoliang.jpg","id":"6","nickname":"曾国良"}
     * is_recommend : 0
     * allow_comment : 1
     * teacher_id : 6
     * synchro_dynamics : 1
     * start_date : 2016-09-27 13:00:00
     * thumb : 0
     * synchro_wb : 1
     */

    private String live_msg;
    private String ins_id;
    private String date;
    private String anchor;
    private String id;
    private String end_date;
    private String title;
    private String create_date;
    private String playback_id;
    private String description;
    private int is_supported;
    private int live_status;//1直播结束，2直播中，3直播未开始
    private String user_id;
    private int is_follow;
    private int support;
    private String status;
    private String permission;
    private String avatar;
    private long start_datetime;
    private long end_datetime;
    /**
     * avatar : http://kuainiu.oss-cn-shanghai.aliyuncs.com/uploadfile/avatar/zengguoliang.jpg
     * id : 6
     * nickname : 曾国良
     */

    private TeacherInfo teacher_info;
    private String is_recommend;
    private String allow_comment;
    private String teacher_id;
    private String synchro_dynamics;
    private String start_date;
    private String thumb;
    private String synchro_wb;
    private int online_num;//在线人数
    private int is_appointment;//是否已预约
    private int appointment_count;//预约总数

    public String getLive_msg() {
        return live_msg;
    }

    public void setLive_msg(String live_msg) {
        this.live_msg = live_msg;
    }

    public String getIns_id() {
        return ins_id;
    }

    public void setIns_id(String ins_id) {
        this.ins_id = ins_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getPlayback_id() {
        return playback_id;
    }

    public void setPlayback_id(String playback_id) {
        this.playback_id = playback_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIs_supported() {
        return is_supported;
    }

    public void setIs_supported(int is_supported) {
        this.is_supported = is_supported;
    }

    public int getLive_status() {
        return live_status;
    }

    public void setLive_status(int live_status) {
        this.live_status = live_status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public int getSupport() {
        return support;
    }

    public void setSupport(int support) {
        this.support = support;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public TeacherInfo getTeacher_info() {
        return teacher_info;
    }

    public void setTeacher_info(TeacherInfo teacher_info) {
        this.teacher_info = teacher_info;
    }

    public String getIs_recommend() {
        return is_recommend;
    }

    public void setIs_recommend(String is_recommend) {
        this.is_recommend = is_recommend;
    }

    public String getAllow_comment() {
        return allow_comment;
    }

    public void setAllow_comment(String allow_comment) {
        this.allow_comment = allow_comment;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getSynchro_dynamics() {
        return synchro_dynamics;
    }

    public void setSynchro_dynamics(String synchro_dynamics) {
        this.synchro_dynamics = synchro_dynamics;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getSynchro_wb() {
        return synchro_wb;
    }

    public void setSynchro_wb(String synchro_wb) {
        this.synchro_wb = synchro_wb;
    }

    public int getOnline_num() {
        return online_num;
    }

    public void setOnline_num(int online_num) {
        this.online_num = online_num;
    }

    public int getIs_appointment() {
        return is_appointment;
    }

    public void setIs_appointment(int is_appointment) {
        this.is_appointment = is_appointment;
    }

    public int getAppointment_count() {
        return appointment_count;
    }

    public void setAppointment_count(int appointment_count) {
        this.appointment_count = appointment_count;
    }

    public long getStart_datetime() {
        return start_datetime;
    }

    public void setStart_datetime(long start_datetime) {
        this.start_datetime = start_datetime;
    }

    public long getEnd_datetime() {
        return end_datetime;
    }

    public void setEnd_datetime(long end_datetime) {
        this.end_datetime = end_datetime;
    }


}
