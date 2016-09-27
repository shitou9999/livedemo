package tv.kuainiu.ui.liveold.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author nanck on 2016/7/25.
 */
public class History implements Parcelable {


    /**
     * id : 82
     * title : 标5题bb
     * anchor : 陈斌宇
     * teacher_id : 13
     * thumb : http://img.iguxuan.com/gx/public/img/teacher/logo/cby.jpg
     * date : 2016-08-01
     * start_date : 2016-08-01 19:30:00
     * end_date : 2016-08-01 20:00:00
     * create_date : 2016-08-01 15:52:12
     * status : 1
     * playback_id : 7AE95A78ACA0FD0E
     * start_date_time : 1470051000
     * end_date_time : 1470052800
     * create_date_time : 1470037932
     * support : 0
     * roomid : 454153203D9994519C33DC5901307461
     * cc_id : 7AE95A78ACA0FD0E
     * playback_times : 0
     * fans_count : 51892
     * live_thumb : http://img.iguxuan.com/gx/public/img/teacher/jp/cby.jpg
     * teacher_thumb : http://img.iguxuan.com/gx/public/img/teacher/logo/cby.jpg
     * is_supported : 0
     * is_follow : 0
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
    private int status;
    private String playback_id;
    private long start_date_time;
    private long end_date_time;
    private long create_date_time;
    private int support;
    private String roomid;
    private String cc_id;
    private int playback_times;
    private int fans_count;
    private String live_thumb;
    private String teacher_thumb;
    private int is_supported;
    private int is_follow;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPlayback_id() {
        return playback_id;
    }

    public void setPlayback_id(String playback_id) {
        this.playback_id = playback_id;
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

    public String getCc_id() {
        return cc_id;
    }

    public void setCc_id(String cc_id) {
        this.cc_id = cc_id;
    }

    public int getPlayback_times() {
        return playback_times;
    }

    public void setPlayback_times(int playback_times) {
        this.playback_times = playback_times;
    }

    public int getFans_count() {
        return fans_count;
    }

    public void setFans_count(int fans_count) {
        this.fans_count = fans_count;
    }

    public String getLive_thumb() {
        return live_thumb;
    }

    public void setLive_thumb(String live_thumb) {
        this.live_thumb = live_thumb;
    }

    public String getTeacher_thumb() {
        return teacher_thumb;
    }

    public void setTeacher_thumb(String teacher_thumb) {
        this.teacher_thumb = teacher_thumb;
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

    @Override public int describeContents() {
        return 0;
    }


    @Override public String toString() {
        return "History{" +
                "anchor='" + anchor + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", teacher_id='" + teacher_id + '\'' +
                ", thumb='" + thumb + '\'' +
                ", date='" + date + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", create_date='" + create_date + '\'' +
                ", status=" + status +
                ", playback_id='" + playback_id + '\'' +
                ", start_date_time=" + start_date_time +
                ", end_date_time=" + end_date_time +
                ", create_date_time=" + create_date_time +
                ", support=" + support +
                ", roomid='" + roomid + '\'' +
                ", cc_id='" + cc_id + '\'' +
                ", playback_times=" + playback_times +
                ", fans_count=" + fans_count +
                ", live_thumb='" + live_thumb + '\'' +
                ", teacher_thumb='" + teacher_thumb + '\'' +
                ", is_supported=" + is_supported +
                ", is_follow=" + is_follow +
                '}';
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
        dest.writeInt(this.status);
        dest.writeString(this.playback_id);
        dest.writeLong(this.start_date_time);
        dest.writeLong(this.end_date_time);
        dest.writeLong(this.create_date_time);
        dest.writeInt(this.support);
        dest.writeString(this.roomid);
        dest.writeString(this.cc_id);
        dest.writeInt(this.playback_times);
        dest.writeInt(this.fans_count);
        dest.writeString(this.live_thumb);
        dest.writeString(this.teacher_thumb);
        dest.writeInt(this.is_supported);
        dest.writeInt(this.is_follow);
    }

    public History() {
    }

    protected History(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.anchor = in.readString();
        this.teacher_id = in.readString();
        this.thumb = in.readString();
        this.date = in.readString();
        this.start_date = in.readString();
        this.end_date = in.readString();
        this.create_date = in.readString();
        this.status = in.readInt();
        this.playback_id = in.readString();
        this.start_date_time = in.readLong();
        this.end_date_time = in.readLong();
        this.create_date_time = in.readLong();
        this.support = in.readInt();
        this.roomid = in.readString();
        this.cc_id = in.readString();
        this.playback_times = in.readInt();
        this.fans_count = in.readInt();
        this.live_thumb = in.readString();
        this.teacher_thumb = in.readString();
        this.is_supported = in.readInt();
        this.is_follow = in.readInt();
    }

    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override public History createFromParcel(Parcel source) {
            return new History(source);
        }

        @Override public History[] newArray(int size) {
            return new History[size];
        }
    };
}
