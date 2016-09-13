package tv.kuainiu.modle;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author nanck on 2016/3/8.
 * 节目
 */
public class ProgramItem implements Parcelable {


    /**
     * catid : 65
     * catname : 金钱风暴
     * description : 特邀来自于美国、台湾具有30年以上实战经验的资深分析师，
     * 用轻松、愉快的讨论方式，全面政策深入解读，带领大家进入投资领域，
     * 让投资人深入透彻股市获利机会和风险， 从艰深的投资理论转化成易懂的投资方法，让投资变得很简单！
     * url : http://www.iguxuan.com/index.php?m=content&c=index&a=lists&catid=65
     * image : http://img.iguxuan.com/uploadfile/2015/1214/20151214080242702.jpg
     * relevant_catid : 68
     * subscibe_count : 6
     * is_subscibe : 0
     * img : http://img.iguxuan.com/statics/images/gx/app/jm/jqfb/jqfb.jpg
     * anchorperson : 曾光辉
     * is_push_vedio : 0
     * is_push_news : 0
     */

    public String catid;
    public String catname;
    public String description;
    public String url;
    public String image;
    public int relevant_catid;
    public int subscibe_count;
    public int is_subscibe;
    public String img;
    public String anchorperson;
    public int is_push_vedio;
    public int is_push_news;


    @Override public String toString() {
        return "ColumnItem{" +
                "catid='" + catid + '\'' +
                ", catname='" + catname + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", image='" + image + '\'' +
                ", relevant_catid=" + relevant_catid +
                ", subscibe_count='" + subscibe_count + '\'' +
                ", is_subscibe=" + is_subscibe +
                ", img='" + img + '\'' +
                ", anchorperson='" + anchorperson + '\'' +
                ", is_push_vedio=" + is_push_vedio +
                ", is_push_news=" + is_push_news +
                '}';
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.catid);
        dest.writeString(this.catname);
        dest.writeString(this.description);
        dest.writeString(this.url);
        dest.writeString(this.image);
        dest.writeInt(this.relevant_catid);
        dest.writeInt(this.subscibe_count);
        dest.writeInt(this.is_subscibe);
        dest.writeString(this.img);
        dest.writeString(this.anchorperson);
        dest.writeInt(this.is_push_vedio);
        dest.writeInt(this.is_push_news);
    }

    public ProgramItem() {
    }

    protected ProgramItem(Parcel in) {
        this.catid = in.readString();
        this.catname = in.readString();
        this.description = in.readString();
        this.url = in.readString();
        this.image = in.readString();
        this.relevant_catid = in.readInt();
        this.subscibe_count = in.readInt();
        this.is_subscibe = in.readInt();
        this.img = in.readString();
        this.anchorperson = in.readString();
        this.is_push_vedio = in.readInt();
        this.is_push_news = in.readInt();
    }

    public static final Creator<ProgramItem> CREATOR = new Creator<ProgramItem>() {
        @Override public ProgramItem createFromParcel(Parcel source) {
            return new ProgramItem(source);
        }

        @Override public ProgramItem[] newArray(int size) {
            return new ProgramItem[size];
        }
    };
}
