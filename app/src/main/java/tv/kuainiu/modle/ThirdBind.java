package tv.kuainiu.modle;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sirius on 2016-11-23.
 */

public class ThirdBind implements Parcelable {
    private ThirdBindContent wb;
    private ThirdBindContent wx;
    private ThirdBindContent qq;

    public ThirdBindContent getWb() {
        return wb;
    }

    public void setWb(ThirdBindContent wb) {
        this.wb = wb;
    }

    public ThirdBindContent getWx() {
        return wx;
    }

    public void setWx(ThirdBindContent wx) {
        this.wx = wx;
    }

    public ThirdBindContent getQq() {
        return qq;
    }

    public void setQq(ThirdBindContent qq) {
        this.qq = qq;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.wb, flags);
        dest.writeParcelable(this.wx, flags);
        dest.writeParcelable(this.qq, flags);
    }

    public ThirdBind() {
    }

    protected ThirdBind(Parcel in) {
        this.wb = in.readParcelable(ThirdBindContent.class.getClassLoader());
        this.wx = in.readParcelable(ThirdBindContent.class.getClassLoader());
        this.qq = in.readParcelable(ThirdBindContent.class.getClassLoader());
    }

    public static final Creator<ThirdBind> CREATOR = new Creator<ThirdBind>() {
        @Override
        public ThirdBind createFromParcel(Parcel source) {
            return new ThirdBind(source);
        }

        @Override
        public ThirdBind[] newArray(int size) {
            return new ThirdBind[size];
        }
    };
}
