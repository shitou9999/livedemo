package tv.kuainiu.modle.push;

/**
 * Created by sirius on 2016/10/21.
 */

public class AppointmentLive extends BaseMeassge{

    /**
     * type : 5
     * need_alert : 1
     * live_room_id : CE52A5E3857306009C33DC5901307461
     * live_id : 33
     */
    private String live_room_id;
    private String live_id;
    private String live_title;
    private String teacher_id;


    public String getLive_room_id() {
        return live_room_id;
    }

    public void setLive_room_id(String live_room_id) {
        this.live_room_id = live_room_id;
    }

    public String getLive_id() {
        return live_id;
    }

    public void setLive_id(String live_id) {
        this.live_id = live_id;
    }

    public String getLive_title() {
        return live_title;
    }

    public void setLive_title(String live_title) {
        this.live_title = live_title;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }
}
