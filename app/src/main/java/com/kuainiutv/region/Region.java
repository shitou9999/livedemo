package com.kuainiutv.region;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author nanck on 2016/5/31.
 */
public class Region implements Parcelable {

    /**
     * regionName : 澳大利亚
     * regionCode : 61
     * firstLetter : A
     */

    private String regionName;
    private int regionCode;
    private String firstLetter;

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public int getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(int regionCode) {
        this.regionCode = regionCode;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }


    @Override public String toString() {
        return "Region{" +
                "firstLetter='" + firstLetter + '\'' +
                ", regionName='" + regionName + '\'' +
                ", regionCode=" + regionCode +
                '}';
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.regionName);
        dest.writeInt(this.regionCode);
        dest.writeString(this.firstLetter);
    }

    public Region() {
    }

    protected Region(Parcel in) {
        this.regionName = in.readString();
        this.regionCode = in.readInt();
        this.firstLetter = in.readString();
    }

    public static final Parcelable.Creator<Region> CREATOR = new Parcelable.Creator<Region>() {
        @Override public Region createFromParcel(Parcel source) {
            return new Region(source);
        }

        @Override public Region[] newArray(int size) {
            return new Region[size];
        }
    };
}
