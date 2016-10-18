package tv.kuainiu.modle;

/**
 * Created by sirius on 2016/10/18.
 */

public class Appointment {

    /**
     * id : 12
     * anchor : 蛋哥
     * teacher_id : 2
     * title : 9月24-大盘直播7
     * start_date : 2016-09-24 18:00:00
     * end_date : 2016-09-24 22:00:00
     * teacher_info :
     * subscribe_num : 1
     * start_datetime : 1474732800
     * live_id : 12
     * live_status : 1
     * live_msg : 已结束
     */

    private String id;
    private String anchor;
    private String teacher_id;
    private String title;
    private String start_date;
    private String end_date;
    private TeacherInfo teacher_info;
    private int subscribe_num;
    private int start_datetime;
    private String live_id;
    private int live_status;
    private String live_msg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public TeacherInfo getTeacher_info() {
        return teacher_info;
    }

    public void setTeacher_info(TeacherInfo teacher_info) {
        this.teacher_info = teacher_info;
    }

    public int getSubscribe_num() {
        return subscribe_num;
    }

    public void setSubscribe_num(int subscribe_num) {
        this.subscribe_num = subscribe_num;
    }

    public int getStart_datetime() {
        return start_datetime;
    }

    public void setStart_datetime(int start_datetime) {
        this.start_datetime = start_datetime;
    }

    public String getLive_id() {
        return live_id;
    }

    public void setLive_id(String live_id) {
        this.live_id = live_id;
    }

    public int getLive_status() {
        return live_status;
    }

    public void setLive_status(int live_status) {
        this.live_status = live_status;
    }

    public String getLive_msg() {
        return live_msg;
    }

    public void setLive_msg(String live_msg) {
        this.live_msg = live_msg;
    }
}
