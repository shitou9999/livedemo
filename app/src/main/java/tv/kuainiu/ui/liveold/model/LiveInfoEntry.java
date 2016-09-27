package tv.kuainiu.ui.liveold.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author nanck on 2016/7/7.
 */
public class LiveInfoEntry implements Parcelable {

    /**
     * id : 18
     * title : 标题77
     * anchor : 黄勤翔
     * teacher_id : 2
     * thumb : http://img.iguxuan.com/gx/public/img/teacher/logo/zgh.jpg
     * date : 2016-07-22
     * start_date : 2016-07-22 16:00:00
     * end_date : 2016-07-22 16:30:00
     * create_date : 2016-07-22 15:48:53
     * status : 1
     * start_date_time : 1469174400
     * end_date_time : 1469176200
     * create_date_time : 1469173733
     * support : 0
     * roomid : 454153203D9994519C33DC5901307461
     * fans_count : 33164
     * is_supported : 0
     * is_follow : 0
     * live_status : 1
     * live_status_msg : 已结束
     */

    private String id;
    private String title;
    private String anchor;
    private String teacher_id;
    private String thumb;
    private String date;
    private String start_date;
    private String end_date;
    private String create_date;
    private String status;
    private long start_date_time;
    private long end_date_time;
    private long create_date_time;
    private int support;
    private String roomid;
    private String fans_count;
    private int is_supported;
    private int is_follow;
    private int live_status;
    private int remindFlag;
    private String live_status_msg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getStart_date_time() {
        return start_date_time;
    }

    public void setStart_date_time(long start_date_time) {
        this.start_date_time = start_date_time;
    }

    public long getEnd_date_time() {
        return end_date_time;
    }

    public void setEnd_date_time(long end_date_time) {
        this.end_date_time = end_date_time;
    }

    public long getCreate_date_time() {
        return create_date_time;
    }

    public void setCreate_date_time(long create_date_time) {
        this.create_date_time = create_date_time;
    }

    public int getSupport() {
        return support;
    }

    public void setSupport(int support) {
        this.support = support;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getFans_count() {
        return fans_count;
    }

    public void setFans_count(String fans_count) {
        this.fans_count = fans_count;
    }

    public int getIs_supported() {
        return is_supported;
    }

    public void setIs_supported(int is_supported) {
        this.is_supported = is_supported;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public int getLive_status() {
        return live_status;
    }

    public void setLive_status(int live_status) {
        this.live_status = live_status;
    }

    public String getLive_status_msg() {
        return live_status_msg;
    }

    public void setLive_status_msg(String live_status_msg) {
        this.live_status_msg = live_status_msg;
    }

    public int getRemindFlag() {
        return remindFlag;
    }

    public void setRemindFlag(int remindFlag) {
        this.remindFlag = remindFlag;
    }

    @Override public String toString() {
        return "LiveInfoEntry{" +
                "anchor='" + anchor + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", teacher_id='" + teacher_id + '\'' +
                ", thumb='" + thumb + '\'' +
                ", date='" + date + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", create_date='" + create_date + '\'' +
                ", status='" + status + '\'' +
                ", start_date_time=" + start_date_time +
                ", end_date_time=" + end_date_time +
                ", create_date_time=" + create_date_time +
                ", support=" + support +
                ", roomid='" + roomid + '\'' +
                ", fans_count='" + fans_count + '\'' +
                ", is_supported=" + is_supported +
                ", is_follow=" + is_follow +
                ", live_status=" + live_status +
                ", remindFlag=" + remindFlag +
                ", live_status_msg='" + live_status_msg + '\'' +
                '}';
    }


    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.anchor);
        dest.writeString(this.teacher_id);
        dest.writeString(this.thumb);
        dest.writeString(this.date);
        dest.writeString(this.start_date);
        dest.writeString(this.end_date);
        dest.writeString(this.create_date);
        dest.writeString(this.status);
        dest.writeLong(this.start_date_time);
        dest.writeLong(this.end_date_time);
        dest.writeLong(this.create_date_time);
        dest.writeInt(this.support);
        dest.writeString(this.roomid);
        dest.writeString(this.fans_count);
        dest.writeInt(this.is_supported);
        dest.writeInt(this.is_follow);
        dest.writeInt(this.live_status);
        dest.writeInt(this.remindFlag);
        dest.writeString(this.live_status_msg);
    }

    public LiveInfoEntry() {
    }

    protected LiveInfoEntry(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.anchor = in.readString();
        this.teacher_id = in.readString();
        this.thumb = in.readString();
        this.date = in.readString();
        this.start_date = in.readString();
        this.end_date = in.readString();
        this.create_date = in.readString();
        this.status = in.readString();
        this.start_date_time = in.readLong();
        this.end_date_time = in.readLong();
        this.create_date_time = in.readLong();
        this.support = in.readInt();
        this.roomid = in.readString();
        this.fans_count = in.readString();
        this.is_supported = in.readInt();
        this.is_follow = in.readInt();
        this.live_status = in.readInt();
        this.remindFlag = in.readInt();
        this.live_status_msg = in.readString();
    }

    public static final Creator<LiveInfoEntry> CREATOR = new Creator<LiveInfoEntry>() {
        @Override public LiveInfoEntry createFromParcel(Parcel source) {
            return new LiveInfoEntry(source);
        }

        @Override public LiveInfoEntry[] newArray(int size) {
            return new LiveInfoEntry[size];
        }
    };
}
