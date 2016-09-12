package com.kuainiutv.modle;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author nanck on 2016/6/30.
 */
public class NewsFlag implements Parcelable {

    public NewsFlag() {
    }

    public NewsFlag(int catid, String catname, String description, String icon, String image, String url) {
        this.catid = catid;
        this.catname = catname;
        this.description = description;
        this.icon = icon;
        this.image = image;
        this.url = url;
    }

    private int catid;
    private String catname;
    private String description;
    private String url;
    private String image;
    private String icon;

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override public int describeContents() {
        return 0;
    }


    @Override public String toString() {
        return "NewsFlag{" +
                "catid=" + catid +
                ", catname='" + catname + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", image='" + image + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.catid);
        dest.writeString(this.catname);
        dest.writeString(this.description);
        dest.writeString(this.url);
        dest.writeString(this.image);
        dest.writeString(this.icon);
    }


    protected NewsFlag(Parcel in) {
        this.catid = in.readInt();
        this.catname = in.readString();
        this.description = in.readString();
        this.url = in.readString();
        this.image = in.readString();
        this.icon = in.readString();
    }

    public static final Parcelable.Creator<NewsFlag> CREATOR = new Parcelable.Creator<NewsFlag>() {
        @Override public NewsFlag createFromParcel(Parcel source) {
            return new NewsFlag(source);
        }

        @Override public NewsFlag[] newArray(int size) {
            return new NewsFlag[size];
        }
    };
}
