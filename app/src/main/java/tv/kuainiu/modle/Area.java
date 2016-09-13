package tv.kuainiu.modle;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author nanck on 2016/3/15.
 */
public class Area implements Parcelable {


    /**
     * id : 81
     * name : 秦皇岛市
     * code : 130300
     * fatherId : 39
     * level : 2
     * memo : null
     */

    public String id;
    public String name;
    public String code;
    public String fatherId;
    public String level;

    public Area() {
    }

    public Area(String id, String name, String code, String fatherId, String level) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.fatherId = fatherId;
        this.level = level;
    }

    @Override public String toString() {
        return "Area{" +
                "code='" + code + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", fatherId='" + fatherId + '\'' +
                ", level='" + level + '\'' +
                '}';
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.code);
        dest.writeString(this.fatherId);
        dest.writeString(this.level);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    protected Area(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.code = in.readString();
        this.fatherId = in.readString();
        this.level = in.readString();
    }

    public static final Parcelable.Creator<Area> CREATOR = new Parcelable.Creator<Area>() {
        @Override public Area createFromParcel(Parcel source) {
            return new Area(source);
        }

        @Override public Area[] newArray(int size) {
            return new Area[size];
        }
    };
}
