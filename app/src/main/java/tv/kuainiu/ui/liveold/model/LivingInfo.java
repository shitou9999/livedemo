package tv.kuainiu.ui.liveold.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author nanck on 2016/7/11.
 */
public class LivingInfo implements Parcelable {


    /**
     * img : http://img.iguxuan.com/gx/iguxuan/img/tv/wap/img01.jpg
     * liveing : {"id":"20","title":"标题99","anchor":"廖英强","teacher_id":"1","thumb":"http://img.iguxuan.com/gx/public/img/teacher/logo/lyq.jpg","date":"2016-07-22","start_date":"2016-07-22 17:00:00","end_date":"2016-07-22 19:00:00","create_date":"2016-07-22 15:48:53","status":"1","start_date_time":1469178000,"end_date_time":1469185200,"create_date_time":1469173733,"support":"5","roomid":"454153203D9994519C33DC5901307461","fans_count":"207512","is_follow":0,"live_status":2,"live_status_msg":"直播中","anchor_about":""}
     */

    private String img;
    /**
     * id : 20
     * title : 标题99
     * anchor : 廖英强
     * teacher_id : 1
     * thumb : http://img.iguxuan.com/gx/public/img/teacher/logo/lyq.jpg
     * date : 2016-07-22
     * start_date : 2016-07-22 17:00:00
     * end_date : 2016-07-22 19:00:00
     * create_date : 2016-07-22 15:48:53
     * status : 1
     * start_date_time : 1469178000
     * end_date_time : 1469185200
     * create_date_time : 1469173733
     * support : 5
     * roomid : 454153203D9994519C33DC5901307461
     * fans_count : 207512
     * is_follow : 0
     * live_status : 2
     * live_status_msg : 直播中
     * anchor_about :
     */

    private LiveingEntity liveing;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public LiveingEntity getLiveing() {
        return liveing;
    }

    public void setLiveing(LiveingEntity liveing) {
        this.liveing = liveing;
    }

    public static class LiveingEntity implements Parcelable {
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
        private int fans_count;
        private int is_follow;
        private int is_supported;
        private int live_status;
        private String live_status_msg;
        private String anchor_about;

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

        public String getAnchor_about() {
            return anchor_about;
        }

        public void setAnchor_about(String anchor_about) {
            this.anchor_about = anchor_about;
        }

        public int getIs_supported() {
            return is_supported;
        }

        public void setIs_supported(int is_supported) {
            this.is_supported = is_supported;
        }

        @Override public String toString() {
            return "LiveingEntity{" +
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
                    ", fans_count=" + fans_count +
                    ", is_follow=" + is_follow +
                    ", is_supported=" + is_supported +
                    ", live_status=" + live_status +
                    ", live_status_msg='" + live_status_msg + '\'' +
                    ", anchor_about='" + anchor_about + '\'' +
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
            dest.writeInt(this.fans_count);
            dest.writeInt(this.is_follow);
            dest.writeInt(this.is_supported);
            dest.writeInt(this.live_status);
            dest.writeString(this.live_status_msg);
            dest.writeString(this.anchor_about);
        }

        public LiveingEntity() {
        }

        protected LiveingEntity(Parcel in) {
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
            this.fans_count = in.readInt();
            this.is_follow = in.readInt();
            this.is_supported = in.readInt();
            this.live_status = in.readInt();
            this.live_status_msg = in.readString();
            this.anchor_about = in.readString();
        }

        public static final Creator<LiveingEntity> CREATOR = new Creator<LiveingEntity>() {
            @Override public LiveingEntity createFromParcel(Parcel source) {
                return new LiveingEntity(source);
            }

            @Override public LiveingEntity[] newArray(int size) {
                return new LiveingEntity[size];
            }
        };
    }

    @Override public String toString() {
        return "LivingInfo{" +
                "img='" + img + '\'' +
                ", liveing=" + liveing +
                '}';
    }


    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.img);
        dest.writeParcelable(this.liveing, flags);
    }

    public LivingInfo() {
    }

    protected LivingInfo(Parcel in) {
        this.img = in.readString();
        this.liveing = in.readParcelable(LiveingEntity.class.getClassLoader());
    }

    public static final Creator<LivingInfo> CREATOR = new Creator<LivingInfo>() {
        @Override public LivingInfo createFromParcel(Parcel source) {
            return new LivingInfo(source);
        }

        @Override public LivingInfo[] newArray(int size) {
            return new LivingInfo[size];
        }
    };
}