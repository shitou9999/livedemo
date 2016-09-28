package tv.kuainiu.modle;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jack on 2016/9/28.
 */

public class LiveItem implements Parcelable {

    /**
     {
     "user_id":"6",
     "ins_id":"0",
     "thumb":"http://kuainiu.oss-cn-shanghai.aliyuncs.com/uploadfile/thumb/201609/kn_live_thumb.png",
     "live_thumb":"",     //直播缩略图
     "live_roomid":"CE52A5E3857306009C33DC5901307461",
     "teacher_id":"6",     //主播老师ID
     "live_title":"谷底篇，均线测度",     //直播标题
     "nickname":"曾国良",     //主播人
     "slogan":"摩根操盘术 独孤九剑",     //标语
     "online_num":3     //在线人数
     }

     */

    private String user_id;
    private String ins_id;
    private String thumb;
    private String live_thumb;
    private String live_roomid;
    private String teacher_id;
    private String live_title;
    private String nickname;
    private String slogan;
    private int online_num;

    protected LiveItem(Parcel in) {
        user_id = in.readString();
        ins_id = in.readString();
        thumb = in.readString();
        live_thumb = in.readString();
        live_roomid = in.readString();
        teacher_id = in.readString();
        live_title = in.readString();
        nickname = in.readString();
        slogan = in.readString();
        online_num = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(ins_id);
        dest.writeString(thumb);
        dest.writeString(live_thumb);
        dest.writeString(live_roomid);
        dest.writeString(teacher_id);
        dest.writeString(live_title);
        dest.writeString(nickname);
        dest.writeString(slogan);
        dest.writeInt(online_num);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LiveItem> CREATOR = new Creator<LiveItem>() {
        @Override
        public LiveItem createFromParcel(Parcel in) {
            return new LiveItem(in);
        }

        @Override
        public LiveItem[] newArray(int size) {
            return new LiveItem[size];
        }
    };

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIns_id() {
        return ins_id;
    }

    public void setIns_id(String ins_id) {
        this.ins_id = ins_id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getLive_thumb() {
        return live_thumb;
    }

    public void setLive_thumb(String live_thumb) {
        this.live_thumb = live_thumb;
    }

    public String getLive_roomid() {
        return live_roomid;
    }

    public void setLive_roomid(String live_roomid) {
        this.live_roomid = live_roomid;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getLive_title() {
        return live_title;
    }

    public void setLive_title(String live_title) {
        this.live_title = live_title;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public int getOnline_num() {
        return online_num;
    }

    public void setOnline_num(int online_num) {
        this.online_num = online_num;
    }
}
