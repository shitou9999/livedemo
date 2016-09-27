package tv.kuainiu.ui.liveold.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author nanck on 2016/7/22.
 */
public class Remind implements Parcelable {
    private String id;
    private int subscribeFlag;

    public Remind() {
    }

    public Remind(String id) {
        this.id = id;
    }

    public Remind(String id, int subscribeFlag) {
        this.id = id;
        this.subscribeFlag = subscribeFlag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSubscribeFlag() {
        return subscribeFlag;
    }

    public void setSubscribeFlag(int subscribeFlag) {
        this.subscribeFlag = subscribeFlag;
    }

    @Override public String toString() {
        return "Remind{" +
                "id='" + id + '\'' +
                ", subscribeFlag=" + subscribeFlag +
                '}';
    }


    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.subscribeFlag);
    }


    protected Remind(Parcel in) {
        this.id = in.readString();
        this.subscribeFlag = in.readInt();
    }

    public static final Creator<Remind> CREATOR = new Creator<Remind>() {
        @Override public Remind createFromParcel(Parcel source) {
            return new Remind(source);
        }

        @Override public Remind[] newArray(int size) {
            return new Remind[size];
        }
    };
}
