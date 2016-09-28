package tv.kuainiu.ui.liveold.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jack on 2016/9/28.
 */

public class LiveParameter implements Parcelable {
    int supportNumber = 0;
    int fansNumber = 0;
    int onLineNumber = 0;
    int isFollow = 0;
    int isSupport = 0;
    String liveId = "";
    String teacherId = "";
    String nickName = "";
    String liveTitle = "";
    String teacherAvatar = "";
    private String roomId = "7h89Z1uHcCTEOTDsn6rQkCaj8lwaztWM";

    public int getSupportNumber() {
        return supportNumber;
    }

    public void setSupportNumber(int supportNumber) {
        this.supportNumber = supportNumber;
    }

    public int getOnLineNumber() {
        return onLineNumber;
    }

    public void setOnLineNumber(int onLineNumber) {
        this.onLineNumber = onLineNumber;
    }

    public int getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(int isFollow) {
        this.isFollow = isFollow;
    }

    public int getIsSupport() {
        return isSupport;
    }

    public void setIsSupport(int isSupport) {
        this.isSupport = isSupport;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLiveTitle() {
        return liveTitle;
    }

    public void setLiveTitle(String liveTitle) {
        this.liveTitle = liveTitle;
    }

    public String getTeacherAvatar() {
        return teacherAvatar;
    }

    public void setTeacherAvatar(String teacherAvatar) {
        this.teacherAvatar = teacherAvatar;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getFansNumber() {
        return fansNumber;
    }

    public void setFansNumber(int fansNumber) {
        this.fansNumber = fansNumber;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.supportNumber);
        dest.writeInt(this.fansNumber);
        dest.writeInt(this.onLineNumber);
        dest.writeInt(this.isFollow);
        dest.writeInt(this.isSupport);
        dest.writeString(this.liveId);
        dest.writeString(this.teacherId);
        dest.writeString(this.nickName);
        dest.writeString(this.liveTitle);
        dest.writeString(this.teacherAvatar);
        dest.writeString(this.roomId);
    }

    public LiveParameter() {
    }

    protected LiveParameter(Parcel in) {
        this.supportNumber = in.readInt();
        this.fansNumber = in.readInt();
        this.onLineNumber = in.readInt();
        this.isFollow = in.readInt();
        this.isSupport = in.readInt();
        this.liveId = in.readString();
        this.teacherId = in.readString();
        this.nickName = in.readString();
        this.liveTitle = in.readString();
        this.teacherAvatar = in.readString();
        this.roomId = in.readString();
    }

    public static final Creator<LiveParameter> CREATOR = new Creator<LiveParameter>() {
        @Override public LiveParameter createFromParcel(Parcel source) {
            return new LiveParameter(source);
        }

        @Override public LiveParameter[] newArray(int size) {
            return new LiveParameter[size];
        }
    };
}
