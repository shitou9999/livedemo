package tv.kuainiu.ui.liveold.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jack on 2016/9/28.
 */

public class LiveParameter implements Parcelable {
    String liveId = "";
    String teacherId = "";
    String liveTitle = "";
    String ccid = "";
    private String roomId = "7h89Z1uHcCTEOTDsn6rQkCaj8lwaztWM";

    public LiveParameter() {

    }

    protected LiveParameter(Parcel in) {

        liveId = in.readString();
        teacherId = in.readString();
        liveTitle = in.readString();
        roomId = in.readString();
        ccid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(liveId);
        dest.writeString(teacherId);
        dest.writeString(liveTitle);
        dest.writeString(roomId);
        dest.writeString(ccid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LiveParameter> CREATOR = new Creator<LiveParameter>() {
        @Override
        public LiveParameter createFromParcel(Parcel in) {
            return new LiveParameter(in);
        }

        @Override
        public LiveParameter[] newArray(int size) {
            return new LiveParameter[size];
        }
    };

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

    public String getLiveTitle() {
        return liveTitle;
    }

    public void setLiveTitle(String liveTitle) {
        this.liveTitle = liveTitle;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getCcid() {
        return ccid;
    }

    public void setCcid(String ccid) {
        this.ccid = ccid;
    }
}
