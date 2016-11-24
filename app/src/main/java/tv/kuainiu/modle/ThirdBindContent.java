package tv.kuainiu.modle;

import android.os.Parcel;
import android.os.Parcelable;


public class ThirdBindContent implements Parcelable {


    private int bind;
    private String name;
    private String avatar;

    public int getBind() {
        return bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.bind);
        dest.writeString(this.name);
        dest.writeString(this.avatar);
    }

    public ThirdBindContent() {
    }

    protected ThirdBindContent(Parcel in) {
        this.bind = in.readInt();
        this.name = in.readString();
        this.avatar = in.readString();
    }

    public static final Creator<ThirdBindContent> CREATOR = new Creator<ThirdBindContent>() {
        @Override
        public ThirdBindContent createFromParcel(Parcel source) {
            return new ThirdBindContent(source);
        }

        @Override
        public ThirdBindContent[] newArray(int size) {
            return new ThirdBindContent[size];
        }
    };
}
